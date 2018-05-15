/*
  This file is part of Dimension.
  Dimension is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  Dimension is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  You should have received a copy of the GNU General Public License
  along with Dimension.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.integration.connectivity;

import es.ucm.fdi.business.connectivity.CommentDTO;
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
