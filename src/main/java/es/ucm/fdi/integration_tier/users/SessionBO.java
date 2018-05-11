package es.ucm.fdi.integration_tier.users;

import java.time.ZonedDateTime;
import java.io.Serializable;

/**
 * Represents a user session.
 *
 * @author Pablo Villalobos
 */
public class SessionBO implements Serializable {

    private final String username;
    /**
     * Date of login
     */
    private final ZonedDateTime date;

    /**
     * Class constructor specifying username and date.
     *
     * @param username Username
     * @param date Date of login
     */
    public SessionBO(String username, ZonedDateTime date) {
        this.username = username;
        this.date = date;
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
     * @return the username of whom started the session
     */
    public String getUser() {
        return username;
    }

    /**
     *
     * @return A ZonedDateTime representing the date.
     */
    public ZonedDateTime getDate() {
        return date;
    }
}
