package UnaryTypes;

import FunctionTypes.UnaryFunction;

public class ConstantProductFunction extends UnaryFunction{
	public ConstantProductFunction(String a, int par) {
		super(a, par, "ConstantProduct");
	}
	public String toString() {
		return Integer.toString(par)+f.toString();
	}
}
