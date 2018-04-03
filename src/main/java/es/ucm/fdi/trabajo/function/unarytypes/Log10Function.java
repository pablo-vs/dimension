package es.ucm.fdi.trabajo.function.unarytypes;

import java.util.regex.Pattern;

import es.ucm.fdi.trabajo.function.Function;
import es.ucm.fdi.trabajo.function.functiontypes.UnaryFunction;
import es.ucm.fdi.trabajo.function.functiontypes.VariablesList;
import es.ucm.fdi.trabajo.function.FunctionParser;

/**
 * Represents the decimal logarithm.
 *
 * @author Inmapg
 * @version 02.04.2018
 */
public class Log10Function extends UnaryFunction{

	public Log10Function(Function f, VariablesList vars) {
		super(f, vars);
	}
        
        @Override
	public String toString() {
            return "log(" + argument.toString() + ")";
	}
       	
	@Override
	protected double evaluateExpr(VariablesList variables) {
	        return Math.log10(argument.evaluate(variables));
	}

	public static class Parser extends UnaryFunction.Parser{
		private static final Pattern REGEX = Pattern.compile("log\\((.*)\\)");

		@Override
		public Log10Function parse(String strParam, VariablesList variables, FunctionParser parser) {
			Log10Function result = null;
			String strArg = UnaryFunction.Parser.parsePattern(strParam, REGEX);
			if(strArg != null) {
				Function arg = parser.parse(strArg, variables);
				if(arg != null) {
					result = new Log10Function(arg, variables);
				}
			}
			return result;
		}
	}
}
