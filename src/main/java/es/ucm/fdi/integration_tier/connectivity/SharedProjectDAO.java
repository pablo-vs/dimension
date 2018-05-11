package es.ucm.fdi.integration_tier.connectivity;

import es.ucm.fdi.business_tier.connectivity.SharedProjectDTO;
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
 no project was found.
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
