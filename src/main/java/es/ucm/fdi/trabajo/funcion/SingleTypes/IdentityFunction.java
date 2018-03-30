package main.java.es.ucm.fdi.trabajo.funcion.SingleTypes;

import main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes.Function;
import main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes.SingleFunction;
import main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes.VariablesList;

public class IdentityFunction extends SingleFunction{
	public IdentityFunction(String a, int par) {
		type="Identity";
		this.par = Integer.parseInt(a);
		f=null;
	}
	public String toString() {
		return "x_"+Integer.toString(par);
	}
	public double evaluate(VariablesList list) {
		return list.getVariable(par);
	}
}
