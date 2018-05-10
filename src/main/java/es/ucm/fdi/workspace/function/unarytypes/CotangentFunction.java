package es.ucm.fdi.workspace.function.unarytypes;

import es.ucm.fdi.util.FunctionParserUtils;
import java.util.regex.Pattern;

import es.ucm.fdi.workspace.FunctionBO;
import es.ucm.fdi.workspace.function.types.UnaryFunction;
import es.ucm.fdi.workspace.function.types.VariablesList;

/**
 * Represents a contangent function
 *
 * @author Inmaculada Pérez
 */
public class CotangentFunction extends UnaryFunction {

    /**
     * Class constructor specifying functionBO and variables list.
     *
     * @param f Function
     * @param vars Variables
     */
    public CotangentFunction(FunctionBO f, VariablesList vars) {
        super(f, vars);
    }

    @Override
    public String toString() {
        return "cotan(" + function.toString() + ")";
    }

    @Override
    protected double evaluateExpression(VariablesList variables) {
        return 1 / Math.tan(function.evaluate(variables));
    }

    public static class Parser extends UnaryFunction.Parser {

        private static final Pattern REGEX = Pattern.compile("cotan\\((.*)\\)");

        @Override
        public CotangentFunction parse(String strParam, VariablesList variables) {
            CotangentFunction result = null;
            String strArg = UnaryFunction.Parser.parsePattern(strParam, REGEX);
            if (strArg != null) {
                FunctionBO arg = FunctionParserUtils.parse(strArg, variables);
                if (arg != null) {
                    result = new CotangentFunction(arg, variables);
                }
            }
            return result;
        }
    }
}
