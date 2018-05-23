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

import es.ucm.fdi.business.users.SessionDTO;
import es.ucm.fdi.business.users.UserDTO;
import es.ucm.fdi.business.users.UserManagerAS;
import es.ucm.fdi.business.util.HashGenerator;
import es.ucm.fdi.business.workspace.Visualization;
import es.ucm.fdi.business.workspace.function.AbstractFunction;
import es.ucm.fdi.business.workspace.function.types.VariablesList;
import es.ucm.fdi.business.workspace.function.types.unary.ConstantFunction;
import es.ucm.fdi.integration.project.ProjectDAO;
import es.ucm.fdi.integration.project.ProjectDAOHashTableImp;
import es.ucm.fdi.integration.project.ProjectDAOSQLImp;
import es.ucm.fdi.integration.users.UserDAOHashTableImp;
import es.ucm.fdi.integration.users.UserDAOSQLImp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit test for ProjectManagerAS class.
 *
 * @see ProjectManagerAS
 * @author Arturo Acuaviva
 */
public class ProjectManagerASTest {

	char [] passwd = {'1','2','3','4'};
	UserManagerAS userMan;
	SessionDTO session;
    ProjectDAO newProject;
    ProjectManagerAS manager;

    public ProjectManagerASTest() {
    }

    /**
     * Setting up the test class. We create a not empty project containing
     * projects with ids prj1 and prj2
     */
    @Before
    public void setUp() {
        newProject = new ProjectDAOHashTableImp("pepe");
        manager = new ProjectManagerAS(newProject, "pepe");
        userMan = UserManagerAS.getManager(new UserDAOHashTableImp());
        UserDTO pepe = new UserDTO("pepe", (new HashGenerator()).hash(passwd));
        userMan.addNewUser(pepe);
        session = userMan.login("pepe", null);
    }

    /**
     * Test of newProject method, of class ProjectManagerAS.
     */
    @Test
    public void testNewProjectAndRemoveProject() {
        // Adding new projects
        manager.newProject(new ProjectDTO("test1"), session);
        manager.newProject(new ProjectDTO("test2"), session);
        manager.newProject(new ProjectDTO("test3"), session);
        // Checking whether or not they were created
        assertTrue("The project was correctly added",
                manager.getDao().containsProject("test1"));
        assertTrue("The project was correctly added",
                manager.getDao().containsProject("test2"));
        assertTrue("The project was correctly added",
                manager.getDao().containsProject("test3"));
        // Removing new projects
        manager.removeProject("test1", session);
        manager.removeProject("test2", session);
        manager.removeProject("test3", session);
        // Check if they were removed
        assertFalse("The project was correctly removed",
                manager.getDao().containsProject("test1"));
        assertFalse("The project was correctly removed",
                manager.getDao().containsProject("test2"));
        assertFalse("The project was correctly removed",
                manager.getDao().containsProject("test3"));

    }

    /**
     * Test of openProject method, of class ProjectManagerAS.
     */
    @Test
    public void testOpenProject() {
        System.out.println("openProject");
        // Adding new projects
        manager.newProject(new ProjectDTO("test1"), session);
        manager.newProject(new ProjectDTO("test2"), session);
        manager.newProject(new ProjectDTO("test3"), session);
        //Opening test1, test2 and test3
        assertEquals("Project opened correctly",
                manager.openProject("test1", session).getID(), "test1");
        assertEquals("Project opened correctly",
                manager.openProject("test2", session).getID(), "test2");
        assertEquals("Project opened correctly",
                manager.openProject("test3", session).getID(), "test3");
        // Removing new projects
        manager.removeProject("test1", session);
        manager.removeProject("test2", session);
        manager.removeProject("test3", session);
    }

    /**
     * Test of saveChanges method, of class ProjectManagerAS.
     */
    @Test
    public void testSaveChanges() {
        System.out.println("saveChanges");
        // We create a new project with the same ID
        ProjectDTO prj1 = new ProjectDTO("prj1");
        // Visualization list creation
        List<Visualization> visualizationList = new ArrayList<>();
        visualizationList.add(new Visualization(new ArrayList<>()));
        visualizationList.add(new Visualization(new ArrayList<>()));
        // Function list creation
        List<AbstractFunction> functionList = new ArrayList<>();
        functionList.add(new ConstantFunction(2, new VariablesList(1)));
        functionList.add(new ConstantFunction(3, new VariablesList(1)));
        // We change the project configuration for views and functions
        prj1.setViews(visualizationList);
        prj1.setFunctions(functionList);
        // Changes saved
        manager.modifyProject(prj1, session);
        // Check if changes were correctly saved
        assertTrue("Views changes correctly saved",
                manager.openProject("prj1", session).getViews().equals(visualizationList));
        assertTrue("Function list changes correctly saved",
                manager.openProject("prj1", session).getFunctions().equals(functionList));

    }

    /**
     * General test using a manager. It tests the class by simulating a normal
     * user interaction with the manager.
     */
    @Test
    public void projectManagementTest() {
        ProjectManagerAS projectMgr = new ProjectManagerAS(new ProjectDAOHashTableImp("pepe"), "pepe");
        ProjectDTO polinomios = new ProjectDTO("polinomios43");
        ProjectDTO raices = new ProjectDTO("ra|||");
        projectMgr.newProject(polinomios, session);
        try {
            projectMgr.newProject(raices, session);
            fail("Illegal character in project name!");
        } catch (IllegalArgumentException e) {
            // OK
        }
        try {
            projectMgr.removeProject("ra|||", session);
            fail("Deleted non-existing project!");
        } catch (IllegalArgumentException e) {
            // OK
        }

        List<AbstractFunction> funcs = new ArrayList<>();
        polinomios.setFunctions(funcs);

        projectMgr.modifyProject(polinomios, session);

        polinomios = projectMgr.openProject("polinomios43", session);
        if (polinomios.getFunctions() == null) {
            fail("Changes not being saved!");
        }

        projectMgr.removeProject("polinomios43", session);

        try {
            projectMgr.openProject("polinomios43", session);
            fail("Opening non-existing project!");
        } catch (IllegalArgumentException e) {
            // OK
        }

        try {
            projectMgr.removeProject("polinomios43", session);
            fail("Removing non-existing project!");
        } catch (IllegalArgumentException e) {
            // OK
        }

        ProjectDTO trigonometricas = new ProjectDTO("trig");
        try {
            projectMgr.modifyProject(trigonometricas, session);
        } catch (IllegalArgumentException e) {
            fail("If saved project doesn't exist must create a new one");
        }

        try {
            trigonometricas = projectMgr.openProject("trig", session);
        } catch (IllegalArgumentException e) {
            fail("If saved project doesn't exist must create a new one");
        }
    }

    @After
    public void clear() throws SQLException {
    	userMan.removeUser("pepe", session);
    	userMan.logout(session);
    	(new UserDAOSQLImp()).clear();
    	(new ProjectDAOSQLImp("")).clear();
    }
}
