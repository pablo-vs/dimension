package es.ucm.fdi.business_tier.workspace.function.functiontypes;

import es.ucm.fdi.business_tier.workspace.function.types.VariablesList;
import org.junit.Test;
import static org.junit.Assert.*;

public class VariableTest {

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
