package es.ucm.fdi.trabajo.funcion.unarytypes;

import java.util.regex.Pattern;

import es.ucm.fdi.trabajo.funcion.functiontypes.Function;
import es.ucm.fdi.trabajo.funcion.functiontypes.UnaryFunction;
import es.ucm.fdi.trabajo.funcion.functiontypes.VariablesList;
import es.ucm.fdi.trabajo.funcion.functiontypes.FunctionParser;

/**
 *
 * @author Inmapg
 */
public class SecantFunction extends UnaryFunction{
	
        public SecantFunction(Function f, VariablesList vars) {
		super(f, vars);
	}

	@Override
	public String toString() {
            return "secan(" + argument.toString() + ")";
	}
 
	
	@Override
	protected double evaluateExpr(VariablesList variables) {
	        return 1/Math.cos(argument.evaluate(variables));
	}

	public static class Parser extends UnaryFunction.Parser{
		private static final Pattern REGEX = Pattern.compile("secan\\((.*)\\)");

		@Override
		public SecantFunction parse(String strParam, VariablesList variables, FunctionParser parser) {
			SecantFunction result = null;
			String strArg = UnaryFunction.Parser.parsePattern(strParam, REGEX);
			if(strArg != null) {
				Function arg = parser.parse(strArg, variables);
				if(arg != null) {
					result = new SecantFunction(arg, variables);
				}
			}
			return result;
		}
	}
}
