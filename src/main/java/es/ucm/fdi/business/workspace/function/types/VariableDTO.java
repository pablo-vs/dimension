/*
  This file is part of Dimension.
  Dimension is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  Dimension is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  You should have received a copy of the GNU General Public License
  along with Dimension.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.business.workspace.function.types;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

/**
 * Representa una variable de una función
 *
 * @author Javier Navalón
 */
@XmlRootElement
public class VariableDTO {

    private final String name;
    private double value = 0;

    public VariableDTO() {
        this("x");
    }

    /**
     * Class constructor specifying name and value of the variable.
     *
     * @param name
     * @param value
     */
    public VariableDTO(String name, double value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Class constructor specifying name of the variable.
     *
     * @param name
     */
    public VariableDTO(String name) {
        this.name = name;
    }

    /**
     * Modifies the value of the variable.
     *
     * @param value the new value
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     *
     * @return the value of the variable
     */
    @XmlElement
    public double getValue() {
        return value;
    }

    /**
     *
     * @return the value's name.
     */
    @XmlElement
    public String getName() {
        return name;
    }
}
