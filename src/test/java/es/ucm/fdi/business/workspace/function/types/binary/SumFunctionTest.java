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
package es.ucm.fdi.business.workspace.function.types.binary;

import es.ucm.fdi.business.util.FunctionParser;
import es.ucm.fdi.business.workspace.function.types.VariablesList;
import es.ucm.fdi.business.workspace.function.types.unary.Log10Function;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Checks the functionality of the sum function.
 *
 * @author Inmaculada Pérez
 * @see SumFunction
 */
public class SumFunctionTest {

    /**
     * Test of toString method, of class SumFunction.
     */
    @Test
    public void testToString() {
        System.out.println("Testing 'toString' method...");
        VariablesList vars = new VariablesList(new String[]{"x"});
        Log10Function instance1 = new Log10Function(FunctionParser
                .parse("x", vars), vars);
        Log10Function instance2 = new Log10Function(FunctionParser
                .parse("2*x", vars), vars);
        SumFunction instanceSum = new SumFunction(instance1, instance2, vars);
        assertEquals("(log(x)) + (log((2.0) * (x)))", instanceSum.toString());
    }

    /**
     * Test of evaluate method, of class SumFunction.
     */
    @Test
    public void testEvaluate() {
        System.out.println("Testing 'evaluate' method...");
        VariablesList vars = new VariablesList(new String[]{"x"});
        Log10Function instance1 = new Log10Function(FunctionParser
                .parse("x", vars), vars);
        Log10Function instance2 = new Log10Function(FunctionParser
                .parse("2*x", vars), vars);
        SumFunction instanceSum = new SumFunction(instance1, instance2, vars);
        int[] varValues = {6};
        assertEquals(Math.log10(6.0) + Math.log10(12.0),
                instanceSum.evaluate(new VariablesList(varValues, new String[]{"x"})), 0.0);
    }

}
