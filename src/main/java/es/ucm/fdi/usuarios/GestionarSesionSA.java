package es.ucm.fdi.usuarios;

import java.lang.IllegalArgumentException;
import java.time.ZonedDateTime;
import java.util.HashMap;

import es.ucm.fdi.util.HashGenerator;

/**
 * Service application para gestionar el inicio y cierre de sesión
 *
 * @author Pablo Villalobos
 * @version 01.04.2018
 */
public class GestionarSesionSA {
	private CuentaUsuarioDAO BDUsuarios;
	private HashGenerator hashgen;
	private HashMap<String, SesionBO> activeSessions;

	/**
	 * Crea un nuevo gestor de sesiones con la persistencia dada.
	 *
	 * @param bd El DAO que almacena los datos.
	 */
	public GestionarSesionSA(CuentaUsuarioDAO bd) {
		BDUsuarios = bd;
		hashgen = new HashGenerator();
		activeSessions = new HashMap<>();
	}

	/**
	 * Valida los datos introducidos por el usuario y devuelve una nueva sesión.
	 *
	 * @param nombre El identificador del usuario.
	 * @param passwd La contraseña del usuario.
	 * @return La sesión iniciada.
	 */
	public SesionBO iniciarSesion(String nombre, String passwd) {
		//Busca el nombre de usuario en la base de datos
		CuentaUsuarioTO user = BDUsuarios.buscarUsuario(nombre);
		if(user != null) {
			//Si la contraseña introducida y la almacenada coinciden,
			//crea una sesión nueva y la devuelve
			if(hashgen.authenticate(passwd.toCharArray(), user.getContrasenya())) {
				return createNewSession(nombre);
			}
			else {
				throw new IllegalArgumentException("Contraseña incorrecta");
			}
		}
		else {
			throw new IllegalArgumentException("No existe ese usuario");
		}
	}

	/**
	 * Comprueba si el identificador dado corresponde a una sesión activa.
	 *
	 * @param id El identificador a comprobar.
	 * @return True si el id es válido.
	 */
	public boolean validarSesion(String id) {
		return activeSessions.containsKey(id);
	}

	/**
	 * Cierra la sesion dada.
	 *
	 * @param sesion La sesión a cerrar.
	 */
	public void cerrarSesion(String id) {
		activeSessions.remove(id);
	}
	
	private SesionBO createNewSession(String nombre) {
		SesionBO sesion = null;
		//Si el usuario ya ha iniciado sesión, lanzar un error
		if(activeSessions.get(nombre) != null) {
			throw new IllegalArgumentException("Este usuario ya tiene una sesión abierta");
		} else {
			//Si no, crear la sesión y añadirla a la lista de sesiones activas.
			sesion = new SesionBO(nombre, ZonedDateTime.now());
			activeSessions.put(sesion.getID(), sesion);
		}
		return sesion;
	}
}
