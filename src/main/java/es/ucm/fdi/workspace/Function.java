package es.ucm.fdi.workspace;

import es.ucm.fdi.workspace.function.types.VariablesList;

/**
 * Defines an abstract class function. A function contains a 
 * {@link es.ucm.fdi.workspace.function.types.VariablesList VariablesList} variables
 * to define the number and name of the parameters. It also provides an accessor method
 * {@link #getVars getVars} which return the {@link es.ucm.fdi.workspace.function.types.VariablesList VariablesList}.
 * The classes inherited from this one are supposed to define a method 
 * {@link #evaluateExpr(es.ucm.fdi.workspace.function.types.VariablesList) evaluateExpr}
 * which calculates the value of the function given a {@link es.ucm.fdi.workspace.function.types.VariablesList VariablesList}.
 * 
 * @author Javier Navalón
 * @version 02.04.2018
 */
public abstract class Function {
    
	private VariablesList variables;
        
        /**
	 * Prevent from no-parameters declaration of new function variables for containers.
	 */
	public Function() {}

	/**
	 * Constructor specifying the list of variables of the function.
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
	 * <b>Note:</b> the given variable names must be equal to those of the function.
	 *
	 * @param vars The variable list.
	 * @return The result of applying the function to the values.
	 */
	public double evaluate(VariablesList vars) {
		if(variables.equals(vars)) {
			return evaluateExpr(vars);
		} else {
			throw new IllegalArgumentException("Mismatching when evaluating variables!");
		}
	}

	protected abstract double evaluateExpr(VariablesList vars);
	
       	public abstract String toString();

	/**
	 * Contains the parser specific for each function.
	 * @author Pablo Villalobos
	 */
	public static abstract class Parser {
		
		/**
		 * Parses a string with the given list of variables and returns the corresponding
		 * function. Not all the variables have to be in the list to appear
		 * in the expression.
		 * 
		 * @param str The String to parse.
		 * @param variables The list of variables.
                 * @return the function which matches the type or null
		 */
		public abstract Function parse(String str, VariablesList variables);
	}
}
