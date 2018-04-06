package es.ucm.fdi.trabajo;

import es.ucm.fdi.trabajo.function.Function;

public class WorkAS {
	private ProjectTO project;
	private ProjectManagerAS projManager = ProjectManagerAS.getManager(new ProjectDAOHashTableImp());

	public WorkAS(ProjectTO proj) {
		project = proj;
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
