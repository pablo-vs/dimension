package es.ucm.fdi.business.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import es.ucm.fdi.business.workspace.function.AbstractFunction;
import es.ucm.fdi.business.workspace.function.types.VariablesList;
import org.junit.Before;

/**
 * JUnit test for FunctionParser class.
 *
 * @see FunctionParser
 * @author Eloy Mósig
 * @author Arturo Acuaviva
 */
public class FunctionParserUtilTest {

    /**
     * List of functions tested at basic test
     */
    String[] functionsBasicTest;
    /**
     * List of functions tested at basic test
     */
    String[] functionsBasicTest2;
    /**
     * List of functions tested at extension of other functions test
     */
    String[] otherFunctionsTest;
    /**
     * Correct output for basic test first set of functions
     */
    double[] correctOutputBasicTest;
    /**
     * Correct output for basic test second set of functions
     */
    double[] correctOutputBasicTest2;
    /**
     * Correct output for other function test set of functions
     */
    double[] correctOutputOtherFunctions;
    /**
     * List of variables names used in the functions
     */
    String[] varNames = {"x"};
    /**
     * List of variables names used in the functions
     */
    String[] varNames2 = {"x", "y", "z"};
    /**
     * List of variables used in the functions
     */
    VariablesList vars;
    /**
     * List of variables used in the functions
     */
    VariablesList vars2;

    /**
     * Creates and initializes all the objects to be used at parsing.
     */
    @Before
    public void setUp() {
        //Basic test
        functionsBasicTest = new String[]{"5", "(3*x) + 5", "3*x + 5", "x", "3*(x^2) + x/5",
            "3*x + 5 + x", "3*x^2+4", "log_(3)(81)", "cos(2*PI)", "sin(3*PI/2)",
            "ln(e^10)", "ln(e^((x*1)+ln(e^5)))", "ln(0+e)+1"};

        correctOutputBasicTest = new double[]{5, 20, 20, 5, 76, 25, 79, 4, 1,
            0.08215400811873169, 10, 10, 2};
        functionsBasicTest2 = new String[]{"x+y+z", "x^(3*y-3*z+4)",
            "cos(PI*(tan(x+y+z*y-x*z+y)+y^(1)))"};
        correctOutputBasicTest2 = new double[]{10, 5, 1.0};
        // Other functions parser test
        otherFunctionsTest = new String[]{"3", "5*x", "cosec(x)", "cotan((PI*x)/2)", "secan(x - 1)",
            "tan(x - 1)", "2^(2*x)"};
        correctOutputOtherFunctions = new double[]{3, 5, 57.29, 36.46, 1, 0, 4};

        // general variables creation for testing
        vars = new VariablesList(varNames);
        vars2 = new VariablesList(varNames2);
    }

    /**
     * Test for basic functions. This test checks the functioning of the
     * identity function, the constant function, the product function, the
     * substract function, the sum function, the logarithmic function, the
     * exponential function, the modulo function, the tangent function, the ln
     * function, the log10 function, the secant function, the sine function, the
     * cosine function, and, more generally, the binary and unary functions.
     */
    @Test
    public void basicParserTest() {
        int value = 5, value2 = 2, value3 = 3;
        System.out.println("First test, evaluating functions at x = " + value);
        for (int i = 0; i < functionsBasicTest.length; ++i) {
            String aux = functionsBasicTest[i];
            AbstractFunction f = FunctionParser.parse(aux, vars);
            vars.setVariable("x", value);
            System.out.println("Input: " + functionsBasicTest[i] + " | Parsed function: " + f
                    + " | Result = " + f.evaluate(vars));
            assertEquals(correctOutputBasicTest[i], f.evaluate(vars), 0.01);
        }
        System.out.println("Second test, evaluating functions at x = " + value
                + " , y = " + value2 + ", z = " + value3);
        for (int i = 0; i < functionsBasicTest2.length; ++i) {
            String aux = functionsBasicTest2[i];
            AbstractFunction f = FunctionParser.parse(aux, vars2);
            vars2.setVariable("x", value);
            vars2.setVariable("y", value2);
            vars2.setVariable("z", value3);
            System.out.println("Input: " + functionsBasicTest2[i] + " | Parsed function: "
                    + f + " | Result = " + f.evaluate(vars2));
            assertEquals(correctOutputBasicTest2[i], f.evaluate(vars2), 0.01);
        }
    }

    /**
     * Test for functions. This test checks the functioning the identity
     * function, the constant function, the product function, the cosecant and
     * cotangent functions, the secan function, the exponential function and the
     * tangent function.
     */
    @Test
    public void otherFuctionsParserTest() {
        int value = 1;
        StringBuilder sb = new StringBuilder("Evaluating some other functions at x = ");
        sb.append(String.valueOf(value));
        System.out.println(sb.toString());
        for (int i = 0; i < otherFunctionsTest.length; ++i) {
            String aux = otherFunctionsTest[i];
            AbstractFunction f = FunctionParser.parse(aux, vars);
            vars.setVariable("x", value);
            StringBuilder check = new StringBuilder("Input: ");
            check.append(otherFunctionsTest[i]).append(" | Parsed function: ").append(f).append(" | Result = ")
                    .append(f.evaluate(vars));
            System.out.println(check.toString());
            assertEquals(correctOutputOtherFunctions[i], f.evaluate(vars), 0.01);
        }
    }
}
