package main.java.es.ucm.fdi.trabajo.funcion.BinaryTypes;

import main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes.BinaryFunction;
import main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes.VariablesList;

public class ExponentialFunction extends BinaryFunction{
	public ExponentialFunction(String a, String b) {
		super(a,b,"Exponential");
	}
	public String toString() {
		String ret;
		ret="(" + f1.toString()+ ")"+"^"+"("+f2.toString() + ")";
		return ret;
	}
	public double evaluate(VariablesList variables) {
		return Double.valueOf(Math.pow(Double.valueOf(f1.evaluate(variables)),Double.valueOf(f2.evaluate(variables))));
	}
}
