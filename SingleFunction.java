package main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes;

public class SingleFunction extends Function {
	protected int par;
	protected Function f;
	public SingleFunction(String a, int param, String type) {
		this.f=parser(a);
		this.type=type;
		par=param;
	}
	public SingleFunction() {
		f=null;
		par=1;
	}
	public double evaluate(int x) {
		return 0;
	}
}
