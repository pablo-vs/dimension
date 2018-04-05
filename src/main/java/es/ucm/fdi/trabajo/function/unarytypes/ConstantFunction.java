package es.ucm.fdi.trabajo.function.unarytypes;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import es.ucm.fdi.trabajo.function.functiontypes.UnaryFunction;
import es.ucm.fdi.trabajo.function.functiontypes.VariablesList;
import es.ucm.fdi.trabajo.function.FunctionParser;
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
		private static final Pattern PI_REGEX = Pattern.compile("PI");
		private static final Pattern E_REGEX = Pattern.compile("e");

		@Override
		public ConstantFunction parse(String strParam, VariablesList variables, FunctionParser parser) {
			String str = ParserUtils.stripExtraParenthesis(strParam);
			Matcher digitMatcher = REGEX.matcher(str), piMatcher = PI_REGEX.matcher(str),
				eMatcher = E_REGEX.matcher(str);
			if(digitMatcher.matches()) {
				return new ConstantFunction(Double.parseDouble(str), variables);
			} else if (piMatcher.matches()) {
				System.out.println("Fuck yeah!");
				return new ConstantFunction(Math.PI, variables);
			} else if (eMatcher.matches()) {
				return new ConstantFunction(Math.E, variables);
			} else {
				return null;
			}
		}
	}
}
