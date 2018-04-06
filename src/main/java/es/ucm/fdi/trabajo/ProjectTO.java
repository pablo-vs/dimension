package es.ucm.fdi.trabajo;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

import es.ucm.fdi.trabajo.function.Function;

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
	}

	public ProjectTO(String iD, List<Visualization> vis, List<Function>  funcs) {
		ID = iD;
		views = new ArrayList<>(vis);
		functions = new ArrayList<>(funcs);
	}

	public ProjectTO(ProjectTO other) {
		if(other.views != null) {
		views = new ArrayList<Visualization>(other.views);
		}
		if(other.functions != null) {
		functions = new ArrayList<Function>(other.functions);
		}
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
