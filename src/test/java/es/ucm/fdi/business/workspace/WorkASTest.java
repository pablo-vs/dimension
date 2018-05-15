/**
 * This file is part of Dimension.
 * Dimension is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Dimension is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Dimension.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.business.workspace;

import es.ucm.fdi.integration.project.ProjectDAOHashTableImp;
import es.ucm.fdi.business.workspace.project.ProjectManagerAS;
import es.ucm.fdi.business.workspace.project.ProjectDTO;
import es.ucm.fdi.business.workspace.project.WorkAS;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

/**
 * JUnit test for WorkAS class.
 *
 * @see WorkAS
 * @author Javier Galiana
 */
public class WorkASTest {

    @Test
    public void workASTest() {

        ProjectDTO exponencial = new ProjectDTO("exponentialex");
        ProjectManagerAS projMan = ProjectManagerAS.getManager(new ProjectDAOHashTableImp());
        WorkAS proj = new WorkAS(exponencial);

        ArrayList<Graph> g = new ArrayList<>();
        g.add(new Graph(3));
        g.add(new Graph(5));

        Visualization views = new Visualization();

        proj.addVisualizationBO(views);

        assertEquals("VisualizationBOs have not been added to the project", true,
                proj.getProject().getViews().contains(views));

        projMan.saveChanges(proj.getProject());

        assertEquals("Project was not saved!", exponencial, projMan.openProject("exponentialex"));
    }
}
