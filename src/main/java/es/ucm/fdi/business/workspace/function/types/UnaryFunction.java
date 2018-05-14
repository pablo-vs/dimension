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
package es.ucm.fdi.business.workspace.function.types;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import es.ucm.fdi.business.util.FunctionParserUtils;
import es.ucm.fdi.business.workspace.function.AbstractFunction;

/**
 * @author Inmaculada Pérez, Javier Navalon
 */
public abstract class UnaryFunction implements AbstractFunction {

    protected AbstractFunction function;

    protected VariablesList vars;

    /**
     * Class constructor specifying the function and the list of variables.
     *
     * @param function
     * @param vars
     */
    public UnaryFunction(AbstractFunction function, VariablesList vars) {
        this.vars = vars;
        this.function = function;
    }

    /**
     * Class constructor specifying the list of variables.
     *
     * @param vars
     */
    public UnaryFunction(VariablesList vars) {
        this.vars = vars;
    }

    /**
     * Returns the variables list of the function.
     *
     * @return The vars list.
     */
    public VariablesList getVars() {
        return this.vars;
    }

    /**
     * Evaluates the function from a given array of vars.
     *
     * @param varsArray
     * @return
     */
    public double evaluate(double[] varsArray) {
        this.vars.setVariables(varsArray);
        return evaluate(vars);
    }

    public static abstract class Parser extends AbstractFunction.Parser {

        @Override
        public abstract UnaryFunction parse(String str, VariablesList variables);

        public static String parsePattern(String strParam, Pattern patron) {
            String result = null;
            String str = FunctionParserUtils.stripExtraParenthesis(strParam);
            Matcher m = patron.matcher(str);
            if (m.matches()) {
                result = m.group(1);
            }
            return result;
        }
    }
}
