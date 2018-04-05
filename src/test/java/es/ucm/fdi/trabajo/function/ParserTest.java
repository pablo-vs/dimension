package es.ucm.fdi.trabajo.function;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import es.ucm.fdi.trabajo.function.functiontypes.VariablesList;

public class ParserTest {

	@Test
	public void basicParserTest()  {
		String[] fs = {"5", "(3*x) + 5", "3*x + 5", "x", "3*(x^2) + x/5", "3*x + 5 + x", "3*x^2+4", "log_(3)(81)", "cos(2*PI)", "sin(3*PI/2)", "ln(e^10)",
				"ln(e^((x*1)+ln(e^5)))"};
		double[] res = {5, 20, 20, 5, 76, 25, 79, 4, 1, -1, 10, 10};
		String[] fs_vv = {"x+y+z", "x^(3*y-3*z+4)", "cos(PI*(tan(x+y+z*y-x*z+y)+y^(1)))"};
		double[] res_vv = {10,5, 0};
		FunctionParser parser = new FunctionParser();
		String[] varNames = {"x"};
		VariablesList vars = new VariablesList(varNames);
		String[] varNames2 = {"x", "y", "z"};
		VariablesList vars2 = new VariablesList(varNames2);
		int value = 5, value2=2, value3=3;
		System.out.println("First test, evaluating functions at x = " + value);
		for (int i = 0; i < fs.length; ++i) {
			String aux = fs[i];
			Function f = parser.parse(aux, vars);
			vars.setVariable("x", value);
			System.out.println("Input: " + fs[i] + " | Parsed function: " + f + " | Result = " + f.evaluate(vars));
			assertEquals(res[i], f.evaluate(vars), 0.01);
		}
		System.out.println("Second test, evaluating functions at x = " + value + " , y = " + value2  + ", z = "+ value3);
		for (int i = 0; i < fs_vv.length; ++i) {
			String aux = fs_vv[i];
			Function f = parser.parse(aux, vars2);
			vars2.setVariable("x", value);
			vars2.setVariable("y", value2);
			vars2.setVariable("z", value3);
			System.out.println("Input: " + fs_vv[i] + " | Parsed function: " + f + " | Result = " + f.evaluate(vars2));
			assertEquals(res_vv[i], f.evaluate(vars2), 0.01);
		}
	}
}
