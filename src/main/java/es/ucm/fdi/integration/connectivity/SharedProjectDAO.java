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
package es.ucm.fdi.integration.connectivity;

import es.ucm.fdi.business.connectivity.SharedProjectDTO;
import java.util.List;

/**
 * The DAO for SharedProjects.
 *
 * @author Pablo Villalobos.
 */
public interface SharedProjectDAO {

    /**
     * Adds a new project to the database.
     *
     * @param proj The new project as a SharedProjectDTO.
     */
    public void addSharedProject(SharedProjectDTO proj);

    /**
     * Removes a project from the database.
     *
     * @param id The identifier of the project.
     */
    public void removeSharedProject(String id);

    /**
     * If there is a project with the same identifier as the given one, replaces
     * it.
     *
     * @param proj A SharedProjectDTO containing the new data of the project.
     */
    public void modifySharedProject(SharedProjectDTO proj);

    /**
     * Finds a project in the database.
     *
     * @param id The identifier of the project.
     * @return A SharedProjectDTO containing the data of the project, or null if
     * no project was found.
     */
    public SharedProjectDTO findSharedProject(String id);

    /**
     * Find a project by name.
     *
     * @param name The name of the project.
     * @return the A List of projects with that name.
     */
    public List<SharedProjectDTO> findByName(String name);

    /**
     * Returns all the stored projects.
     *
     * @return A List of SharedProjectBOs.
     */
    public List<SharedProjectDTO> getSharedProjects();
}
