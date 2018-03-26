package BinaryTypes;

import FunctionTypes.BinaryFunction;

public class DivideFunction extends BinaryFunction{
	public DivideFunction(String a, String b) {
		super(a,b,"Division");
	}
	public String toString() {
		String ret=null;
		ret+="(" + f1.toString()+ ")"+"/"+"("+f2.toString() + ")";
		return ret;
	}
	public double evaluate(int x) {
		return (f1.evaluate(x)/f2.evaluate(x));
	}
}
