package main.java.es.ucm.fdi.trabajo.funcion.BinaryTypes;

import main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes.BinaryFunction;

public class ExponentialFunction extends BinaryFunction{
	public ExponentialFunction(String a, String b) {
		super(a,b,"Exponential");
	}
	public String toString() {
		String ret=null;
		ret+="(" + f1.toString()+ ")"+"^"+"("+f2.toString() + ")";
		return ret;
	}
	public double evaluate(int x) {
		return Double.valueOf(Math.pow(Double.valueOf(f1.evaluate(x)),Double.valueOf(f2.evaluate(x))));
	}
}
