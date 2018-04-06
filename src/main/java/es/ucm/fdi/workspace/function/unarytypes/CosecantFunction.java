package es.ucm.fdi.workspace.function.unarytypes;

import java.util.regex.Pattern;

import es.ucm.fdi.workspace.Function;
import es.ucm.fdi.workspace.function.types.UnaryFunction;
import es.ucm.fdi.workspace.function.types.VariablesList;
import es.ucm.fdi.util.FunctionParserUtils;

/**
 * Represents a cosecant function
 *
 * @author Inmapg
 */
public class CosecantFunction extends UnaryFunction{
	
	public CosecantFunction(Function f, VariablesList vars) {
		super(f, vars);
	}

	@Override
	public String toString() {
            return "cosec(" + arg.toString() + ")";
	}
 
	
	@Override
	protected double evaluateExpr(VariablesList variables) {
	        return 1/Math.sin(arg.evaluate(variables));
	}

	public static class Parser extends UnaryFunction.Parser{
		private static final Pattern REGEX = Pattern.compile("cosec\\((.*)\\)");

		@Override
		public CosecantFunction parse(String strParam, VariablesList variables) {
			CosecantFunction result = null;
			String strArg = UnaryFunction.Parser.parsePattern(strParam, REGEX);
			if(strArg != null) {
				Function arg = FunctionParserUtils.parse(strArg, variables);
				if(arg != null) {
					result = new CosecantFunction(arg, variables);
				}
			}
			return result;
		}
	}
}