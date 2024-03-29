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
import es.ucm.fdi.business.workspace.function.AbstractFunction;
import es.ucm.fdi.business.workspace.function.types.VariablesList;
import es.ucm.fdi.business.workspace.function.types.unary.IdentityFunction;
import java.util.ArrayList;
import java.util.Iterator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for Graph class.
 *
 * @see Graph
 * @author Arturo Acuaviva
 */
public class GraphTest {

    /**
     * We create a testing 3 dimensional graph.
     */
    Graph testGraph = new Graph(3);
    ArrayList<AbstractFunction> functionsList = new ArrayList<>();

    public GraphTest() {
    }

    @Before
    public void setUp() {
        functionsList.add(new IdentityFunction("x_1", new VariablesList(3)));
    }

    /**
     * Test of generate and getGrid methods, of class Graph.
     *
     * @throws es.ucm.fdi.business.exceptions.NoMatchDimensionException
     */
    @Test
    public void testGenerateAndGrid() throws NoMatchDimensionException {
        double[] dom_ini = {
            0.0, -1.0/*, 0.0*/};
        double[] dom_fin = {
            3.0, 2.0/*, 3.0*/};

        System.out.println("Graph generation: generate | getGrid");
        testGraph.add(new IdentityFunction("x_0", new VariablesList(2/*3*/)));
        testGraph.add(new IdentityFunction("x_1", new VariablesList(2/*3*/)));
        testGraph.add(new IdentityFunction("x_1", new VariablesList(2/*3*/)));
        testGraph.generate(dom_ini, dom_fin, 0);
        /*Iterator<Vertex> it = testGraph.getDomainIterator();
        Iterator<Vertex> it2 = testGraph.getRangeIterator();
        /*while(it.hasNext()) {
        	System.out.print(it.next());
        	System.out.print(" ");
        	System.out.println(it2.next());
        }
        		
        /* Checking the output produced
        Iterator it = testGraph.getCompositeIterator();
	  while (it.hasNext()) {
            System.out.println(it.next());
	  }
         */
    }

    /**
     * Test of getDimension method, of class Graph.
     */
    @Test
    public void testGetDimension() {
        System.out.println("getDimension");
        assertEquals(3, testGraph.getRangeDimension(), 0);
        assertEquals(2, new Graph(2).getRangeDimension(), 0);
        assertEquals(0, new Graph(0).getRangeDimension(), 0);
    }

    /**
     * Test of add, delete and getCompositeIterator methods, of class Graph.
     */
    @Test
    public void testCompositeInterface() {
        System.out.println("CompositeInterface: add | delete | getCompositeIterator"
                + " | deleteAll");

        Vertex v1 = new Vertex(1.2, 3.1, 2.3);
        Vertex v12 = new Vertex(1.0, 3.2, 3.0);

        // We adds the vertexes
        Vertex[] listOfVertex = {
            v1, new Vertex(), new Vertex(),
            new Vertex(), new Vertex(), new Vertex(),
            new Vertex(), new Vertex(), new Vertex(),
            new Vertex(), new Vertex(), v12};
        testGraph.addRange(v1);
        testGraph.addRange(new Vertex());
        testGraph.addRange(new Vertex());
        testGraph.addRange(new Vertex());
        testGraph.addRange(new Vertex());
        testGraph.addRange(new Vertex());
        testGraph.addRange(new Vertex());
        testGraph.addRange(new Vertex());
        testGraph.addRange(new Vertex());
        testGraph.addRange(new Vertex());
        testGraph.addRange(new Vertex());
        testGraph.addRange(v12);
        // Iterator testing
        Iterator it = testGraph.getRangeIterator();
        int counter = 0;
        while (it.hasNext()) {
            if (counter >= 12) {
                fail("We only added 12 elements");
            }
            assertEquals(listOfVertex[counter], it.next());
            counter++;
        }
        // test erase method
        //testGraph.deleteAll();
        //assertFalse("No elements in the graph", testGraph.getIteratorRange().hasNext());
    }

}
