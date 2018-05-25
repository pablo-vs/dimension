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

import es.ucm.fdi.business.network.operations.twitter.TwitterManager;
import es.ucm.fdi.business.users.SessionDTO;
import es.ucm.fdi.business.users.UserDTO;
import es.ucm.fdi.business.users.UserManagerAS;
import es.ucm.fdi.business.users.UserType;
import es.ucm.fdi.business.util.HashGenerator;
import es.ucm.fdi.integration.users.UserDAOHashTableImp;
import java.text.ParseException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import twitter4j.TwitterException;

/**
 * Use case test: tweeting a message from the user. This test could be taken as
 * an integration test using a TwitterAS. More information on how TwitterManager
 * handles the twitter interaction can be found on TwitterAS class. More tests
 * on the class are available on TwitterManagerTest
 *
 * @see TwitterManagerTest
 * @see TwitterManager
 *
 * @author Arturo Acuaviva
 */
public class TwitterUCTest {

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
     * Twitter Manager used for use case testing
     */
    private TwitterManager twitterManager;
    /**
     * User access token for testing. The token was previously calculated for
     * the account @dimensionapp
     */
    private final static String USER_ACCESS_TOKEN = "991590712000081920-Oti7vSJgP1ScuksjZ6xpmvU7f1c71QR";
    /**
     * User access token secret for testing. The token secret was previously
     * calculated for the account @dimensionapp
     */
    private final static String USER_ACCESS_TOKEN_SECRET = "LemCxfr8noVmeJaxlFMyon1tnl9QGdYvO6v7WfSlWx053";

    public TwitterUCTest() {
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
     * Initializes all the components for the testing.
     */
    @Before
    public void setUp() {
        database = new UserDAOHashTableImp();
        hashgen = new HashGenerator();
        try {
            database.addUser(new UserDTO(USER_ID, null,
                    hashgen.hash(USER_PASSWORD.toCharArray()), null, null, null,
                    null, null, UserType.USER, null, USER_ACCESS_TOKEN,
                    USER_ACCESS_TOKEN_SECRET));
        } catch (ParseException ex) {
            fail("Valid user created should not fail at parsing");
        }
        manager = UserManagerAS.getManager(database);
    }

    /**
     * Deletes all the changes made during the testing.
     */
    @After
    public void tearDown() {
        try {
            twitterManager.deleteAllStatus();
        } catch (TwitterException ex) {
        }
    }

    /**
     * Tests the use case tweeting. An user tries to tweet a message portraying
     * the description of their new function.
     */
    @Test
    public void testTwitterUC() {
        System.out.println("Twitter use case test");
        // We login in the system
        SessionDTO userTwitting = manager.login(USER_ID, USER_PASSWORD);
        // We obtain the user information from the database
        UserDTO userInformation = manager.getUserInformation(userTwitting.getUser(), userTwitting);
        // The user must have access to twitter
        assertTrue("The user should have twitter access!", userInformation.hasTwitterAccess());
        // A TwitterManager is created from the user information
        twitterManager = new TwitterManager(userInformation.getTwitterAccess());
        try {
            // A new tweet is sent from the user account
            twitterManager.updateStatus("I'm enjoying dimension right now!");
        } catch (TwitterException ex) {
            fail("No exception should be thrown when updating the status!");
        }

        assertTrue("There was a new status added!", twitterManager.hasPublishedStatus());

        try {
            // The user removes all the status added
            twitterManager.deleteAllStatus();
        } catch (TwitterException ex) {
            fail("There was a problem removing the status added!");
        }

        assertFalse("The status were not removed!", twitterManager.hasPublishedStatus());
    }

}
