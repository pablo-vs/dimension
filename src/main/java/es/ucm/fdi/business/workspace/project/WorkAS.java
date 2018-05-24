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

import es.ucm.fdi.business.workspace.Graph;
import es.ucm.fdi.business.workspace.Vertex;
import es.ucm.fdi.business.workspace.Visualization;
import es.ucm.fdi.business.workspace.function.AbstractFunction;
import es.ucm.fdi.business.workspace.transformations.TransformationStrategy;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

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
     * Returns a list of the functions in the project.
     *
     * @return
     */
    public List<AbstractFunction> getFunctions() {
        return project.getFunctions();
    }

    /**
     * Returns a list of the visualizations in the project.
     *
     * @return
     */
    public List<Visualization> getVisualizations() {
        return project.getViews();
    }

    /**
     * Transforms a visualization according to the given transformation.
     *
     * @param view
     * @param transformation
     */
    public static void transformVisualization(Visualization view,
            TransformationStrategy transformation) {
        Iterator it = view.getGraphIterator();
        while (it.hasNext()) {
            transformation.apply((Graph) it.next());
        }
    }

    /**
     * Transforms all the views in the project using the given transformation.
     *
     * @param transformation
     */
    public void transformProject(TransformationStrategy transformation) {
        Iterator it = project.getViews().listIterator();
        while (it.hasNext()) {
            transformation.apply((Graph) it.next());
        }
    }
    
    /**
     * 
     */
    public List<BufferedImage> paint(Visualization view, int height, int width, int res, List<List<Vertex>> params, List<Double> scales) {
    	return view.paint(height, width, res, params, scales);
    }

}
