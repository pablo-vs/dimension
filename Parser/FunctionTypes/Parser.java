package FunctionTypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Parser {
	public static void main(String[] args)  {
		System.out.println("Prueba parser, introduce la función:");
		 BufferedReader reader = 
                 new BufferedReader(new InputStreamReader(System.in));
		 String aux = null;
		try {
			aux = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Function conm = new Function();
		Function f = conm.parser(aux);
		System.out.println(f);
		System.out.println("Introduce un valor para evaluarla:");
		try {
			System.out.println(f.evaluate(reader.read()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
