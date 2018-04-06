package es.ucm.fdi.workspace.project;

import es.ucm.fdi.workspace.Grafico;

/**
 * Application service to manage the traffic of projects.
 *
 * @author Eduardo Amaya
 * @version 05.04.2018
 */
public class ProjectManagerAS {

	// Singleton pattern
	private static ProjectManagerAS instance;
	private ProjectDAO dao;

	private ProjectManagerAS(ProjectDAO dao) {
		this.dao = dao;
	}

	/**
	 * Get the current manager or create a new one if it does not exist, using
	 * the given database.
	 *
	 * @param dao
	 *            The ProjectDAO to use.
	 * @return The Project Manager.
	 */
	public static ProjectManagerAS getManager(ProjectDAO dao) {
		if (instance == null) {
			instance = new ProjectManagerAS(dao);
		}
		return instance;
	}

	/**
	 * Adds a new project to the database.
	 * 
	 * @param proj
	 *            The project to add.
	 */
	public void newProject(ProjectTO proj) {
		if (validateProjectDetails(proj)) {
			if (dao.findProject(proj.getID()) == null) {
				dao.addProject(proj);
			} else {
				throw new IllegalArgumentException("Project " + proj.getID()
						+ " already exists");
			}
		} else {
			throw new IllegalArgumentException("Invalid project details");
		}
	}
	
	/**
	 * Removes a project from the database.
	 * 
	 * @param id The id of the project to be deleted.
	 */
	public void removeProject(String id) {
		if(dao.findProject(id) != null) {
			dao.removeProject(id);
		} else {
			throw new IllegalArgumentException("Project " + id + " does not exist");
		}
	}
	
	/**
	 * Opens a project from the database.
	 * 
	 * @param id The id of the project to be opened.
	 * @return the project.
	 */
	public ProjectTO openProject(String id) {
		ProjectTO proj = dao.findProject(id);
		if(proj == null) {
			throw new IllegalArgumentException("Project " + id + " does not exist");
		}
		return proj;
	}
	
	public void saveChanges(ProjectTO proj) {
		if(dao.containsProject(proj.getID())) {
			dao.modifyProject(proj);
		} else {
			newProject(proj);
		}
	}
	
	public void newObj(Grafico graph) {
		//TODO
	}
	
	public void changeObj(int change) {
		//TODO
	}
	
	public void viewObj(int view) {
		//TODO
	}
	
	private boolean validateProjectDetails(ProjectTO proj) {
		String id = proj.getID();
		return id.matches("[\\w]++");
	}

}
