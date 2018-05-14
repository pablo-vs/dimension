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
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import es.ucm.fdi.business.workspace.function.AbstractFunction;

public class ProjectManagerTest {

    @Test
    public void projectManagementTest() {
        ProjectManagerAS projectMgr = ProjectManagerAS
                .getManager(new ProjectDAOHashTableImp());
        ProjectDTO polinomios = new ProjectDTO("polinomios43");
        ProjectDTO raices = new ProjectDTO("ra|||");
        projectMgr.newProject(polinomios);
        try {
            projectMgr.newProject(raices);
            fail("Illegal character in project name!");
        } catch (IllegalArgumentException e) {
            // todo correcto
        }
        try {
            projectMgr.removeProject("ra|||");
            fail("Deleted non-existing project!");
        } catch (IllegalArgumentException e) {
            // todo correcto
        }

        List<AbstractFunction> funcs = new ArrayList<>();
        polinomios.setFunctions(funcs);

        projectMgr.saveChanges(polinomios);

        polinomios = projectMgr.openProject("polinomios43");
        if (polinomios.getFunctions() == null) {
            fail("Changes not being saved!");
        }

        projectMgr.removeProject("polinomios43");

        try {
            projectMgr.openProject("polinomios43");
            fail("Opening non-existing project!");
        } catch (IllegalArgumentException e) {
            // todo correcto
        }

        try {
            projectMgr.removeProject("polinomios43");
            fail("Removing non-existing project!");
        } catch (IllegalArgumentException e) {
            // todo correcto
        }

        ProjectDTO trigonometricas = new ProjectDTO("trig");
        try {
            projectMgr.saveChanges(trigonometricas);
        } catch (IllegalArgumentException e) {
            fail("If saved project doesn't exist must create a new one");
        }

        try {
            trigonometricas = projectMgr.openProject("trig");
        } catch (IllegalArgumentException e) {
            fail("If saved project doesn't exist must create a new one");
        }
    }
}
