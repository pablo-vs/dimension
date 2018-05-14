/**
 * This file is part of Dimension.
 * Dimension is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Dimension is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Dimension.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.business.workspace.function;

import es.ucm.fdi.business.workspace.function.types.VariablesList;

/**
 * Defines an interface function object. A function should contain a
 * {@link es.ucm.fdi.business.workspace.function.types.VariablesList VariablesList}
 * vars to define the number and name of the parameters. It also provides an
 * accessor method {@link #getVars getVars} which return the
 * {@link es.ucm.fdi.business.workspace.function.types.VariablesList VariablesList}.
 * The classes which implement the interface are supposed to define a method
 * {@link #evaluate(es.ucm.fdi.workspace.function.types.VariablesList) evaluate}
 * which calculates the value of the function given a
 * {@link es.ucm.fdi.business.workspace.function.types.VariablesList VariablesList}.
 *
 * @author Javier Navalón
 * @author Arturo Acuaviva
 */
public interface AbstractFunction {

    /**
     * Returns the variables list of the function.
     *
     * @return The vars list.
     */
    public VariablesList getVars();

    /**
     * Evaluates the function at the point given by the variable list.
     * <b>Note:</b> the given variable names must be equal to those of the
     * function.
     *
     * @param vars The vars list.
     * @return The result of applying the function to the values.
     */
    public double evaluate(VariablesList vars);

    /**
     * Evaluates the function from a given list of vars.
     *
     * @param variables
     * @return
     */
    public double evaluate(double[] variables);

    /**
     *
     * @return a sequence of characters describing the function
     */
    @Override
    public abstract String toString();

    /**
     * Contains the specific parser for each function.
     *
     * @author Pablo Villalobos
     */
    public static abstract class Parser {

        /**
         * Parses a string with the given list of vars and returns the
         * corresponding function. Not all the vars have to be in the list to
         * appear in the expression.
         *
         * @param str The String to parse.
         * @param variables The list of vars.
         * @return the function which matches the type or null
         */
        public abstract AbstractFunction parse(String str, VariablesList variables);
    }
}
