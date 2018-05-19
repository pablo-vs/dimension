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
import es.ucm.fdi.business.workspace.function.types.unary.ConstantFunction;
import es.ucm.fdi.business.workspace.function.types.unary.IdentityFunction;
import es.ucm.fdi.business.workspace.function.types.unary.Log10Function;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Checks the functionality of the exponential function.
 *
 * @author Inmaculada Pérez
 * @see ExponentialFunction
 */
public class ExponentialFunctionTest {

    /**
     * Test of toString method, of class ExponentialFunction.
     */
    @Test
    public void testToString() {
        System.out.println("Testing 'toString' method...");
        VariablesList vars = new VariablesList(new String[]{"x"});
        IdentityFunction instance1 = new IdentityFunction(FunctionParser
                .parse("x", vars).toString(), vars);
        Log10Function instance2 = new Log10Function(FunctionParser
                .parse("x", vars), vars);
        ExponentialFunction instanceExp = new ExponentialFunction(instance1, instance2, vars);
        assertEquals("(x) ^ (log(x))", instanceExp.toString());
    }

    /**
     * Test of evaluate method, of class ExponentialFunction.
     */
    @Test
    public void testEvaluate() {
        System.out.println("Testing 'evaluate' method...");
        VariablesList vars = new VariablesList(new String[]{"x"});
        IdentityFunction instance2 = new IdentityFunction(FunctionParser
                .parse("x", vars).toString(), vars);
        ConstantFunction instance1 = new ConstantFunction(Math.E, vars);
        ExponentialFunction instanceExp = new ExponentialFunction(instance1, instance2, vars);
        int[] varValues = {14};
        assertEquals(Math.pow(Math.E, 14),
                instanceExp.evaluate(new VariablesList(varValues, new String[]{"x"})), 0.1);
    }

}
