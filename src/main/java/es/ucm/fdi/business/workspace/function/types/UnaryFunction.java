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

import javax.xml.bind.annotation.XmlElement;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import es.ucm.fdi.business.util.FunctionParser;
import es.ucm.fdi.business.workspace.function.AbstractFunction;

/**
 * Represents a unary function.
 *
 * @author Inmaculada Pérez, Javier Navalon
 */
public abstract class UnaryFunction extends AbstractFunction {

    @XmlElement
    protected AbstractFunction function;

    /**
     * Class constructor specifying the function and the list of variables.
     *
     * @param function
     * @param vars
     */
    public UnaryFunction(AbstractFunction function, VariablesList vars) {
        this.variables = vars;
        this.function = function;
    }

    /**
     * Class constructor specifying the list of variables.
     *
     * @param vars
     */
    public UnaryFunction(VariablesList vars) {
        this.variables = vars;
    }

    public static abstract class Parser extends AbstractFunction.Parser {

        @Override
        public abstract UnaryFunction parse(String str, VariablesList variables);

        public static String parsePattern(String strParam, Pattern patron) {
            String result = null;
            String str = FunctionParser.stripExtraParenthesis(strParam);
            Matcher m = patron.matcher(str);
            if (m.matches()) {
                result = m.group(1);
            }
            return result;
        }
    }
}
