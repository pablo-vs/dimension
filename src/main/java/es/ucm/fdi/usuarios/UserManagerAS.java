package es.ucm.fdi.usuarios;

import java.time.ZonedDateTime;
import java.util.HashMap;

import es.ucm.fdi.util.HashGenerator;

/**
 * Application service to manage user accounts and sessions.
 *
 * @author Pablo Villalobos
 * @version 02.04.2018
 */
public class UserManagerAS {
	//Singleton pattern
       	private static UserManagerAS instance;
	private CuentaUsuarioDAO dao;
	private HashGenerator hashgen;
	private HashMap<String, SesionBO> activeSessions;
	
	private UserManagerAS(CuentaUsuarioDAO dao) {
		this.dao = dao;
		hashgen = new HashGenerator();
		activeSessions = new HashMap<>();
	}

	/**
	 * Get the current manager or create a new one if it does not exist, using the given database.
	 *
	 * @param dao The CuentaUsuarioDAO to use.
	 * @return The User Manager.
	 */
	public static UserManagerAS getManager(CuentaUsuarioDAO dao) {
		if(instance == null) {
			instance = new UserManagerAS(dao);
		}
		return instance;
	}

	/**
	 * Adds a new user to the database.
	 *
	 * @param user The user to add.
	 */
	public void newUser(CuentaUsuarioTO user) {
		if(validateAccountDetails(user)) {
			if(dao.buscarUsuario(user.getID()) == null) {
				dao.addUsuario(user);
			} else {
				throw new IllegalArgumentException("User " + user.getID() + " already exists");
			}
		} else {
			throw new IllegalArgumentException("Invalid account details");
		}
	}

	/**
	 * Remove an user from the database. This requires an active session.
	 *
	 * @param id The identifier of the user to be deleted.
	 * @param sesion The session from which to perform the action.
	 */
	public void removeUser(String id, String sesion) {
		if(authenticate(id, sesion)) {
			if( dao.buscarUsuario(id) != null) {
				dao.eliminarUsuario(id);
			} else {
				throw new IllegalArgumentException("User " + id + " does not exist");
			}
		} else {
			throw new IllegalArgumentException("Invalid session");
		}
	}

	/**
	 * Modify an user's information in the database. This requires an active session.
	 *
	 * @param id The new account details.
	 * @param sesion The session from which to perform the action.
	 */
	public void modifyUser(CuentaUsuarioTO user, String sesion) {
		if(authenticate(user.getID(), sesion)) {
			if(validateAccountDetails(user)) {
				if(dao.buscarUsuario(user.getID()) != null) {
					dao.modificarUsuario(user);
				} else {
					throw new IllegalArgumentException("User " + user.getID() + " does not exist");
				}
			} else {
				throw new IllegalArgumentException("Invalid account details");
			}
		} else {
			throw new IllegalArgumentException("Invalid session");
		}
	}

	/**
	 * Validates the given data and returns a new session.
	 *
	 * @param username The identifier of the user.
	 * @param passwd The password provided
	 * @return The new session.
	 */
	public String login(String username, String passwd) {
		//Busca el nombre de usuario en la base de datos
		CuentaUsuarioTO user = dao.buscarUsuario(username);
		if(user != null) {
			//Si la contraseña introducida y la almacenada coinciden,
			//crea una sesión nueva y la devuelve
			if(hashgen.authenticate(passwd.toCharArray(), user.getContrasenya())) {
				return createNewSession(username).getID();
			}
			else {
				throw new IllegalArgumentException("Incorrect password");
			}
		}
		else {
			throw new IllegalArgumentException("This user does not exist");
		}
	}

	/**
	 * Checks if a session ID is valid.
	 *
	 * @param id The ID to check.
	 * @return True if the ID is valid.
	 */
	public boolean validateSession(String id) {
		return activeSessions.containsKey(id);
	}

	/**
	 * Checks if the given session is an active session with privileges over
	 * the given user acount.
	 *
	 * @param username The claimed user name.
	 * @param session The session to check.
	 */
	public boolean authenticate(String username, String session) {
		if(validateSession(session)) {
			String sessionUser = activeSessions.get(session).getUser();
			return sessionUser.equals(username) ||
				dao.buscarUsuario(sessionUser).getTipo() == TipoUsuario.ADMIN;
		} else {
			return false;
		}
	}

	/**
	 * Closes the given session.
	 *
	 * @param sesion The session to close.
	 */
	public void logout(String id) {
		activeSessions.remove(id);
	}
	
	private SesionBO createNewSession(String user) {
		SesionBO sesion = null;
		//Si el usuario ya ha iniciado sesión, lanzar un error
		if(activeSessions.get(user) != null) {
			throw new IllegalArgumentException("This user is already logged in");
		} else {
			//Si no, crear la sesión y añadirla a la lista de sesiones activas.
			sesion = new SesionBO(user, ZonedDateTime.now());
			activeSessions.put(sesion.getID(), sesion);
		}
		return sesion;
	}	

	private boolean validateAccountDetails(CuentaUsuarioTO user) {
		//TODO!!!!!!!!!!
		return true;
	}
}
