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
package es.ucm.fdi.business_tier.exceptions;

/**
 * This exception is thrown whenever there is a problem working with objects in
 * different dimension. If an object of n dimension is evaluated with m
 * arguments with m not equal to n the exception is thrown.
 *
 * @author Arturo Acuaviva Huertos
 */
public class NoMatchDimensionException extends Exception {

    /**
     * Simple constructor. It calls the exception constructor indicating in the
     * message that the exception has been thrown.
     */
    public NoMatchDimensionException() {
        super("No match dimension exception thrown!");
    }

    /**
     * Constructor with a context message. It calls the exception constructor
     * with the given message.
     *
     * @param message
     */
    public NoMatchDimensionException(String message) {
        super(message);
    }

    /**
     * Constructor with cause. It calls the exception constructor indicating in
     * the message that the exception has been thrown and the cause.
     *
     * @param cause
     */
    public NoMatchDimensionException(Throwable cause) {
        super("No match dimension exception thrown!", cause);
    }

    /**
     * Constructor with a context message and a cause. It calls the exception
     * constructor with the given arguments.
     *
     * @param message
     * @param cause
     */
    public NoMatchDimensionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with a dimension argument. It calls the exception constructor
     * with a message indicating the context of the exception.
     *
     * @param d1 dimension of the object evaluated when the exception was thrown
     */
    public NoMatchDimensionException(int d1) {
        super("No matching dimension exception thrown when using an object of "
                + "dimension " + d1);
    }

    /**
     * Constructor with dimensions. It calls the exception constructor with a
     * message indicating the context of the exception.
     *
     * @param d1 dimension of the first object
     * @param d2 dimension of the second object
     */
    public NoMatchDimensionException(int d1, int d2) {
        super("No matching dimension exception thrown when using objects of "
                + "dimension " + d1 + " and " + d2);
    }
}
