package main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes;

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
		String s = null;	
		System.out.println(f);
		System.out.println("Introduce el n de variables");
		int x=0; try {
			try {
				x=Integer.parseInt(reader.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} 
		VariablesList variables = new VariablesList(x);
		for (int i=0; i<x; i++) {
			try {
				s=reader.readLine();
				variables.setVariable(i, Integer.parseInt(s));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		System.out.println(f.evaluate(variables));
	}
}
