package es.ucm.fdi.workspace.function.binarytypes;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import es.ucm.fdi.workspace.FunctionBO;
import es.ucm.fdi.workspace.function.types.BinaryFunction;
import es.ucm.fdi.workspace.function.types.VariablesList;
import es.ucm.fdi.util.FunctionParserUtils;

/**
 * Represents the logarithmic function.
 *
 * @author Brian Leiva
 */
public class LogarithmicFunction extends BinaryFunction {

    /**
     * Empty class constructor.
     */
    public LogarithmicFunction() {
        super();
    }

    /**
     * Class constructor specifying the base and the argument of the function.
     *
     * @param function1
     * @param function2
     * @param variables
     */
    public LogarithmicFunction(FunctionBO function1, FunctionBO function2,
            VariablesList variables) {
        super(function1, function2, variables);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("");
        ret.append("log_(").append(function1.toString()).append(")(")
                .append(function2.toString()).append(")");
        return ret.toString();
    }

    @Override
    protected double evaluateExpression(VariablesList variables) {
        return Math.log(function2.evaluate(variables)) / Math.log(function1.evaluate(variables));
    }

    public static class Parser extends BinaryFunction.Parser {

        private static final Pattern REGEX = Pattern.compile(" *log_(\\(.*\\))(\\(.*\\))");

        @Override
        public LogarithmicFunction parse(String strParam, VariablesList variables) {
            String str = FunctionParserUtils.stripExtraParenthesis(strParam);
            LogarithmicFunction result = null;
            Matcher m = REGEX.matcher(str);
            if (m.matches()) {
                FunctionBO f1 = FunctionParserUtils.parse(m.group(1), variables),
                        f2 = FunctionParserUtils.parse(m.group(2), variables);
                if (f1 != null && f2 != null) {
                    result = new LogarithmicFunction(f1, f2, variables);
                }
            }
            return result;
        }
    }
}
