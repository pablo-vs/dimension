package es.ucm.fdi.util;

import es.ucm.fdi.workspace.FunctionBO;
import es.ucm.fdi.workspace.function.types.VariablesList;
import es.ucm.fdi.workspace.function.binarytypes.DivideFunction;
import es.ucm.fdi.workspace.function.binarytypes.ExponentialFunction;
import es.ucm.fdi.workspace.function.binarytypes.LogarithmicFunction;
import es.ucm.fdi.workspace.function.binarytypes.ModuloFunction;
import es.ucm.fdi.workspace.function.binarytypes.ProductFunction;
import es.ucm.fdi.workspace.function.binarytypes.SubstractFunction;
import es.ucm.fdi.workspace.function.binarytypes.SumFunction;
import es.ucm.fdi.workspace.function.unarytypes.ConstantFunction;
import es.ucm.fdi.workspace.function.unarytypes.CosecantFunction;
import es.ucm.fdi.workspace.function.unarytypes.CosineFunction;
import es.ucm.fdi.workspace.function.unarytypes.CotangentFunction;
import es.ucm.fdi.workspace.function.unarytypes.IdentityFunction;
import es.ucm.fdi.workspace.function.unarytypes.LnFunction;
import es.ucm.fdi.workspace.function.unarytypes.Log10Function;
import es.ucm.fdi.workspace.function.unarytypes.SecantFunction;
import es.ucm.fdi.workspace.function.unarytypes.SineFunction;
import es.ucm.fdi.workspace.function.unarytypes.TangentFunction;

/**
 * FunctionBO-parser utility class. This class contains only static methods and a 
 * private constructor to avoid instantiation. Given a list of parsers <i>parserList</i>
 * containing different types of functions, the method {@link #parse parse} 
 * returns a {@link es.ucm.fdi.workspace.FunctionBO FunctionBO}. 
 *
 * @author Pablo Villalobos
 * @author Arturo Acuaviva
 * @version 06.04.2018
 */

public final class FunctionParserUtils {
    
        /**
         * This private constructor is used to avoid utility class instantiation.
         */
        private FunctionParserUtils(){ /*Exists only to avoid instantiation.*/ }
        
        /**
         * The list which contains all the types of {@link es.ucm.fdi.workspace.FunctionBO.Parser Parse} for each type of {@link es.ucm.fdi.workspace.FunctionBO FunctionBO}. 
         */
	private static final FunctionBO.Parser[] parserList = {
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
         * It turns a given String and a {@link es.ucm.fdi.workspace.function.types.VariablesList VariablesList} 
         * into a {@link es.ucm.fdi.workspace.FunctionBO FunctionBO}.
	 * It returns null when the string does not match any of the functions in
         * {@link es.ucm.fdi.workspace.function.types function.types}.
	 * <b>Warning:</b> Not all values from {@link es.ucm.fdi.workspace.function.types.VariablesList VariablesList} 
         * have to be present in the given expresion.
	 * @param str The String to be parsed
	 * @param var List of variables the function is supposed to contain. 
         * @return the function which matches the type.
	 */
	public static FunctionBO parse(String str, VariablesList var) {
		FunctionBO f = null;
		for(FunctionBO.Parser p : parserList) {
			f = p.parse(str, var);
			if(f != null) {
				break;
			}
		}
		return f;
	}

	/**
	 * Eliminates unnecessary parenthesis at the ends of the string.
	 */
	public static String stripExtraParenthesis(String str1) {
		String str = str1.trim();
		int ini = 0, end = 0, min = str.length(), current;
		int i = 0;
		while(str.charAt(i) == '(') {
			ini++;
			i++;
		}
		i = 0;
		while(str.charAt(str.length()-i-1) == ')') {
			end++;
			i++;
		}
		if(ini > end) {
			min = end;
		} else {
			min = ini;
		}
		current = ini;
		i = ini;
		while(i < str.length()-end) {
			char c = str.charAt(i);
			if(c == '(') {
				current++;
			} else if (c == ')' && current > 0) {
				current--;
				if(current < min) {
					min = current;
				}
			}
			++i;
		}
		return str.substring(min, str.length()-min);
	}

	/**
	 * Given the position of an opening parenthesis, return the position
	 * next to the corresponding closing one.
	  */
	public static int getEndOfParenthesis(String str, int ini) {
		if(str.charAt(ini) == '(') {
			int current = 1, i = ini+1;
			while(i < str.length() && current > 0) {
				if(str.charAt(i) == '(') {
					current++;
				} else if (str.charAt(i) == ')') {
					current--;
				}
				++i;
			}
			if(current > 0) {
				throw new IllegalArgumentException("Expected )");
			} else {
				return i;
			}
		} else {
			throw new IllegalArgumentException("Character at position " + ini + " is not '('");
		}
	}
}
