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

import es.ucm.fdi.business.exceptions.network.ServerSSLException;
import java.io.FileInputStream;
import java.net.Socket;
import java.io.IOException;
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
 * Implementation
 *
 * There is a problem with the idGenerator
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
     *
     */
    private SSLServerSocketFactory sslServerSocketFactory;

    private SSLContext context;

    /**
     * Server constructor with no parameters. The port used will be the default
     * port 8080
     */
    public Server() {
        PORT = 8080;
    }

    /**
     * Server constructor specifying the port which will be used.
     *
     * @param _PORT
     */
    public Server(int _PORT) {
        this.PORT = _PORT;
    }

    private void initializeSSLContext() throws ServerSSLException {
        KeyManager[] keyManagers = null;
        TrustManager[] trustManagers = null;

        //Server key certificates storage access
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            FileInputStream file = new FileInputStream("src/main/resources/certs/server/serverKey.jks");
            keyStore.load(file, "servpass".toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, "servpass".toCharArray());
            keyManagers = kmf.getKeyManagers();
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException
                | UnrecoverableKeyException ex) {
            throw new ServerSSLException("Unexpected exception at server key certificates storage", ex);
        }

        // Trusted certificates storage access
        try {
            KeyStore trustedStore = KeyStore.getInstance(KeyStore.getDefaultType());            
            FileInputStream file = new FileInputStream("src/main/certs/server/serverTrustedCerts.jks");
            trustedStore.load(file,"servpass".toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustedStore);
            trustManagers = tmf.getTrustManagers();
        } catch (NoSuchAlgorithmException | KeyStoreException | IOException | CertificateException ex) {
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

    private void initializeServer() {

        // Initializes SSL by setting the certificates location
        try{
            initializeSSLContext();
        } catch(ServerSSLException e){
            throw new RuntimeException("Cannot configurate SSL context", e);
        } 
        // SSL server socket creation
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
     * method allows the class to change how data is showed.
     *
     * @param msg
     */
    private void displayMessage(String msg) {
        System.out.println( "[" + dataTimeFormat.format(new Date()) + "] " + msg);
    }
    /**
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

    public synchronized void stop() {
        // stops main loop
        stopsRunning = true;
        
        // Closing the server socket
        try {
            this.serverSocket.close();
            displayMessage("Server is closing!");
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

    private void clientLogout(ClientThread client) throws IOException{
        // remove from the client list
        clientsListeners.remove(client.getID()); 
        // closing client sockets and streams
        client.close();
    }
    


    
    
    
    
    
    
    
    
}
