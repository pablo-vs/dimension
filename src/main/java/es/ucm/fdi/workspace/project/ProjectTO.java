package es.ucm.fdi.workspace.project;

import es.ucm.fdi.workspace.FunctionBO;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

import es.ucm.fdi.workspace.VisualizationBO;
import es.ucm.fdi.workspace.VisualizationBO;

/**
 * Contains the data of a project.
 *
 * @author Eduardo Amaya
 * @author Javier Galiana
 * @version 05.05.18
 */
public class ProjectTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private ArrayList<VisualizationBO> views;
	private ArrayList<FunctionBO> functions;
	private String ID;
	
	public ProjectTO(String iD) {
		ID = iD;
		views = new ArrayList<>();
		functions = new ArrayList<>();
	}

	public ProjectTO(String iD, List<VisualizationBO> vis, List<FunctionBO>  funcs) {
		ID = iD;
		views = new ArrayList<>(vis);
		functions = new ArrayList<>(funcs);
	}

	public ProjectTO(ProjectTO other) {
		if(other.views != null) {
		views = new ArrayList<VisualizationBO>(other.views);
		}
		if(other.functions != null) {
		functions = new ArrayList<FunctionBO>(other.functions);
		}
		ID = other.ID;
	}
	
	public String getID() {
		return ID;
	}

	public List<VisualizationBO> getViews() {
		return views;
	}

	public List<FunctionBO> getFunctions() {
		return functions;
	}

	public void setFunctions(List<FunctionBO> funcs) {
		functions = new ArrayList<>(funcs);
	}

	public void setViews(List<VisualizationBO> vis) {
		views = new ArrayList<>(vis);
	}
	
}
