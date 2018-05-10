package es.ucm.fdi.workspace.function.unarytypes;

import es.ucm.fdi.util.FunctionParserUtils;
import java.util.regex.Pattern;

import es.ucm.fdi.workspace.FunctionBO;
import es.ucm.fdi.workspace.function.types.UnaryFunction;
import es.ucm.fdi.workspace.function.types.VariablesList;

/**
 * Representa el logaritmo neperiano.
 *
 * @author Inmapg
 */
public class LnFunction extends UnaryFunction {

    /**
     * Class constructor specifying functionBO and variables list.
     * @param f Function
     * @param vars Variables
     */
    public LnFunction(FunctionBO f, VariablesList vars) {
        super(f, vars);
    }

    @Override
    public String toString() {
        return "ln(" + arg.toString() + ")";
    }

    @Override
    protected double evaluateExpr(VariablesList variables) {
        return Math.log(arg.evaluate(variables));
    }

    public static class Parser extends UnaryFunction.Parser {

        private static final Pattern REGEX = Pattern.compile("ln\\((.*)\\)");

        @Override
        public LnFunction parse(String strParam, VariablesList variables) {
            LnFunction result = null;
            String strArg = UnaryFunction.Parser.parsePattern(strParam, REGEX);
            if (strArg != null) {
                FunctionBO arg = FunctionParserUtils.parse(strArg, variables);
                if (arg != null) {
                    result = new LnFunction(arg, variables);
                }
            }
            return result;
        }
    }

}
