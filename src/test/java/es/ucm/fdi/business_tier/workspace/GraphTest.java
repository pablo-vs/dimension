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
import es.ucm.fdi.business_tier.workspace.function.AbstractFunction;
import es.ucm.fdi.business_tier.workspace.function.types.VariablesList;
import es.ucm.fdi.business_tier.workspace.function.types.unary.IdentityFunction;
import java.util.ArrayList;
import java.util.Iterator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
        functionsList.add(new IdentityFunction("x", new VariablesList(1)));
    }

    /**
     * Test of generate and getGrid methods, of class Graph.
     *
     * @throws es.ucm.fdi.business_tier.exceptions.NoMatchDimensionException
     */
    @Test
    public void testGenerateAndGrid() throws NoMatchDimensionException {
        double[] dom_ini = {
            0.0, -1.0, 0.0
        };
        double[] dom_fin = {
            3.0, 2.0, 3.0
        };

        System.out.println("Graph generation: generate | getGrid");
        testGraph.generate(functionsList, dom_ini, dom_fin, 250);

        Iterator it = testGraph.getCompositeIterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    /**
     * Test of getDimension method, of class Graph.
     */
    @Test
    public void testGetDimension() {
        System.out.println("getDimension");
        assertEquals(3, testGraph.getDimension(), 0);
        assertEquals(2, new Graph(2).getDimension(), 0);
        assertEquals(0, new Graph(0).getDimension(), 0);
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
        testGraph.add(v1);
        testGraph.add(new Vertex());
        testGraph.add(new Vertex());
        testGraph.add(new Vertex());
        testGraph.add(new Vertex());
        testGraph.add(new Vertex());
        testGraph.add(new Vertex());
        testGraph.add(new Vertex());
        testGraph.add(new Vertex());
        testGraph.add(new Vertex());
        testGraph.add(new Vertex());
        testGraph.add(v12);
        // Iterator testing
        Iterator it = testGraph.getCompositeIterator();
        int counter = 0;
        while (it.hasNext()) {
            if (counter >= 12) {
                fail("We only added 12 elements");
            }
            assertEquals(listOfVertex[counter], it.next());
            counter++;
        }
        // deleting the last and the first element
        testGraph.delete(v1);
        testGraph.delete(v12);
        it = testGraph.getCompositeIterator();
        counter = 1;
        while (it.hasNext()) {
            if (counter >= 11) {
                fail("We had 12 elements and deleted 2 elements");
            }
            assertEquals(listOfVertex[counter], it.next());
            counter++;
        }
        // test erase method
        testGraph.deleteAll();
        assertFalse("No elements in the graph", testGraph.getCompositeIterator().hasNext());
    }

}
