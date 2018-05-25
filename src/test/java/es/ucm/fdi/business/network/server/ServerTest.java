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

import org.junit.After;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for Server class.
 *
 * @see Server
 * @author Arturo Acuaviva
 */
public class ServerTest {

    /**
     * Server object to be tested
     */
    Server serverTest;
    /**
     * The port where the server starts listening
     */
    private static final int PORT = 9000;

    /**
     * Empty class constructor
     */
    public ServerTest() {
    }

    /**
     * Initializes the server and sets up the port.
     */
    @Before
    public void setUp() {
        serverTest = new Server(PORT);
    }

    /**
     * Closes the server.
     */
    @After
    public void tearDown() {
        serverTest.stop();
    }

    @Test
    public void testCreateAndStopServer() {
        System.out.println("Creation and stopping of server test");
        try {
            // Starts listening
            new Thread(serverTest).start();
            Thread.sleep(500);
            // Stops the server
            serverTest.stop();
        } catch (Exception e) {
            fail("The server was correctly set up!");
        }

    }

}
