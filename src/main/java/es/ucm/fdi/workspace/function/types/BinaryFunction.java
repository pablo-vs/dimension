package es.ucm.fdi.workspace.function.types;

import es.ucm.fdi.util.FunctionParserUtils;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


import es.ucm.fdi.workspace.FunctionBO;

/**
 * Represents a binary function.
 *
 * @author Javier Navalón
 * @version 01.04.2018
 */
public abstract class BinaryFunction extends FunctionBO {
	protected FunctionBO f1;
	protected FunctionBO f2;

	public BinaryFunction() {
		super();
	}
	
	public BinaryFunction(FunctionBO fun1, FunctionBO fun2, VariablesList vars) {
		super(vars);
		f1 = fun1;
		f2 = fun2;
	}

	public static abstract class Parser extends FunctionBO.Parser {
		
		@Override
		public abstract BinaryFunction parse(String strParam, VariablesList variables);

		/**
		 * Useful method for parsing binary functions with infix operators.
		 * Given a regex specifying the operator, returns the functions to the left and
		 * right, which will be null if the parsing cannot be done.
		 *
	         * @param strParam The string to parse.
		 * @param variables The variable list.
		 * @param operator The infix operator.
		 */
		public static FunctionBO[] parseFunctions(String strParam, VariablesList variables, Pattern operator) {
		        boolean success;
			FunctionBO[] funcs = {null, null};
			boolean succ=true;
			String str =FunctionParserUtils.stripExtraParenthesis(strParam);
			int endFirst = 0, startSecond;
			if(!str.equals("")) {
				if(str.charAt(0) == '(') {
					try {
						endFirst =FunctionParserUtils.getEndOfParenthesis(str, 0);
					} catch(IllegalArgumentException e) {
						succ=false;
					}
				}
				if (succ) {
					Matcher m = operator.matcher(str);
					boolean done = false;
					int i = 0;
					if(m.find(endFirst)) {
						do {
							endFirst = m.start();
							startSecond = m.end();
							funcs[0] = FunctionParserUtils.parse(str.substring(0, endFirst), variables);	
							if(funcs[0] != null) {
								funcs[1] = FunctionParserUtils.parse(str.substring(startSecond), variables);
								done = funcs[1] != null;
							}
						} while(!done && !m.hitEnd() && m.find());
					}
				}
			}
			return funcs;

		}
	}
}
