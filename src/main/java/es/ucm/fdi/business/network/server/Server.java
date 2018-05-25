/*
  This file is part of Dimension.
  Dimension is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  Dimension is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  You should have received a copy of the GNU General Public License
  along with Dimension.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.business.network.server;

import es.ucm.fdi.business.connectivity.ShareManagerAS;
import es.ucm.fdi.business.exceptions.network.ServerSSLException;
import es.ucm.fdi.business.users.UserManagerAS;
import es.ucm.fdi.business.workspace.project.ProjectDTO;
import es.ucm.fdi.business.workspace.project.WorkAS;
import es.ucm.fdi.integration.users.UserDAOSQLImp;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/**
 * Implementation of a <code>Server</code>. A <code>Server</code> is a runnable
 * object which allows multiple connections from clients. The server listens
 * from each clients and multiple operations are allow such as: login, register,
 * logout, generate image from function... All of these options are received as
 * a package request from a client; each package is processed and then an
 * appropiate protocol is applied according to the type of request that has been
 * made. The server keeps a list of all clients that are currently connected to
 * the server. Each client is managed through each own
 * <code>ClientThread</code>. The sockets are handled in an SSL context which is
 * created using the appropiated certificates. The encryption algorithm used for
 * the SSL connections is RSA and its implementation relies on the
 * <code>SSLSocketServer</code> methods. Also, it is remarkable that the
 * idGenerator creates a new id for each new client. Due to the variable type
 * (int), its range is limitated to the range of an int. What's more, once the
 * limit is reached the server will start over giving negative id values, and if
 * many users are still connected during a long time, id collision could happen.
 * This is really unlike though, since the range of connections the project is
 * supposed to handle does not overcome thousand of connections simultaneously.
 *
 * @author Arturo Acuaviva
 */
public class Server implements Runnable {

    /**
     * Server port used during listening
     */
    private final int PORT;
    /**
     * Main loop control variable
     */
    protected volatile boolean stopsRunning = false;
    /**
     * Current thread, used for synchronization
     */
    protected Thread runningThread = null;
    /**
     * Current main server socket
     */
    protected SSLServerSocket serverSocket = null;
    /**
     * ID number assigned to new clients. It starts from 0 and increases each
     * time a new client is accepted.
     */
    private static int idGenerator = 0;
    /**
     * Date format definition to show messages with format hh:mm:ss
     */
    private final SimpleDateFormat dataTimeFormat = new SimpleDateFormat("HH:mm:ss");
    ;
    /**
     * Maps of id to clients listening the server 
     */
    private final Map<Integer, ClientThread> clientsListeners = new HashMap<>();

    /**
     * Factory object to create a SSLServerSocket
     */
    private SSLServerSocketFactory sslServerSocketFactory;
    /**
     * Context to create a SSL socket from a factory (providing the
     * certificates)
     */
    private SSLContext context;
    /**
     * Password of the serverKey.jks file containing the keys for the server
     */
    private static final String PASSWORD_SERVERJKS = "password";
    /**
     * Password of the serverTrustedCerts.jks file containing the clients
     * trusted
     */
    private static final String PASSWORD_TRUSTEDJKS = "password";
    /**
     * Application Service for managing users
     */
    private UserManagerAS userManager;
    /**
     * Application Service for sharing and importing projects
     */
    private ShareManagerAS shareManager;
    /**
     * Application Service for managing a project
     */
    private WorkAS workManager;

    /**
     * Server constructor with no parameters. The port used will be the default
     * port 8080
     */
    public Server() {
        applicationServiceInitialization();
        PORT = 8080;
    }

    /**
     * Server constructor specifying the port which will be used.
     *
     * @param _PORT
     */
    public Server(int _PORT) {
        applicationServiceInitialization();
        this.PORT = _PORT;
    }

    /**
     * Initializes the Application Service provided. It access to the databases
     * and initializes the Application Service to be ready to manage them.
     */
    private void applicationServiceInitialization() {
        userManager = UserManagerAS.getManager(new UserDAOSQLImp());
        // shareManager = ShareManagerAS.getManager(new SharedProjectDAOSQLImp(),
        //         new AuthorshipDAOSQLImp());
        workManager = new WorkAS(new ProjectDTO("empty_project"));
    }

    /**
     * Initializes the SSL context checking the certificates. It reads the files
     * with the keys and certificates of the trusted clients and allows the
     * creation of a context to create SSL sockets (RSA algorithm encryption
     * used).
     *
     * @throws ServerSSLException whenever a certificate is corrupted or cannot
     * be accessed
     */
    private void initializeSSLContext() throws ServerSSLException {
        KeyManager[] keyManagers = null;
        TrustManager[] trustManagers = null;

        //Server key certificates storage access
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            URL resource = Server.class.getClassLoader()
                    .getResource("certs/server/serverKey.jks");
            File serverKeyFile = new File(resource.toURI());
            FileInputStream fileStream = new FileInputStream(serverKeyFile);
            keyStore.load(fileStream, PASSWORD_SERVERJKS.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, PASSWORD_SERVERJKS.toCharArray());
            keyManagers = kmf.getKeyManagers();
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException
                | UnrecoverableKeyException | URISyntaxException ex) {
            throw new ServerSSLException("Unexpected exception at server key certificates storage", ex);
        }

