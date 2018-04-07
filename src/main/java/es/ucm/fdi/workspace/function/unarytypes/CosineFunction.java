package es.ucm.fdi.workspace.function.unarytypes;

import es.ucm.fdi.util.FunctionParserUtils;
import java.util.regex.Pattern;

import es.ucm.fdi.workspace.FunctionBO;
import es.ucm.fdi.workspace.function.types.UnaryFunction;
import es.ucm.fdi.workspace.function.types.VariablesList;

/**
 * Represents a cosine function
 *
 * @author Inmapg
 * @version 02.04.2018
 */
public class CosineFunction extends UnaryFunction{
	
        public CosineFunction(FunctionBO f, VariablesList vars) {
		super(f, vars);
	}

	@Override
	public String toString() {
            return "cos(" + arg.toString() + ")";
	}
 
	
	@Override
	protected double evaluateExpr(VariablesList variables) {
	        return Math.cos(arg.evaluate(variables));
	}

	public static class Parser extends UnaryFunction.Parser{
		private static final Pattern REGEX = Pattern.compile("cos\\((.*)\\)");

		@Override
		public CosineFunction parse(String strParam, VariablesList variables) {
			CosineFunction result = null;
			String strArg = UnaryFunction.Parser.parsePattern(strParam, REGEX);
			if(strArg != null) {
				FunctionBO arg = FunctionParserUtils.parse(strArg, variables);
				if(arg != null) {
					result = new CosineFunction(arg, variables);
				}
			}
			return result;
		}
	}
}
