package es.ucm.fdi.business_tier.workspace.function.unarytypes;

import es.ucm.fdi.business_tier.util.FunctionParserUtils;
import java.util.regex.Pattern;

import es.ucm.fdi.business_tier.workspace.FunctionBO;
import es.ucm.fdi.business_tier.workspace.function.types.UnaryFunction;
import es.ucm.fdi.business_tier.workspace.function.types.VariablesList;

/**
 * Represents a cosine function.
 *
 * @author Inmaculada Pérez
 */
public class CosineFunction extends UnaryFunction {

    /**
     * Class constructor specifying functionBO and variables list.
     *
     * @param f Function
     * @param vars Variables
     */
    public CosineFunction(FunctionBO f, VariablesList vars) {
        super(f, vars);
    }

    @Override
    public String toString() {
        return "cos(" + function.toString() + ")";
    }

    @Override
    protected double evaluateExpression(VariablesList variables) {
        return Math.cos(function.evaluate(variables));
    }

    public static class Parser extends UnaryFunction.Parser {

        private static final Pattern REGEX = Pattern.compile("cos\\((.*)\\)");

        @Override
        public CosineFunction parse(String strParam, VariablesList variables) {
            CosineFunction result = null;
            String strArg = UnaryFunction.Parser.parsePattern(strParam, REGEX);
            if (strArg != null) {
                FunctionBO arg = FunctionParserUtils.parse(strArg, variables);
                if (arg != null) {
                    result = new CosineFunction(arg, variables);
                }
            }
            return result;
        }
    }
}
