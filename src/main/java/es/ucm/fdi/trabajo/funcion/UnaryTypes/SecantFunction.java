package es.ucm.fdi.trabajo.funcion.UnaryTypes;

import es.ucm.fdi.trabajo.funcion.FunctionTypes.UnaryFunction;

/**
 *
 * @author Inmapg
 */
public class SecantFunction extends UnaryFunction{
        public SecantFunction(String a, int param) {
            super(a, param, "Secant");
	}
        
        @Override
	public String toString() {
            return Integer.toString(param) + "secan(" + function.toString() + ")";
	}
        
        @Override
        public double evaluate(int x){
            return param*Math.acos(x);
        }
}
