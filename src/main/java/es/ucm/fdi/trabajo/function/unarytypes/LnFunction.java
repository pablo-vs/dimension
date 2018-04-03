
package es.ucm.fdi.trabajo.function.unarytypes;

import java.util.regex.Pattern;

import es.ucm.fdi.trabajo.function.Function;
import es.ucm.fdi.trabajo.function.functiontypes.UnaryFunction;
import es.ucm.fdi.trabajo.function.functiontypes.VariablesList;
import es.ucm.fdi.trabajo.function.FunctionParser;

/**
 * Representa el logaritmo neperiano.
 *
 * @author Inmapg
 * @version 02.04.2018
 */
public class LnFunction extends UnaryFunction{
        
    	public LnFunction(Function f, VariablesList vars) {
		super(f, vars);
	}

	@Override
	public String toString() {
            return "ln(" + argument.toString() + ")";
	}
 
	
	@Override
	protected double evaluateExpr(VariablesList variables) {
	        return Math.log(argument.evaluate(variables));
	}

	public static class Parser extends UnaryFunction.Parser{
		private static final Pattern REGEX = Pattern.compile("ln\\((.*)\\)");

		@Override
		public LnFunction parse(String strParam, VariablesList variables, FunctionParser parser) {
			LnFunction result = null;
			String strArg = UnaryFunction.Parser.parsePattern(strParam, REGEX);
			if(strArg != null) {
				Function arg = parser.parse(strArg, variables);
				if(arg != null) {
					result = new LnFunction(arg, variables);
				}
			}
			return result;
		}
	}

}
