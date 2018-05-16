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
package es.ucm.fdi.business.workspace.function;

import es.ucm.fdi.business.workspace.function.types.VariablesList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit test for VariablesList class.
 *
 * @see VariablesList
 * @author Inmaculada Pérez
 */
public class VariablesListTest {

    @Test
    public void variableTest() {
        VariablesList l1, l2;
        String[] vars = {"x", "y", "z", "t"};
        l1 = new VariablesList(vars);
        l2 = new VariablesList(5);
        assertEquals(0, l1.getVariable("y"), 0.01);
        l1.setVariable("y", 12);
        l1.setVariable("x", 28);
        l1.setVariable("z", 34);
        assertEquals(12, l1.getVariable("y"), 0.01);
        assertEquals(l1.getVariable(2), l1.getVariable("y"), 0.01);
        l1.setVariable(3, 5);
        l2.setVariable(4, 18);
        assertEquals(5, l1.getVariable("z"), 0.01);
        assertEquals(18, l2.getVariable(4), 0.01);
    }
}
