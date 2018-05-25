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
package es.ucm.fdi.usecases;

import es.ucm.fdi.business.exceptions.DuplicatedIDException;
import es.ucm.fdi.business.users.UserDTO;
import es.ucm.fdi.business.users.UserManagerAS;
import es.ucm.fdi.business.util.HashGenerator;
import es.ucm.fdi.integration.users.UserDAOHashTableImp;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Use case test: registering a new user in the system. This test could be taken
 * as an integration test using a UserManagerAS. More information on how
 * UserManagerAS handles the registration process can be found on UserManagerAS
 * test class:
 *
 * @see UserManagerASTest
 * @author Arturo Acuaviva
 */
public class RegisterUCTest {

    /**
     * Database to be used during the tests
     */
    private UserDAOHashTableImp database;
    /**
     * User manager Application Service
     */
    private UserManagerAS manager;
    /**
     * Hash generator for passwords creation
     */
    private HashGenerator hashgen;

    /**
     * User test id
     */
    private final static String USER_ID = "Nick";
    /**
     * User test password
     */
    private final static String USER_PASSWORD = "1234";
    /**
     * New user test id
     */
    private final static String NEW_USER_ID = "James";
    /**
     * New user test password
     */
    private final static String NEW_USER_PASSWORD = "lemon1234";
    /**
     * Non valid id
     */
    private final static String NON_VALID_ID = "4sad@#·";

    public RegisterUCTest() {
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
        hashgen = new HashGenerator();
        database.addUser(new UserDTO(USER_ID,
                hashgen.hash(USER_PASSWORD.toCharArray())));
        manager = UserManagerAS.getManager(database);
    }

    /**
     * Test the use case when the user is registered correctly.
     */
    @Test
    public void testUseCaseRegisteringCorrectly() {
        System.out.println("Use case registering (valid user logging)");
        UserDTO newUser = new UserDTO(NEW_USER_ID,
                hashgen.hash(NEW_USER_PASSWORD.toCharArray()));
        try {
            manager.addNewUser(newUser);
        } catch (IllegalArgumentException | DuplicatedIDException ex) {
            fail("No expected exception thrown!");
        }
        assertTrue("The new user has been added to the db", manager.existsUser(NEW_USER_ID));
    }

    /**
     * Test the use case when the user is registered incorrectly. For more
     * details on testing the registration process check the UserManagerAS test
     * class.
     *
     * @see UserManagerASTest
     */
    @Test
    public void testUseCaseRegisteringIncorrectly() {
        System.out.println("Use case registering (non-valid user logging: user already exists)");
        UserDTO newUser = new UserDTO(USER_ID,
                hashgen.hash(USER_PASSWORD.toCharArray()));
        try {
            manager.addNewUser(newUser);
            fail("A new user equals to the new one already exists!");
        } catch (IllegalArgumentException ex) {
            fail("The new user details are valid!");
        } catch (DuplicatedIDException ex) {
            // Expected exception caught...
        }

        System.out.println("Use case registering (non-valid user logging: user incorrectly set up)");
        UserDTO newInvalidUser = new UserDTO(NON_VALID_ID,
                hashgen.hash(USER_PASSWORD.toCharArray()));
        try {
            manager.addNewUser(newInvalidUser);
            fail("A new non-valid user has been added to the system!");
        } catch (IllegalArgumentException ex) {
            // Expected exception caught...
        } catch (DuplicatedIDException ex) {
            fail("There are no users with this id in the system!");
        }
        assertFalse("The user was not created", manager.existsUser(NON_VALID_ID));
    }

}
