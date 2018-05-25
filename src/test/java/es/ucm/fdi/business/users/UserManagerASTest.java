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
package es.ucm.fdi.business.users;

import es.ucm.fdi.business.exceptions.DuplicatedIDException;
import es.ucm.fdi.integration.users.UserDAO;
import es.ucm.fdi.integration.users.UserDAOHashTableImp;
import java.time.ZonedDateTime;
import java.security.AccessControlException;
import org.junit.Test;
import static org.junit.Assert.*;
import es.ucm.fdi.business.util.HashGenerator;
import es.ucm.fdi.business.exceptions.NotFoundException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * JUnit test for UserManagerAS class.
 *
 * @see UserManagerAS
 */
public class UserManagerASTest {

    /**
     * Database to be used during the tests
     */
    UserDAOHashTableImp database;
    /**
     * User manager Application Service
     */
    UserManagerAS manager;
    /**
     * Hash generator for passwords creation
     */
    HashGenerator hashgen;

    public UserManagerASTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        // This method is added in case a DB SQL test is carried out and the 
        // database needs to be initialize
    }

    @AfterClass
    public static void tearDownClass() {
        // This method is added in case a DB SQL test is carried out and the 
        // database needs to be set back to how it was before the tests. 
    }

    /**
     * Initializes all the values to be used during the tests.
     */
    @Before
    public void setUp() {
        database = new UserDAOHashTableImp();
        database.addUser(new UserDTO("removing", "physics1234"));
        manager = UserManagerAS.getManager(database);
        hashgen = new HashGenerator();
        database.addUser(new UserDTO("us1", "Zucker", hashgen.hash("1234".toCharArray()),
                null, null, null, null, null, UserType.ADMIN, null));
        database.addUser(new UserDTO("normalUser", "Jackie", hashgen.hash("1234".toCharArray()),
                null, null, null, null, null, UserType.USER, null));
        database.addUser(new UserDTO("removeItself", "Suicide", hashgen.hash("1234".toCharArray()),
                null, null, null, null, null, UserType.USER, null));
    }

    /**
     * UserManagerAS test for operations on user management.
     */
    @Test
    public void testUserManagement() {
        System.out.println("User management");
        UserManagerAS userMgr = UserManagerAS.getManager(new UserDAOHashTableImp());
        userMgr.addNewUser(new UserDTO("Ash", hashgen.hash("1234".toCharArray())));
        userMgr.addNewUser(new UserDTO("Tim", hashgen.hash("4321".toCharArray())));
        SessionDTO peterSession = userMgr.login("Ash", "1234");
        SessionDTO timSession = userMgr.login("Tim", "4321");
        try {
            userMgr.removeUser("Ash", timSession);
            fail("Illegal account access!");
        } catch (AccessControlException e) {
            //System.out.println("Authentication works");
        }

        userMgr.removeUser("Ash", peterSession);
        userMgr.logout(peterSession);
        try {
            peterSession = userMgr.login("Ash", "1234");
            fail("Logged in to nonexistent account!");
        } catch (NotFoundException e) {
            // OK
        }
    }

    /**
     * Test for invalid session.
     */
    @Test
    public void testInvalidSession() {
        System.out.println("Invalid session");
        UserDAO dao = new UserDAOHashTableImp();
        String passwd = hashgen.hash("1234".toCharArray());
        UserDTO user = new UserDTO("Peter", passwd);
        UserManagerAS userMgr = UserManagerAS.getManager(dao);
        userMgr.addNewUser(user);
        try {
            SessionDTO sesion = userMgr.login("Peter", "12345");
            fail("Session should have not begun.");
        } catch (AccessControlException e) {
            // OK
        }
        SessionDTO sesion = new SessionDTO("Peter", ZonedDateTime.now());
        assertFalse(userMgr.validateSession(sesion));
    }

    /**
     * Logs up a new user in the system. It adds a new valid user to the db.
     */
    @Test
    public void testRegisterValidUser() {
        System.out.println("Register valid user");
        // non empty user test log up
        UserDTO newUser = new UserDTO("user1", "James", hashgen.hash("maths1234".toCharArray()), null,
                "jamesbond@yahoo.com", "683923212", "https://i.blogs.es/6eda63/jamesbond1/450_1000.jpg",
                "Your favourite secret agent", UserType.USER, null);
        try {
            manager.addNewUser(newUser);
        } catch (IllegalArgumentException ex) {
            fail("The user was correctly set up but illegal argument exception was thrown!");
        } catch (DuplicatedIDException ex) {
            fail("The user was correctly set up but duplicated id exception was thrown!");
        }

    }

    /**
     * Tries to create a new non valid user. This method test if a new user can
     * be registered with a non valid id.
     */
    @Test
    public void testRegisterUserNonValidID() {
        System.out.println("Register invalid user (illegal id)");

        UserDTO newUser = new UserDTO("@user", null, hashgen.hash("maths1234".toCharArray()), null,
                null, null, null, null, UserType.USER, null);
        try {
            manager.addNewUser(newUser);
            fail("The user id was not valid!");
        } catch (IllegalArgumentException ex) {
            // Expected exception caught...
        } catch (DuplicatedIDException ex) {
            fail("The user id was not valid, it should have failed when checking the id!!");
        }
    }

    /**
     * Tries to create a new non valid user. This method test if a new user can
     * be registered with a non valid name.
     */
    @Test
    public void testRegisterUserNonValidName() {
        System.out.println("Register invalid user (illegal name)");

        UserDTO newUser = new UserDTO("user2", "#Jammy", hashgen.hash("maths1234".toCharArray()), null,
                null, null, null, null, UserType.USER, null);
        try {
            manager.addNewUser(newUser);
            fail("The user name was not valid!");
        } catch (IllegalArgumentException ex) {
            // Expected exception caught...
        } catch (DuplicatedIDException ex) {
            fail("The user name was not valid, it should have failed when checking the name!");
        }
    }

    /**
     * Tries to create a new non valid user. This method test if a new user can
     * be registered with a non valid password.
     */
    @Test
    public void testRegisterUserNonValidPassword() {
        System.out.println("Register invalid user (illegal password)");
        UserDTO newUser = new UserDTO("user3", null, "maths1234", null,
                null, null, null, null, UserType.USER, null);
        try {
            manager.addNewUser(newUser);
            fail("The user password was not valid!");
        } catch (IllegalArgumentException ex) {
            // Expected exception caught...
        } catch (DuplicatedIDException ex) {
            fail("The user password was not valid, it should have failed when checking the password!");
        }
    }

    /**
     * Tries to create a new non valid user. This method test if a new user can
     * be registered with a non valid description.
     */
    @Test
    public void testRegisterUserNonValidDescription() {
        System.out.println("Register invalid user (illegal description)");
        UserDTO newUser = new UserDTO("user4", null, hashgen.hash("maths1234".toCharArray()), null,
                null, null, null, "#@|44tdf dfsfds#@d º !¡'?4", UserType.USER, null);
        try {
            manager.addNewUser(newUser);
            fail("The user description was not valid!");
        } catch (IllegalArgumentException ex) {
            // Expected exception caught...
        } catch (DuplicatedIDException ex) {
            fail("The user description was not valid, it should have failed when checking the description!");
        }
    }

    /**
     * Tries to create a new non valid user. This method test if a new user can
     * be registered with a non valid email.
     */
    @Test
    public void testRegisterUserNonValidEmail() {
        System.out.println("Register invalid user (illegal email)");
        UserDTO newUser = new UserDTO("user5", null, hashgen.hash("maths1234".toCharArray()), null,
                "google.com", null, null, null, UserType.USER, null);
        try {
            manager.addNewUser(newUser);
            fail("The user email was not valid!");
        } catch (IllegalArgumentException ex) {
            // Expected exception caught...
        } catch (DuplicatedIDException ex) {
            fail("The user email was not valid, it should have failed when checking the email!");
        }
    }

    /**
     * Tries to create a new non valid user. This method test if a new user can
     * be registered with a non valid telephone.
     */
    @Test
    public void testRegisterUserNonValidTelephone() {
        System.out.println("Register invalid user (illegal email)");
        UserDTO newUser = new UserDTO("user5", null, hashgen.hash("maths1234".toCharArray()), null,
                null, "-3", null, null, UserType.USER, null);
        try {
            manager.addNewUser(newUser);
            fail("The user telephone was not valid!");
        } catch (IllegalArgumentException ex) {
            // Expected exception caught...
        } catch (DuplicatedIDException ex) {
            fail("The user telephone was not valid, it should have failed when checking the telephone!");
        }
    }

    /**
     * Tries to create a new non valid user. This method test if a new user can
     * be registered with a non valid picture.
     */
    @Test
    public void testRegisterUserNonValidPicture() {
        System.out.println("Register invalid user (illegal email)");
        UserDTO newUser = new UserDTO("user5", "Jammy", hashgen.hash("maths1234".toCharArray()), null,
                null, null, "non_valid.jpg", null, UserType.USER, null);
        try {
            manager.addNewUser(newUser);
            fail("The user picture was not valid!");
        } catch (IllegalArgumentException ex) {
            // Expected exception caught...
        } catch (DuplicatedIDException ex) {
            fail("The user picture was not valid, it should have failed when checking the picture!");
        }
    }

    /**
     * Tries to create a valid user with the same id of an already existing
     * user.
     */
    @Test
    public void testRegisterExistingUser() {
        System.out.println("Register existing user");
        UserDTO newUser = new UserDTO("user1", "Nicky", hashgen.hash("physics12aa".toCharArray()),
                null, null, null, null, null, UserType.USER, null);
        try {
            manager.addNewUser(newUser);
            fail("The user was already registered in the system!");
        } catch (IllegalArgumentException ex) {
            fail("The user was already registered in the system, not checking process should be going on!");
        } catch (DuplicatedIDException ex) {
            // Expected exception caught...
        }
    }

    /**
     * Test for removing users. It tries to remove non-existing user.
     */
    @Test
    public void testRemovingNonExistingUser() {
        System.out.println("Removing non existing user");
        SessionDTO adminSession = manager.login("us1", "1234");
        try {
            manager.removeUser("us233", adminSession);
            fail("Non-existing user trying to be removed!");
        } catch (NotFoundException ex) {
            //Expected exception caught
        } catch (AccessControlException ex) {
            fail("An admin should have remove permissions!");
        }

    }

    /**
     * Test for removing users. It tries to remove an user being an admin.
     */
    @Test
    public void testRemovingExistingUser() {
        System.out.println("Admin removing user");
        SessionDTO adminSession = manager.login("us1", "1234");
        try {
            manager.removeUser("removing", adminSession);
        } catch (NotFoundException ex) {
            fail("The user does exist!");
        } catch (AccessControlException ex) {
            fail("An admin should have remove permissions!");
        }
    }

    /**
     * Test for removing users. It tries to remove an user being a normal user.
     */
    @Test
    public void testNormalUserRemoving() {
        System.out.println("Normal user removing user");
        SessionDTO normalUserSession = manager.login("normalUser", "1234");
        try {
            manager.removeUser("us1", normalUserSession);
            fail("An user should have no remove permissions!");
        } catch (NotFoundException ex) {
            fail("The user does exist!");
        } catch (AccessControlException ex) {
            // Expected exception caught
        }
    }

    /**
     * Test for removing users. An user tries to remove itself.
     */
    @Test
    public void testUserRemovingItself() {
        System.out.println("Normal user removing themselves");
        SessionDTO normalUserSession = manager.login("removeItself", "1234");
        try {
            manager.removeUser("removeItself", normalUserSession);
        } catch (NotFoundException ex) {
            fail("The user does exist!");
        } catch (AccessControlException ex) {
            fail("An user should have remove permissions over themselves!");
        }
    }
    
    

}
