package es.ucm.fdi.integration.project;

import org.junit.Test;	
import static org.junit.Assert.*;

import java.util.ArrayList;

import es.ucm.fdi.business.workspace.Visualization;
import es.ucm.fdi.business.workspace.project.ProjectDTO;
import es.ucm.fdi.business.workspace.project.WorkAS;

public class ProjectDAOSQLImpTest {
	
	@Test
	public void ProjectDAOSQLTest() {
		ProjectDAOSQLImp dao1 = new ProjectDAOSQLImp("Laura");
		ProjectDAOSQLImp dao2 = new ProjectDAOSQLImp("Ana");
		ProjectDTO exp = new ProjectDTO("exponential");
		ProjectDTO log = new ProjectDTO("logaritmica");
		ProjectDTO trig = new ProjectDTO("trigonometrica");
		
		WorkAS proj = new WorkAS(exp);
		
		ArrayList<ProjectDTO> results1 = new ArrayList<>(), 
		    results2 = new ArrayList<>();
			
		results1.add(exp);
		results1.add(log);
		results2.add(trig);

		dao1.addProject(exp);
		dao1.addProject(log);
		dao2.addProject(trig);

		assertEquals("Invalid Project search results", exp,
			     dao1.findProject("exponential"));
		assertEquals("The Projects cannot be obtained", results2,
			     dao2.getProjects());
				
		assertEquals("The Projects cannot be obtained", results1,
			     dao1.getProjects());
		
		Visualization views = new Visualization();
		proj.addVisualizationBO(views);
		dao1.modifyProject(exp);

		assertEquals("The Project found is not the expected", exp,
			     dao1.findProject("exponential"));

		dao1.removeProject(exp.getID());
		dao1.removeProject(log.getID());
		dao2.removeProject(trig.getID());
	}

}
