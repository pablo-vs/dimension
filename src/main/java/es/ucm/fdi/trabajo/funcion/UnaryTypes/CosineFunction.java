package es.ucm.fdi.trabajo.funcion.UnaryTypes;

import es.ucm.fdi.trabajo.funcion.FunctionTypes.UnaryFunction;

/**
 *
 * @author Inmapg
 */
public class CosineFunction extends UnaryFunction{
        public CosineFunction(String a, int param) {
            super(a, param, "Cosine");
	}
        
        @Override
	public String toString() {
            return Integer.toString(param) + "cos(" + function.toString() + ")";
	}
        
        @Override
        public double evaluate(int x){
            return param*Math.cos(x);
        }
}
