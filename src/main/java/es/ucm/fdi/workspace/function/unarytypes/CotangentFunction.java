package es.ucm.fdi.workspace.function.unarytypes;

import es.ucm.fdi.util.FunctionParserUtils;
import java.util.regex.Pattern;

import es.ucm.fdi.workspace.Function;
import es.ucm.fdi.workspace.function.types.UnaryFunction;
import es.ucm.fdi.workspace.function.types.VariablesList;

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
            return "cotan(" + arg.toString() + ")";
	}
 
	
	@Override
	protected double evaluateExpr(VariablesList variables) {
	        return 1/Math.tan(arg.evaluate(variables));
	}

	public static class Parser extends UnaryFunction.Parser{
		private static final Pattern REGEX = Pattern.compile("cotan\\((.*)\\)");

		@Override
		public CotangentFunction parse(String strParam, VariablesList variables) {
			CotangentFunction result = null;
			String strArg = UnaryFunction.Parser.parsePattern(strParam, REGEX);
			if(strArg != null) {
				Function arg = FunctionParserUtils.parse(strArg, variables);
				if(arg != null) {
					result = new CotangentFunction(arg, variables);
				}
			}
			return result;
		}
	}
}
