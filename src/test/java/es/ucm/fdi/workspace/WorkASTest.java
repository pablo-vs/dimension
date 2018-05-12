package es.ucm.fdi.workspace;

import es.ucm.fdi.business_tier.workspace.Visualization;
import es.ucm.fdi.business_tier.workspace.Graph;
import es.ucm.fdi.integration_tier.project.ProjectDAOHashTableImp;
import es.ucm.fdi.business_tier.workspace.project.ProjectManagerAS;
import es.ucm.fdi.business_tier.workspace.project.ProjectDTO;
import es.ucm.fdi.business_tier.workspace.project.WorkAS;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;

public class WorkASTest {

    @Test
    public void workASTest() {

        ProjectDTO exponencial = new ProjectDTO("exponentialex");
        ProjectManagerAS projMan = ProjectManagerAS.getManager(new ProjectDAOHashTableImp());
        WorkAS proj = new WorkAS(exponencial);

        ArrayList<Graph> g = new ArrayList<>();
        g.add(new Graph(3));
        g.add(new Graph(5));

        Visualization views = new Visualization(g);

        proj.addVisualizationBO(views);

        if (!proj.getProject().getViews().contains(views)) {
            fail("VisualizationBOs not being added to project");
        }

        projMan.saveChanges(proj.getProject());

        if (projMan.openProject("exponentialex") == null) {
            fail("Project was not saved!");
        }

    }
}
