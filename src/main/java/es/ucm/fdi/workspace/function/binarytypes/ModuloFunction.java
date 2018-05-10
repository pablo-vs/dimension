package es.ucm.fdi.workspace.function.binarytypes;

import java.util.regex.Pattern;

import es.ucm.fdi.workspace.FunctionBO;
import es.ucm.fdi.workspace.function.types.BinaryFunction;
import es.ucm.fdi.workspace.function.types.VariablesList;

/**
 * Represents the modulo function.
 *
 * @author Inmaculada Pérez, Javier Navalon
 */
public class ModuloFunction extends BinaryFunction {

    /**
     * Empty class constructor.
     */
    public ModuloFunction() {
        super();
    }

    /**
     * Class constructor specifying both functions and the list of variables.
     * 
     * @param function1
     * @param function2
     * @param variables 
     */
    public ModuloFunction(FunctionBO function1, FunctionBO function2, VariablesList variables) {
        super(function1, function2, variables);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("");
        ret.append("(").append(function1.toString()).append(") % (")
                .append(function2.toString()).append(")");
        return ret.toString();
    }

    @Override
    protected double evaluateExpression(VariablesList variables) {
        return (function1.evaluate(variables) % function2.evaluate(variables));
    }

    public static class Parser extends BinaryFunction.Parser {

        private static final Pattern REGEX = Pattern.compile(" *% *");

        @Override
        public ModuloFunction parse(String str, VariablesList variables) {
            ModuloFunction func = null;
            FunctionBO[] funcs = BinaryFunction.Parser.parseFunctions(str, variables, REGEX);
            if (funcs[0] != null && funcs[1] != null) {
                func = new ModuloFunction(funcs[0], funcs[1], variables);
            }
            return func;
        }
    }
}
