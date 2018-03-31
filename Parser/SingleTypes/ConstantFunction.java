package SingleTypes;

import FunctionTypes.UnaryFunction;

public class ConstantFunction extends UnaryFunction{
	public ConstantFunction(String a, int par) {
		super();
		f=null;
		this.par=par;
	}
	public String toString() {
		return Integer.toString(par);
	}
	public double evaluate(int x) {
		return par;
	}
}
