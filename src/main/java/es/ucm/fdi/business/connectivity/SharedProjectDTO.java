/**
 * This file is part of Dimension.
 * Dimension is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Dimension is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Dimension.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.business.connectivity;

import es.ucm.fdi.business.workspace.project.ProjectDTO;

/**
 * Represents a shared project by an author.
 *
 * @author Pablo Villalobos
 * @see ProjectDTO
 */
public abstract class SharedProjectDTO extends ProjectDTO {

    /**
     *
     */
    private static final long serialVersionUID = -2809118441039530565L;
    /**
     * Project id
     */
    private final String sharedID;

    /**
     * Class constructor specifying id and author name.
     *
     * @param sharedID Identifier
     * @param name Author
     */
    public SharedProjectDTO(String sharedID, String name) {
        super(name);
        this.sharedID = sharedID;
    }

    /**
     * Class constructor specifying id and existing project
     *
     * @param sharedID Identifier
     * @param proj Existing project
     */
    public SharedProjectDTO(String sharedID, ProjectDTO proj) {
        super(proj);
        this.sharedID = sharedID;
    }

    /**
     * Checks if a user can read the project.
     *
     * @param username Identifier of the user to check.
     * @return True if this user has read access to the project.
     */
    public abstract boolean hasReadAccess(String username);

    /**
     * Checks if a user can modify the project.
     *
     * @param username Identifier of the user to check.
     * @return True if this user has write access to the project.
     */
    public abstract boolean hasWriteAccess(String username);

    /**
     *
     * @return the shared ID
     */
    public String getSharedID() {
        return sharedID;
    }

}
