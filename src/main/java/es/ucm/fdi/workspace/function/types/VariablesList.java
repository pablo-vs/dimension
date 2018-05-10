package es.ucm.fdi.workspace.function.types;

import java.util.TreeMap;
import java.util.Collection;
import java.util.Iterator;

/**
 * Represents the list of variables of a function.
 *
 * @author Javier Navalón, Inmaculada Pérez, Pablo Villalobos
 */
public class VariablesList {

    private TreeMap<String, Variable> variables;

    /**
     * Class constructor specifying another list of variables.
     *
     * @param other list to copy from
     */
    public VariablesList(VariablesList other) {
        variables = (TreeMap<String, Variable>) other.variables.clone();
    }

    /**
     * Class constructor that initializes the list of variables with the size n.
     *
     * @param n Size
     */
    public VariablesList(int n) {
        variables = new TreeMap<>();
        for (int i = 0; i < n; ++i) {
            variables.put("x_" + i, new Variable("x_" + i));
        }

    }

    /**
     * Class constructor that initializes the list of variables with the given
     * names.
     *
     * @param nameList the list with the names of the variables
     */
    public VariablesList(String[] nameList) {
        variables = new TreeMap<>();
        for (String s : nameList) {
            variables.put(s, new Variable(s));
        }
    }

    /**
     * Sets the value of the given variable.
     *
     * @param variable the number of the variable to change
     * @param value
     */
    public void setVariable(int variable, double value) {
        if (0 <= variable && variable < variables.size()) {
            getVariableAtPosition(variable).setVal(value);
        }
    }

    /**
     * Sets the value of the given variable.
     *
     * @param variable the name of the variable to change
     * @param value
     */
    public void setVariable(String variable, double value) {
        if (variables.containsKey(variable)) {
            variables.get(variable).setVal(value);
        } else {
            throw new IllegalArgumentException("Variable " + variable + " does "
                    + "not exist!");
        }
    }

    /**
     * Sets the value of the variables.
     * 
     * @param values 
     */
    public void setVariables(double[] values) {
        if (values.length == variables.size()) {
            for (int i = 0; i < values.length; ++i) {
                setVariable(i, values[i]);
            }
        } else {
            throw new IllegalArgumentException("Number of values does not match"
                    + " the number of variables");
        }
    }

    /**
     * Devuelve el valor de la variable indicada.
     *
     * @param variable
     * @return the value of the given variable
     */
    public double getVariable(String variable) {
        if (variables.containsKey(variable)) {
            return variables.get(variable).getValue();
        } else {
            throw new IllegalArgumentException("Variable " + variable + " does"
                    + " not exist");
        }
    }

    /**
     *
     * @return the ordered set of variables
     */
    public Collection<Variable> getVariables() {
        return variables.values();
    }

    /**
     *
     * @param variable 
     * @return the value of the given number of variable
     */
    public double getVariable(int variable) {
        return getVariableAtPosition(variable).getValue();
    }

    /**
     *
     * @param other list to compare
     * @return if the list of variables is equal to the other one given
     */
    public boolean equals(VariablesList other) {
        return (this == other) || (variables.keySet().equals(other.variables.keySet()));
    }

    /**
     * 
     * @param position
     * @return the variable at a determined position 
     */
    private Variable getVariableAtPosition(int position) {
        Variable variable = null;
        if (0 <= position && position < variables.size()) {
            Iterator<Variable> it = variables.values().iterator();
            for (int i = 0; i <= position; ++i) {
                variable = it.next();
            }
        } else {
            throw new IndexOutOfBoundsException("There is no variable at"
                    + " position " + position);
        }
        return variable;
    }
}
