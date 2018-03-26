package SingleTypes;

import FunctionTypes.SingleFunction;
import FunctionTypes.Function;

public class IdentityFunction extends SingleFunction{
	public IdentityFunction(String a, int par) {
		type="Identity";
		par=1;
		f=null;
	}
	public String toString() {
		return "x";
	}
	public double evaluate(int x) {
		return (x);
	}
}
