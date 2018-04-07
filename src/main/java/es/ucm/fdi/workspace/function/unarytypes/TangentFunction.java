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
public class TangentFunction extends UnaryFunction{
	
	public TangentFunction(FunctionBO f, VariablesList vars) {
		super(f, vars);
	}

	@Override
	public String toString() {
            return "tan(" + arg.toString() + ")";
	}
 
	
	@Override
	protected double evaluateExpr(VariablesList variables) {
	        return Math.tan(arg.evaluate(variables));
	}

	public static class Parser extends UnaryFunction.Parser{
		private static final Pattern REGEX = Pattern.compile("tan\\((.*)\\)");

		@Override
		public TangentFunction parse(String strParam, VariablesList variables) {
			TangentFunction result = null;
			String strArg = UnaryFunction.Parser.parsePattern(strParam, REGEX);
			if(strArg != null) {
				FunctionBO arg = FunctionParserUtils.parse(strArg, variables);
				if(arg != null) {
					result = new TangentFunction(arg, variables);
				}
			}
			return result;
		}
	}
}
