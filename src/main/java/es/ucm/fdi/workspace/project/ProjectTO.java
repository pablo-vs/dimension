package es.ucm.fdi.workspace.project;

import es.ucm.fdi.workspace.Function;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

import es.ucm.fdi.workspace.Visualization;
import es.ucm.fdi.workspace.Visualization;

/**
 * Represents a project.
 *
 * @author Eduardo Amaya
 * @author Javier Galiana
 * @version 05.05.18
 */

//TODO: javadoc
public class ProjectTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Visualization> views;
	private ArrayList<Function> functions;
	private String ID;
	
	public ProjectTO(String iD) {
		ID = iD;
		views = new ArrayList<>();
		functions = new ArrayList<>();
	}

	public ProjectTO(String iD, List<Visualization> vis, List<Function>  funcs) {
		ID = iD;
		views = new ArrayList<>(vis);
		functions = new ArrayList<>(funcs);
	}

	public ProjectTO(ProjectTO other) {
		views = new ArrayList<Visualization>(other.views);
		functions = new ArrayList<Function>(other.functions);
		ID = other.ID;
	}
	
	public String getID() {
		return ID;
	}

	public List<Visualization> getViews() {
		return views;
	}

	public List<Function> getFunctions() {
		return functions;
	}

	public void setFunctions(List<Function> funcs) {
		functions = new ArrayList<>(funcs);
	}

	public void setViews(List<Visualization> vis) {
		views = new ArrayList<>(vis);
	}
	
}
