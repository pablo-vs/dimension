package es.ucm.fdi.trabajo.funcion.UnaryTypes;

import es.ucm.fdi.trabajo.funcion.FunctionTypes.UnaryFunction;

/**
 *
 * @author Inmapg
 */
public class Log10Function extends UnaryFunction{
        public Log10Function(String a, int param) {
            super(a, param, "Logarithm (base 10)");
	}
        
        @Override
	public String toString() {
            return Integer.toString(param)+ "log(" + function.toString() + ")";
	}
        
        @Override
        public double evaluate(int x){
            return param*Math.log10(x);
        }
}
