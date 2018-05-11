package es.ucm.fdi.integration_tier.users;

import es.ucm.fdi.business_tier.users.UserDTO;
import java.util.List;

/**
 * Interface to manage user data persistence.
 *
 * @author Pablo Villalobos
 */
public interface UserDAO {

    /**
     * Adds a new user to the database.
     *
     * @param user The new user.
     */
    public void addUser(UserDTO user);

    /**
     * If there is a user with the given identifier, removes it.
     *
     * @param id The identifier of the user.
     */
    public void removeUser(String id);

    /**
     * If there is a user with the given identifier, changes its account
     * details.
     *
     * @param user A TO containing the new account details.
     */
    public void modifyUser(UserDTO user);

    /**
     * Returns the account details of the user for the given id.
     *
     * @param id Identifier of the user.
     * @return A TO containing the relevant data.
     */
    public UserDTO findUser(String id);

    /**
     * Returns a list of all the registered users.
     *
     * @return A list of users.
     */
    public List<UserDTO> getUsers();

    /**
     * Bans a user from the system given its ID
     *
     * @param id
     */
    public void banUser(String id);
}
