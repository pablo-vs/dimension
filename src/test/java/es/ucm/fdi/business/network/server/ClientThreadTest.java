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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit test for ClientThread class.
 *
 * @see ClientThread
 * @author Arturo Acuaviva
 */
public class ClientThreadTest {

    /**
     * Server port
     */
    private static final int PORT = 8000;
    /**
     * Host where the server runs
     */
    private static final String HOST = "localhost";
    /**
     * Server socket
     */
    private ServerSocket serverSocket = null;
    /**
     * Client socket
     */
    private Socket clientSocket = null;
    /**
     * Example of a server created ad hoc
     */
    private Server testServer = new Server();
    /**
     * ClientThread to be tested
     */
    private ClientThread client;

    /**
     * Sets up the server. The server starts to listen from the port indicated.
     */
    @Before
    public void setUp() {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            fail("Port of the test server cannot be opened for ClientThreadTest");
        }
    }

    /**
     * Turns off the streams opened to handle the connections.
     */
    @After
    public void tearDown() {

        try {
            client.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientThreadTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Receives a connection from a new client created in another thread.
     */
    @Test
    public void testHandleConnection() {

        (new Thread() {
            ObjectOutputStream out;
            ObjectInputStream in;

            void sendRequestLogin() {

            }

            @Override
            public void run() {
                try {
                    clientSocket = new Socket(HOST, PORT);
                    out = new ObjectOutputStream(clientSocket.getOutputStream());
                    in = new ObjectInputStream(clientSocket.getInputStream());
                } catch (UnknownHostException e) {
                    fail("The server was correctly set up and so was the client! ");
                } catch (IOException e) {
                    fail("The server was correctly set up and so was the client! ");
                }
            }
        }).start();
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            fail("Error while connecting client to server!");
        }
        try {
            client = new ClientThread(1, clientSocket, testServer);
        } catch (IOException ex) {
            Logger.getLogger(ClientThreadTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertTrue("ClientThread was created from the clientSocket", client != null);

    }

}
