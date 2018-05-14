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
package es.ucm.fdi.business.workspace.function.types.binary;

import es.ucm.fdi.business.workspace.function.types.VariablesList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the DivideFunction class.
 *
 * @author Inmaculada Pérez
 */
public class DivideFunctionTest {

    public DivideFunctionTest() {
    }

    /**
     * Test of toString method, of class DivideFunction.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        DivideFunction instance = new DivideFunction();
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of evaluate method, of class DivideFunction.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        VariablesList vars = null;
        DivideFunction instance = new DivideFunction();
        double expResult = 0.0;
        double result = instance.evaluate(vars);
        assertEquals(expResult, result, 0.0);
        fail("The test case is a prototype.");
    }

}
