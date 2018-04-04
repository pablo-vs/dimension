package es.ucm.fdi.connectivity;

import java.util.List;

import es.ucm.fdi.connectivity.SharedProjectBO;

/**
 * The DAO for SharedProjects.
 *
 * @author Pablo Villalobos.
 * @version 03.04.2018
 */
public interface SharedProjectDAO {

	/**
	 * Adds a new project to the database.
	 *
	 * @param proj The new project as a SharedProjectBO.
	 */
	public void addSharedProject(SharedProjectBO proj);

	/**
	 * Removes a project from the database.
	 *
	 * @param id The identifier of the project.
	 */
	public void removeSharedProject(String id);

	/**
	 * If there is a project with the same identifier as the given one,
	 * replaces it.
	 *
	 * @param proj A SharedProjectBO containing the new data of the project.
	 */
	public void modifySharedProject(SharedProjectBO proj);

	/**
	 * Find a project in the database.
	 *
	 * @param id The identifier of the project.
	 * @return A SharedProjectBO containing the data of the project, or null if
	 * no project was found.
	 */
	public SharedProjectBO findSharedProject(String id);

	/**
	 * Returns all the stored projects.
	 *
	 * @return A List of SharedProjectBOs.
	 */
	public List<SharedProjectBO> getSharedProjects();
}