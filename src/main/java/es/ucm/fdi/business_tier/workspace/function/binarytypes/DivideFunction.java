package es.ucm.fdi.business_tier.workspace.function.binarytypes;

import java.util.regex.Pattern;

import es.ucm.fdi.business_tier.workspace.FunctionBO;
import es.ucm.fdi.business_tier.workspace.function.types.BinaryFunction;
import es.ucm.fdi.business_tier.workspace.function.types.VariablesList;

/**
 * Represents the division function.
 *
 * @author Inmaculada Pérez, Javier Navalón
 */
public class DivideFunction extends BinaryFunction {

    /**
     * Empty class constructor.
     */
    public DivideFunction() {
        super();
    }

    /**
     * Class constructor specifying two functions to divide and the list of
     * variables.
     *
     * @param function1
     * @param function2
     * @param variables
     */
    public DivideFunction(FunctionBO function1, FunctionBO function2,
            VariablesList variables) {
        super(function1, function2, variables);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("");
        ret.append("(").append(function1.toString()).append(") / (")
                .append(function2.toString()).append(")");
        return ret.toString();
    }

    @Override
    protected double evaluateExpression(VariablesList variables) {
        return (function1.evaluate(variables) / function2.evaluate(variables));
    }

    public static class Parser extends BinaryFunction.Parser {

        private static final Pattern REGEX = Pattern.compile(" */ *");

        @Override
        public DivideFunction parse(String name, VariablesList variables) {
            DivideFunction function = null;
            FunctionBO[] parsedFunction = BinaryFunction.Parser.parseFunctions(name, variables, REGEX);
            if (parsedFunction[0] != null && parsedFunction[1] != null) {
                function = new DivideFunction(parsedFunction[0], parsedFunction[1], variables);
            }
            return function;
        }
    }
}
