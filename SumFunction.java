package main.java.es.ucm.fdi.trabajo.funcion.BinaryTypes;

import main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes.BinaryFunction;
import main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes.Function;

public class SumFunction extends BinaryFunction{
	public SumFunction(String a, String b) {
		super(a,b,"Sum");
	}
	public String toString() {
		String ret=null;
		ret+="(" + f1.toString()+ ")"+"+"+"("+f2.toString() + ")";
		return ret;
	}
	public double evaluate(int x) {
		return (f1.evaluate(x)+f2.evaluate(x));
	}
}
