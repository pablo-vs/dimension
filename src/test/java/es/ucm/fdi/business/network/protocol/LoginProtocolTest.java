/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ucm.fdi.business.network.protocol;

import es.ucm.fdi.business.exceptions.network.ProtocolException;
import es.ucm.fdi.business.network.messages.client.ClientMessage;
import es.ucm.fdi.business.network.messages.client.RequestImage;
import es.ucm.fdi.business.network.messages.client.RequestLogin;
import es.ucm.fdi.business.users.UserDTO;
import es.ucm.fdi.business.users.UserManagerAS;
import es.ucm.fdi.business.users.UserType;
import es.ucm.fdi.integration.users.UserDAO;
import es.ucm.fdi.integration.users.UserDAOHashTableImp;
import java.time.Instant;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit test for LoginProtocol class.
 *
 * @see LoginProtocol
 * @author Arturo Acuaviva
 */
public class LoginProtocolTest {

    private LoginProtocol protocolObject;

    /**
     * Creates a login protocol object for a database contanining users.
     */
    @Before
    public void setUp() {
        // Create the database
        UserDAO db = new UserDAOHashTableImp();
        // create the protocol to manage the database
        UserManagerAS manager = UserManagerAS.getManager(db);
        manager.addNewUser(new UserDTO("user1", "James", "eda3eda", Date.from(Instant.now()),
                "jamesEDA@is.es", "600323421", "NULL", "Python programmer", UserType.USER,
                null));
        manager.addNewUser(new UserDTO("user2", "Charles", "cr#sd", Date.from(Instant.now()),
                "charleston@is.es", "9482039341", "NULL", "Java programmer", UserType.ADMIN,
                null));
        protocolObject = new LoginProtocol(manager);

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
        msg.password = "asdf1234";
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
