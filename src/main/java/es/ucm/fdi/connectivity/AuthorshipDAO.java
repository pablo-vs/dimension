package es.ucm.fdi.connectivity;

import java.util.List;

import es.ucm.fdi.exceptions.DAOError;

/**
 * The DAO for Authorships.
 *
 * @author Pablo Villalobos.
 */
public interface AuthorshipDAO {

    /**
     * Adds a new authorship to the database.
     *
     * @param auth The new authorship as a AuthorshipBO.
     */
    public void addAuthorship(AuthorshipBO auth) throws DAOError;

    /**
     * Removes a authorship from the database.
     *
     * @param auth The authorship to remove.
     */
    public void removeAuthorship(AuthorshipBO auth) throws DAOError;

    /**
     * Find an autorship in the database matching the given username.
     *
     * @param username The identifier of the user.
     * @return A List of authorships where the author is the given user.
     */
    public List<AuthorshipBO> findByUser(String username) throws DAOError;

    /**
     * Find autorships in the database matching the given project.
     *
     * @param project The identifier of the project.
     * @return A List of authorships where the project is the given one.
     */
    public List<AuthorshipBO> findByProject(String project) throws DAOError;

    /**
     * Returns all the stored authorships.
     *
     * @return A List of AuthorshipBOs.
     */
    public List<AuthorshipBO> getAuthorships() throws DAOError;
}
