package main.java.es.ucm.fdi.trabajo.funcion.SingleTypes;

import main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes.Function;
import main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes.SingleFunction;

public class IdentityFunction extends SingleFunction{
	public IdentityFunction(String a, int par) {
		type="Identity";
		par=1;
		f=null;
	}
	public String toString() {
		return "x";
	}
	public double evaluate(int x) {
		System.out.println("X:" + x);
		return (x);
	}
}
