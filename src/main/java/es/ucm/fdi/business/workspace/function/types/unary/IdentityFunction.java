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
import es.ucm.fdi.business.workspace.function.types.VariableDTO;

/**
 * Represents the identity function.
 *
 * @author Javier Navalón
 */
@XmlRootElement
public class IdentityFunction extends UnaryFunction {

	@XmlElement
    private final String variable;

	public IdentityFunction() {
		this("x_0", new VariablesList());
	}
	
    /**
     * Class constructor specifying variable and variables list.
     *
     * @param variable
     * @param vars List of variables
     */
    public IdentityFunction(String variable, VariablesList vars) {
        super(vars);
        this.variable = variable;
    }

    @Override
    public String toString() {
        return variable;
    }

    @Override
    public double evaluate(VariablesList vars) {
        return vars.getVariable(variable);
    }

    public static class Parser extends UnaryFunction.Parser {

        @Override
        public UnaryFunction parse(String str, VariablesList variables) {
            IdentityFunction f = null;
            for (VariableDTO v : variables.getVariables()) {
                if (Pattern.matches(" *" + v.getName() + " *", str)) {
                    f = new IdentityFunction(v.getName(), variables);
                    break;
                }
            }
            return f;
        }

    }
}
