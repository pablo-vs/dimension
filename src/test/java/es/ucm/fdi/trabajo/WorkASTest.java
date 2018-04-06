package es.ucm.fdi.trabajo;

import es.ucm.fdi.workspace.project.ProjectDAOHashTableImp;
import es.ucm.fdi.workspace.project.ProjectManagerAS;
import es.ucm.fdi.workspace.project.ProjectTO;
import es.ucm.fdi.workspace.project.WorkAS;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import es.ucm.fdi.workspace.Function;
import es.ucm.fdi.workspace.Grafico;
import es.ucm.fdi.workspace.Visualization;

public class WorkASTest {
	
	@Test
	public void workASTest(){
		
		ProjectTO exponencial = new ProjectTO("exponencialex"); 
		WorkAS proj = new WorkAS(exponencial);
		
		ArrayList<Grafico> g = new ArrayList<>();
		g.add(new Grafico(3));
		g.add(new Grafico(5));
		
		Visualization views = new Visualization(g);
		
		proj.addVisualization(views);
		
		if(!proj.getProject().getViews().contains(views)) {
			fail("Visualizations not being added to project");
		}
		
		proj.saveProject();
	
		if(!proj.getProjManager().getDao().getProjects().contains(exponencial)) {
			fail("Project not being saved!");
		}
		
	}
}
