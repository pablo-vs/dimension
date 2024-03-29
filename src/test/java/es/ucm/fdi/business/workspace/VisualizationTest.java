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
import es.ucm.fdi.business.util.FunctionParser;
import es.ucm.fdi.business.workspace.function.AbstractFunction;
import es.ucm.fdi.business.workspace.function.types.VariablesList;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for Visualization class.
 *
 * @see Visualization
 * @author Arturo Acuaviva
 */
public class VisualizationTest {

    /**
     * Nodes where the vertexes will be set up
     */
    double[] n1 = {1, 3, 2, 4, 6}, n2 = {3, 7, 5, 1, 4}, n3 = {2, 8, 4, 2, 1}, n4 = {3, 3, 5, 5, 6},
            n5 = {3, 8, 5, 3, 0}, n6 = {1, 0, 0, 0, 0}, n7 = {1, 2, 2, 4, 4}, n8 = {1, 0, 2, 5, 0},
            n9 = {1, 1, 2, 1, 2}, n10 = {3, 0, 5, 0, 0}, n11 = {3, 2, 5, 9, 6}, n12 = {5, 3, 5, 2, 1};
    /**
     * Hyperplanes where the graph will be evaluated
     */
    double[] hp = {3, 5};
    /**
     * 5D vertexes which will be projected
     */
    Vertex v1 = new Vertex(n1), v2 = new Vertex(n2), v3 = new Vertex(n3), v4 = new Vertex(n4), v5 = new Vertex(n5),
            v6 = new Vertex(n6), v7 = new Vertex(n7), v8 = new Vertex(n8), v9 = new Vertex(n9), v10 = new Vertex(n10),
            v11 = new Vertex(n11), v12 = new Vertex(n12);
    /**
     * 5 dimension graph which will be projected
     */
    Graph graph = new Graph(5);

    /**
     * Sets up the graph
     */
    @Before
    public void setUp() {
        graph.addRange(v1);
        graph.addRange(v2);
        graph.addRange(v3);
        graph.addRange(v4);
        graph.addRange(v5);
        graph.addRange(v6);
        graph.addRange(v7);
        graph.addRange(v8);
        graph.addRange(v9);
        graph.addRange(v10);
        graph.addRange(v11);
        graph.addRange(v12);
    }

    @Test
    public void projectGraphTest() throws NoMatchDimensionException {
        System.out.println("projectGraph");

        // We project a 5D graph into 3D with the axis X,Y,Z being 1st, 2nd and
        // 4th dimensions from the 5D graph respectively.
        double results[] = {7, 1, 4, 3, 5, 6, 8, 3, 0, 0, 0, 0, 2, 9, 6};
        int counter = 0;

        Graph newGraph = Visualization.projectGraph(graph, 1, 3, 4, hp);
        Iterator newIt = newGraph.getRangeIterator();

        while (newIt.hasNext()) {
            Vertex v = (Vertex) newIt.next();
            assertTrue("The new graph has correct list of new vertexes",
                    v.at(0) == results[counter] && v.at(1) == results[1 + counter] && v.at(2) == results[2 + counter]);
            counter += 3;
        }
    }

    /**
     * Test of add, delete and getCompositeIterator method, of class
     * Visualization.
     */
    @Test
    public void testCompositeInterface() {

        // We previosly added these vertexes in the setUp method
        Vertex[] listOfVertex = {new Vertex(n1), new Vertex(n2), new Vertex(n3), new Vertex(n4), new Vertex(n5),
            new Vertex(n6), new Vertex(n7), new Vertex(n8), new Vertex(n9), new Vertex(n10), new Vertex(n11),
            new Vertex(n12)};

        // Iterator testing
        Iterator it = graph.getRangeIterator();
        int counter = 0;
        while (it.hasNext()) {
            if (counter >= 12) {
                fail("We only added 12 elements");
            }
            assertEquals(listOfVertex[counter], it.next());
            counter++;
        }
        // deleting the last and the first element
        /*
		 * graph.delete(v1); graph.delete(v12); it = graph.getCompositeIterator();
		 * counter = 1; while (it.hasNext()) { if (counter >= 11) {
		 * fail("We had 12 elements and deleted 2 elements"); }
		 * assertEquals(listOfVertex[counter], it.next()); counter++; }
		 * graph.deleteAll(); assertFalse("No elements in the graph",
		 * graph.getCompositeIterator().hasNext());
         */

    }

    private static class FGraph extends JFrame {

        public FGraph() {
            SwingUtilities.invokeLater(() -> {
                InitGUI();
            });
        }

        public void InitGUI() {
            Graph g = new Graph(1);
            String[] var = {"x", "y"};
            AbstractFunction f = FunctionParser.parse("y^2 + (x^3)/20", new VariablesList(var));
            g.add(f);
            Visualization view = new Visualization();
            double[] ini = {-3, -3}, end = {5, 3};
            try {
                int res = 2;
                double scale = 60;
                List<Double> scales = new ArrayList<>();
                scales.add(scale);
                g.generate(ini, end, res);
                view.add(g, Color.CYAN);
                Vertex from = new Vertex(-8, -2, 5),
                        to = new Vertex(3, 0, -1),
                        lat = new Vertex(0, 0.5, 0),
                        up = new Vertex(0, 0, 1);
                List<List<Vertex>> params = new ArrayList<>();
                List<Vertex> p1 = new ArrayList<>();
                p1.add(from);
                p1.add(to);
                p1.add(lat);
                p1.add(up);
                params.add(p1);
                List<BufferedImage> result = view.paint(1080, 720, res, params, scales);
                this.add(new JLabel(new ImageIcon(result.get(0))));
                this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            } catch (NoMatchDimensionException e) {

            }

            repaint();
            revalidate();
            setSize(1080, 720);
            setVisible(true);
        }

    }

    public static void main(String[] args) {
        FGraph f = new FGraph();
    }

}
