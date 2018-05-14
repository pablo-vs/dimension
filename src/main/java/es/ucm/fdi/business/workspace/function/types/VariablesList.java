/**
 * This file is part of Dimension.
 * Dimension is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Dimension is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Dimension.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.business.workspace.function.types;

import java.util.TreeMap;
import java.util.Collection;
import java.util.Iterator;

/**
 * Represents the list of variables of a function.
 *
 * @author Javier Navalón, Inmaculada Pérez, Pablo Villalobos
 */
public class VariablesList {

    private TreeMap<String, VariableDTO> vars;

    /**
     * Class constructor specifying another list of variables.
     *
     * @param other list to copy from
     */
    public VariablesList(VariablesList other) {
        vars = (TreeMap<String, VariableDTO>) other.vars.clone();
    }

    /**
     * Class constructor that initializes the list of variables with the size n.
     *
     * @param n Size
     */
    public VariablesList(int n) {
        vars = new TreeMap<>();
        for (int i = 0; i < n; ++i) {
            vars.put("x_" + i, new VariableDTO("x_" + i));
        }

    }

    /**
     * Class constructor that initializes the list of variables with the given
     * names.
     *
     * @param nameList the list with the names of the variables
     */
    public VariablesList(String[] nameList) {
        vars = new TreeMap<>();
        for (String s : nameList) {
            vars.put(s, new VariableDTO(s));
        }
    }

    /**
     * Sets the value of the given variable.
     *
     * @param variable the number of the variable to change
     * @param value
     */
    public void setVariable(int variable, double value) {
        if (0 <= variable && variable < vars.size()) {
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
        if (vars.containsKey(variable)) {
            vars.get(variable).setVal(value);
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
        if (values.length == vars.size()) {
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
        if (vars.containsKey(variable)) {
            return vars.get(variable).getValue();
        } else {
            throw new IllegalArgumentException("Variable " + variable + " does"
                    + " not exist");
        }
    }

    /**
     *
     * @return the ordered set of variables
     */
    public Collection<VariableDTO> getVariables() {
        return vars.values();
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
        return (this == other) || (vars.keySet().equals(other.vars.keySet()));
    }

    /**
     *
     * @param position
     * @return the variable at a determined position
     */
    private VariableDTO getVariableAtPosition(int position) {
        VariableDTO variable = null;
        if (0 <= position && position < vars.size()) {
            Iterator<VariableDTO> it = vars.values().iterator();
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
