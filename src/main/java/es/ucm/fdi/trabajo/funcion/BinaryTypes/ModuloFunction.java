package es.ucm.fdi.trabajo.funcion.BinaryTypes;

import es.ucm.fdi.trabajo.funcion.FunctionTypes.BinaryFunction;

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
	public double evaluate(int x) {
            return (f1.evaluate(x) % f2.evaluate(x));
	}
}
