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
package es.ucm.fdi.integration.project;

import es.ucm.fdi.business.workspace.project.ProjectDTO;
import java.util.List;

/**
 * The DAO for Projects.
 *
 * @author Pablo Villalobos
 */
public interface ProjectDAO {

    /**
     * Adds a new project to the database.
     *
     * @param proj The new project as a ProjectDTO.
     */
    public void addProject(ProjectDTO proj);

    /**
     * Removes a project from the database.
     *
     * @param id The identifier of the project.
     */
    public void removeProject(String id);

    /**
     * If there is a project with the same identifier as the given one, replaces
     * it.
     *
     * @param proj A ProjectDTO containing the new data of the project.
     */
    public void modifyProject(ProjectDTO proj);

    /**
     * Find a project in the database.
     *
     * @param id The identifier of the project.
     * @return A ProjectDTO containing the data of the project, or null if no
     * project was found.
     */
    public ProjectDTO findProject(String id);

    /**
     * Checks if a project exists in the database.
     *
     * @param id The id of the project to be checked.
     * @return if project exists
     */
    public boolean containsProject(String id);

    /**
     * Returns all the stored projects.
     *
     * @return A List of ProjectTOs.
     */
    public List<ProjectDTO> getProjects();
}
