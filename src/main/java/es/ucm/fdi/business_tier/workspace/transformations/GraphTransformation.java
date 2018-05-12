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
package es.ucm.fdi.business_tier.workspace.transformations;

import es.ucm.fdi.business_tier.workspace.Graph;

/**
 * Public interface representing a modification in a graph. Every modification
 * that could be applied to a graph implements this interface. The interface
 * allows the usage of the strategy pattern, each different transformation is 
 * taken as a different approach (strategy) to the problem of transforming a
 * function. 
 */
public interface GraphTransformation {

    /**
     * Applies the transformation to the given graph. Each class which
     * implements this interface must define its own apply method.
     *
     * @param graph
     */
    public void apply(Graph graph);
}
