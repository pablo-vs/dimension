package es.ucm.fdi.trabajo.funcion.UnaryTypes;

import es.ucm.fdi.trabajo.funcion.FunctionTypes.UnaryFunction;
import main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes.VariablesList;


/**
 * @author Javier Navalon
 * @author Inmapg
 */
public class ConstantFunction extends UnaryFunction{
	public ConstantFunction(String a, int param) {
		super(a, param, "Constant");
		this.function = null;
	}
        
        @Override
	public String toString() {
		return Integer.toString(param);
	}
        
        @Override
	public double evaluate(VariablesList variables) {
            return param;
	}
}
