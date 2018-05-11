package es.ucm.fdi.business_tier.workspace.function.unarytypes;

import es.ucm.fdi.business_tier.util.FunctionParserUtils;
import java.util.regex.Pattern;

import es.ucm.fdi.business_tier.workspace.FunctionBO;
import es.ucm.fdi.business_tier.workspace.function.types.UnaryFunction;
import es.ucm.fdi.business_tier.workspace.function.types.VariablesList;

/**
 * Represents the decimal logarithm.
 *
 * @author Inmaculada Pérez
 */
public class Log10Function extends UnaryFunction {

    /**
     * Class constructor specifying functionBO and variables list.
     *
     * @param f Function
     * @param vars Variables
     */
    public Log10Function(FunctionBO f, VariablesList vars) {
        super(f, vars);
    }

    @Override
    public String toString() {
        return "log(" + function.toString() + ")";
    }

    @Override
    protected double evaluateExpression(VariablesList variables) {
        return Math.log10(function.evaluate(variables));
    }

    public static class Parser extends UnaryFunction.Parser {

        private static final Pattern REGEX = Pattern.compile("log\\((.*)\\)");

        @Override
        public Log10Function parse(String strParam, VariablesList variables) {
            Log10Function result = null;
            String strArg = UnaryFunction.Parser.parsePattern(strParam, REGEX);
            if (strArg != null) {
                FunctionBO arg = FunctionParserUtils.parse(strArg, variables);
                if (arg != null) {
                    result = new Log10Function(arg, variables);
                }
            }
            return result;
        }
    }
}
