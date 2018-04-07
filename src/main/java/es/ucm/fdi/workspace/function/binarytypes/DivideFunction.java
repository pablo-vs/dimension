package es.ucm.fdi.workspace.function.binarytypes;

import java.util.regex.Pattern;

import es.ucm.fdi.workspace.FunctionBO;
import es.ucm.fdi.workspace.function.types.BinaryFunction;
import es.ucm.fdi.workspace.function.types.VariablesList;

/**
 * Represents the division function.
 *
 * @author Javier Navalon
 * @author Inmapg
 * @version 01.04.2018
 */
public class DivideFunction extends BinaryFunction{


	public DivideFunction() {
		super();
	}
	
	public DivideFunction(FunctionBO f1, FunctionBO f2, VariablesList vars) {
		super(f1, f2, vars);
	}
	
	@Override
	public String toString() {
            StringBuilder ret = new StringBuilder("");
            ret.append("(").append(f1.toString()).append(") / (").append(f2.toString()).append(")");
            return ret.toString();
	}

	@Override
        protected double evaluateExpr(VariablesList variables) {
		return (f1.evaluate(variables) / f2.evaluate(variables));
	}

	public static class Parser extends BinaryFunction.Parser {
		private static final Pattern REGEX = Pattern.compile(" */ *");

		@Override
		public DivideFunction parse(String str, VariablesList variables) {
			DivideFunction func = null;
			FunctionBO[] funcs = BinaryFunction.Parser.parseFunctions(str, variables, REGEX);
			if(funcs[0] != null && funcs[1] != null) {
				func = new DivideFunction(funcs[0], funcs[1], variables);
			}
			return func;
		}
	}
}
