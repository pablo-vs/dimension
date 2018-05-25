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
package es.ucm.fdi.business.workspace.project;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

import es.ucm.fdi.business.workspace.Visualization;
import es.ucm.fdi.business.workspace.function.AbstractFunction;
import java.util.Objects;

/**
 * Contains the data of a project.
 *
 * @author Eduardo Amaya, Javier Galiana, Inmaculada Pérez
 */
@XmlRootElement
public class ProjectDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private ArrayList<Visualization> views = new ArrayList<>();
    private ArrayList<AbstractFunction> functions = new ArrayList<>();
    private String ID;

    public ProjectDTO() {
        ID = "default";
    }

    /**
     * Class constructor specifying ID.
     *
     * @param ID
     */
    public ProjectDTO(String ID) {
        this.ID = ID;
    }

    /**
     * Class constructor specifying ID, different views and functions.
     *
     * @param ID
     * @param views
     * @param functions
     */
    public ProjectDTO(String ID, List<Visualization> views, List<AbstractFunction> functions) {
        this.ID = ID;
        this.views = new ArrayList<>(views);
        this.functions = new ArrayList<>(functions);
    }

    /**
     * Class constructor specifying another ProjectTO object.
     *
     * @param other
     */
    public ProjectDTO(ProjectDTO other) {
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
    @XmlElement
    public String getID() {
        return ID;
    }

    /**
     *
     * @return the list of visualization object of the project
     */
    @XmlElement
    public List<Visualization> getViews() {
        return views;
    }

    /**
     *
     * @return the different functions
     */
    @XmlElement
    public List<AbstractFunction> getFunctions() {
        return functions;
    }

    /**
     * Sets the ID
     *
     * @param newID New ID;
     */
    public void setID(String newID) {
        ID = newID;
    }

    /**
     * Sets a new list of functions.
     *
     * @param functions
     */
    public void setFunctions(List<AbstractFunction> functions) {
        this.functions = new ArrayList<>(functions);
    }

    /**
     * Sets a new list of views.
     *
     * @param views
     */
    public void setViews(List<Visualization> views) {
        this.views = new ArrayList<>(views);
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof ProjectDTO) && ((ProjectDTO) other).ID.equals(ID);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.views);
        hash = 97 * hash + Objects.hashCode(this.functions);
        hash = 97 * hash + Objects.hashCode(this.ID);
        return hash;
    }

}
