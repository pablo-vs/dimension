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
import es.ucm.fdi.business.util.Matrix;
import es.ucm.fdi.business.util.MultiTreeMap;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import es.ucm.fdi.business.workspace.function.AbstractFunction;
import es.ucm.fdi.business.workspace.function.types.VariablesList;

import java.util.ListIterator;
import java.util.Map.Entry;

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
	 * Int value representing the dimension of the range of the function depicted by
	 * the graph.
	 */
	@XmlElement
	private int rangeDim = 0;

	/**
	 *
	 * Int value representing the dimension of the domain of the function depicted
	 * by the graph.
	 */
	@XmlElement
	private int domainDim = 0;
	
	@XmlElement
	private List<Double> axis_length = new ArrayList<>();

	/**
	 * List of {@link es.ucm.fdi.workspace Vertex} containing the vertex in the
	 * domain.
	 */
	@XmlElement
	private List<Vertex> domain = new ArrayList<>();

	/**
	 * List of {@link es.ucm.fdi.workspace Vertex} containing the vertex in the
	 * range.
	 */
	@XmlElement
	private List<Vertex> range = new ArrayList<>();

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
	private MultiTreeMap<Integer, Integer> object = new MultiTreeMap<>((a, b) -> a - b);

	/**
	 * Resolution of the graph, it indicates the factor used to calculate the length
	 * of the
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
		this.rangeDim = dimension;
	}

	/**
	 * Calculates the domain (the points where the function will be evaluated). The
	 * parameters are the list of bounds where the function can be evaluated.
	 *
	 * @param dom_ini
	 * @param dom_fin
	 */
	private void getGrid(double[] dom_ini, double[] dom_fin) {
		if (dom_ini.length != dom_fin.length) {
			throw new IllegalArgumentException();
		}
		domainDim = dom_ini.length;
		for(int i = 0; i < domainDim; ++i) {
			axis_length.add(dom_fin[i]-dom_ini[i]);
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
		// tam = número de vértices por cada dimensión
		// dim = número total de vértices
		int n = 1;
		// Para cada dimension
		for (int i = 0; i < tam.length; ++i) {
			// suma = posición actual en la dimensión i-esima
			double suma = dom_ini[i];
			int aux = n;
			n *= tam[i];
			// Para cada posición en esta dimension
			for (int j = 0; j < tam[i]; ++j) {
				// Empezando en el vértice que ocupa la posición j-esima de la dimensión actual
				for (int k = j * aux; k < dim; k += n) {
					/*
					 * System.out.println(j*aux); System.out.println(k == j*aux);
					 * System.out.println(aux); System.out.println(dim); System.out.println(n);
					 */
					int cont = 0;
					while (cont < aux) {
						domain.get(k + cont).set(i, suma);
						if (j > 0) {
							object.putValue(k + cont, k + cont - aux);
						}
						++cont;
					}
				}
				suma += lado;
			}
		}
	}

	/**
	 * Generates the graph. Given a List of functions and a numeric resolution, it
	 * generates a grid for the given points and then generates the vertex
	 * representing the n-dimensional points in the range.
	 *
	 * @param dom_ini
	 * @param dom_fin
	 * @param resolution
	 *            of the graph generated
	 * @throws NoMatchDimensionException
	 */
	public void generate(double[] dom_ini, double[] dom_fin, int resolution) throws NoMatchDimensionException {
		if(rangeDim != functionList.size()) {
			throw new NoMatchDimensionException("There are undefined range dimensions");
		}
		this.resolution = resolution;
		getGrid(dom_ini, dom_fin);
		for(int i = 0; i < rangeDim; ++i) {
			axis_length.add(0.0);
		}
		for (int i = 0; i < domain.size(); ++i) {
			Vertex fv = new Vertex(functionList.size());
			for (int j = 0; j < rangeDim; ++j) {
				double val = ((AbstractFunction) functionList.get(j)).evaluate(domain.get(i).getComps());
				if(Math.abs(val) > axis_length.get(domainDim+j)) {
					axis_length.set(domainDim+j, val);
				}
				fv.set(j, val);
			}
			addRange(fv);
		}
	}

	/**
	 *
	 * @return The dimension of the graph's range.
	 */
	public int getRangeDimension() {
		return rangeDim;
	}

	/**
	 *
	 * @return The dimension of the graph's domain.
	 */
	public int getDomainDimension() {
		return domainDim;
	}

	/**
	 * Adds a new vertexes to the Graph object. It is added to the range.
	 *
	 * @param component
	 *            which will be added
	 */
	public void addRange(Vertex component) {
		range.add(component);
	}

	/**
	 * Returns an operator over the list of vertexes that a Graph object contains in
	 * its range.
	 *
	 * @return listIterator over the elements of the graph
	 */
	public ListIterator<Vertex> getRangeIterator() {
		return range.listIterator();
	}

	/**
	 * Returns an operator over the list of vertexes that a Graph object contains in
	 * its domain.
	 *
	 * @return listIterator over the elements of the graph
	 */
	public ListIterator<Vertex> getDomainIterator() {
		return domain.listIterator();
	}

	/**
	 * Removes all the elements in the range. All vertexes are deleted.
	 */
	public void cleanRangeList() {
		range.removeAll(range);
	}

	/**
	 * Adds a new ComponentComposite to the Graph object. Typically in Graph this
	 * elements will be vertex.
	 *
	 * @param component
	 *            which will be added
	 */
	public void add(AbstractFunction func) {
		functionList.add(func);
		if(rangeDim < functionList.size()) {
			++rangeDim;
		}
	}

	public void paint(Graphics2D g, double x0, double y0, double scale) {
		if (rangeDim == 2) {
			for (Entry<Integer, ArrayList<Integer>> connection : object.entrySet()) {
				Vertex ini = range.get(connection.getKey());
				for (Integer end : connection.getValue()) {
					double x1 = x0 + ini.at(0) * scale, y1 = y0 - ini.at(1) * scale,
							x2 = x0 + range.get(end).at(0) * scale, y2 = y0 - range.get(end).at(1) * scale;
					if (0 < x1 && x1 < 2 * x0 && 0 < y1 && y1 < 2 * y0 && 0 < x2 && x2 < 2 * x0 && 0 < y2
							&& y2 < 2 * y0) {
						Path2D.Double path = new Path2D.Double();
						path.moveTo(x0 + ini.at(0) * scale, y0 - ini.at(1) * scale);
						path.lineTo(x0 + range.get(end).at(0) * scale, y0 - range.get(end).at(1) * scale);
						g.draw(path);
					}
				}
			}
		} else {
			throw new UnsupportedOperationException("Cannot paint graphs of higher dimensions");
		}
	}

	public Graph getCombinedGraph() {
		Graph result = new Graph(domainDim + rangeDim);
		Iterator<Vertex> domainIt = domain.iterator();
		Iterator<Vertex> rangeIt = range.iterator();
		while (domainIt.hasNext() && rangeIt.hasNext()) {
			result.addRange(new Vertex(domainIt.next().getComps(), rangeIt.next().getComps()));
		}
		result.object = this.object;
		result.axis_length = axis_length;
		return result;
	}

	public Graph projectTo2D(Vertex from, Vertex to, Vertex lat, Vertex up) throws NoMatchDimensionException {
		Graph result = new Graph(2);
		if (rangeDim == 3) {
			Matrix baseChange = (new Matrix(to, lat/*Vertex.cross(to, up)*/, up)).inverse();
			for (Vertex v : range) {
				Vertex changedPoint = baseChange.multiply(v.substract(from));
				Vertex projection = new Vertex(changedPoint.at(1) / changedPoint.at(0),
						changedPoint.at(2) / changedPoint.at(0));
				result.addRange(projection);
			}
		} else {
			throw new UnsupportedOperationException("Cannot project graphs of higher dimensions");
		}
		result.object = object;
		result.axis_length = axis_length;
		return result;
	}

	public void drawAxis(int res) {
			try {
				double dist = 1/Math.pow(2, res);
				for(int i = 0; i < rangeDim; ++i) {
					double length = axis_length.get(i);
					double num = 2*length/dist;
					Vertex v;
					v = new Vertex(rangeDim);
					v.set(i, -length);
					range.add(v);
					for(int j = 0; j < num; ++j) {
						Vertex v2 = new Vertex(rangeDim);
						v2.set(i, -length + j*dist);
						range.add(v2);
						System.out.println("From " + range.get(range.size()-1) + " to " + range.get(range.size()-2));
						object.putValue(range.size() - 1, range.size() - 2);
					}
				}
			} catch (NoMatchDimensionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	// Not in the final code
	public double evaluate(double x) {
		VariablesList v = new VariablesList(1);
		v.setVariable("x_0", x);
		return (double) ((AbstractFunction) functionList.get(0)).evaluate(v);
	}
}
