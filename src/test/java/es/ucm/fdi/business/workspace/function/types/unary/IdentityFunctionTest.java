/**
 * This file is part of Dimension.
 * Dimension is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Dimension is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Dimension.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.business.workspace.function.types.unary;

import es.ucm.fdi.business.util.FunctionParserUtils;
import es.ucm.fdi.business.workspace.function.types.VariablesList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Checks the functionality of the identity function.
 *
 * @author Inmaculada Pérez
 * @see IdentityFunction
 */
public class IdentityFunctionTest {

    /**
     * Test of toString method, of class IdentityFunction.
     */
    @Test
    public void testToString() {
        System.out.println("Testing 'toString' method...");
        VariablesList vars = new VariablesList(new String[]{"x"});
        IdentityFunction instance = new IdentityFunction(FunctionParserUtils
                .parse("x", vars).toString(), vars);
        assertEquals("x", instance.toString());
    }

    /**
     * Test of evaluate method, of class IdentityFunction.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        String[] varNames = {"x"};
        VariablesList vars = new VariablesList(varNames);
        IdentityFunction instance = new IdentityFunction(FunctionParserUtils
                .parse("x", vars).toString(), vars);
        int[] varsValues = {18};
        VariablesList varsEvaluate = new VariablesList(varsValues, varNames);
        assertEquals(18.0, instance.evaluate(varsEvaluate), 0.0);
    }

}
