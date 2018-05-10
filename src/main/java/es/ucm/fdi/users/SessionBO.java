package es.ucm.fdi.users;

import java.time.ZonedDateTime;
import java.io.Serializable;

/**
 * Represents a user session.
 *
 * @author Pablo Villalobos
 * @version 01.04.2018
 */
public class SessionBO implements Serializable {

    /**
     * Username
     */
    private final String username;
    /**
     * Date of login
     */
    private final ZonedDateTime date;

    /**
     * Class constructor specifying username and date.
     *
     * @param user Username
     * @param time Date
     */
    public SessionBO(String user, ZonedDateTime time) {
        username = user;
        date = time;
    }

    /**
     * Returns an unique session identifier.
     *
     * @return a string which uniquely determines the session.
     */
    public String getID() {
        return Integer.toString((username + date.toString()).hashCode());
    }

    /**
     * Returns the user who started the session.
     *
     * @return username
     */
    public String getUser() {
        return username;
    }

    /**
     * Returns the date of login.
     *
     * @return A ZonedDateTime representing the date.
     */
    public ZonedDateTime getDate() {
        return date;
    }
}
