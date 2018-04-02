package es.ucm.fdi.trabajo.funcion.unarytypes;

import java.util.regex.Pattern;

import es.ucm.fdi.trabajo.funcion.functiontypes.UnaryFunction;
import es.ucm.fdi.trabajo.funcion.functiontypes.VariablesList;
import es.ucm.fdi.trabajo.funcion.functiontypes.FunctionParser;
import es.ucm.fdi.util.ParserUtils;

/**
 * Represents a constant function.
 *
 * @author Javier Navalón
 * @version 01.04.2018
 */
public class ConstantFunction extends UnaryFunction{
	private double num;
	
	public ConstantFunction(double num, VariablesList vars) {
		super(vars);
		this.num = num;
	}
	
	public String toString() {
		return Double.toString(num);
	}

	@Override
	protected double evaluateExpr(VariablesList variables) {
		return num;
	}

	public static class Parser extends UnaryFunction.Parser{
		private static final Pattern REGEX = Pattern.compile("\\d+");

		@Override
		public ConstantFunction parse(String strParam, VariablesList variables, FunctionParser parser) {
			String str = ParserUtils.stripExtraParenthesis(strParam);
			if(parsePattern(str, REGEX)) {
				return new ConstantFunction(Double.parseDouble(str), variables);
			} else {
				return null;
			}
		}
	}
}
