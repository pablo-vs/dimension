package es.ucm.fdi.trabajo.funcion.BinaryTypes;

import es.ucm.fdi.trabajo.funcion.FunctionTypes.BinaryFunction;
import main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes.VariablesList;

/**
 * 
 * @author Inmapg
 */
public class ModuloFunction extends BinaryFunction{
	public ModuloFunction(String a, String b) {
		super(a, b, "Modulo");
	}
        
        @Override
	public String toString() {
            StringBuilder ret = new StringBuilder("");
            ret.append("(").append(f1.toString()).append(") % (").append(f2.toString()).append(")");
            return ret.toString();
	}
        
        @Override
	public double evaluate(VariablesList variables) {
            return (f1.evaluate(variables) % f2.evaluate(variables));
	}
}
