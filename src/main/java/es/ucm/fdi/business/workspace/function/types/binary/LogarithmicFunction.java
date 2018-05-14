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
package es.ucm.fdi.business.workspace.function.types.binary;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import es.ucm.fdi.business.workspace.function.types.BinaryFunction;
import es.ucm.fdi.business.workspace.function.types.VariablesList;
import es.ucm.fdi.business.util.FunctionParserUtils;
import es.ucm.fdi.business.workspace.function.AbstractFunction;

/**
 * Represents the logarithmic function.
 *
 * @author Brian Leiva
 */
public class LogarithmicFunction extends BinaryFunction {

    /**
     * Empty class constructor.
     */
    public LogarithmicFunction() {
        super();
    }

    /**
     * Class constructor specifying the base and the argument of the function.
     *
     * @param function1
     * @param function2
     * @param variables
     */
    public LogarithmicFunction(AbstractFunction function1, AbstractFunction function2,
            VariablesList variables) {
        super(function1, function2, variables);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("");
        ret.append("log_(").append(function1.toString()).append(")(")
                .append(function2.toString()).append(")");
        return ret.toString();
    }

    /**
     * Evaluates the function at the point given by the variable list.
     * <b>Note:</b> the given variable names must be equal to those of the
     * function.
     *
     * @see AbstractFunction
     * @param vars The vars list.
     * @return The result of applying the function to the values.
     */
    @Override
    public double evaluate(VariablesList vars) {
        return Math.log(function2.evaluate(vars)) / Math.log(function1.evaluate(vars));
    }

    public static class Parser extends BinaryFunction.Parser {

        private static final Pattern REGEX = Pattern.compile(" *log_(\\(.*\\))(\\(.*\\))");

        @Override
        public LogarithmicFunction parse(String strParam, VariablesList variables) {
            String str = FunctionParserUtils.stripExtraParenthesis(strParam);
            LogarithmicFunction result = null;
            Matcher m = REGEX.matcher(str);
            if (m.matches()) {
                AbstractFunction f1 = FunctionParserUtils.parse(m.group(1), variables);
                AbstractFunction f2 = FunctionParserUtils.parse(m.group(2), variables);
                if (f1 != null && f2 != null) {
                    result = new LogarithmicFunction(f1, f2, variables);
                }
            }
            return result;
        }
    }
}
