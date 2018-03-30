
package es.ucm.fdi.trabajo.funcion.UnaryTypes;

import es.ucm.fdi.trabajo.funcion.FunctionTypes.UnaryFunction;

/**
 *
 * @author Inmapg
 */
public class LnFunction extends UnaryFunction{
        public LnFunction(String a, int param) {
            super(a, param, "Napierian logarithm");
	}
        
        @Override
	public String toString() {
            return Integer.toString(param) + "ln(" + function.toString() + ")";
	}
        
        @Override
        public double evaluate(int x){
            return param*Math.log(x);
        }

}
