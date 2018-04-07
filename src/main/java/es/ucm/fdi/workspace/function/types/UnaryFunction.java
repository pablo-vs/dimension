package es.ucm.fdi.workspace.function.types;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import es.ucm.fdi.workspace.FunctionBO;
import es.ucm.fdi.util.FunctionParserUtils;


/**
 * @author Javier Navalon
 * @author Inmapg
 * @version 01.04.2018
 */
public abstract class UnaryFunction extends FunctionBO {

	protected FunctionBO arg;

	public UnaryFunction(FunctionBO f, VariablesList vars) {
		super(vars);
		arg = f;
	}
	
	public UnaryFunction(VariablesList vars) {
		super(vars);
	}
	
	public static abstract class Parser extends FunctionBO.Parser {

		@Override
		public abstract UnaryFunction parse(String str, VariablesList variables);
		
		public static String parsePattern(String strParam, Pattern patron) {
			String result = null;
			String str = FunctionParserUtils.stripExtraParenthesis(strParam);
			Matcher m = patron.matcher(str);
			if(m.matches()) {
				result = m.group(1);
			}
			return result;
		}
	}
}
