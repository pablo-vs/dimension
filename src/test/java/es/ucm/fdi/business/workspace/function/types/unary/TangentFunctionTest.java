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
package es.ucm.fdi.business.workspace.function.types.unary;

import es.ucm.fdi.business.util.FunctionParserUtils;
import es.ucm.fdi.business.workspace.function.types.VariablesList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Checks the functionality of the tangent function.
 *
 * @author Inmaculada Pérez
 * @see TangentFunction
 */
public class TangentFunctionTest {

    /**
     * Test of toString method, of class TangentFunction.
     */
    @Test
    public void testToString() {
        System.out.println("Testing 'toString' method...");
        VariablesList vars = new VariablesList(new String[]{"x", "y"});
        TangentFunction instance = new TangentFunction(FunctionParserUtils
                .parse("x*y", vars), vars);
        assertEquals("tan((x) * (y))", instance.toString());
    }

    /**
     * Test of evaluate method, of class TangentFunction.
     */
    @Test
    public void testEvaluate() {
        System.out.println("Testing 'evaluate' method...");
        String[] varNames = {"x", "y"};
        VariablesList vars = new VariablesList(varNames);
        TangentFunction instance = new TangentFunction(FunctionParserUtils
                .parse("x*y", vars), vars);
        int[] varsValues = {8, 6};
        assertEquals(Math.tan(Math.toRadians(48)),
                instance.evaluate(new VariablesList(varsValues, varNames)), 0.1);
    }

}
