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
package es.ucm.fdi.business_tier.workspace.project;

import es.ucm.fdi.business_tier.workspace.Visualization;
import es.ucm.fdi.business_tier.workspace.function.AbstractFunction;
import es.ucm.fdi.business_tier.workspace.function.types.VariablesList;
import es.ucm.fdi.business_tier.workspace.function.types.unary.ConstantFunction;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit test for ProjectDTO class.
 *
 * @see ProjectDTO
 * @author Arturo Acuaviva
 */
public class ProjectDTOTest {

    /**
     * Project object test with no list
     */
    ProjectDTO project;
    /**
     * Project object test filled up with one list
     */
    ProjectDTO project2;
    /**
     * Project object test filled up with two lists
     */
    ProjectDTO project3;

    /**
     * Non-empty AbstractFunction list used for testing
     */
    List<AbstractFunction> functionList = new ArrayList<>();
    /**
     * Non-empty Visualization list used for testing
     */
    List<Visualization> visualizationList = new ArrayList<>();

    public ProjectDTOTest() {
    }

    /**
     * Sets up the test creating the objects to be tested.
     */
    @Before
    public void setUp() {
        visualizationList.add(new Visualization(new ArrayList<>()));
        visualizationList.add(new Visualization(new ArrayList<>()));
        functionList.add(new ConstantFunction(2, new VariablesList(1)));
        functionList.add(new ConstantFunction(3, new VariablesList(1)));

        project = new ProjectDTO("prj1", new ArrayList<>(), new ArrayList<>());
        project2 = new ProjectDTO("prj2", visualizationList, new ArrayList<>());
        project3 = new ProjectDTO("prj3", visualizationList, functionList);

    }

    /**
     * Test of getID method, of class ProjectDTO.
     */
    @Test
    public void testGetID() {
        System.out.println("getID");
        String expResult = "prj1";
        String result = project.getID();
        // Equal ID test
        assertTrue("Project get ID should be equal to the expected value",
                expResult.equals(result));
        // Different ID test
        assertFalse("Projects ID are different", project.getID().equals(project2.getID()));

    }

    /**
     * Test of getViews method, of class ProjectDTO.
     */
    @Test
    public void testGetViews() {
        System.out.println("getViews");
        // We test emptiness
        assertTrue("Project get views must be empty",
                new ArrayList<>().equals(project.getViews()));
        // We test compare the list given by a non-empty project and an empty project
        assertFalse("Projects have different visualization lists",
                project.getViews().equals(project2.getViews()));
    }

    /**
     * Test of getFunctions method, of class ProjectDTO.
     */
    @Test
    public void testGetFunctions() {
        System.out.println("getFunctions");
        // We test emptiness
        assertTrue("Project get functions must be empty",
                new ArrayList<>().equals(project.getFunctions()));
        // We test compare the list given by a non-empty project and an empty project
        assertFalse("Projects have different function lists",
                project3.getFunctions().equals(project2.getFunctions()));
    }

    /**
     * Test of setFunctions method, of class ProjectDTO.
     */
    @Test
    public void testSetFunctions() {
        System.out.println("setFunctions");
        assertFalse("Projects have a different function list",
                project3.getFunctions().equals(project.getFunctions()));
        // We add to an empty project
        project.setFunctions(functionList);
        assertTrue("Projects have the same function list",
                project3.getFunctions().equals(project.getFunctions()));
        // We change a project that has previously a non-empty list
        project3.setFunctions(new ArrayList<>());
        assertFalse("Projects have different function lists",
                project3.getFunctions().equals(project.getFunctions()));
    }

    /**
     * Test of setViews method, of class ProjectDTO.
     */
    @Test
    public void testSetViews() {
        System.out.println("setViews");
        assertFalse("Projects have a different visualization list",
                project3.getViews().equals(project.getViews()));
        // We add to an empty project
        project.setViews(visualizationList);
        assertTrue("Projects have the same function list",
                project3.getViews().equals(project.getViews()));
        // We change a project that has previously a non-empty list
        project3.setViews(new ArrayList<>());
        assertFalse("Projects have different visualization lists",
                project3.getViews().equals(project.getViews()));
    }

}
