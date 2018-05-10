package es.ucm.fdi.workspace.function.types;

/**
 * Representa una variable de una función
 *
 * @author Javier Navalón
 */
public class Variable {

    private String name;
    private double value;

    /**
     * Class constructor specifying name and value of the variable.
     *
     * @param name
     * @param value
     */
    public Variable(String name, double value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Class constructor specifying name of the variable.
     *
     * @param name
     */
    public Variable(String name) {
        this.name = name;
        value = 0;
    }

    /**
     * Modifies the value of the variable.
     *
     * @param value the new value
     */
    public void setVal(double value) {
        this.value = value;
    }

    /**
     *
     * @return the value of the variable
     */
    public double getValue() {
        return value;
    }

    /**
     *
     * @return the value's name.
     */
    public String getNombre() {
        return name;
    }
}
