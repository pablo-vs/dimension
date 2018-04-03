package es.ucm.fdi.usuarios;

import java.time.ZonedDateTime;
import java.io.Serializable;

/**
 * Representa una sesión de un usuario.
 *
 * @author Pablo Villalobos
 * @version 01.04.2018
 */
public class SesionBO implements Serializable{
	private String username;
	private ZonedDateTime date;

	/**
	 * Constructor.
	 */
	public SesionBO(String user, ZonedDateTime time) {
		username = user;
		date = time;
	}

	/**
	 * Devuelve un identificador único para cada sesión.
	 *
	 * @return Un String que identifica unívocamente la sesión.
	 */
	public String getID() {
		return Integer.toString((username + date.toString().hashCode()).hashCode());
	}

	/**
	 * Devuelve el nombre del usuario que inició la sesión.
	 *
	 * @return Identificador del usuario.
	 */
	public String getUser() {
		return username;
	}

	/**
	 * Devuelve la date en la que se inició la sesión.
	 *
	 * @return Una ZonedDateTime que representa la date.
	 */
	public ZonedDateTime getDate() {
		return date;
	}
}
