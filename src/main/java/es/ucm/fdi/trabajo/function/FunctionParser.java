package es.ucm.fdi.trabajo.function;

import es.ucm.fdi.trabajo.function.unarytypes.*;
import es.ucm.fdi.trabajo.function.binarytypes.*;
import es.ucm.fdi.trabajo.function.functiontypes.VariablesList;


/**
 * Esta clase actúa como interfaz del parser. Su única utilidad
 * es el método parse().
 *
 * @author Pablo Villalobos
 * @version 02.04.2018
 */
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

	/**
	 * Parsea un string con las variables dadas y devuelve
	 * la función correspondiente, o null
	 * si el string no representa una función válida.
	 * Nota: No todas las variables de la lista deben aparecer en la expresión
	 *
	 * @param str El string a parsear.
	 * @param variables Lista de variables que se espera que contenga la función
	 * @return La Funcion correspondiente.
	 */
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
