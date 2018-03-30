package es.ucm.fdi.trabajo.funcion.SingleTypes;

import es.ucm.fdi.trabajo.funcion.FunctionTypes.SingleFunction;

public class ConstantFunction extends SingleFunction{
	public ConstantFunction(String a, int par) {
		super();
		f=null;
		this.par=par;
	}
	public String toString() {
		return Integer.toString(par);
	}
	public double evaluate(int x) {
		return par;
	}
}
