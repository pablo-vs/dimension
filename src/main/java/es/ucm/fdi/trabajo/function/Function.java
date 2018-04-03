package es.ucm.fdi.trabajo.function;

import es.ucm.fdi.trabajo.function.functiontypes.VariablesList;

/**
 * Defines the Function interface.
 *
 * @author Javier Navalón
 * @version 02.04.2018
 */
public abstract class Function {
	private VariablesList variables;
	
	public Function() {}

	/**
	 * Constructor specifying the list of variables of the function.
	 *
	 * @param vars The variable list.
	 */
	public Function(VariablesList vars) {
		variables = vars;
	}
	
	/**
	 * Returns the variables of the function.
	 * 
       	 * @return The variable list.
	 */
	public VariablesList getVars() {
		return variables;
	}

	/**
	 * Evaluates the function at the point given by the variable list.
	 * Note: the given variable names must be equal to those of the function.
	 *
	 * @param vars The variable list.
	 * @return The result of applying the function to the values.
	 */
	public double evaluate(VariablesList vars) {
		if(variables.equals(vars)) {
			return evaluateExpr(vars);
		} else {
			throw new IllegalArgumentException("Incorrect variables");
		}
	}

	protected abstract double evaluateExpr(VariablesList vars);
	
       	public abstract String toString();

	/**
	 * Contains the parser specific for each function.
	 *
	 * @author Pablo Villalobos
	 */
	public static abstract class Parser {
		
		/**
		 * Parses a string with the given list of variables and returns the corresponding
		 * function. It is not necessary for all the variables in the list to appear
		 * in the expression.
		 * 
		 * @param str The string to parse.
		 * @param variables The variable list.
		 * @param parser FunctionParser, needed to parse subfunctions.
		 */
		public abstract Function parse(String str, VariablesList variables, FunctionParser parser);
	}
}
