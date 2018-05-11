package es.ucm.fdi.business_tier.workspace.function.unarytypes;

import es.ucm.fdi.business_tier.util.FunctionParserUtils;
import java.util.regex.Pattern;

import es.ucm.fdi.business_tier.workspace.FunctionBO;
import es.ucm.fdi.business_tier.workspace.function.types.UnaryFunction;
import es.ucm.fdi.business_tier.workspace.function.types.VariablesList;

/**
 * Representa el logaritmo neperiano.
 *
 * @author Inmaculada Pérez
 */
public class LnFunction extends UnaryFunction {

    /**
     * Class constructor specifying functionBO and variables list.
     *
     * @param f Function
     * @param vars Variables
     */
    public LnFunction(FunctionBO f, VariablesList vars) {
        super(f, vars);
    }

    @Override
    public String toString() {
        return "ln(" + function.toString() + ")";
    }

    @Override
    protected double evaluateExpression(VariablesList variables) {
        return Math.log(function.evaluate(variables));
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
