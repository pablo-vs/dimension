package es.ucm.fdi.business_tier.network_operations.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * Creates an object which manages a given twitter session instantiated from an
 * AccessToken. Specifying the consumerKey and consumerSecret values of the
 * application and a certain AccessToken this objects provides a way of dealing
 * effortless with twitter without considering how sessions are implemented.
 *
 * @author Arturo Acuaviva, Inmaculada Pérez, Javier Galiana
 */
public class TwitterAS {

    /**
     * Twitter interface
     */
    private Twitter twitter;
    /**
     * Representing authorized Access Token which is passed to the service
     * provider in order to access protected resources
     */
    private AccessToken twitterAccess;
    /**
     * The consumer key identifies which application is making the twitter
     * request.
     */
    private String consumerKey;
    /**
     * API password that is used to authenticate with the authentication server
     * that authenticates the API.
     */
    private String consumerSecret;

    /**
     * Creates a TwitterService object from a given AccessToken and the
     * consumerKey and consumerSecret of the application.
     *
     * @param twitterAccess
     * @param consumerKey
     * @param consumerSecret
     */
    public TwitterAS(AccessToken twitterAccess, String consumerKey,
            String consumerSecret) {
        this.twitterAccess = twitterAccess;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        try {
            registerAccess();
        } catch (IllegalStateException e) {
            System.err.println("Something went wrong while trying to access "
                    + "twitter...");
        }
    }

    /**
     * Tries to register the access in twitter using the twitterAccess provided
     *
     * @throws IllegalStateException when error while trying to access to
     * twitter
     */
    private void registerAccess() throws IllegalStateException {
        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        twitter.setOAuthAccessToken(twitterAccess);
    }

    /**
     * Updates the status in twitter of the user
     *
     * @param message new status
     * @throws TwitterException when session was not correctly registered or
     * status cannot be updated
     */
    public void updateStatus(String message) throws TwitterException {
        twitter.updateStatus(message);
    }

}
