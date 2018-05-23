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
package es.ucm.fdi.business.workspace;
	
import javax.xml.bind.annotation.XmlRootElement;	
import javax.xml.bind.annotation.XmlElement;

import es.ucm.fdi.business.exceptions.NoMatchDimensionException;	
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents the view of a function.
 *
 * @author Brian Leiva
 * @author Arturo Acuaviva
 */
@XmlRootElement
public class Visualization {

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
    @XmlElement
    private ArrayList<Graph> graphsAvailable = new ArrayList<>();

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
    public Visualization(List<Graph> graphics) {
        this.graphsAvailable = new ArrayList<>(graphics);
    }

    public Iterator<Graph> getGraphIterator() {
        return graphsAvailable.listIterator();
    }
    
    /**
     * Add a new ComponentComposite to the list of elements that a Visualization
     * object contains. In Visualization the elements added will be
     * graphs.
     *
     * @param component new ComponentComposite element in the inner list.
     */
    public void add(Graph g) {
    	graphsAvailable.add(g);
    }

    /**
     * Deletes a ComponentComposite that a Visualization object contains.
     * In Visualization this elements will be graphs.
     *
     * @param component which will be removed
     */
    public void delete(Graph g) {
        if (!graphsAvailable.remove(g)) {
            throw new IllegalArgumentException("Could not delete graph:"
            		+ " The element was not in the list.");
        }
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
        Iterator it = graph.getIteratorRange();
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
                graphAux.addRange(newV);
            }
        }
        return graphAux;
    }

}
