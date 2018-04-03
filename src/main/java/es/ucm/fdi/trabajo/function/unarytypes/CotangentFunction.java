package es.ucm.fdi.trabajo.function.unarytypes;

import java.util.regex.Pattern;

import es.ucm.fdi.trabajo.function.Function;
import es.ucm.fdi.trabajo.function.functiontypes.UnaryFunction;
import es.ucm.fdi.trabajo.function.functiontypes.VariablesList;
import es.ucm.fdi.trabajo.function.FunctionParser;

/**
 * Represents a contangent function
 *
 * @author Inmapg
 */
public class CotangentFunction extends UnaryFunction{
	
        public CotangentFunction(Function f, VariablesList vars) {
		super(f, vars);
	}

	@Override
	public String toString() {
            return "cotan(" + argument.toString() + ")";
	}
 
	
	@Override
	protected double evaluateExpr(VariablesList variables) {
	        return 1/Math.tan(argument.evaluate(variables));
	}

	public static class Parser extends UnaryFunction.Parser{
		private static final Pattern REGEX = Pattern.compile("cotan\\((.*)\\)");

		@Override
		public CotangentFunction parse(String strParam, VariablesList variables, FunctionParser parser) {
			CotangentFunction result = null;
			String strArg = UnaryFunction.Parser.parsePattern(strParam, REGEX);
			if(strArg != null) {
				Function arg = parser.parse(strArg, variables);
				if(arg != null) {
					result = new CotangentFunction(arg, variables);
				}
			}
			return result;
		}
	}
}
