package es.ucm.fdi.workspace.function.types;

/**
 * Representa una variable de una función
 *
 * @author Javier Navalón
 * @version 01.04.2018
 */
public class Variable {

    private String nombre;
    private double val;

    public Variable(String n, double _val) {
        nombre = n;
        val = _val;
    }

    public Variable(String n) {
        this(n, 0);
    }

    /**
     * Modifica el valor de la variable.
     *
     * @param x el nuevo valor.
     */
    public void setVal(double x) {
        val = x;
    }

    /**
     * Devuelve el valor de la variable.
     *
     * @return El valor almacenado.
     */
    public double getVal() {
        return val;
    }

    /**
     * Devuelve el nombre de la variable.
     *
     * @return El nombre de la variable.
     */
    public String getNombre() {
        return nombre;
    }
}
