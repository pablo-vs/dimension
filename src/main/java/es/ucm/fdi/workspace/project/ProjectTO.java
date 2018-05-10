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
 * @author Eduardo Amaya, Javier Galiana, Inmaculada Pérez
 */
public class ProjectTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private ArrayList<VisualizationBO> views = new ArrayList<>();
    private ArrayList<FunctionBO> functions = new ArrayList<>();
    private final String ID;

    /**
     * Class constructor specifying ID.
     *
     * @param ID
     */
    public ProjectTO(String ID) {
        this.ID = ID;
    }

    /**
     * Class constructor specifying ID, different views and functions.
     *
     * @param ID
     * @param views
     * @param functions
     */
    public ProjectTO(String ID, List<VisualizationBO> views, List<FunctionBO> functions) {
        this.ID = ID;
        this.views = new ArrayList<>(views);
        this.functions = new ArrayList<>(functions);
    }

    /**
     * Class constructor specifying another ProjectTO object.
     *
     * @param other
     */
    public ProjectTO(ProjectTO other) {
        if (other.views != null) {
            views = new ArrayList<>(other.views);
        }
        if (other.functions != null) {
            functions = new ArrayList<>(other.functions);
        }
        ID = other.ID;
    }

    /**
     *
     * @return the ID of the project
     */
    public String getID() {
        return ID;
    }

    /**
     *
     * @return the list of visualization object of the project
     */
    public List<VisualizationBO> getViews() {
        return views;
    }

    /**
     *
     * @return the different functions
     */
    public List<FunctionBO> getFunctions() {
        return functions;
    }

    /**
     * Sets a new list of functions.
     *
     * @param functions
     */
    public void setFunctions(List<FunctionBO> functions) {
        this.functions = new ArrayList<>(functions);
    }

    /**
     * Sets a new list of views.
     *
     * @param views
     */
    public void setViews(List<VisualizationBO> views) {
        this.views = new ArrayList<>(views);
    }

}
