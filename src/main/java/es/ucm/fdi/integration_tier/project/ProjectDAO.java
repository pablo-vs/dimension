package es.ucm.fdi.integration_tier.project;

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
     * @param proj The new project as a ProjectTransfer.
     */
    public void addProject(ProjectTransfer proj);

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
     * @param proj A ProjectTransfer containing the new data of the project.
     */
    public void modifyProject(ProjectTransfer proj);

    /**
     * Find a project in the database.
     *
     * @param id The identifier of the project.
     * @return A ProjectTransfer containing the data of the project, or null if no
 project was found.
     */
    public ProjectTransfer findProject(String id);

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
    public List<ProjectTransfer> getProjects();
}
