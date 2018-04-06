package es.ucm.fdi.connectivity;

import java.util.List;

import es.ucm.fdi.connectivity.AuthorshipBO;
import es.ucm.fdi.datos.BDMemoria;
import es.ucm.fdi.users.UserTO;

/**
 * The DAO for Authorships.
 *
 * @author Pablo Villalobos.
 * @version 03.04.2018
 */
public interface AuthorshipDAO {
	/**
	 * Adds a new authorship to the database.
	 *
	 * @param proj The new authorship as a AuthorshipBO.
	 */
	public void addAuthorship(AuthorshipBO auth);

	/**
	 * Removes a authorship from the database.
	 *
	 * @param id The authorship to remove.
	 */
	public void removeAuthorship(AuthorshipBO auth);

	/**
	 * Find autorships in the database matching the given username.
	 *
	 * @param username The identifier of the user.
	 * @return A List of authorships where the author is the given user.
	 */
	public List<AuthorshipBO> findByUser(String username);

	/**
	 * Find autorships in the database matching the given project.
	 *
	 * @param project The identifier of the project.
	 * @return A List of authorships where the project is the given one.
	 */
	public List<AuthorshipBO> findByProject(String project);

	/**
	 * Returns all the stored authorships.
	 *
	 * @return A List of AuthorshipBOs.
	 */
	public List<AuthorshipBO> getAuthorships();
}
