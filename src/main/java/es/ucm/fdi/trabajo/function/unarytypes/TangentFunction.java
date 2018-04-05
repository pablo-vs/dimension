package es.ucm.fdi.trabajo.function.unarytypes;

import java.util.regex.Pattern;

import es.ucm.fdi.trabajo.function.Function;
import es.ucm.fdi.trabajo.function.functiontypes.UnaryFunction;
import es.ucm.fdi.trabajo.function.functiontypes.VariablesList;
import es.ucm.fdi.trabajo.function.FunctionParser;

/**
 *
 * @author Inmapg
 */
public class TangentFunction extends UnaryFunction{
	
	public TangentFunction(Function f, VariablesList vars) {
		super(f, vars);
	}

	@Override
	public String toString() {
            return "tan(" + argument.toString() + ")";
	}
 
	
	@Override
	protected double evaluateExpr(VariablesList variables) {
	        return Math.tan(argument.evaluate(variables));
	}

	public static class Parser extends UnaryFunction.Parser{
		private static final Pattern REGEX = Pattern.compile("tan\\((.*)\\)");

		@Override
		public TangentFunction parse(String strParam, VariablesList variables, FunctionParser parser) {
			TangentFunction result = null;
			String strArg = UnaryFunction.Parser.parsePattern(strParam, REGEX);
			//System.out.println("Intento tangente " + strArg);
			if(strArg != null) {
				System.out.println("tangente " + strArg);
				Function arg = parser.parse(strArg, variables);
				if(arg != null) {
					result = new TangentFunction(arg, variables);
				}
			}
			return result;
		}
	}
}
