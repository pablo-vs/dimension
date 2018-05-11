package es.ucm.fdi.business_tier.workspace.function.unarytypes;

import java.util.regex.Pattern;

import es.ucm.fdi.business_tier.workspace.function.types.UnaryFunction;
import es.ucm.fdi.business_tier.workspace.function.types.VariablesList;
import es.ucm.fdi.business_tier.workspace.function.types.Variable;

/**
 * Represents the identity function
 *
 * @author Javier Navalón
 */
public class IdentityFunction extends UnaryFunction {

    private String variable;

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
    protected double evaluateExpression(VariablesList list) {
        return list.getVariable(variable);
    }

    public static class Parser extends UnaryFunction.Parser {

        @Override
        public IdentityFunction parse(String str, VariablesList variables) {
            IdentityFunction f = null;
            for (Variable v : variables.getVariables()) {
                if (Pattern.matches(" *" + v.getNombre() + " *", str)) {
                    f = new IdentityFunction(v.getNombre(), variables);
                    break;
                }
            }
            return f;
        }

    }
}
