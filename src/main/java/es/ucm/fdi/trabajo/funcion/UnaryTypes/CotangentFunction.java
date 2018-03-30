package es.ucm.fdi.trabajo.funcion.UnaryTypes;

import es.ucm.fdi.trabajo.funcion.FunctionTypes.UnaryFunction;

/**
 *
 * @author Inmapg
 */
public class CotangentFunction extends UnaryFunction{
        public CotangentFunction(String a, int param) {
            super(a, param, "Cotangent");
	}
        
        @Override
	public String toString() {
            return Integer.toString(param) + "cotan(" + function.toString() + ")";
	}
        
        @Override
        public double evaluate(int x){
            return param*Math.atan(x);
        }
}
