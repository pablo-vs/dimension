package es.ucm.fdi.integration_tier.connectivity;

import es.ucm.fdi.business_tier.connectivity.CommentDTO;
import java.util.List;

/**
 * The DAO for Authorships.
 *
 * @author Brian Leiva.
 */
public interface CommentDAO {

    /**
     * Adds a new comment to the database.
     *
     * @param comment The new comment as a CommentDTO.
     */
    public void addComment(CommentDTO comment);

    /**
     * Removes a comment from the database.
     *
     * @param comment The comment to remove.
     */
    public void removeComment(CommentDTO comment);

    /**
     * Find comments in the database matching the given username.
     *
     * @param username The identifier of the user.
     * @return A List of comments where the author is the given user.
     */
    public List<CommentDTO> findByUser(String username);

    /**
     * Find comments in the database matching the given project.
     *
     * @param project The identifier of the project.
     * @return A List of comments made on the project.
     */
    public List<CommentDTO> findByProject(String project);

    /**
     * Returns all the stored comments.
     *
     * @return A List of CommentBOs.
     */
    public List<CommentDTO> getComments();
}
