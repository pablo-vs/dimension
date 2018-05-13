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
package es.ucm.fdi.business_tier.workspace;

import es.ucm.fdi.business_tier.exceptions.NoMatchDimensionException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents the view of a function.
 *
 * @author Brian Leiva
 * @author Arturo Acuaviva
 */
public class Visualization implements ComponentComposite {

    /**
     * All graphs will have been shrinking to the third dimension, projected
     * graphs will use this constant value
     *
     * @see projectGraph
     */
    private static final int DEFAULT_DIMENSION = 3;

    /**
     * List of graphs
     */
    private ArrayList<ComponentComposite> graphsAvailable = new ArrayList<>();

    /**
     * Empty class constructor.
     */
    public Visualization() {
    }

    /**
     * Class constructor specifying another list of graphs.
     *
     * @param graphics
     */
    public Visualization(List<ComponentComposite> graphics) {
        this.graphsAvailable = new ArrayList<>(graphics);
    }

    /**
     * Add a new ComponentComposite to the list of elements that a Visualization
     * object contains. Typically in Visualization the elements added will be
     * graphs.
     *
     * @param component new ComponentComposite element in the inner list.
     */
    @Override
    public void add(ComponentComposite component) {
        graphsAvailable.add(component);
    }

    /**
     * Deletes a ComponentComposite that a Visualization object contains.
     * Typically in Visualization this elements will be graphs.
     *
     * @param component which will be removed
     */
    @Override
    public void delete(ComponentComposite component) {
        if (!graphsAvailable.remove(component)) {
            throw new IllegalArgumentException("The element was"
                    + "not in the list " + graphsAvailable.getClass().getName());
        }
    }

    /**
     * Removes all the elements in the range. All ComponentComposites are
     * deleted.
     */
    @Override
    public void deleteAll() {
        graphsAvailable.removeAll(graphsAvailable);
    }

    /**
     * Returns an operator over the list of ComponentComposite that a
     * Visualization object contains. Typically in Visualization this elements
     * will be graphs.
     *
     * @return listIterator over the elements of the visualization
     */
    @Override
    public Iterator getCompositeIterator() {
        return graphsAvailable.listIterator();
    }

    /**
     * Given a certain graph the method projectGraph returns its projection into
     * the 3D axis. A graph has a range of values, this function takes into
     * account three of the n dimensions of the object given. It also evaluates
     * in the correspondant hyperplanes the graph.
     *
     * @param graph the graph with the range of vertex to project
     * @param dimX the dimension which will be depicted in the X axis
     * @param dimY the dimension which will be depicted in the Y axis
     * @param dimZ the dimension which will be depicted in the Z axis
     * @param hp the hyperplane where the function will be evaluated
     * @return the projected graph
     * @throws NoMatchDimensionException if not found
     */
    public static Graph projectGraph(Graph graph, int dimX, int dimY, int dimZ, double[] hp) throws NoMatchDimensionException {
        Graph graphAux = new Graph(DEFAULT_DIMENSION);
        Iterator it = graph.getCompositeIterator();
        while (it.hasNext()) {
            Vertex v = (Vertex) it.next();
            int j = 0, cont = 0;
            boolean b = true;
            while (b && j < hp.length) {
                while (cont == dimX || cont == dimY || cont == dimZ) {
                    ++cont;
                }
                if (cont < graph.getDimension()) {
                    if (Math.abs(v.at(cont) - hp[j]) > 0.1) {
                        b = false;
                    }
                }
                ++j;
                ++cont;
            }
            if (b) {
                Vertex newV = new Vertex(DEFAULT_DIMENSION);
                newV.set(0, v.at(dimX));
                newV.set(1, v.at(dimY));
                newV.set(2, v.at(dimZ));
                graphAux.add(newV);
            }
        }
        return graphAux;
    }

}
