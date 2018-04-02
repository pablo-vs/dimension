package es.ucm.fdi.trabajo.funcion.functiontypes;

import java.util.Map;
import java.util.TreeMap;
import java.util.Collection;
import java.util.Iterator;

/**
 * Representa la lista de variables de una función.
 *
 * @author Javier Navalón
 * @author Pablo Villalobos
 * @version 01.04.2018
 */
public class VariablesList {
	private TreeMap<String, Variable> variables;

	/**
	 * Constructor por copia.
	 *
	 * @param La lista que se desea copiar.
	 */
	public VariablesList(VariablesList other) {
		variables = (TreeMap<String, Variable>) other.variables.clone();
	}
	
	/**
	 * Construye e inicializa una lista de variables
	 *
	 * @param n El número de la lista.
	 */
	public VariablesList(int n) {
		variables = new TreeMap<String, Variable>();
		for(int i = 0; i < n; ++i) {
			String s = "x_" + i;
			variables.put(s, new Variable(s));
		}
		
	}

	/**
	 * Construye e inicializa una lista de variables con los nombres dados.
	 *
	 * @param nameList La lista de nombres de variables. 
	 */
	public VariablesList(String[] nameList) {
		variables = new TreeMap<String, Variable>();
		for (String s : nameList) {
			variables.put(s, new Variable(s));
		}
	}
	
	/**
	 * Establece el valor de la variable dada.
	 *
	 * @param var El número de la variable que se quiere cambiar.
	 * @param val El valor que se quiere establecer.
	 */
	public void setVariable(int var, double val) {
		if (0 <= var && var < variables.size()) {
			getVarAtPos(var).setVal(val);
		}
	}

	/**
         * Establece el valor de la variable dada.
	 *
	 * @param var El nombre de la variable que se quiere cambiar.
	 * @param val El valor que se quiere establecer.
	 */
	public void setVariable(String var, double val) {
		if (variables.containsKey(var)) {
			variables.get(var).setVal(val);
		} else {
			throw new IllegalArgumentException("La variable " + var + " no existe");
		}
	}

	/**
	 * Devuelve el valor de la variable indicada.
	 *
	 * @param var El nombre de la variable a la que acceder.
	 * @return El valor almacenado.
	 */
	public double getVariable(String var) {
	        if (variables.containsKey(var)) {
			return variables.get(var).getVal();
		} else {
			throw new IllegalArgumentException("La variable " + var + " no existe");
		}
	}

	/**
	 * Returns the ordered set of variables.
	 *
	 * @return The set.
	 */
	public Collection<Variable> getVariables() {
		return variables.values();
	}
	
	/**
	 * Devuelve el valor de la variable indicada.
	 *
	 * @param var El número de la variable a la que acceder.
	 * @return El valor almacenado.
	 */
	public double getVariable(int var) {
		return getVarAtPos(var).getVal();
	}

	/**
	 * Compara esta lista con otra y devuelve true si los nombres de las variables son iguales.
	 *
	 * @param other La lista con la que se desea comparar.
	 */
	public boolean equals(VariablesList other) {
		return (this == other) || (variables.keySet().equals(other.variables.keySet()));
	}
	
	private Variable getVarAtPos(int pos) {
		Variable var = null;
		if(0 <= pos && pos < variables.size()) {
			Iterator<Variable> it = variables.values().iterator();
			for(int i = 0; i <= pos; ++i) var = it.next();
		} else {
			throw new IndexOutOfBoundsException("No variable at position " + pos);
		}
		return var;
	}
}
