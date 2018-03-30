package main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Parser {
	public static void main(String[] args)  {
		System.out.println("Prueba parser, introduce la funci�n:");
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
		String s = null;	
		int x=0;
		System.out.println(f);
		System.out.println("Introduce un valor para evaluarla:");
		try {
			s=reader.readLine();
		 x =Integer.parseInt(s);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(f.evaluate(x));
	}
}
