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
import es.ucm.fdi.business.workspace.Vertex;
import es.ucm.fdi.business.workspace.function.AbstractFunction;
import es.ucm.fdi.business.workspace.function.AbstractFunction.Parser;
import es.ucm.fdi.business.workspace.function.types.VariablesList;
import es.ucm.fdi.business.workspace.function.types.unary.IdentityFunction;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
* JUnit test for ScaleTransformation class.
*
* @see ScaleTransformation
* @author Brian Leiva
*/
public class ScaleTransformationTest {
  
  ScaleTransformation noScale, scale;
  
  public ScaleTransformationTest() {
  }
 
 // @Before
  public void setUp() {
      noScale = new ScaleTransformation(1, 1, 1);
      scale = new ScaleTransformation(1, 10, 1);
  }
  
  /**
   * Test of apply method, of class RotateTransformation.
   * @throws es.ucm.fdi.business.exceptions.NoMatchDimensionException
   */
  @Test
  public void testApply() throws NoMatchDimensionException {
  	setUp();
  	System.out.println("apply");
    double[] dom_ini = { 0.0, -1.0, 0.0 };
    double[] dom_fin = { 3.0, 2.0, 3.0 };

    Graph testGraph = new Graph(3);
      
	// The function to test is going to be f(x,y,z) = (x+y+z, y, x+z)
	String[] varNames = {"x", "y", "z"};
	VariablesList vars = new VariablesList(varNames);
	AbstractFunction function1 = FunctionParser.parse("x + y + z", vars);
	System.out.println(function1.toString());
	testGraph.add(function1);
	AbstractFunction function2 = FunctionParser.parse("y", vars);
	System.out.println(function2.toString());
	testGraph.add(function2);
	AbstractFunction function3 = FunctionParser.parse("x + z", vars);
	System.out.println(function3.toString());
	testGraph.add(function3);
	testGraph.generate(dom_ini, dom_fin, 2);
	
	System.out.println("Previous:");
	Iterator it = testGraph.getIteratorRange();
	List<Vertex> range1 = new ArrayList<>();
	List<Vertex> range2 = new ArrayList<>();
	while(it.hasNext()) {
		Vertex v = (Vertex) it.next();
        System.out.print("(" + v + ") ");
	    range1.add(v);
	}
	noScale.apply(testGraph);
  	System.out.println();
    System.out.println("AfterNoModification:");
	it = testGraph.getIteratorRange();
	while(it.hasNext()) {
		Vertex v = (Vertex) it.next();
        System.out.print("(" + v + ") ");
	    range2.add(v);
	}
	assertEquals("Invalid range results", range1, range2);
	
  	scale.apply(testGraph);
  	System.out.println();
  	System.out.println("AfterModification:");
    it = testGraph.getIteratorRange();
    while (it.hasNext()) {
        System.out.print("(" + it.next() + ") ");
    }
    System.out.println();

  }
}