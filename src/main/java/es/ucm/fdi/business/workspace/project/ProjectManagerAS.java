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
package es.ucm.fdi.business.workspace.project;

import es.ucm.fdi.integration.project.ProjectDAO;

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
    public void newProject(ProjectDTO proj) {
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
    public ProjectDTO openProject(String id) {
        ProjectDTO proj = dao.findProject(id);
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
    public void saveChanges(ProjectDTO proj) {
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
    private boolean validateProjectDetails(ProjectDTO proj) {
        return proj.getID().matches("[\\w]++");
    }

}
