package es.ucm.fdi.connectivity;

import es.ucm.fdi.datos.BDMemoria;

public class SharedProjectDAOHashTableImp {
	private BDMemoria<SharedProjectBO> database;

	/**
	 * Adds a new project to the database.
	 *
	 * @param proj The new project as a SharedProjectBO.
	 */
	@Override
	public void addSharedProject(SharedProjectBO proj) {

	}

	/**
	 * Removes a project from the database.
	 *
	 * @param id The identifier of the project.
	 */
	@Override
	public void removeSharedProject(String id) {

	}

	/**
	 * If there is a project with the same identifier as the given one,
	 * replaces it.
	 *
	 * @param proj A SharedProjectBO containing the new data of the project.
	 */
	@Override
	public void modifySharedProject(SharedProjectBO proj) {

	}

	/**
	 * Finds a project in the database.
	 *
	 * @param id The identifier of the project.
	 * @return A SharedProjectBO containing the data of the project, or null if
	 * no project was found.
	 */
	@Override
	public SharedProjectBO findSharedProject(String id) {
		return null;
	}

	/**
	 * Find a project by name.
	 *
	 * @param name The name of the project.
	 * @return the A List of projects with that name.
	 */
	@Override
	public List<SharedProjectBO> findByName(String name) {
		return null;
	}

	/**
	 * Returns all the stored projects.
	 *
	 * @return A List of SharedProjectBOs.
	 */
	@Override
	public List<SharedProjectBO> getSharedProjects() {
		return null;
	}

}
