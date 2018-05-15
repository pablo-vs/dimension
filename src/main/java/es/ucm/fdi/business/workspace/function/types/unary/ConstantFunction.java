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

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import es.ucm.fdi.business.workspace.function.types.UnaryFunction;
import es.ucm.fdi.business.workspace.function.types.VariablesList;
import es.ucm.fdi.business.util.FunctionParserUtils;

/**
 * Represents a constant function.
 *
 * @author Javier Navalón
 */
public class ConstantFunction extends UnaryFunction {

    private final double num;

    /**
     * Class constructor specifying number and variables list.
     *
     * @param num Number
     * @param vars Variables
     */
    public ConstantFunction(double num, VariablesList vars) {
        super(vars);
        this.num = num;
    }

    @Override
    public String toString() {
        return Double.toString(num);
    }

    @Override
    public double evaluate(VariablesList vars) {
        return num;
    }

    public static class Parser extends UnaryFunction.Parser {

        private static final Pattern REGEX = Pattern.compile("\\d+");
        private static final Pattern PI_REGEX = Pattern.compile("PI");
        private static final Pattern E_REGEX = Pattern.compile("e");

        @Override
        public ConstantFunction parse(String strParam, VariablesList variables) {
            String str = FunctionParserUtils.stripExtraParenthesis(strParam);
            Matcher digitMatcher = REGEX.matcher(str), piMatcher = PI_REGEX.matcher(str),
                    eMatcher = E_REGEX.matcher(str);
            if (digitMatcher.matches()) {
                return new ConstantFunction(Double.parseDouble(str), variables);
            } else if (piMatcher.matches()) {
                return new ConstantFunction(Math.PI, variables);
            } else if (eMatcher.matches()) {
                return new ConstantFunction(Math.E, variables);
            } else {
                return null;
            }
        }
    }
}
