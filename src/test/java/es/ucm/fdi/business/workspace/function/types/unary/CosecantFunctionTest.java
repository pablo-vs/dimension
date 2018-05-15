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
 * Checks the functionality of the cosecant function.
 *
 * @author Inmaculada Pérez
 * @see CosecantFunction
 */
public class CosecantFunctionTest {

    /**
     * Test of toString method, of class CosecantFunction.
     */
    @Test
    public void testToString() {
        System.out.println("Testing 'toString' method...");
        VariablesList vars = new VariablesList(new String[]{"x"});
        CosecantFunction instance = new CosecantFunction(FunctionParserUtils
                .parse("PI/2", vars), vars);
        assertEquals("cosec((3.141592653589793) / (2.0))", instance.toString());
    }

    /**
     * Test of evaluate method, of class CosecantFunction.
     */
    @Test
    public void testEvaluate() {
        System.out.println("Testing 'evaluate' method...");
        VariablesList vars = new VariablesList(new String[]{"x"});
        CosecantFunction instance = new CosecantFunction(FunctionParserUtils
                .parse("PI/2", vars), vars);
        assertEquals(1 / Math.sin(Math.toRadians(Math.PI / 2)), instance.evaluate(vars), 0.1);
    }

}
