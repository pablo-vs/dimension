package es.ucm.fdi.workspace.function.unarytypes;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import es.ucm.fdi.workspace.FunctionBO;
import es.ucm.fdi.workspace.function.types.UnaryFunction;
import es.ucm.fdi.workspace.function.types.VariablesList;
import es.ucm.fdi.workspace.function.types.Variable;

/**
 * Represents the identity function
 * 
 * @author Javier Navalón
 * @version 01.04.2018
 */
public class IdentityFunction extends UnaryFunction{
	String variable;
	
	public IdentityFunction(String var, VariablesList vars) {
		super(vars);
		variable = var;
	}
	
	public String toString() {
		return variable;
	}

	@Override
	protected double evaluateExpr(VariablesList list) {
		return list.getVariable(variable);
	}

	public static class Parser extends UnaryFunction.Parser {

		@Override
		public IdentityFunction parse(String str, VariablesList variables) {
			IdentityFunction f = null;
			for(Variable v : variables.getVariables()) {
				if(Pattern.matches(" *"+v.getNombre()+" *", str)) {
					f = new IdentityFunction(v.getNombre(), variables);
					break;
				}
			}
			return f;
		}

	}
}
