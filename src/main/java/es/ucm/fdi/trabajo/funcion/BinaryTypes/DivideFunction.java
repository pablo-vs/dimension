package main.java.es.ucm.fdi.trabajo.funcion.BinaryTypes;

import main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes.BinaryFunction;
import main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes.VariablesList;

public class DivideFunction extends BinaryFunction{
	public DivideFunction(String a, String b) {
		super(a,b,"Division");
	}
	public String toString() {
		String ret=null;
		ret+="(" + f1.toString()+ ")"+"/"+"("+f2.toString() + ")";
		return ret;
	}
	public double evaluate(VariablesList variables) {
		return (f1.evaluate(variables)/f2.evaluate(variables));
	}
}
//((x_0^3)*(x_1^(x_1*3))+x_0*3)
//x^((x^2)+x*(x^2))+x*4