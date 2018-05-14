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
