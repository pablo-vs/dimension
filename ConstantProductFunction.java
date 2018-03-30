package main.java.es.ucm.fdi.trabajo.funcion.SingleTypes;

import main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes.Function;
import main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes.SingleFunction;

public class ConstantProductFunction extends SingleFunction{
	public ConstantProductFunction(String a, int par) {
		super(a, par, "ConstantProduct");
	}
	public String toString() {
		return Integer.toString(par)+f.toString();
	}
}
