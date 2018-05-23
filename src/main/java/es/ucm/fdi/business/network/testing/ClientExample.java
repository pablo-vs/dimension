package es.ucm.fdi.business.network.testing;

import es.ucm.fdi.business.network.messages.client.RequestLogin;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Client created for the sake of testing the Server. The client is incomplete
 * and should not be used as a full-working client.
 *
 * @author Arturo Acuaviva
 */
public class ClientExample {

    private ObjectInputStream sInput;		// to read from the socket
    private ObjectOutputStream sOutput;		// to write on the socket
    private SSLSocket socket;					// socket object
    private final String server;
    private final String username;	// server and username
    private final int port;

    ClientExample(String server, int port, String username) {
        this.server = server;
        this.port = port;
        this.username = username;
    }

    public boolean start() {
        // try to connect to the server
        try {
            SocketFactory socketFactory = SSLSocketFactory.getDefault();
            socket = (SSLSocket) socketFactory.createSocket(server, port);
        } // exception handler if it failed
        catch (IOException ec) {
            display("Error connectiong to server:" + ec);
            return false;
        }

        String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
        display(msg);

        /* Creating both Data Stream */
        try {
            sOutput = new ObjectOutputStream(socket.getOutputStream());
            sOutput.flush();
            sInput = new ObjectInputStream(socket.getInputStream());
            display("INPUT/OUTPUT created");
        } catch (IOException eIO) {
            display("Exception creating new Input/output Streams: " + eIO);
            return false;
        }

        // creates the Thread to listen from the server 
        new ListenFromServer().start();

        return true;
    }

    private void display(String msg) {

        System.out.println("CLIENTE: " + msg);

    }

    public void close() {
        disconnect();
    }

    private void disconnect() {
        try {
            if (sInput != null) {
                sInput.close();
            }
        } catch (IOException e) {
        }
        try {
            if (sOutput != null) {
                sOutput.close();
            }
        } catch (IOException e) {
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
        }

    }

    private class ListenFromServer extends Thread {

        public void run() {
            while (true) {
                try {
                    // System.out.println("Trying to send a message");
                    sOutput.writeObject(new RequestLogin());
                } catch (Exception e) {
                    display("*** Server has closed the connection: " + e + "***");
                    break;
                }
            }
        }
    }
}
