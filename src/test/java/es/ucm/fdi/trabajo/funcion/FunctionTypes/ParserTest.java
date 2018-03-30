package es.ucm.fdi.trabajo.funcion.FunctionTypes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ParserTest {

	@Test
	public void parserTest()  {
		String aux = "(x*3)+5";
		Function conm = new Function();
		Function f = conm.parser(aux);
		int x = 5;
		System.out.println(f.evaluate(x));
	}
}
