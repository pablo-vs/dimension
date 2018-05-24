package es.ucm.fdi.business.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import es.ucm.fdi.business.workspace.function.AbstractFunction;
import es.ucm.fdi.business.workspace.function.types.VariablesList;
import org.junit.Before;

/**
 * Tests the functionality of the parser method for the functions.
 *
 * @author Eloy Mósig, Inmaculada Pérez
 * @see FunctionParserUtil
 */
public class FunctionParserUtilTest {

    /**
     * List of variables' names
     */
    private String[] variablesNames1 = {"x"};
    private String[] variablesNames2 = {"x", "y", "z"};
    /**
     * List of variables
     */
    private VariablesList variablesList1;
    private VariablesList variablesList2;
    /**
     * List of functions to test
     */
    private String[] functions1;
    private String[] functions2;
    private String[] functions3;
    /**
     * Correct results of the operations
     */
    private double[] correctResultFunctions1;
    private double[] correctResultFunctions2;
    private double[] correctResultFunctions3;
    /**
     * Value of the different variables used
     */
    private int valueX, valueY, valueZ;

    @Before
    public void initialize() {
        functions1 = new String[]{"5", "(3*x) + 5", "3*x + 5", "x", "3*(x^2) + x/5",
            "3*x + 5 + x", "3*x^2+4", "log_(3)(81)", "cos(2*PI)", "sin(3*PI/2)",
            "ln(e^10)", "ln(e^((x*1)+ln(e^5)))", "ln(0+e)+1"};
        functions2 = new String[]{"x+y+z", "x^(3*y-3*z+4)",
            "cos(PI*(tan(x+y+z*y-x*z+y)+y^(1)))"};
        functions3 = new String[]{"3", "5*x", "cosec(x)", "cotan((PI*x)/2)", "secan(x - 1)",
            "tan(x - 1)", "2^(2*x)"};
        correctResultFunctions1 = new double[]{5, 20, 20, 5, 76, 25, 79, 4, 1,
            -1.0, 10, 10, 2};
        correctResultFunctions2 = new double[]{10, 5, 1};
        correctResultFunctions3 = new double[]{3, 25, 1 / Math.sin(Math.toRadians(5)),
            1 / Math.tan(Math.toRadians(Math.PI * 5/ 2)), 1 / Math.cos(Math.toRadians(4)),
            Math.tan(Math.toRadians(4)), Math.pow(2, 10)};

        variablesList1 = new VariablesList(variablesNames1);
        variablesList2 = new VariablesList(variablesNames2);
        valueX = 5;
        valueY = 2;
        valueZ = 3;
    }

    @Test
    public void functionParserTest1() {
        System.out.println("First test, evaluating functions at x = " + valueX);
        for (int i = 0; i < functions1.length; ++i) {
            String aux = functions1[i];
            AbstractFunction f = FunctionParser.parse(aux, variablesList1);
            variablesList1.setVariable("x", valueX);
            System.out.println("Input: " + functions1[i] + " | Parsed function: " + f
                    + " | Result = " + f.evaluate(variablesList1));
            assertEquals(correctResultFunctions1[i], f.evaluate(variablesList1), 0.01);
        }
    }

    @Test
    public void functionParserTest2() {
        System.out.println("Second test, evaluating functions at x = " + valueX
                + " , y = " + valueY + ", z = " + valueZ);
        for (int i = 0; i < functions2.length; ++i) {
            String aux = functions2[i];
            AbstractFunction f = FunctionParser.parse(aux, variablesList2);
            variablesList2.setVariable("x", valueX);
            variablesList2.setVariable("y", valueY);
            variablesList2.setVariable("z", valueZ);
            System.out.println("Input: " + functions2[i] + " | Parsed function: "
                    + f + " | Result = " + f.evaluate(variablesList2));
            assertEquals(correctResultFunctions2[i], f.evaluate(variablesList2), 0.01);
        }
    }

    @Test
    public void functionParserTest3() {
        System.out.println("Third test, evaluating functions at x = " + valueX);
        for (int i = 0; i < functions3.length; ++i) {
            AbstractFunction f = FunctionParser.parse(functions3[i], variablesList1);
            variablesList1.setVariable("x", valueX);
            StringBuilder check = new StringBuilder("Input: ");
            check.append(functions3[i]).append(" | Parsed function: ").append(f)
                    .append(" | Result = ").append(f.evaluate(variablesList1));
            System.out.println(check.toString());
            assertEquals(correctResultFunctions3[i], f.evaluate(variablesList1), 0.01);
        }
    }
}
