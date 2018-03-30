package es.ucm.fdi.trabajo.funcion.UnaryTypes;

import es.ucm.fdi.trabajo.funcion.FunctionTypes.UnaryFunction;

/**
 *
 * @author Inmapg
 */
public class TangentFunction extends UnaryFunction{
        public TangentFunction(String a, int param) {
            super(a, param, "Tangent");
	}
        
        @Override
	public String toString() {
            return Integer.toString(param) + "tan(" + function.toString() + ")";
	}
        
        @Override
        public double evaluate(int x){
            return param*Math.tan(x);
        }
}