        // Trusted certificates storage access
        try {
            KeyStore trustedStore = KeyStore.getInstance(KeyStore.getDefaultType());
            URL resource = Server.class.getClassLoader()
                    .getResource("certs/server/serverTrustedCerts.jks");
            File serverKeyTrustedFile = new File(resource.toURI());
            FileInputStream fileStream = new FileInputStream(serverKeyTrustedFile);
            trustedStore.load(fileStream, PASSWORD_TRUSTEDJKS.toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustedStore);
            trustManagers = tmf.getTrustManagers();
        } catch (NoSuchAlgorithmException | URISyntaxException | KeyStoreException | IOException | CertificateException ex) {
            throw new ServerSSLException("Unexpected exception at trusted certificates storage", ex);
        }

        // Context instantiation
        try {
            context = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException ex) {
            throw new ServerSSLException("Unexpected exception at getting instance TLS", ex);
        }
        // Initializing context given keyManagers and trustManagers certificates
        try {
            context.init(keyManagers, trustManagers, null);
        } catch (KeyManagementException e) {
            throw new ServerSSLException("Unexpected exception context.init", e);
        }
    }

    /**
     * Initializes the server. It creates a context to generate a new server
     * socket. Once created the main server socket the server will be start
     * listening from the port indicated.
     */
    private void initializeServer() {

        //  Initializes SSL by setting the certificates location
        try {
            initializeSSLContext();
        } catch (ServerSSLException e) {
            throw new RuntimeException("Cannot configurate SSL context", e);
        }
        //  SSL server socket creation
        try {
            SSLServerSocketFactory ssf = context.getServerSocketFactory();
            serverSocket = (SSLServerSocket) ssf.createServerSocket(PORT);
            serverSocket.setNeedClientAuth(false);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + PORT, e);
        }
    }

    /**
     * Method to show messages with the date when they were received. This
     * method allows the class to change how data is portrayed.
     *
     * @param msg
     */
    protected void displayMessage(String msg) {
        System.out.println("[" + dataTimeFormat.format(new Date()) + "] " + msg);
    }

    /**
     * Runs the server. It creates a new Socket listening from the port
     * indicated, after that each new client which tries to connect to the
     * server is added to the list an a new <code>ClientThread</code> is
     * assigned to.
     *
     * @throws RuntimeException
     */
    public void run() throws RuntimeException {
        // The main thread should be modified just one at a time
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        // Starts listening at the port given
        initializeServer();

        displayMessage("Server starts running!");

        // Main loop, while not stop
        while (!stopsRunning) {
            // Creates and tries to accept a new client
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if (stopsRunning) {
                    displayMessage("Server stops running!");
                    break; // breaks the loop iteration
                }
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }

            //Clients successfully accepted
            displayMessage("New client accepted ["
                    + clientSocket.getLocalAddress().getHostAddress() + "]");

            //We add the client to the clients map
            try {
                clientsListeners.put(idGenerator, new ClientThread(idGenerator, clientSocket, this));
                clientsListeners.get(idGenerator).run();
            } catch (IOException e) {
                clientsListeners.remove(idGenerator);
                throw new RuntimeException("Error at client creation, client id: "
                        + idGenerator, e);
            }
            idGenerator++;
        }
    }

    /**
     * Stops the execution of the server. This method closes the server socket
     * and prevents from listening from more clients. It also closes each client
     * socket opened for listening.
     */
    public synchronized void stop() {
        // stops main loop
        stopsRunning = true;
        // Closing the server socket
        if (serverSocket != null) {
            try {

                this.serverSocket.close();
                serverSocket = null;
                displayMessage("Server notifies clients the shutdown!");

            } catch (IOException e) {
                throw new RuntimeException("Error while closing server", e);
            }

            // Closing streams opened by clients
            clientsListeners.values().forEach((ClientThread client) -> {
                try {
                    client.close();
                } catch (IOException e) {
                    throw new RuntimeException("Error while closing client with id " + client.getID(), e);
                }

            });
        }
    }

    /**
     * Provides a way of registering a logging out from a client. A client could
     * call this method by indicating its thread and therefore it will be
     * removed from the list of listening clients.
     *
     * @param client to be removed
     * @throws IOException thrown whenever closing sockets connection fails
     */
    protected void clientLogout(ClientThread client) throws IOException {
        // remove from the client list
        clientsListeners.remove(client.getID());
        // closing client sockets and streams
        client.close();
    }

    public void addComment() {
        return;
    }

}
