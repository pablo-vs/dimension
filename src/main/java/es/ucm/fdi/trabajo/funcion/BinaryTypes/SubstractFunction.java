package es.ucm.fdi.trabajo.funcion.BinaryTypes;

import es.ucm.fdi.trabajo.funcion.FunctionTypes.BinaryFunction;

public class SubstractFunction extends BinaryFunction{
	public SubstractFunction(String a, String b) {
		super(a,b,"Substract");
	}
	public String toString() {
		String ret=null;
		ret+="(" + f1.toString()+ ")"+"-"+"("+f2.toString() + ")";
		return ret;
	}
	public double evaluate(int x) {
		return (f1.evaluate(x)-f2.evaluate(x));
	}
}
