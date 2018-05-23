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
package es.ucm.fdi.business.workspace.transformations;

import es.ucm.fdi.business.exceptions.NoMatchDimensionException;
import es.ucm.fdi.business.util.FunctionParser;
import es.ucm.fdi.business.workspace.Graph;
import es.ucm.fdi.business.workspace.function.AbstractFunction;
import es.ucm.fdi.business.workspace.function.types.VariablesList;
import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for RotateTransformation class.
 *
 * @see RotateTransformation
 * @author Arturo Acuaviva
 */
public class RotateTransformationTest {

    private RotateTransformation noRotation, rotation;

    public RotateTransformationTest() {
    }

    @Before
    public void setUp() {
        noRotation = new RotateTransformation(0, 0, 0);
        rotation = new RotateTransformation(0, Math.PI, 0);
    }

    /**
     * Test of apply method, of class RotateTransformation.
     *
     * @throws es.ucm.fdi.business.exceptions.NoMatchDimensionException
     */
    @Test
    public void testApply() throws NoMatchDimensionException {
        System.out.println("apply");
        double[] dom_ini = {
            0.0, -1.0, 0.0
        };
        double[] dom_fin = {
            3.0, 2.0, 3.0
        };
        Graph testGraph = new Graph(3);

        // The function to test is going to be f(x,y,z) = x+y+z
        String[] varNames = {"x", "y", "z"};
        VariablesList vars = new VariablesList(varNames);
        AbstractFunction functionX = FunctionParser.parse("x", vars);
        AbstractFunction functionY = FunctionParser.parse("y", vars);
        AbstractFunction functionZ = FunctionParser.parse("z", vars);
        
        System.out.println(functionX.toString() + " + " + functionY.toString() + 
                " + " + functionZ.toString());
        testGraph.add(functionX);
        testGraph.add(functionY);
        testGraph.add(functionZ);
        testGraph.generate(dom_ini, dom_fin, 2);

        System.out.println("Previous:");
        Iterator it = testGraph.getIteratorRange();

        while (it.hasNext()) {
            System.out.print("(" + it.next() + ") ");
        }
        System.out.println();

        //THIS FAILS
        noRotation.apply(testGraph);
        System.out.println("AfterNoModification:");
        it = testGraph.getIteratorRange();

        while (it.hasNext()) {
            System.out.print("(" + it.next() + ") ");
        }
        System.out.println();

        rotation.apply(testGraph);
        System.out.println("AfterModification:");
        it = testGraph.getIteratorRange();
        while (it.hasNext()) {
            System.out.print("(" + it.next() + ") ");
        }
        System.out.println();

    }

}
