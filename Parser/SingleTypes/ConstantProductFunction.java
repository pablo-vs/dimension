package SingleTypes;

import FunctionTypes.SingleFunction;
import FunctionTypes.Function;

public class ConstantProductFunction extends SingleFunction{
	public ConstantProductFunction(String a, int par) {
		super(a, par, "ConstantProduct");
	}
	public String toString() {
		return Integer.toString(par)+f.toString();
	}
}
