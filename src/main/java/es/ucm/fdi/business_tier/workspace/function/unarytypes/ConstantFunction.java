package es.ucm.fdi.business_tier.workspace.function.unarytypes;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import es.ucm.fdi.business_tier.workspace.function.types.UnaryFunction;
import es.ucm.fdi.business_tier.workspace.function.types.VariablesList;
import es.ucm.fdi.business_tier.util.FunctionParserUtils;

/**
 * Represents a constant function.
 *
 * @author Javier Navalón
 */
public class ConstantFunction extends UnaryFunction {

    private double num;

    /**
     * Class constructor specifying number and variables list.
     *
     * @param num Number
     * @param vars Variables
     */
    public ConstantFunction(double num, VariablesList vars) {
        super(vars);
        this.num = num;
    }

    @Override
    public String toString() {
        return Double.toString(num);
    }

    @Override
    protected double evaluateExpression(VariablesList variables) {
        return num;
    }

    public static class Parser extends UnaryFunction.Parser {

        private static final Pattern REGEX = Pattern.compile("\\d+");
        private static final Pattern PI_REGEX = Pattern.compile("PI");
        private static final Pattern E_REGEX = Pattern.compile("e");

        @Override
        public ConstantFunction parse(String strParam, VariablesList variables) {
            String str = FunctionParserUtils.stripExtraParenthesis(strParam);
            Matcher digitMatcher = REGEX.matcher(str), piMatcher = PI_REGEX.matcher(str),
                    eMatcher = E_REGEX.matcher(str);
            if (digitMatcher.matches()) {
                return new ConstantFunction(Double.parseDouble(str), variables);
            } else if (piMatcher.matches()) {
                return new ConstantFunction(Math.PI, variables);
            } else if (eMatcher.matches()) {
                return new ConstantFunction(Math.E, variables);
            } else {
                return null;
            }
        }
    }
}
