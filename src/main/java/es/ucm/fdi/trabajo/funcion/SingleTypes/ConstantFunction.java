package main.java.es.ucm.fdi.trabajo.funcion.SingleTypes;

import main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes.SingleFunction;
import main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes.VariablesList;

public class ConstantFunction extends SingleFunction{
	public ConstantFunction(String a, int par) {
		super();
		f=null;
		this.par=par;
	}
	public String toString() {
		return Integer.toString(par);
	}
	public double evaluate(VariablesList variables) {
		return par;
	}
}
