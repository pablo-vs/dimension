package BinaryTypes;

import FunctionTypes.BinaryFunction;
import FunctionTypes.Function;

public class ProductFunction extends BinaryFunction{
	public ProductFunction(String a, String b) {
		super(a,b,"Product");
	}
	public String toString() {
		String ret=null;
		ret+="(" + f1.toString()+ ")"+"*"+"("+f2.toString() + ")";
		return ret;
	}
	public double evaluate(int x) {
		return (f1.evaluate(x)*f2.evaluate(x));
	}
}
