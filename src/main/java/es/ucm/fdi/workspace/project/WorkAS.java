package es.ucm.fdi.workspace.project;

import es.ucm.fdi.workspace.Function;
import es.ucm.fdi.workspace.Visualization;
import es.ucm.fdi.workspace.project.ProjectTO;
import es.ucm.fdi.workspace.project.ProjectDAOHashTableImp;
import es.ucm.fdi.workspace.project.ProjectManagerAS;

public class WorkAS {
	private ProjectTO project;
	private ProjectManagerAS projManager = ProjectManagerAS.getManager(new ProjectDAOHashTableImp());

	public WorkAS(ProjectTO proj) {
		project = proj;
	}

	public ProjectManagerAS getProjManager() {
		return projManager;
	}

	public ProjectTO getProject() {
		return project;
	}

	public void addFunction(Function f) {
		project.getFunctions().add(f);
	}

	public void addVisualization(Visualization v) {
		project.getViews().add(v);
	}

	public void saveProject() {
		projManager.saveChanges(project);
	}
	
	public void TransformFunction(int n, Visualization v) {
		project.getViews().set(n, v);
	}
}
