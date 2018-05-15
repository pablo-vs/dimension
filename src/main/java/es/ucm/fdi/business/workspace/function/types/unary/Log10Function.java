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
package es.ucm.fdi.business.workspace.function.types.unary;

import es.ucm.fdi.business.util.FunctionParserUtils;
import java.util.regex.Pattern;

import es.ucm.fdi.business.workspace.function.types.UnaryFunction;
import es.ucm.fdi.business.workspace.function.types.VariablesList;
import es.ucm.fdi.business.workspace.function.AbstractFunction;

/**
 * Represents the decimal logarithm function.
 *
 * @author Inmaculada Pérez
 */
public class Log10Function extends UnaryFunction {

    /**
     * Class constructor specifying functionBO and variables list.
     *
     * @param f Function
     * @param vars Variables
     */
    public Log10Function(AbstractFunction f, VariablesList vars) {
        super(f, vars);
    }

    @Override
    public String toString() {
        return "log(" + function.toString() + ")";
    }

    @Override
    public double evaluate(VariablesList vars) {
        return Math.log10(function.evaluate(vars));
    }

    public static class Parser extends UnaryFunction.Parser {

        private static final Pattern REGEX = Pattern.compile("log\\((.*)\\)");

        @Override
        public Log10Function parse(String strParam, VariablesList variables) {
            Log10Function result = null;
            String strArg = UnaryFunction.Parser.parsePattern(strParam, REGEX);
            if (strArg != null) {
                AbstractFunction arg = FunctionParserUtils.parse(strArg, variables);
                if (arg != null) {
                    result = new Log10Function(arg, variables);
                }
            }
            return result;
        }
    }
}
