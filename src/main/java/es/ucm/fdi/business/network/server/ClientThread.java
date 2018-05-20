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

import es.ucm.fdi.business.exceptions.network.UnrecognizablePackageException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
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
    private final PrintWriter streamOutput;
    /**
     * Reference to the server
     */
    private final Server server;

    /**
     * Class Constructor. It receives the identification of the client, a socket
     * assigned by the server to listen from the client and a reference of the server.
     * @param id client identification
     * @param clientSocket socket listening from the client
     * @param server reference to the main server
     * @throws IOException 
     */
    ClientThread(int id, Socket clientSocket, Server server) throws IOException {
        this.server = server;
        this.id = id;
        this.clientSocket = clientSocket;
        streamInput = new ObjectInputStream(clientSocket.getInputStream());
        streamOutput = new PrintWriter(clientSocket.getOutputStream());
    }

    @Override
    public void run() {
        // sd

    }

    public boolean welcomeClient() throws UnrecognizablePackageException {
        return false;
    }

    private String waitMessage() throws UnrecognizablePackageException {
        return null;
    }

    public void close() throws IOException {
        //    streamOutput.print(new ServerMessages(ServerMessages.LOG_OUT));
        streamInput.close();
        streamOutput.close();
        clientSocket.close();
    }

    public int getID() {
        return id;
    }
}
