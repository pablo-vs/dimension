package FunctionTypes;

public class BinaryFunction extends Function {
	protected Function f1;
	protected Function f2;
	public BinaryFunction(String a, String b, String type) {
		this.type=type;
		f1=parser(a);
		f2=parser(b);
	}
}
