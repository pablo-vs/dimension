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
import es.ucm.fdi.workspace.VisualizationBO;
import es.ucm.fdi.workspace.GraphBO;
import es.ucm.fdi.workspace.transformations.GraphTransformationBO;

/**
 * Application service which provides functions for working in projects.
 *
 * @author Brian Leiva
 *
 */
public class WorkAS {
	private final ProjectTO project;

	/**
	 * Create a Work Application Service using the given project.
	 *
	 * @param proj The project to work on.
	 */
	public WorkAS(ProjectTO proj) {
		project = proj;
	}

	/**
	 * @return The current project.
	 */
	public ProjectTO getProject() {
		return project;
	}

	/**
	 * Adds a new Function to the project.
	 *
	 * @param f The new Function to add.
	 */
	public void addFunction(FunctionBO f) {
		project.getFunctions().add(f);
	}

	/**
	 * Adds a new Visualization to the project.
	 *
	 * @param v The new Visualization to add.
	 */
	public void addVisualizationBO(VisualizationBO v) {
		project.getViews().add(v);
	}

	/**
	 * Transforms a visualized function according to the given transformation.
	 *
	 * @param view The view to transform.
	 * @param func The function to transform.
	 * @param transformation The GraphTransformation to apply.
	 */
	public void transformFunction(int view, int func, GraphTransformationBO transformation) {
		VisualizationBO visual = project.getViews().get(view);
		GraphBO graph = visual.getGraph().get(func);
		transformation.apply(graph);
		visual.getGraph().set(func, graph);
		project.getViews().set(view, visual);
	}
}
