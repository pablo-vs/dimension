package main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes;

import java.util.ArrayList;

public class VariablesList {
	ArrayList<Variable> variables;
	int dimension;
	public VariablesList(int n) {
		dimension=n;
		variables = new ArrayList<Variable>();
		Variable aux;
		for (int i=0; i<n; i++) {
			aux = new Variable(i, 0);
			variables.add(aux);
		}
	}
	public void setVariable(int var, int val) {
		if (var<dimension) {
			variables.get(var).setVal(val);
		}
	}
	public int getVariable(int var) {
		return variables.get(var).getVal();
	}
}
