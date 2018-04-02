package es.ucm.fdi.trabajo.funcion.functiontypes;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import es.ucm.fdi.util.ParserUtils;

/**
 * @author Javier Navalon
 * @author Inmapg
 * @version 01.04.2018
 */
public abstract class UnaryFunction extends Function {

	public UnaryFunction(VariablesList vars) {
		super(vars);
	}
	
	public static abstract class Parser extends Function.Parser {

		@Override
		public abstract UnaryFunction parse(String str, VariablesList variables, FunctionParser parser);
		
		public boolean parsePattern(String str, Pattern patron) {
			Matcher m = patron.matcher(str);
			return m.matches();
		}
	}
}
