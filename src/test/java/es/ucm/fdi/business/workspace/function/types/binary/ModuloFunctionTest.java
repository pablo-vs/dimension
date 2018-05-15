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

import es.ucm.fdi.business.util.FunctionParserUtils;
import es.ucm.fdi.business.workspace.function.types.VariablesList;
import es.ucm.fdi.business.workspace.function.types.unary.ConstantFunction;
import es.ucm.fdi.business.workspace.function.types.unary.IdentityFunction;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Checks the functionality of the modulo function.
 *
 * @author Inmaculada Pérez
 * @see ModuloFunction
 */
public class ModuloFunctionTest {

    /**
     * Test of toString method, of class ModuloFunction.
     */
    @Test
    public void testToString() {
        System.out.println("Testing 'toString' method...");
        VariablesList vars = new VariablesList(new String[]{"x"});
        IdentityFunction instance1 = new IdentityFunction(FunctionParserUtils
                .parse("2*x", vars).toString(), vars);
        IdentityFunction instance2 = new IdentityFunction(FunctionParserUtils
                .parse("x", vars).toString(), vars);
        ModuloFunction instanceMod = new ModuloFunction(instance1, instance2, vars);
        assertEquals("((2.0) * (x)) % (x)", instanceMod.toString());
    }

    /**
     * Test of evaluate method, of class ModuloFunction.
     */
    @Test
    public void testEvaluate() {
        System.out.println("Testing 'evaluate' method...");
        VariablesList vars = new VariablesList(new String[]{"x"});
        ConstantFunction instance1 = new ConstantFunction(7.0, vars);
        IdentityFunction instance2 = new IdentityFunction(FunctionParserUtils
                .parse("x", vars).toString(), vars);
        ModuloFunction instanceMod = new ModuloFunction(instance1, instance2, vars);
        int[] varValues = {3};
        
        assertEquals(1.0, 
                instanceMod.evaluate(new VariablesList(varValues, new String[]{"x"})), 0.0);
    }
    
}
