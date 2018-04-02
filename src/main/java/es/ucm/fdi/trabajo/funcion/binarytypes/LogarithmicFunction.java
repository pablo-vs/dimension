package es.ucm.fdi.trabajo.funcion.binarytypes;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import es.ucm.fdi.trabajo.funcion.functiontypes.Function;
import es.ucm.fdi.trabajo.funcion.functiontypes.BinaryFunction;
import es.ucm.fdi.trabajo.funcion.functiontypes.VariablesList;
import es.ucm.fdi.trabajo.funcion.functiontypes.FunctionParser;
import es.ucm.fdi.util.ParserUtils;

/**
 * Representa la funcion logarítmica.
 *
 * @author Brian Leiva
 * @version 01.04.2018
 */
public class LogarithmicFunction extends BinaryFunction{
	
	public LogarithmicFunction() {
		super();
	}
	
	public LogarithmicFunction(Function f1, Function f2, VariablesList vars) {
		super(f1, f2, vars);
	}
	
	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder("");
		ret.append("log_(").append(f1.toString()).append(")(").append(f2.toString()).append(")");
		return ret.toString();
	}

	@Override
	protected double evaluateExpr(VariablesList variables) {
		return Math.log(f2.evaluate(variables)) / Math.log(f1.evaluate(variables));
	}


	public static class Parser extends BinaryFunction.Parser {
		
		private static final Pattern REGEX = Pattern.compile(" *log_(\\(.*\\))(\\(.*\\))");

		@Override
		public LogarithmicFunction parse(String strParam, VariablesList variables, FunctionParser parser) {
			String str = ParserUtils.stripExtraParenthesis(strParam);
			LogarithmicFunction result = null;
			Matcher m = REGEX.matcher(str);
			if(m.matches()) {
				Function f1 = parser.parse(m.group(1), variables),
					f2 = parser.parse(m.group(2), variables);
				if(f1 != null && f2 != null) {
					result = new LogarithmicFunction(f1, f2, variables);
				}
			}
			return result;
		}
	}
}
