package es.ucm.fdi.trabajo.function.functiontypes;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import es.ucm.fdi.trabajo.function.Function;
import es.ucm.fdi.trabajo.function.FunctionParser;
import es.ucm.fdi.util.ParserUtils;

/**
 * @author Javier Navalon
 * @author Inmapg
 * @version 01.04.2018
 */
public abstract class UnaryFunction extends Function {

	protected Function argument;

	public UnaryFunction(Function f, VariablesList vars) {
		super(vars);
		argument = f;
	}
	
	public UnaryFunction(VariablesList vars) {
		super(vars);
	}
	
	public static abstract class Parser extends Function.Parser {

		@Override
		public abstract UnaryFunction parse(String str, VariablesList variables, FunctionParser parser);
		
		public static String parsePattern(String strParam, Pattern patron) {
			String result = null;
			String str = ParserUtils.stripExtraParenthesis(strParam);
			Matcher m = patron.matcher(str);
			if(m.matches()) {
				result = m.group(1);
			}
			return result;
		}
	}
}
