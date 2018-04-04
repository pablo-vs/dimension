package es.ucm.fdi.trabajo.function.binarytypes;

import java.util.regex.Pattern;

import es.ucm.fdi.trabajo.function.Function;
import es.ucm.fdi.trabajo.function.functiontypes.BinaryFunction;
import es.ucm.fdi.trabajo.function.functiontypes.VariablesList;
import es.ucm.fdi.trabajo.function.FunctionParser;

/**
 * Represents the product function.
 *
 * @author Javier Navalon
 * @author Inmapg
 * @version 01.04.2018
 */
public class ProductFunction extends BinaryFunction{

	public ProductFunction() {
		super();
	}
	
	public ProductFunction(Function f1, Function f2, VariablesList vars) {
		super(f1, f2, vars);
	}
	
	@Override
	public String toString() {
            StringBuilder ret = new StringBuilder("");
            ret.append("(").append(f1.toString()).append(") * (").append(f2.toString()).append(")");
            return ret.toString();
	}
        
	@Override
	protected double evaluateExpr(VariablesList variables) {
		return (f1.evaluate(variables) * f2.evaluate(variables));
	}

	public static class Parser extends BinaryFunction.Parser {
		private static final Pattern REGEX = Pattern.compile(" *\\* *");

		@Override
		public ProductFunction parse(String str, VariablesList variables, FunctionParser parser) {
			ProductFunction func = null;
		        Function[] funcs = BinaryFunction.Parser.parseFunctions(str, variables, REGEX, parser);
			if(funcs != null) {
				func = new ProductFunction(funcs[0], funcs[1], variables);
			}
			return func;
		}
	}
}