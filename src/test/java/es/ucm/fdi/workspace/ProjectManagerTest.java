package es.ucm.fdi.workspace;

import es.ucm.fdi.integration_tier.project.ProjectDAOHashTableImp;
import es.ucm.fdi.business_tier.workspace.project.ProjectManagerAS;
import es.ucm.fdi.business_tier.workspace.project.ProjectDTO;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import es.ucm.fdi.business_tier.workspace.function.FunctionComposite;

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

        List<FunctionComposite> funcs = new ArrayList<>();
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
