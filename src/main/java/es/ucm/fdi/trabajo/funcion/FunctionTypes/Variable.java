package main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes;

public class Variable {
	int pos;
	int val;
	public Variable(int _pos, int _val) {
		pos=_pos;
		val=_val;
	}
	public void setVal(int x) {
		val=x;
	}
	public int getVal() {
		return val;
	}
}
