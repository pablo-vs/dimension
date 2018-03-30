package es.ucm.fdi.trabajo.funcion.BinaryTypes;

import es.ucm.fdi.trabajo.funcion.FunctionTypes.BinaryFunction;
import es.ucm.fdi.trabajo.funcion.FunctionTypes.VariablesList;

/**
 * @author Brian Leiva
 */
public class LogarithmicFunction extends BinaryFunction{
	public LogarithmicFunction(String a, String b) {
		super(a, b, "Logarithmic");
	}
	public String toString() {
		StringBuilder ret = new StringBuilder("");
		ret.append("(log_").append(f1.toString()).append("(").append(f2.toString()).append("))");
		return ret.toString();
	}
	public double evaluate(VariablesList variables) {
		return Math.log(f2.evaluate(variables) / Math.log(f1.evaluate(variables)));
	}
}
