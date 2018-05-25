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

import es.ucm.fdi.business.network.messages.client.ClientMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * ClientThread represents a thread of a client handled by the server. The class
 * is built using a socket from which it will creates its own output and input
 * streams. It also contains a reference to the server in order to call
 * notification methods just in case of receiving packages such as logout which
 * needs the server to erase the user from the users list. A Client thread is
 * capable of handling the connection with the client, from the welcome to the
 * logout, listening to packages being sent by a certain user connected to the
 * server.
 *
 * @see Socket
 * @see Server
 * @author Arturo Acuaviva
 */
public class ClientThread implements Runnable {

    /**
     * Client socket which provides the streams for input/output
     */
    protected final Socket clientSocket;
    /**
     * Clients identification number
     */
    private final int id;
    /**
     * Stream for input data received from the client
     */
    private final ObjectInputStream streamInput;
    /**
     * Streams for output data to send information to the client
     */
    private final ObjectOutputStream streamOutput;
    /**
     * Reference to the server
     */
    private final Server server;
    /**
     * Controls the execution of the listening loop
     */
    private volatile boolean keepListening = true;

    /**
     * Class Constructor. It receives the identification of the client, a socket
     * assigned by the server to listen from the client and a reference of the
     * server.
     *
     * @param id client identification
     * @param clientSocket socket listening from the client
     * @param server reference to the main server
     * @throws IOException
     */
    ClientThread(int id, Socket clientSocket, Server server) throws IOException {
        this.server = server;
        this.id = id;
        this.clientSocket = clientSocket;
        streamOutput = new ObjectOutputStream(clientSocket.getOutputStream());
        streamOutput.flush();
        streamInput = null;
        // streamInput = new ObjectInputStream(clientSocket.getInputStream());
    }

    /**
     * Not finished yet. A run method for the client thread is supposed to
     * handle the clients connection. The implementation of a full-working
     * server is far from the objectives and goals of the subjects "Ingenieria
     * del Software". This method represents a sketch of what the method would
     * look like or how it would be structured when it is implementated.
     */
    @Override
    public void run() {
        while (keepListening) {
            try {
                ClientMessage message = (ClientMessage) streamInput.readObject();
                switch (message.getType()) {
                    case ClientMessage.REQUEST_LOGIN: {
                        server.displayMessage("Received from " + clientSocket.getLocalAddress()
                                + " with client.id=" + getID() + " a REQUEST_LOGIN");
                        break;
                    }
                    case ClientMessage.REQUEST_REGISTER: {
                        server.displayMessage("Received from " + clientSocket.getLocalAddress()
                                + " with client.id=" + getID() + " a REQUEST_REGISTER");
                        break;
                    }
                    case ClientMessage.REQUEST_LOGOUT: {
                        server.displayMessage("Received from " + clientSocket.getLocalAddress()
                                + " with client.id=" + getID() + " a REQUEST_LOGOUT");
                        break;
                    }
                    case ClientMessage.REQUEST_IMAGE: {
                        server.displayMessage("Received from " + clientSocket.getLocalAddress()
                                + " with client.id=" + getID() + " a REQUEST_IMAGE");
                        break;
                    }
                    default:
                        server.displayMessage("Received from " + clientSocket.getLocalAddress()
                                + " with client.id=" + getID() + " an unidentifiable CLIENT_MESSAGE");
                        break;
                }
            } catch (IOException | ClassNotFoundException ex) {
                server.displayMessage("Received from " + clientSocket.getLocalAddress()
                        + " with client.id=" + getID() + " a non-valid package");
            }
        }
    }

    /**
     * Tries to close all streams channels.
     *
     * @throws IOException
     */
    public void close() throws IOException {
        //    streamOutput.print(new ServerMessages(ServerMessages.LOG_OUT));
        if (streamInput != null) {
            streamInput.close();
        }
        if (streamOutput != null) {
            streamOutput.close();
        }
        if (clientSocket != null) {
            clientSocket.close();
        }
    }

    /**
     * Getter method.
     *
     * @return the id of the client thread
     */
    public int getID() {
        return id;
    }
}
