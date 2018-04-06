package es.ucm.fdi.workspace.function.unarytypes;

import es.ucm.fdi.util.FunctionParserUtils;
import java.util.regex.Pattern;

import es.ucm.fdi.workspace.Function;
import es.ucm.fdi.workspace.function.types.UnaryFunction;
import es.ucm.fdi.workspace.function.types.VariablesList;

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
            return "log(" + arg.toString() + ")";
	}
       	
	@Override
	protected double evaluateExpr(VariablesList variables) {
	        return Math.log10(arg.evaluate(variables));
	}

	public static class Parser extends UnaryFunction.Parser{
		private static final Pattern REGEX = Pattern.compile("log\\((.*)\\)");

		@Override
		public Log10Function parse(String strParam, VariablesList variables) {
			Log10Function result = null;
			String strArg = UnaryFunction.Parser.parsePattern(strParam, REGEX);
			if(strArg != null) {
				Function arg = FunctionParserUtils.parse(strArg, variables);
				if(arg != null) {
					result = new Log10Function(arg, variables);
				}
			}
			return result;
		}
	}
}
