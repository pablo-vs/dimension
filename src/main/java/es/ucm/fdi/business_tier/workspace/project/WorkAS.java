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
package es.ucm.fdi.business_tier.workspace.project;

import es.ucm.fdi.business_tier.workspace.Visualization;
import es.ucm.fdi.business_tier.workspace.function.AbstractFunction;
import es.ucm.fdi.business_tier.workspace.transformations.GraphTransformation;

/**
 * Application service which provides functions for working in projects.
 *
 * @author Brian Leiva
 */
public class WorkAS {

    private final ProjectDTO project;

    /**
     * Create a Work Application Service using the given project.
     *
     * @param proj The project to work on.
     */
    public WorkAS(ProjectDTO proj) {
        project = proj;
    }

    /**
     * @return The current project.
     */
    public ProjectDTO getProject() {
        return project;
    }

    /**
     * Adds a new Function to the project.
     *
     * @param f The new Function to add.
     */
    public void addFunction(AbstractFunction f) {
        project.getFunctions().add(f);
    }

    /**
     * Adds a new Visualization to the project.
     *
     * @param view The new Visualization to add.
     */
    public void addVisualizationBO(Visualization view) {
        project.getViews().add(view);
    }

    /**
     * Transforms a visualized function according to the given transformation.
     *
     * @param view The view to transform.
     * @param func The function to transform.
     * @param transformation The GraphTransformation to apply.
     */
    public void transformFunction(int view, int func, GraphTransformation transformation) {
        /*    Visualization visual = project.getViews().get(view);
        Graph graph = visual.getGraph().get(func);
        transformation.apply(graph);
        visual.getGraph().set(func, graph);
        project.getViews().set(view, visual); */
    }
}
