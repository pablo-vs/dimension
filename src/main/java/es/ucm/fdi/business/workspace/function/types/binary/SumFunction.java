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
package es.ucm.fdi.business.workspace.function.types.binary;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import java.util.regex.Pattern;

import es.ucm.fdi.business.workspace.function.types.BinaryFunction;
import es.ucm.fdi.business.workspace.function.types.VariablesList;
import es.ucm.fdi.business.workspace.function.AbstractFunction;

/**
 * Represents the sum function.
 *
 * @author Inmaculada Pérez, Javier Navalón
 */
@XmlRootElement
public class SumFunction extends BinaryFunction {

    /**
     * Empty class constructor.
     */
    public SumFunction() {
        super();
    }

    /**
     * Class constructor specifying both functions to sum and the list of
     * variables.
     *
     * @param function1
     * @param function2
     * @param variables
     */
    public SumFunction(AbstractFunction function1, AbstractFunction function2,
            VariablesList variables) {
        super(function1, function2, variables);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("");
        ret.append("(").append(function1.toString()).append(") + (")
                .append(function2.toString()).append(")");
        return ret.toString();
    }

    @Override
    public double evaluate(VariablesList vars) {
        return (function1.evaluate(vars) + function2.evaluate(vars));
    }

    public static class Parser extends BinaryFunction.Parser {

        private static final Pattern REGEX = Pattern.compile(" *\\+ *");

        @Override
        public BinaryFunction parse(String str, VariablesList variables) {
            SumFunction func = null;
            AbstractFunction[] funcs = BinaryFunction.Parser.parseFunctions(str, variables, REGEX);
            if (funcs[0] != null && funcs[1] != null) {
                func = new SumFunction(funcs[0], funcs[1], variables);
            }
            return func;
        }
    }
}
