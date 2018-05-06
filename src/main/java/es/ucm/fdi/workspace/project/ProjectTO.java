/*
  This file is part of Dimension.
  Dimension is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  Dimension is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  You should have received a copy of the GNU General Public License
  along with Dimension.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.ucm.fdi.workspace.project;

import es.ucm.fdi.workspace.FunctionBO;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
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
	private final String ID;
	
	public ProjectTO(String ID) {
		this.ID = ID;
		views = new ArrayList<>();
		functions = new ArrayList<>();
	}

	public ProjectTO(String ID, List<VisualizationBO> vis, List<FunctionBO>  funcs) {
		this.ID = ID;
		views = new ArrayList<>(vis);
		functions = new ArrayList<>(funcs);
	}

	public ProjectTO(ProjectTO other) {
		if(other.views != null) {
		views = new ArrayList<>(other.views);
		}
		if(other.functions != null) {
		functions = new ArrayList<>(other.functions);
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
