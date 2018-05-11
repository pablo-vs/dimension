package es.ucm.fdi.business_tier.workspace.function.types;

import es.ucm.fdi.business_tier.util.FunctionParserUtils;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import es.ucm.fdi.business_tier.workspace.FunctionBO;

/**
 * Represents a binary function.
 *
 * @author Inmaculada Pérez, Javier Navalón
 */
public abstract class BinaryFunction extends FunctionBO {

    protected FunctionBO function1;
    protected FunctionBO function2;

    /**
     * Class constructor.
     */
    public BinaryFunction() {
        super();
    }

    /**
     * Class constructor specifying two functions and a list of variables.
     *
     * @param function1
     * @param function2
     * @param vars
     */
    public BinaryFunction(FunctionBO function1, FunctionBO function2, VariablesList vars) {
        super(vars);
        this.function1 = function1;
        this.function2 = function2;
    }

    public static abstract class Parser extends FunctionBO.Parser {

        @Override
        public abstract BinaryFunction parse(String strParam, VariablesList variables);

        /**
         * Useful method for parsing binary functions with infix operators.
         * Given a regex specifying the operator, returns the functions to the
         * left and right, which will be null if the parsing cannot be done.
         *
         * @param strParam The string to parse.
         * @param variables The variable list.
         * @param operator The infix operator.
         * @return the parsed functions
         */
        public static FunctionBO[] parseFunctions(String strParam, VariablesList variables, Pattern operator) {
            FunctionBO[] funcs = {null, null};
            boolean success = true;
            String str = FunctionParserUtils.stripExtraParenthesis(strParam);
            int endFirst = 0, startSecond;
            if (!str.equals("")) {
                if (str.charAt(0) == '(') {
                    try {
                        endFirst = FunctionParserUtils.getEndOfParenthesis(str, 0);
                    } catch (IllegalArgumentException e) {
                        success = false;
                    }
                }
                if (success) {
                    Matcher m = operator.matcher(str);
                    boolean done = false;
                    int i = 0;
                    if (m.find(endFirst)) {
                        do {
                            endFirst = m.start();
                            startSecond = m.end();
                            funcs[0] = FunctionParserUtils.parse(str.substring(0, endFirst), variables);
                            if (funcs[0] != null) {
                                funcs[1] = FunctionParserUtils.parse(str.substring(startSecond), variables);
                                done = funcs[1] != null;
                            }
                        } while (!done && !m.hitEnd() && m.find());
                    }
                }
            }
            return funcs;
        }
    }
}
