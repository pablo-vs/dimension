package es.ucm.fdi.workspace.function.unarytypes;

import es.ucm.fdi.util.FunctionParserUtils;
import java.util.regex.Pattern;

import es.ucm.fdi.workspace.FunctionBO;
import es.ucm.fdi.workspace.function.types.UnaryFunction;
import es.ucm.fdi.workspace.function.types.VariablesList;

/**
 * Represents the tangent function.
 * 
 * @author Inmaculada Pérez
 */
public class TangentFunction extends UnaryFunction {

    /**
     * Class constructor specifying functionBO and variables list.
     * @param f Function
     * @param vars Variables
     */
    public TangentFunction(FunctionBO f, VariablesList vars) {
        super(f, vars);
    }

    @Override
    public String toString() {
        return "tan(" + function.toString() + ")";
    }

    @Override
    protected double evaluateExpression(VariablesList variables) {
        return Math.tan(function.evaluate(variables));
    }

    public static class Parser extends UnaryFunction.Parser {

        private static final Pattern REGEX = Pattern.compile("tan\\((.*)\\)");

        @Override
        public TangentFunction parse(String strParam, VariablesList variables) {
            TangentFunction result = null;
            String strArg = UnaryFunction.Parser.parsePattern(strParam, REGEX);
            if (strArg != null) {
                FunctionBO arg = FunctionParserUtils.parse(strArg, variables);
                if (arg != null) {
                    result = new TangentFunction(arg, variables);
                }
            }
            return result;
        }
    }
}
