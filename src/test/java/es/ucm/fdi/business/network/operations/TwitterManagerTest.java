/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ucm.fdi.business.network.operations;

import es.ucm.fdi.business.network.operations.twitter.TwitterManager;
import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

/**
 * JUnit test for TwitterManager class.
 *
 * @see TwitterManager
 * @author Arturo Acuaviva
 */
public class TwitterManagerTest {

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
    /**
     * Access Token.
     */
    AccessToken userAccess;
    /**
     * TwitterManager for testing
     */
    TwitterManager manager;

    public TwitterManagerTest() {
        userAccess = new AccessToken(USER_ACCESS_TOKEN, USER_ACCESS_TOKEN_SECRET);
    }

    /**
     * Creates a Twitter Manager for an account and manages it. It updates its
     * status and deletes it afterwards.
     */
    @Test
    public void testManagingTwitterAccount() {
        System.out.println("Managing twitter account updating one status");
        try {
            manager = new TwitterManager(userAccess);
        } catch (IllegalStateException ex) {
            fail("Something went wrong while using the twitter access provided!");
        }
        // New status added
        try {
            manager.updateStatus("Test message, updating my status.");
        } catch (TwitterException ex) {
            fail("The new status couldn't be added!");
        }
        // Deleting the new status
        try {
            manager.deleteLastStatus();
        } catch (TwitterException ex) {
            fail("The new status couldn't be erased!");
        }
    }

    /**
     * Creates a Twitter Manager for an account and manages it. It updates its
     * status severely times and the delete all the status added.
     */
    @Test
    public void testManaginTwitterAccountMultipleStatus() {
        System.out.println("Managing twitter account updating multiple status");
        try {
            manager = new TwitterManager(userAccess);
        } catch (IllegalStateException ex) {
            fail("Something went wrong while using the twitter access provided!");
        }
        // New status added
        try {
            manager.updateStatus("Test message, updating my status 1.");
            manager.updateStatus("Test message, updating my status 2.");
            manager.updateStatus("Test message, updating my status 3.");
            manager.updateStatus("Test message, updating my status 4.");
        } catch (TwitterException ex) {
            fail("The new status couldn't be added!");
        }
        assertTrue("New status have been published!", manager.hasPublishedStatus());
        // Deleting all the status added
        try {
            manager.deleteAllStatus();
        } catch (TwitterException ex) {
            fail("The new status couldn't be erased!");
        }
         assertFalse("New status have been removed!", manager.hasPublishedStatus());
    }

}
