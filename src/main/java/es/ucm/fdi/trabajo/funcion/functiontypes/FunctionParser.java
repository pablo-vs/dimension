package es.ucm.fdi.trabajo.funcion.functiontypes;

import es.ucm.fdi.trabajo.funcion.unarytypes.*;
import es.ucm.fdi.trabajo.funcion.binarytypes.*;

public class FunctionParser {
	private final Function.Parser[] parserList = {
		new IdentityFunction.Parser(),
		new ConstantFunction.Parser(),
		new Log10Function.Parser(),
		new LnFunction.Parser(),
		new SineFunction.Parser(),
		new CosineFunction.Parser(),
		new SecantFunction.Parser(),
		new CosecantFunction.Parser(),
		new TangentFunction.Parser(),
		new CotangentFunction.Parser(),
		new LogarithmicFunction.Parser(),
		new SumFunction.Parser(),
		new SubstractFunction.Parser(),
		new ProductFunction.Parser(),
		new DivideFunction.Parser(),
		new ModuloFunction.Parser(),
		new ExponentialFunction.Parser()
	};

	public Function parse(String str, VariablesList variables) {
		Function f = null;
		for(Function.Parser p : parserList) {
			f = p.parse(str, variables, this);
			if(f != null) {
				break;
			}
		}
		return f;
	}
}
