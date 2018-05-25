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

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import java.util.regex.Pattern;

import es.ucm.fdi.business.workspace.function.types.UnaryFunction;
import es.ucm.fdi.business.workspace.function.types.VariablesList;
import es.ucm.fdi.business.util.FunctionParser;
import es.ucm.fdi.business.workspace.function.AbstractFunction;

/**
 * Represents a cosecant function.
 *
 * @author Inmaculada Pérez
 */
@XmlRootElement
public class CosecantFunction extends UnaryFunction {

    public CosecantFunction() {
        this(new ConstantFunction(), new VariablesList());
    }

    /**
     * Class constructor specifying functionBO and variables list.
     *
     * @param f Function
     * @param vars Variables
     */
    public CosecantFunction(AbstractFunction f, VariablesList vars) {
        super(f, vars);
    }

    @Override
    public String toString() {
        return "cosec(" + function.toString() + ")";
    }

    @Override
    public double evaluate(VariablesList vars) {
        return 1 / Math.sin(Math.toRadians(function.evaluate(vars)));
    }

    public static class Parser extends UnaryFunction.Parser {

        private static final Pattern REGEX = Pattern.compile("cosec\\((.*)\\)");

        @Override
        public UnaryFunction parse(String strParam, VariablesList variables) {
            CosecantFunction result = null;
            String strArg = UnaryFunction.Parser.parsePattern(strParam, REGEX);
            if (strArg != null) {
                AbstractFunction arg = FunctionParser.parse(strArg, variables);
                if (arg != null) {
                    result = new CosecantFunction(arg, variables);
                }
            }
            return result;
        }
    }
}
