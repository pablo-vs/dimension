
package es.ucm.fdi.workspace.function.unarytypes;

import es.ucm.fdi.util.FunctionParserUtils;
import java.util.regex.Pattern;

import es.ucm.fdi.workspace.Function;
import es.ucm.fdi.workspace.function.types.UnaryFunction;
import es.ucm.fdi.workspace.function.types.VariablesList;

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
            return "ln(" + arg.toString() + ")";
	}
 
	
	@Override
	protected double evaluateExpr(VariablesList variables) {
	        return Math.log(arg.evaluate(variables));
	}

	public static class Parser extends UnaryFunction.Parser{
		private static final Pattern REGEX = Pattern.compile("ln\\((.*)\\)");

		@Override
		public LnFunction parse(String strParam, VariablesList variables) {
			LnFunction result = null;
			String strArg = UnaryFunction.Parser.parsePattern(strParam, REGEX);
			if(strArg != null) {
				Function arg = FunctionParserUtils.parse(strArg, variables);
				if(arg != null) {
					result = new LnFunction(arg, variables);
				}
			}
			return result;
		}
	}

}