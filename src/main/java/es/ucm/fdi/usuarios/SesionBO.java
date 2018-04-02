package es.ucm.fdi.usuarios;

import java.time.ZonedDateTime;

/**
 * Representa una sesión de un usuario.
 *
 * @author Pablo Villalobos
 * @version 01.04.2018
 */
public class SesionBO {
	private String nombreUsuario;
	private ZonedDateTime fecha;

	/**
	 * Constructor.
	 */
	public SesionBO(String usu, ZonedDateTime time) {
		nombreUsuario = usu;
		fecha = time;
	}

	/**
	 * Devuelve un identificador único para cada sesión.
	 *
	 * @return Un String que identifica unívocamente la sesión.
	 */
	public String getID() {
		return (nombreUsuario + fecha.toString().hashCode());
	}

	/**
	 * Devuelve el nombre del usuario que inició la sesión.
	 *
	 * @return Identificador del usuario.
	 */
	public String getUsuario() {
		return nombreUsuario;
	}

	/**
	 * Devuelve la fecha en la que se inició la sesión.
	 *
	 * @return Una ZonedDateTime que representa la fecha.
	 */
	public ZonedDateTime getFecha() {
		return fecha;
	}
}
