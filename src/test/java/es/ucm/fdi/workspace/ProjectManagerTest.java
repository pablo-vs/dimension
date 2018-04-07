package es.ucm.fdi.workspace;

import es.ucm.fdi.workspace.project.ProjectDAOHashTableImp;
import es.ucm.fdi.workspace.project.ProjectManagerAS;
import es.ucm.fdi.workspace.project.ProjectTO;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import es.ucm.fdi.workspace.FunctionBO;

public class ProjectManagerTest {
	@Test
	public void projectManagementTest() {
		ProjectManagerAS projectMgr = ProjectManagerAS
				.getManager(new ProjectDAOHashTableImp());
		ProjectTO polinomios = new ProjectTO("polinomios43"), raices = new ProjectTO(
				"ra|||");
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

		List<FunctionBO> funcs = new ArrayList<FunctionBO>();
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

		ProjectTO trigonometricas = new ProjectTO("trig");
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
