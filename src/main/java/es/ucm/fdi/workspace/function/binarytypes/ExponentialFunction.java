package es.ucm.fdi.workspace.function.binarytypes;

import java.util.regex.Pattern;

import es.ucm.fdi.workspace.FunctionBO;
import es.ucm.fdi.workspace.function.types.BinaryFunction;
import es.ucm.fdi.workspace.function.types.VariablesList;

/**
 * Represents the exponential function.
 *
 * @author Inmaculada Pérez, Javier Navalón
 */
public class ExponentialFunction extends BinaryFunction {

    /**
     * Empty class constructor.
     */
    public ExponentialFunction() {
        super();
    }

    /**
     * Class constructor specifying the base function, the exponent and the
     * list of variables.
     * 
     * @param function1
     * @param function2
     * @param variables 
     */
    public ExponentialFunction(FunctionBO function1, FunctionBO function2,
            VariablesList variables) {
        super(function1, function2, variables);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("");
        ret.append("(").append(function1.toString()).append(") ^ (")
                .append(function2.toString()).append(")");
        return ret.toString();
    }

    @Override
    protected double evaluateExpression(VariablesList variables) {
        return Math.pow(function1.evaluate(variables), function2.evaluate(variables));
    }

    public static class Parser extends BinaryFunction.Parser {

        private static final Pattern REGEX = Pattern.compile(" *\\^ *");

        @Override
        public ExponentialFunction parse(String str, VariablesList variables) {
            ExponentialFunction function = null;
            FunctionBO[] parsedFunction = BinaryFunction.Parser.parseFunctions(str, variables, REGEX);
            if (parsedFunction[0] != null && parsedFunction[1] != null) {
                function = new ExponentialFunction(parsedFunction[0], 
                        parsedFunction[1], variables);
            }
            return function;
        }
    }
}
