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

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import es.ucm.fdi.business.util.FunctionParser;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import es.ucm.fdi.business.workspace.function.AbstractFunction;

/**
 * Represents a binary function.
 *
 * @author Inmaculada Pérez, Javier Navalón
 */
public abstract class BinaryFunction extends AbstractFunction {
	
	@XmlElement
    protected AbstractFunction function1;
	@XmlElement
    protected AbstractFunction function2;

    /**
     * Class constructor.
     */
    public BinaryFunction() {
        super();
    }

    /**
     * Class constructor specifying two functions and a list of variables.
     *
     * @param function1
     * @param function2
     * @param variables
     */
    public BinaryFunction(AbstractFunction function1, AbstractFunction function2, VariablesList variables) {
        this.variables = variables;
        this.function1 = function1;
        this.function2 = function2;
    }

    public static abstract class Parser extends AbstractFunction.Parser {

        @Override
        public abstract BinaryFunction parse(String strParam, VariablesList variables);

        /**
         * Parsing method for binary functions with infix operators. Given a
         * regex specifying the operator, returns the functions to the left and
         * right, which will be null if the parsing cannot be done.
         *
         * @param strParam The string to parse.
         * @param variables The variable list.
         * @param operator The infix operator.
         * @return the parsed functions
         */
        public static AbstractFunction[] parseFunctions(String strParam, VariablesList variables, Pattern operator) {
            AbstractFunction[] funcs = {null, null};
            boolean success = true;
            String str = FunctionParser.stripExtraParenthesis(strParam);
            int endFirst = 0, startSecond;
            if (!str.isEmpty()) {
                if (str.charAt(0) == '(') {
                    try {
                        endFirst = FunctionParser.getEndOfParenthesis(str, 0);
                    } catch (IllegalArgumentException e) {
                        success = false;
                    }
                }
                if (success) {
                    Matcher m = operator.matcher(str);
                    boolean done = false;
                    int i = 0;
                    if (m.find(endFirst)) {
                        do {
                            endFirst = m.start();
                            startSecond = m.end();
                            funcs[0] = FunctionParser.parse(str.substring(0, endFirst), variables);
                            if (funcs[0] != null) {
                                funcs[1] = FunctionParser.parse(str.substring(startSecond), variables);
                                done = funcs[1] != null;
                            }
                        } while (!done && !m.hitEnd() && m.find());
                    }
                }
            }
            return funcs;
        }
    }
}
