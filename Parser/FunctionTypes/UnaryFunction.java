package FunctionTypes;

public class UnaryFunction extends Function {
	protected int par;
	protected Function f;
	public UnaryFunction(String a, int param, String type) {
		this.f=parser(a);
		this.type=type;
		par=param;
	}
	public UnaryFunction() {
		f=null;
		par=1;
	}
	public double evaluate(int x) {
		return 0;
	}
}
