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
package es.ucm.fdi.business.network.operations.twitter;

import java.util.ArrayDeque;
import java.util.Deque;
import twitter4j.Status;
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
public class TwitterManager {

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
    private final String consumerKey = "zloMLWgfUKOCdEzZG1MsR8CPn";
    /**
     * API password that is used to authenticate with the authentication server
     * that authenticates the API.
     */
    private final String consumerSecret = "d8KqXiETwULE3fkO7ef7MZCWEobtDXlARXVuZ1yFRLC9bqTvHU";
    /**
     * An stack storing all the status updates using the manager.
     */
    private final Deque<Status> updatesStatus = new ArrayDeque<>();

    /**
     * Creates a TwitterService object from a given AccessToken.
     *
     * @param twitterAccess
     */
    public TwitterManager(AccessToken twitterAccess) {
        this.twitterAccess = twitterAccess;
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
        updatesStatus.addLast(twitter.updateStatus(message));
    }

    /**
     * Destroys the last status made by the user.
     *
     * @throws TwitterException
     */
    public void deleteLastStatus() throws TwitterException {
        if (!updatesStatus.isEmpty()) {
            twitter.destroyStatus(updatesStatus.pollLast().getId());
        }
    }

    /**
     * Destroys all the status added during the managing process.
     *
     * @throws TwitterException
     */
    public void deleteAllStatus() throws TwitterException {
        while (!updatesStatus.isEmpty()) {
            twitter.destroyStatus(updatesStatus.pollLast().getId());
        }
    }

    /**
     * Returns if there was any status added during the managing.
     *
     * @return
     */
    public boolean hasPublishedStatus() {
        return !updatesStatus.isEmpty();
    }
}
