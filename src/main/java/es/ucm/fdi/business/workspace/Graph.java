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

import es.ucm.fdi.business.exceptions.NoMatchDimensionException;
import es.ucm.fdi.business.util.MultiTreeMap;
import java.util.ArrayList;
import java.util.List;
import es.ucm.fdi.business.workspace.function.AbstractFunction;
import java.util.ListIterator;

/**
 * A graph object is used to represent an abstract interpretration of a
 * function. It contains two lists of {@link es.ucm.fdi.workspace Vertex} with
 * values from the range and the domain of the function. It also provides
 *
 * @author Brian Leiva
 * @author Eloy Mósig
 */
public class Graph implements ComponentComposite {

    /**
     *
     * Int value representing the dimension of the object depicted by the graph.
     */
    private final int dimension;

    /**
     * List of {@link es.ucm.fdi.workspace Vertex} containing the vertex in the
     * domain.
     */
    private final List<Vertex> domain = new ArrayList<>();

    /**
     * List of {@link es.ucm.fdi.workspace Vertex} containing the vertex in the
     * range.
     */
    private final List<ComponentComposite> range = new ArrayList<>();

    /**
     * Tree map with domain values.
     */
    private final MultiTreeMap<Integer, Integer> object = new MultiTreeMap<>((a, b) -> a - b);
    /**
     * Resolution of the graph, it indicates the factor used to calculate the
     * length of the
     */
    private int resolution;

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
     * @param functions list of functions to be evaluated
     * @param dom_ini
     * @param dom_fin
     * @param resolution of the graph generated
     * @throws NoMatchDimensionException
     */
    public void generate(List<AbstractFunction> functions, double[] dom_ini, double[] dom_fin, int resolution) throws NoMatchDimensionException {
        this.resolution = resolution;
        getGrid(dom_ini, dom_fin);
        for (int i = 0; i < domain.size(); ++i) {
            Vertex fv = new Vertex(functions.size());
            for (int j = 0; j < functions.size(); ++j) {
                fv.set(j, functions.get(j).evaluate(domain.get(i).getComps()));
            }
            range.add(fv);
        }
    }

    /**
     *
     * @return the dimension of the graph
     */
    public int getDimension() {
        return dimension;
    }

    @Override
    public void add(ComponentComposite component) {
        range.add(component);
    }

    @Override
    public void delete(ComponentComposite component) {
        if (!range.remove(component)) {
            throw new IllegalArgumentException("The component to be removed in "
                    + "the graph was not in the range");
        }
    }

    @Override
    public ListIterator<ComponentComposite> getCompositeIterator() {
        return range.listIterator();
    }

    @Override
    public void deleteAll() {
        range.removeAll(range);
    }
}