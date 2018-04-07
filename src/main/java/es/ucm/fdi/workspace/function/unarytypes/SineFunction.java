package es.ucm.fdi.workspace.function.unarytypes;

import es.ucm.fdi.util.FunctionParserUtils;
import java.util.regex.Pattern;

import es.ucm.fdi.workspace.FunctionBO;
import es.ucm.fdi.workspace.function.types.UnaryFunction;
import es.ucm.fdi.workspace.function.types.VariablesList;

/**
 * Representa la función seno.
 *
 * @author Inmapg
 * @version 02.04.2018
 */
public class SineFunction extends UnaryFunction{
                        
	public SineFunction(FunctionBO f, VariablesList vars) {
		super(f, vars);
	}

	@Override
	public String toString() {
            return "sin(" + arg.toString() + ")";
	}
 
	
	@Override
	protected double evaluateExpr(VariablesList variables) {
	        return Math.sin(arg.evaluate(variables));
	}

	public static class Parser extends UnaryFunction.Parser{
		private static final Pattern REGEX = Pattern.compile("sin\\((.*)\\)");

		@Override
		public SineFunction parse(String strParam, VariablesList variables) {
			SineFunction result = null;
			String strArg = UnaryFunction.Parser.parsePattern(strParam, REGEX);
			if(strArg != null) {
				FunctionBO arg = FunctionParserUtils.parse(strArg, variables);
				if(arg != null) {
					result = new SineFunction(arg, variables);
				}
			}
			return result;
		}
	}
	
}
