package es.ucm.fdi.trabajo.funcion.unarytypes;

import java.util.regex.Pattern;

import es.ucm.fdi.trabajo.funcion.functiontypes.Function;
import es.ucm.fdi.trabajo.funcion.functiontypes.UnaryFunction;
import es.ucm.fdi.trabajo.funcion.functiontypes.VariablesList;
import es.ucm.fdi.trabajo.funcion.functiontypes.FunctionParser;

/**
 * Representa la función seno.
 *
 * @author Inmapg
 * @version 02.04.2018
 */
public class SineFunction extends UnaryFunction{
                        
	public SineFunction(Function f, VariablesList vars) {
		super(f, vars);
	}

	@Override
	public String toString() {
            return "sin(" + argument.toString() + ")";
	}
 
	
	@Override
	protected double evaluateExpr(VariablesList variables) {
	        return Math.sin(argument.evaluate(variables));
	}

	public static class Parser extends UnaryFunction.Parser{
		private static final Pattern REGEX = Pattern.compile("sin\\((.*)\\)");

		@Override
		public SineFunction parse(String strParam, VariablesList variables, FunctionParser parser) {
			SineFunction result = null;
			String strArg = UnaryFunction.Parser.parsePattern(strParam, REGEX);
			if(strArg != null) {
				Function arg = parser.parse(strArg, variables);
				if(arg != null) {
					result = new SineFunction(arg, variables);
				}
			}
			return result;
		}
	}
	
}
