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

import java.security.AccessControlException;

import es.ucm.fdi.business.users.SessionDTO;
import es.ucm.fdi.business.users.UserManagerAS;
import es.ucm.fdi.integration.project.ProjectDAO;	
import es.ucm.fdi.integration.project.ProjectDAOSQLImp;
import es.ucm.fdi.integration.users.UserDAOHashTableImp;

/**
 * Application service to manage the traffic of projects.
 *
 * @author Eduardo Amaya
 */
public class ProjectManagerAS {

	private final UserManagerAS userMan = UserManagerAS
            .getManager(new UserDAOHashTableImp());
    private final ProjectDAO dao;
    private final String user;

    /**
     * Class constructor specifying DAO project and user.
     *
     * @param dao
     */
    public ProjectManagerAS(ProjectDAO dao, String user) {
        this.dao = dao;
        this.user = user;
    }
    
    /**
     * Class constructor specifying user (default DAO SQL).
     *
     * @param dao
     */
    public ProjectManagerAS(String user) {
        this(new ProjectDAOSQLImp(user), user);
    }

    /**
     *
     * @return the DAO project
     */
    public ProjectDAO getDao() {
        return dao;
    }

    /**
     * Adds a new project to the database.
     *
     * @param proj The project to add.
     */
    public void newProject(ProjectDTO proj, SessionDTO session) {
    	if (userMan.authenticate(user, session)) {
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
    	} else {
    		throw new AccessControlException("Invalid session");
    	}
    }

    /**
     * Removes a project from the database.
     *
     * @param id The id of the project to be deleted.
     */
    public void removeProject(String id, SessionDTO session) {
    	if (userMan.authenticate(user, session)) {
	        if (dao.findProject(id) != null) {
	            dao.removeProject(id);
	        } else {
	            throw new IllegalArgumentException("Project " + id + " does not exist");
	        }
    	} else {
    		throw new AccessControlException("Invalid session");
    	}
    }

    /**
     * Opens a project from the database.
     *
     * @param id The id of the project to be opened.
     * @return the project.
     */
    public ProjectDTO openProject(String id, SessionDTO session) {
    	if (userMan.authenticate(user, session)) {
	        ProjectDTO proj = dao.findProject(id);
	        if (proj == null) {
	            throw new IllegalArgumentException("Project " + id + " does not exist");
	        }
	        return proj;
    	} else {
    		throw new AccessControlException("Invalid session");
    	}
    }

    /**
     * Saves the different modifications of the project.
     *
     * @param proj Project
     */
    public void modifyProject(ProjectDTO proj, SessionDTO session) {
    	if (userMan.authenticate(user, session)) {
	        if (dao.containsProject(proj.getID())) {
	            dao.modifyProject(proj);
	        } else {
	            newProject(proj, session);
	        }
    	} else {
    		throw new AccessControlException("Invalid session");
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
