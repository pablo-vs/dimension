package es.ucm.fdi.business_tier.workspace.project;

import es.ucm.fdi.integration_tier.project.ProjectDAO;
import es.ucm.fdi.integration_tier.project.ProjectTransfer;

/**
 * Application service to manage the traffic of projects.
 *
 * @author Eduardo Amaya
 */
public class ProjectManagerAS {

    // Singleton pattern
    private static ProjectManagerAS instance;
    private ProjectDAO dao;

    /**
     * Class constructor specifying DAO project.
     *
     * @param dao
     */
    private ProjectManagerAS(ProjectDAO dao) {
        this.dao = dao;
    }

    /**
     *
     * @return the DAO project
     */
    public ProjectDAO getDao() {
        return dao;
    }

    /**
     * Get the current manager or create a new one if it does not exist, using
     * the given database.
     *
     * @param dao The ProjectDAO to use.
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
     * @param proj The project to add.
     */
    public void newProject(ProjectTransfer proj) {
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
        if (dao.findProject(id) != null) {
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
    public ProjectTransfer openProject(String id) {
        ProjectTransfer proj = dao.findProject(id);
        if (proj == null) {
            throw new IllegalArgumentException("Project " + id + " does not exist");
        }
        return proj;
    }

    /**
     * Saves the different modifications of the project.
     *
     * @param proj Project
     */
    public void saveChanges(ProjectTransfer proj) {
        if (dao.containsProject(proj.getID())) {
            dao.modifyProject(proj);
        } else {
            newProject(proj);
        }
    }

    /**
     * Parses the project id.
     *
     * @param proj Project
     * @return if the format is valid
     */
    private boolean validateProjectDetails(ProjectTransfer proj) {
        return proj.getID().matches("[\\w]++");
    }

}
