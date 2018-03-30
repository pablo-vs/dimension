package es.ucm.fdi.trabajo.funcion.UnaryTypes;

import es.ucm.fdi.trabajo.funcion.FunctionTypes.UnaryFunction;

/**
 *
 * @author Inmapg
 */
public class SineFunction extends UnaryFunction{
        public SineFunction(String a, int param) {
            super(a, param, "Sine");
	}
        
        @Override
	public String toString() {
            return Integer.toString(param) + "sin(" + function.toString() + ")";
	}
        
        @Override
        public double evaluate(int x){
            return param*Math.sin(x);
        }
}
