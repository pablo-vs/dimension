package es.ucm.fdi.trabajo.funcion.UnaryTypes;

import es.ucm.fdi.trabajo.funcion.FunctionTypes.UnaryFunction;

/**
 *
 * @author Inmapg
 */
public class CosecantFunction extends UnaryFunction{
        public CosecantFunction(String a, int param) {
            super(a, param, "Cosecant");
	}
        
        @Override
	public String toString() {
            return Integer.toString(param) + "cosec(" + function.toString() + ")";
	}
        
        @Override
        public double evaluate(int x){
            return param*Math.asin(x);
        }
}
