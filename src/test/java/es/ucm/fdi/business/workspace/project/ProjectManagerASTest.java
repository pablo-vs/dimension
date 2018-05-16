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

import es.ucm.fdi.business.workspace.Visualization;
import es.ucm.fdi.business.workspace.function.AbstractFunction;
import es.ucm.fdi.business.workspace.function.types.VariablesList;
import es.ucm.fdi.business.workspace.function.types.unary.ConstantFunction;
import es.ucm.fdi.integration.project.ProjectDAO;
import es.ucm.fdi.integration.project.ProjectDAOHashTableImp;
import java.util.ArrayList;
import java.util.List;
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
        newProject = new ProjectDAOHashTableImp();
        newProject.addProject(new ProjectDTO("prj1"));
        newProject.addProject(new ProjectDTO("prj2"));
        manager = ProjectManagerAS.getManager(newProject);
    }

    /**
     * Test of newProject method, of class ProjectManagerAS.
     */
    @Test
    public void testNewProjectAndRemoveProject() {
        System.out.println("newProject | removeProject");
        // Adding new projects
        manager.newProject(new ProjectDTO("test1"));
        manager.newProject(new ProjectDTO("test2"));
        manager.newProject(new ProjectDTO("test3"));
        // Checking whether or not they were created
        assertTrue("The project was correctly added",
                manager.getDao().containsProject("test1"));
        assertTrue("The project was correctly added",
                manager.getDao().containsProject("test2"));
        assertTrue("The project was correctly added",
                manager.getDao().containsProject("test3"));
        // Removing new projects
        manager.removeProject("test1");
        manager.removeProject("test2");
        manager.removeProject("test3");
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
        manager.newProject(new ProjectDTO("test1"));
        manager.newProject(new ProjectDTO("test2"));
        manager.newProject(new ProjectDTO("test3"));
        //Opening test1, test2 and test3
        assertEquals("Project opened correctly",
                manager.openProject("test1").getID(), "test1");
        assertEquals("Project opened correctly",
                manager.openProject("test2").getID(), "test2");
         assertEquals("Project opened correctly",
                manager.openProject("test3").getID(), "test3");
                // Removing new projects
        manager.removeProject("test1");
        manager.removeProject("test2");
        manager.removeProject("test3");
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
        manager.saveChanges(prj1);
        // Check if changes were correctly saved
        assertTrue("Views changes correctly saved",
                manager.openProject("prj1").getViews().equals(visualizationList));
        assertTrue("Function list changes correctly saved",
                manager.openProject("prj1").getFunctions().equals(functionList));

    }

}
