package es.ucm.fdi.users;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.security.AccessControlException;

import es.ucm.fdi.util.HashGenerator;
import es.ucm.fdi.exceptions.DuplicatedIDException;
import es.ucm.fdi.exceptions.NotFoundException;

/**
 * Application service to manage user accounts and sessions.
 *
 * @author Pablo Villalobos
 * @version 02.04.2018
 */
public class UserManagerAS {
	//Singleton pattern
       	private static UserManagerAS instance;
	private UserDAO dao;
	private HashGenerator hashgen;
	private HashMap<String, SessionBO> activeSessions;
	
	private UserManagerAS(UserDAO dao) {
		this.dao = dao;
		hashgen = new HashGenerator();
		activeSessions = new HashMap<>();
	}

	/**
	 * Get the current manager or create a new one if it does not exist, using the given database.
	 *
	 * @param dao The UserDAO to use.
	 * @return The User Manager.
	 */
	public static UserManagerAS getManager(UserDAO dao) {
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
	public void newUser(UserTO user) throws DuplicatedIDException, IllegalArgumentException{
		if(!existsUser(user.getID())) {
			if(validateAccountDetails(user)) {
				dao.addUser(user);
				
			} else {
				throw new IllegalArgumentException("Invalid account details");
			}
		} else {
			throw new DuplicatedIDException("User " + user.getID() + " already exists");
		}
	}

	/**
	 * Remove a user from the database. This requires an active session.
	 *
	 * @param id The identifier of the user to be deleted.
	 * @param sesion The session from which to perform the action.
	 */
	public void removeUser(String id, SessionBO session) throws NotFoundException, AccessControlException {
		if(authenticate(id, session)) {
			if(existsUser(id)) {
				dao.removeUser(id);
			} else {
				throw new NotFoundException("User " + id + " does not exist");
			}
		} else {
			throw new AccessControlException("Invalid session");
		}
	}

	/**
	 * Modify a user's information in the database. This requires an active session.
	 *
	 * @param id The new account details.
	 * @param sesion The session from which to perform the action.
	 */
	public void modifyUser(UserTO user, SessionBO session) throws AccessControlException, IllegalArgumentException {
		if(authenticate(user.getID(), session)) {
			if(validateAccountDetails(user)) {
				dao.modifyUser(user);
			} else {
				throw new IllegalArgumentException("Invalid account details");
			}
		} else {
			throw new AccessControlException("Invalid session");
		}
	}

	/**
	 * Validates the given data and returns a new session.
	 *
	 * @param username The identifier of the user.
	 * @param passwd The password provided
	 * @return The new session.
	 */
	public SessionBO login(String username, String passwd) throws AccessControlException, NotFoundException {
		//Looks for this username in the database.
		if(existsUser(username)) {
			UserTO user = dao.findUser(username);
			//If the introduced password matches the given one
			//create and return a new session
			if(hashgen.authenticate(passwd.toCharArray(), user.getPassword())) {
				return createNewSession(username);
			}
			else {
				throw new AccessControlException("Incorrect password");
			}
		}
		else {
			throw new NotFoundException("User " + username + " does not exist");
		}
	}

	/**
	 * Checks if a session is valid.
	 *
	 * @param id The session to check.
	 * @return True if the session is valid.
	 */
	public boolean validateSession(SessionBO session) {
		return activeSessions.containsKey(session.getID());
	}

	/**
	 * Checks if the given session is an active session with privileges over
	 * the given user acount.
	 *
	 * @param username The claimed user name.
	 * @param session The session to check.
	 */
	public boolean authenticate(String username, SessionBO session) {
		if(validateSession(session)) {
		        String sessionUser = session.getUser();
			return sessionUser.equals(username) ||
				dao.findUser(sessionUser).getType() == UserType.ADMIN;
		} else {
			return false;
		}
	}

	/**
	 * Closes the given session.
	 *
	 * @param sesion The session to close.
	 */
	public void logout(SessionBO session) {
		activeSessions.remove(session.getID());
	}

	/**
	 * Checks if a user exists.
	 *
	 * @param username The name to check.
	 */
	public boolean existsUser(String username) {
		return dao.findUser(username) != null;
	}

	private SessionBO createNewSession(String user) throws IllegalArgumentException {
		SessionBO sesion = null;
		//Si el usuario ya ha iniciado sesión, lanzar un error
		if(activeSessions.get(user) != null) {
			throw new IllegalArgumentException("This user is already logged in");
		} else {
			//Si no, crear la sesión y añadirla a la lista de sesiones activas.
			sesion = new SessionBO(user, ZonedDateTime.now());
			activeSessions.put(sesion.getID(), sesion);
		}
		return sesion;
	}	

	private boolean validateAccountDetails(UserTO user) {
		//TODO!!!!!!!!!!
		return true;
	}
}
