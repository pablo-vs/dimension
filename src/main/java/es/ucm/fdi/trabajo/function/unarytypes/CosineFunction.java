package es.ucm.fdi.trabajo.function.unarytypes;

import java.util.regex.Pattern;

import es.ucm.fdi.trabajo.function.Function;
import es.ucm.fdi.trabajo.function.functiontypes.UnaryFunction;
import es.ucm.fdi.trabajo.function.functiontypes.VariablesList;
import es.ucm.fdi.trabajo.function.FunctionParser;

/**
 * Represents a cosine function
 *
 * @author Inmapg
 * @version 02.04.2018
 */
public class CosineFunction extends UnaryFunction{
	
        public CosineFunction(Function f, VariablesList vars) {
		super(f, vars);
	}

	@Override
	public String toString() {
            return "cos(" + argument.toString() + ")";
	}
 
	
	@Override
	protected double evaluateExpr(VariablesList variables) {
	        return Math.cos(argument.evaluate(variables));
	}

	public static class Parser extends UnaryFunction.Parser{
		private static final Pattern REGEX = Pattern.compile("cos\\((.*)\\)");

		@Override
		public CosineFunction parse(String strParam, VariablesList variables, FunctionParser parser) {
			CosineFunction result = null;
			String strArg = UnaryFunction.Parser.parsePattern(strParam, REGEX);
			if(strArg != null) {
				Function arg = parser.parse(strArg, variables);
				if(arg != null) {
					result = new CosineFunction(arg, variables);
				}
			}
			return result;
		}
	}
}
