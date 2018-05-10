package es.ucm.fdi.workspace.function.types;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import es.ucm.fdi.workspace.FunctionBO;
import es.ucm.fdi.util.FunctionParserUtils;

/**
 * @author Inmaculada Pérez, Javier Navalon
 */
public abstract class UnaryFunction extends FunctionBO {

    protected FunctionBO function;

    /**
     * Class constructor specifying the function and the list of variables.
     *
     * @param function
     * @param variables
     */
    public UnaryFunction(FunctionBO function, VariablesList variables) {
        super(variables);
        this.function = function;
    }

    /**
     * Class constructor specifying the list of variables.
     *
     * @param variables
     */
    public UnaryFunction(VariablesList variables) {
        super(variables);
    }

    public static abstract class Parser extends FunctionBO.Parser {

        @Override
        public abstract UnaryFunction parse(String str, VariablesList variables);

        public static String parsePattern(String strParam, Pattern patron) {
            String result = null;
            String str = FunctionParserUtils.stripExtraParenthesis(strParam);
            Matcher m = patron.matcher(str);
            if (m.matches()) {
                result = m.group(1);
            }
            return result;
        }
    }
}
