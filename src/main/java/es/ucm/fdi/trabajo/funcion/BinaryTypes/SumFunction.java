package main.java.es.ucm.fdi.trabajo.funcion.BinaryTypes;

import main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes.BinaryFunction;
import main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes.Function;
import main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes.VariablesList;

public class SumFunction extends BinaryFunction{
	public SumFunction(String a, String b) {
		super(a,b,"Sum");
	}
	public String toString() {
		String ret;
		ret="(" + f1.toString()+ ")"+"+"+"("+f2.toString() + ")";
		return ret;
	}
	public double evaluate(VariablesList variables) {
		return (f1.evaluate(variables)+f2.evaluate(variables));
	}
}
