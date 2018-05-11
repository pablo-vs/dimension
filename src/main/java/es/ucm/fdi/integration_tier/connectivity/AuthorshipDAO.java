package es.ucm.fdi.integration_tier.connectivity;

import es.ucm.fdi.business_tier.connectivity.AuthorshipDTO;
import java.util.List;

import es.ucm.fdi.integration_tier.exceptions.DAOErrorException;

/**
 * The DAO for Authorships.
 *
 * @author Pablo Villalobos.
 */
public interface AuthorshipDAO {

    /**
     * Adds a new authorship to the database.
     *
     * @param auth The new authorship as a AuthorshipDTO.
     */
    public void addAuthorship(AuthorshipDTO auth) throws DAOErrorException;

    /**
     * Removes a authorship from the database.
     *
     * @param auth The authorship to remove.
     */
    public void removeAuthorship(AuthorshipDTO auth) throws DAOErrorException;

    /**
     * Find an autorship in the database matching the given username.
     *
     * @param username The identifier of the user.
     * @return A List of authorships where the author is the given user.
     */
    public List<AuthorshipDTO> findByUser(String username) throws DAOErrorException;

    /**
     * Find autorships in the database matching the given project.
     *
     * @param project The identifier of the project.
     * @return A List of authorships where the project is the given one.
     */
    public List<AuthorshipDTO> findByProject(String project) throws DAOErrorException;

    /**
     * Returns all the stored authorships.
     *
     * @return A List of AuthorshipBOs.
     */
    public List<AuthorshipDTO> getAuthorships() throws DAOErrorException;
}
