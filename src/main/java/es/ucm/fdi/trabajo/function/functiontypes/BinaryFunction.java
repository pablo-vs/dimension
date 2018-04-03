package es.ucm.fdi.trabajo.function.functiontypes;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import es.ucm.fdi.util.ParserUtils;
import es.ucm.fdi.trabajo.function.Function;
import es.ucm.fdi.trabajo.function.FunctionParser;
import es.ucm.fdi.trabajo.function.functiontypes.VariablesList;

/**
 * Represents a binary function.
 *
 * @author Javier Navalón
 * @version 01.04.2018
 */
public abstract class BinaryFunction extends Function {
	protected Function f1;
	protected Function f2;

	public BinaryFunction() {
		super();
	}
	
	public BinaryFunction(Function fun1, Function fun2, VariablesList vars) {
		super(vars);
		f1 = fun1;
		f2 = fun2;
	}

	public static abstract class Parser extends Function.Parser {
		
		@Override
		public abstract BinaryFunction parse(String strParam, VariablesList variables, FunctionParser parser);

		/**
		 * Useful method for parsing binary functions with infix operators.
		 * Given a regex specifying the operator, returns the functions to the left and
		 * right, which will be null if the parsing cannot be done.
		 *
	         * @param strParam The string to parse.
		 * @param variables The variable list.
		 * @param operator The infix operator.
		 * @param parser FunctionParser, needed to parse subfunctions.
		 */
		public static Function[] parseFunctions(String strParam, VariablesList variables, Pattern operator, FunctionParser parser) {
			boolean success;
			Function[] funcs = new Function[2];
			String str = ParserUtils.stripExtraParenthesis(strParam);
			int endFirst = 0, startSecond;
			if(str.charAt(0) == '(') {
				endFirst = ParserUtils.getEndOfParenthesis(str, 0);
			}
			Matcher m = operator.matcher(str);
			if(m.find(endFirst)) {
				if(endFirst == 0) {
					endFirst = m.start();
				}
				startSecond = m.end();
				try {
					funcs[0] = parser.parse(str.substring(0, endFirst), variables);
					funcs[1] = parser.parse(str.substring(startSecond), variables);
				} catch (IllegalArgumentException e) {
					funcs = null;
				}
			} else {
				funcs = null;
			}
			return funcs;
		}
	}
}
