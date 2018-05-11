package es.ucm.fdi.workspace;

import es.ucm.fdi.business_tier.workspace.VisualizationBO;
import es.ucm.fdi.business_tier.workspace.GraphBO;
import es.ucm.fdi.business_tier.workspace.project.ProjectDAOHashTableImp;
import es.ucm.fdi.business_tier.workspace.project.ProjectManagerAS;
import es.ucm.fdi.business_tier.workspace.project.ProjectTO;
import es.ucm.fdi.business_tier.workspace.project.WorkAS;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;


public class WorkASTest {

    @Test
    public void workASTest() {

        ProjectTO exponencial = new ProjectTO("exponentialex");
        ProjectManagerAS projMan = ProjectManagerAS.getManager(new ProjectDAOHashTableImp());
        WorkAS proj = new WorkAS(exponencial);

        ArrayList<GraphBO> g = new ArrayList<>();
        g.add(new GraphBO(3));
        g.add(new GraphBO(5));

        VisualizationBO views = new VisualizationBO(g);

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
