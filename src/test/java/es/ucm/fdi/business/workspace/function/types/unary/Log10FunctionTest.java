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
 * Checks the functionality of the logarithmic with base 10 function.
 *
 * @author Inmaculada Pérez
 * @see Log10Function
 */
public class Log10FunctionTest {

    /**
     * Test of toString method, of class Log10Function.
     */
    @Test
    public void testToString() {
        System.out.println("Testing 'toString' method...");
        VariablesList vars = new VariablesList(new String[]{"x", "y"});
        Log10Function instance = new Log10Function(FunctionParserUtils
                .parse("x-y", vars), vars);
        assertEquals("log((x) - (y))", instance.toString());
    }

    /**
     * Test of evaluate method, of class Log10Function.
     */
    @Test
    public void testEvaluate() {
        System.out.println("Testing 'evaluate' method...");
        String[] varNames = {"x", "y"};
        VariablesList vars = new VariablesList(new String[]{"x", "y"});
        Log10Function instance = new Log10Function(FunctionParserUtils
                .parse("x-y", vars), vars);
        int[] varsValues = {20, 14};
        assertEquals(Math.log10(6), instance.evaluate(new VariablesList(varsValues, varNames)), 0.1);
    }

}
