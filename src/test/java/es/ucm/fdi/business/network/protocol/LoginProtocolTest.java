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
package es.ucm.fdi.business.network.protocol;

import es.ucm.fdi.business.exceptions.network.ProtocolException;
import es.ucm.fdi.business.network.messages.client.ClientMessage;
import es.ucm.fdi.business.network.messages.client.RequestImage;
import es.ucm.fdi.business.network.messages.client.RequestLogin;
import es.ucm.fdi.business.users.SessionDTO;
import es.ucm.fdi.business.users.UserDTO;
import es.ucm.fdi.business.users.UserManagerAS;
import es.ucm.fdi.business.util.HashGenerator;
import es.ucm.fdi.integration.users.UserDAO;
import es.ucm.fdi.integration.users.UserDAOHashTableImp;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.AfterClass;

/**
 * JUnit test for LoginProtocol class.
 *
 * @see LoginProtocol
 * @author Arturo Acuaviva
 */
public class LoginProtocolTest {

    private static LoginProtocol protocolObject;
    private static HashGenerator hashgen = new HashGenerator();
    private static UserManagerAS manager;
    private static SessionDTO session;

    /**
     * Creates a login protocol object for a database contanining users.
     */
    @BeforeClass
    public static void setUp() {
        // Create the database
        UserDAO db = new UserDAOHashTableImp();
        // create the protocol to manage the database
        manager = UserManagerAS.getManager(db);
        manager.addNewUser(new UserDTO("James", hashgen.hash("eda3eda".toCharArray())));
        protocolObject = new LoginProtocol(manager);
        session = manager.login("James", "eda3eda");
    }

    @AfterClass
    public static void clear() {
        manager.removeUser("James", session);
        manager.logout(session);
    }

    /**
     * Test of apply method, of class LoginProtocol, using a non-valid package.
     */
    @Test
    public void testApplyIncorrectPackage() {
        System.out.println("apply with incorrect package");
        ClientMessage msg = new RequestImage();
        try {
            protocolObject.apply(msg);
            fail("Non valid package used!");
        } catch (ProtocolException e) {
            // That's fine, the exception was correctly caugth
        }
    }

    /**
     * Test of apply method, of class LoginProtocol, using a corrupted package.
     * The package received is an empty request login package.
     */
    @Test
    public void testApplyCorruptedPackage() {
        System.out.println("apply with corrupted package");
        ClientMessage msg = new RequestLogin();
        try {
            protocolObject.apply(msg);
            fail("A corrupted package used!");
        } catch (ProtocolException e) {
            // That's fine, the exception was correctly caugth
        }
    }

    /**
     * Test of apply method, of class LoginProtocol, using a package filled up
     * with incorrect values. The user in the package does not exist.
     */
    @Test

    public void testApplyNonExistingUser() {
        System.out.println("apply with package contanining a non-existing user");
        RequestLogin msg = new RequestLogin();
        msg.username = "Patty";
        msg.password = "asdf1234";
        try {
            if (protocolObject.apply(msg)) {
                fail("LoginProtocol should return false for a non-existing user");
            }
        } catch (ProtocolException e) {
            fail("The package was correctly set up!");
        }
    }

    /**
     * Test of apply method, of class LoginProtocol, using a package containing
     * an existing user but with an incorrect password.
     */
    @Test
    public void testApplyIncorrectPassword() {
        System.out.println("apply with package with incorrect password");
        RequestLogin msg = new RequestLogin();
        msg.username = "James";
        msg.password = "eda35ed3a";
        try {
            if (protocolObject.apply(msg)) {
                fail("LoginProtocol should return false for an incorrect password!");
            }
        } catch (ProtocolException e) {
            fail("The package was correctly set up!");
        }
    }

    /**
     * Test of apply method, of class LoginProtocol, using a correct package.
     */
    @Test
    public void testApplyCorrectPackage() throws Exception {
        System.out.println("apply with correct package");
        RequestLogin msg = new RequestLogin();
        msg.username = "James";
        msg.password = "eda3eda";
        try {
            if (!protocolObject.apply(msg)) {
                fail("A correct user and password must return true!");
            }
        } catch (ProtocolException e) {
            fail("The package was correctly set up!");
        }
    }

    /**
     * Test of getSession method, of class LoginProtocol.
     */
    @Test
    public void testGetSession() {
        System.out.println("getSession");
        RequestLogin msg = new RequestLogin();
        msg.username = "James";
        msg.password = "eda3eda";
        try {
            if (!protocolObject.apply(msg)) {
                fail("A correct user and password must return true!");
            }
        } catch (ProtocolException e) {
            fail("The package was correctly set up!");
        }
        assertTrue("A valid session must be loaded into the protocol due to James log-in!",
                protocolObject.getSession() != null);
    }

}
