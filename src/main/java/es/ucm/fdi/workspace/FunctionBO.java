/*
  This file is part of Dimension.
  Dimension is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  Dimension is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  You should have received a copy of the GNU General Public License
  along with Dimension.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.workspace;

import es.ucm.fdi.workspace.function.types.VariablesList;

/**
 * Defines an abstract class function. A function contains a
 * {@link es.ucm.fdi.workspace.function.types.VariablesList VariablesList}
 * variables to define the number and name of the parameters. It also provides
 * an accessor method {@link #getVars getVars} which return the
 * {@link es.ucm.fdi.workspace.function.types.VariablesList VariablesList}. The
 * classes inherited from this one are supposed to define a method
 * {@link #evaluateExpr(es.ucm.fdi.workspace.function.types.VariablesList) evaluateExpr}
 * which calculates the value of the function given a
 * {@link es.ucm.fdi.workspace.function.types.VariablesList VariablesList}.
 *
 * @author Javier Navalón
 */
public abstract class FunctionBO {

    /**
     * List of variables available
     */
    private VariablesList variables;

    /**
     * Prevents from no-parameters declaration of new function variables for
     * containers.
     */
    public FunctionBO() {
    }

    /**
     * Constructor specifying the list of variables of the function.
     *
     * @param vars The variable list.
     */
    public FunctionBO(VariablesList vars) {
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
     * <b>Note:</b> the given variable names must be equal to those of the
     * function.
     *
     * @param vars The variable list.
     * @return The result of applying the function to the values.
     */
    public double evaluate(VariablesList vars) {
        if (variables.equals(vars)) {
            return evaluateExpr(vars);
        } else {
            throw new IllegalArgumentException("Mismatching when evaluating variables!");
        }
    }

    public double evaluate(double[] vars) {
        variables.setVariables(vars);
        return evaluate(variables);
    }

    protected abstract double evaluateExpr(VariablesList vars);

    public abstract String toString();

    /**
     * Contains the specific parser for each function.
     *
     * @author Pablo Villalobos
     */
    public static abstract class Parser {

        /**
         * Parses a string with the given list of variables and returns the
         * corresponding function. Not all the variables have to be in the list
         * to appear in the expression.
         *
         * @param str The String to parse.
         * @param variables The list of variables.
         * @return the function which matches the type or null
         */
        public abstract FunctionBO parse(String str, VariablesList variables);
    }
}
