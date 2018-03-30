package main.java.es.ucm.fdi.trabajo.funcion.BinaryTypes;

import main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes.BinaryFunction;
import main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes.VariablesList;

/**
 * @author Javier Navalon
 * @author Inmapg
 */
public class DivideFunction extends BinaryFunction{
	public DivideFunction(String a, String b) {
		super(a, b, "Division");
	}
	
	@Override
	public String toString() {
            StringBuilder ret = new StringBuilder("");
            ret.append("(").append(f1.toString()).append(") / (").append(f2.toString()).append(")");
            return ret.toString();
	}
	
	public double evaluate(VariablesList variables) {
		return (f1.evaluate(variables) / f2.evaluate(variables));
	}
}
