package es.ucm.fdi.users;

import java.util.List;

/**
 * Interface to manage user data persistence
 *
 * @author Pablo Villalobos
 * @version 01.04.2018
 */
public interface UserDAO {

	/**
	 * Adds a new user to the database.
	 *
	 * @param user The new user.
	 */
	public void addUser(UserTO user);

	/**
	 * If there is a user with the given identifier, removes it.
	 *
	 * @param id The identifier of the user.
	 */
	public void removeUser(String id);

	/**
	 * If there is a user with the given identifier, changes its account details.
	 *
	 * @param user A TO containing the new account details.
	 */
	public void modifyUser(UserTO user);

	/**
	 * Returns the account details of the user with the given id.
	 *
	 * @param id Identifier of the user.
	 * @return A TO containing the relevant data.
	 */
	public UserTO findUser(String id);

	/**
	 * Returns a list of all registered users.
	 *
	 * @return A list of users.
	 */
	public List<UserTO> getUsers();
}
