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
import es.ucm.fdi.business.util.MultiTreeMap;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import es.ucm.fdi.business.workspace.function.AbstractFunction;
import es.ucm.fdi.business.workspace.function.types.VariablesList;

import java.util.Iterator;
import java.util.ListIterator;

/**
 * A graph object is used to represent an abstract interpretration of a
 * function. It contains two lists of {@link es.ucm.fdi.workspace Vertex} with
 * values from the range and the domain of the function. It also provides
 *
 * @author Brian Leiva
 * @author Eloy Mósig
 */
@XmlRootElement
public class Graph {

    /**
     *
     * Int value representing the dimension of the object depicted by the graph.
     */
	@XmlElement
    private final int dimension;

    /**
     * List of {@link es.ucm.fdi.workspace Vertex} containing the vertex in the
     * domain.
     */
	@XmlElement
    private final List<Vertex> domain = new ArrayList<>();

    /**
     * List of {@link es.ucm.fdi.workspace Vertex} containing the vertex in the
     * range.
     */
	@XmlElement
    private final List<Vertex> range = new ArrayList<>();

    /**
     * List of {@link es.ucm.fdi.workspace ComponentComposite} containing the
     * functions.
     */
	@XmlElement
    private final List<AbstractFunction> functionList = new ArrayList<>();

    /**
     * Tree map with domain values.
     */
	@XmlElement
    private final MultiTreeMap<Integer, Integer> object
            = new MultiTreeMap<>((a, b) -> a - b);
	
    /**
     * Resolution of the graph, it indicates the factor used to calculate the
     * length of the
     */
	@XmlElement
    private int resolution;

	public Graph() {
		this(3);
	}
	
    /**
     * Class constructor specifying the dimension of the graph.
     *
     * @param dimension
     */
    public Graph(int dimension) {
        this.dimension = dimension;
    }

    /**
     * Calculates the domain (the points where the function will be evaluated).
     * The parameters are the list of bounds where the function can be
     * evaluated.
     *
     * @param dom_ini
     * @param dom_fin
     */
    private void getGrid(double[] dom_ini, double[] dom_fin) {
        if (dom_ini.length != dom_fin.length) {
            throw new IllegalArgumentException();
        }
        double lado = 1 / (Math.pow(2, resolution));
        int[] tam = new int[dom_ini.length];
        int dim = 1;
        for (int i = 0; i < tam.length; ++i) {
            tam[i] = (int) ((dom_fin[i] - dom_ini[i]) / lado + 1);
            dim *= tam[i];
        }
        for (int j = 0; j < dim; ++j) {
            try {
                domain.add(new Vertex(tam.length));
            } catch (NoMatchDimensionException e) {
                // e.printStackTrace();
            }
        }
        int n = 1;
        for (int i = 0; i < tam.length; ++i) {
            double suma = dom_ini[i];
            int aux = n;
            n *= tam[i];
            for (int j = 0; j < tam[i]; ++j) {
                for (int k = j * aux; k < domain.size(); k += n) {
                    int cont = 0;
                    while (cont < aux) {
                        domain.get(k + cont).set(i, suma);
                        if (j > 0) {
                            object.putValue(k, k - (j * aux));
                        }
                        ++cont;
                    }
                }
                suma += lado;
            }
        }
    }

    /**
     * Generates the graph. Given a List of functions and a numeric resolution,
     * it generates a grid for the given points and then generates the vertex
     * representing the n-dimensional points in the range.
     *
     * @param dom_ini
     * @param dom_fin
     * @param resolution of the graph generated
     * @throws NoMatchDimensionException
     */
    public void generate(double[] dom_ini, double[] dom_fin, int resolution)
            throws NoMatchDimensionException {
        this.resolution = resolution;
        getGrid(dom_ini, dom_fin);
        for (int i = 0; i < domain.size(); ++i) {
            Vertex fv = new Vertex(functionList.size());
            for (int j = 0; j < functionList.size(); ++j) {
                fv.set(j, ((AbstractFunction) functionList.get(j))
                        .evaluate(domain.get(i).getComps()));
            }
            addRange(fv);
        }
    }

    /**
     *
     * @return the dimension of the graph
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * Adds a new vertexes to the Graph object. It is added to the range.
     *
     * @param component which will be added
     */
    public void addRange(Vertex component) {
        range.add(component);
    }

    /**
     * Returns an operator over the list of vertexes that a Graph object
     * contains in its range.
     *
     * @return listIterator over the elements of the graph
     */
    public ListIterator<Vertex> getIteratorRange() {
        return range.listIterator();
    }

    /**
     * Removes all the elements in the range. All vertexes are deleted.
     */
    public void cleanRangeList() {
        range.removeAll(range);
    }

    /**
     * Adds a new ComponentComposite to the Graph object. Typically in Graph
     * this elements will be vertex.
     *
     * @param component which will be added
     */
    public void add(AbstractFunction func) {
        functionList.add(func);
    }
    
    public void paint(Graphics2D g) {
    	if(dimension == 2) {
    		
    	} else {
    		throw new UnsupportedOperationException("Cannot project graphs of higher dimensions");
    	}
    }
    
   //Not in the final code
    public double evaluate(double x) {
    	VariablesList v = new VariablesList(1);
    	v.setVariable("x_0", x);
    	return (double)((AbstractFunction)functionList.get(0)).evaluate(v);
    }
}
