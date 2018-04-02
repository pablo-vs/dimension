package es.ucm.fdi.trabajo.funcion.functiontypes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ParserTest {

	@Test
	public void parserTest()  {
		String[] fs = {"5", "(3*x) + 5", "3*x + 5", "x", "3*(x^2) + x/5", "3*x + 5 + x", "3*x^2+4"};
		double[] res = {5, 20, 20, 5, 76, 25, 79};
		FunctionParser parser = new FunctionParser();
		String[] varNames = {"x"};
		VariablesList vars = new VariablesList(varNames);
		int value = 5;
		System.out.println("Evaluating functions at x = " + value);
		for (int i = 0; i < fs.length; ++i) {
			String aux = fs[i];
			Function f = parser.parse(aux, vars);
			vars.setVariable("x", value);
			System.out.println("Input: " + fs[i] + " | Parsed function: " + f + " | Result = " + res[i]);
			assertEquals(res[i], f.evaluate(vars), 0.01);
		}
	}
}
