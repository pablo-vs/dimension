package es.ucm.fdi.trabajo.funcion.UnaryTypes;

import es.ucm.fdi.trabajo.funcion.FunctionTypes.UnaryFunction;

/**
 *
 * @author Inmapg
 */
public class SquareRootFunction extends UnaryFunction{
        public SquareRootFunction(String a, int param) {
            super(a, param, "Square root");
	}
        
        @Override
	public String toString() { // Check it
            return Integer.toString(param) + "(" + function.toString() + ")^1/2";
	}
        
        @Override
        public double evaluate(int x){
            return param*Math.sqrt(x);
        }
}
