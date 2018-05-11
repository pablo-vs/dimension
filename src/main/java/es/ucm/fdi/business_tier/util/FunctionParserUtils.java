package es.ucm.fdi.business_tier.util;

import es.ucm.fdi.business_tier.workspace.function.types.VariablesList;
import es.ucm.fdi.business_tier.workspace.function.types.binary.DivideFunction;
import es.ucm.fdi.business_tier.workspace.function.types.binary.ExponentialFunction;
import es.ucm.fdi.business_tier.workspace.function.types.binary.LogarithmicFunction;
import es.ucm.fdi.business_tier.workspace.function.types.binary.ModuloFunction;
import es.ucm.fdi.business_tier.workspace.function.types.binary.ProductFunction;
import es.ucm.fdi.business_tier.workspace.function.types.binary.SubstractFunction;
import es.ucm.fdi.business_tier.workspace.function.types.binary.SumFunction;
import es.ucm.fdi.business_tier.workspace.function.types.unary.ConstantFunction;
import es.ucm.fdi.business_tier.workspace.function.types.unary.CosecantFunction;
import es.ucm.fdi.business_tier.workspace.function.types.unary.CosineFunction;
import es.ucm.fdi.business_tier.workspace.function.types.unary.CotangentFunction;
import es.ucm.fdi.business_tier.workspace.function.types.unary.IdentityFunction;
import es.ucm.fdi.business_tier.workspace.function.types.unary.LnFunction;
import es.ucm.fdi.business_tier.workspace.function.types.unary.Log10Function;
import es.ucm.fdi.business_tier.workspace.function.types.unary.SecantFunction;
import es.ucm.fdi.business_tier.workspace.function.types.unary.SineFunction;
import es.ucm.fdi.business_tier.workspace.function.types.unary.TangentFunction;
import es.ucm.fdi.business_tier.workspace.function.FunctionComposite;

/**
 * FunctionComposite-parser utility class. This class contains only static
 * methods and a private constructor to avoid instantiation. Given a list of
 * parsers
 * <i>parserList</i>
 * containing different types of functions, the method {@link #parse parse}
 * returns a
 * {@link es.ucm.fdi.workspace.workspace.FunctionBO FunctionComposite}.
 *
 * @author Pablo Villalobos
 * @author Arturo Acuaviva
 * @version 06.04.2018
 */
public final class FunctionParserUtils {

    /**
     * This private constructor is used to avoid utility class instantiation.
     */
    private FunctionParserUtils() {
    }

    /**
     * The list which contains all the types of
     * {@link es.ucm.fdi.workspace.FunctionBO.Parser Parse} for each type of
     * {@link es.ucm.fdi.workspace.workspace.FunctionBO FunctionComposite}.
     */
    private static final FunctionComposite.Parser[] PARSER_LIST = {
        new IdentityFunction.Parser(),
        new ConstantFunction.Parser(),
        new Log10Function.Parser(),
        new LnFunction.Parser(),
        new SineFunction.Parser(),
        new CosineFunction.Parser(),
        new SecantFunction.Parser(),
        new CosecantFunction.Parser(),
        new TangentFunction.Parser(),
        new CotangentFunction.Parser(),
        new LogarithmicFunction.Parser(),
        new SumFunction.Parser(),
        new SubstractFunction.Parser(),
        new ProductFunction.Parser(),
        new DivideFunction.Parser(),
        new ModuloFunction.Parser(),
        new ExponentialFunction.Parser()
    };

    /**
     * It turns a given String and a
     * {@link es.ucm.fdi.business_tier.workspace.function.types.VariablesList VariablesList}
     * into a
     * {@link es.ucm.fdi.workspace.workspace.FunctionBO FunctionComposite}. It
     * returns null when the string does not match any of the functions in
     * {@link es.ucm.fdi.workspace.function.types function.types}.
     * <b>Warning:</b> Not all values from
     * {@link es.ucm.fdi.business_tier.workspace.function.types.VariablesList VariablesList}
     * have to be present in the given expresion.
     *
     * @param str The String to be parsed
     * @param var List of variables the function is supposed to contain.
     * @return the function which matches the type.
     */
    public static FunctionComposite parse(String str, VariablesList var) {
        FunctionComposite f = null;
        for (FunctionComposite.Parser p : PARSER_LIST) {
            f = p.parse(str, var);
            if (f != null) {
                break;
            }
        }
        return f;
    }

    /**
     * Eliminates unnecessary parenthesis at the ends of the string.
     *
     * @param str
     * @return the parsed string
     */
    public static String stripExtraParenthesis(String str) {
        String strAux = str.trim();
        int ini = 0, end = 0, min = strAux.length(), current;
        int i = 0;
        while (strAux.charAt(i) == '(') {
            ini++;
            i++;
        }
        i = 0;
        while (strAux.charAt(strAux.length() - i - 1) == ')') {
            end++;
            i++;
        }
        if (ini > end) {
            min = end;
        } else {
            min = ini;
        }
        current = ini;
        i = ini;
        while (i < strAux.length() - end) {
            char c = strAux.charAt(i);
            if (c == '(') {
                current++;
            } else if (c == ')' && current > 0) {
                current--;
                if (current < min) {
                    min = current;
                }
            }
            ++i;
        }
        return strAux.substring(min, strAux.length() - min);
    }

    /**
     * Given the position of an opening parenthesis, return the position next to
     * the corresponding closing one.
     *
     * @param str
     * @param ini
     * @return
     */
    public static int getEndOfParenthesis(String str, int ini) {
        if (str.charAt(ini) == '(') {
            int current = 1, i = ini + 1;
            while (i < str.length() && current > 0) {
                if (str.charAt(i) == '(') {
                    current++;
                } else if (str.charAt(i) == ')') {
                    current--;
                }
                ++i;
            }
            if (current > 0) {
                throw new IllegalArgumentException("Expected )");
            } else {
                return i;
            }
        } else {
            throw new IllegalArgumentException("Character at position " + ini + " is not '('");
        }
    }
}
