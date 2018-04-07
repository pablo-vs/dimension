package es.ucm.fdi.workspace.function.unarytypes;

import es.ucm.fdi.util.FunctionParserUtils;
import java.util.regex.Pattern;

import es.ucm.fdi.workspace.FunctionBO;
import es.ucm.fdi.workspace.function.types.UnaryFunction;
import es.ucm.fdi.workspace.function.types.VariablesList;

/**
 *
 * @author Inmapg
 */
public class SecantFunction extends UnaryFunction{
	
        public SecantFunction(FunctionBO f, VariablesList vars) {
		super(f, vars);
	}

	@Override
	public String toString() {
            return "secan(" + arg.toString() + ")";
	}
 
	
	@Override
	protected double evaluateExpr(VariablesList variables) {
	        return 1/Math.cos(arg.evaluate(variables));
	}

	public static class Parser extends UnaryFunction.Parser{
		private static final Pattern REGEX = Pattern.compile("secan\\((.*)\\)");

		@Override
		public SecantFunction parse(String strParam, VariablesList variables) {
			SecantFunction result = null;
			String strArg = UnaryFunction.Parser.parsePattern(strParam, REGEX);
			if(strArg != null) {
				FunctionBO arg = FunctionParserUtils.parse(strArg, variables);
				if(arg != null) {
					result = new SecantFunction(arg, variables);
				}
			}
			return result;
		}
	}
}
