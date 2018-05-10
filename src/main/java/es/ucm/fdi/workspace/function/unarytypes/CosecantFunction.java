package es.ucm.fdi.workspace.function.unarytypes;

import java.util.regex.Pattern;

import es.ucm.fdi.workspace.FunctionBO;
import es.ucm.fdi.workspace.function.types.UnaryFunction;
import es.ucm.fdi.workspace.function.types.VariablesList;
import es.ucm.fdi.util.FunctionParserUtils;

/**
 * Represents a cosecant function
 *
 * @author Inmaculada Pérez
 */
public class CosecantFunction extends UnaryFunction {

    /**
     * Class constructor specifying functionBO and variables list.
     *
     * @param f Function
     * @param vars Variables
     */
    public CosecantFunction(FunctionBO f, VariablesList vars) {
        super(f, vars);
    }

    @Override
    public String toString() {
        return "cosec(" + function.toString() + ")";
    }

    @Override
    protected double evaluateExpression(VariablesList variables) {
        return 1 / Math.sin(function.evaluate(variables));
    }

    public static class Parser extends UnaryFunction.Parser {

        private static final Pattern REGEX = Pattern.compile("cosec\\((.*)\\)");

        @Override
        public CosecantFunction parse(String strParam, VariablesList variables) {
            CosecantFunction result = null;
            String strArg = UnaryFunction.Parser.parsePattern(strParam, REGEX);
            if (strArg != null) {
                FunctionBO arg = FunctionParserUtils.parse(strArg, variables);
                if (arg != null) {
                    result = new CosecantFunction(arg, variables);
                }
            }
            return result;
        }
    }
}
