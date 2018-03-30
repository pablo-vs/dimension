package es.ucm.fdi.trabajo.funcion.FunctionTypes;

/**
 * @author Javier Navalon
 * @author Inmapg
 */
public abstract class UnaryFunction extends Function {
	protected int param;
	protected Function function;
	public UnaryFunction(String function, int param, String type) {
            super(type);
            this.function = parser(function);
            this.param = param;
	}
	public UnaryFunction() {
            super(null);
            function = null;
            param = 1;
	}
       
        @Override
	public double evaluate(int x){
            return 0;
        }
}
