package es.ucm.fdi.users;

import java.time.ZonedDateTime;
import java.io.Serializable;

/**
 * Represents a user session.
 *
 * @author Pablo Villalobos
 * @version 01.04.2018
 */
public class SessionBO implements Serializable{
	private String username;
	private ZonedDateTime date;

	/**
	 * Constructor.
	 */
	public SessionBO(String user, ZonedDateTime time) {
		username = user;
		date = time;
	}

	/**
	 * Returns a unique session identifier.
	 *
	 * @return A String which uniquely determines the session.
	 */
	public String getID() {
		return Integer.toString((username + date.toString()).hashCode());
	}

	/**
	 * Returns the user who started the session.
	 *
	 * @return Username.
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
