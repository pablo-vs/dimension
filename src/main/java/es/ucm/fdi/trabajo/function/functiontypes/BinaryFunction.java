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
			boolean succ=true;
			String str = ParserUtils.stripExtraParenthesis(strParam);
			int endFirst = 0, startSecond;
			if(!str.equals("")) {
				if(str.charAt(0) == '(') {
					try {
						endFirst = ParserUtils.getEndOfParenthesis(str, 0);
					} catch(IllegalArgumentException e) {
						succ=false;
					}
				}
				if (succ) {
					Matcher m = operator.matcher(str);
					boolean done = false;
					if(m.find(endFirst)) {
						do {
							if(endFirst == 0) {
								endFirst = m.start();
							}
							startSecond = m.end();
							if(str.substring(0, endFirst).equals("PI") && str.substring(startSecond).equals("(tan(x+y+z*y-x*z+y)+y^(1))")) {
								System.out.println("Bingo");
							}
							if(str.substring(0, endFirst).equals("(tan(x+y+z*y")) System.out.println("N2");
							funcs[0] = parser.parse(str.substring(0, endFirst), variables);	
							if(funcs[0] != null) {
								funcs[1] = parser.parse(str.substring(startSecond), variables);
								done = funcs[1] != null;
							} else funcs[1] = null;
						} while(!done && !m.hitEnd() && m.find());
					}
				}
			} else {
				funcs[0]=null; funcs[1]=null;
			}
			
			return funcs;
		}
	}
}
