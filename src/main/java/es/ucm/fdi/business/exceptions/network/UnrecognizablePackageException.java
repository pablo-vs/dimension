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
package es.ucm.fdi.business.exceptions.network;

/**
 * This exception is thrown whenever an error occurred while reading a package.
 * The exception is thrown so as to warm about a not valid package received. 
 * @author Arturo Acuaviva Huertos
 */
public class UnrecognizablePackageException extends Exception {

    /**
     * Simple constructor. It calls the exception constructor indicating in the
     * message that the exception has been thrown.
     */
    public UnrecognizablePackageException() {
        super("Error while reading package!");
    }

    /**
     * Constructor with a context message. It calls the exception constructor
     * with the given message.
     *
     * @param message
     */
    public UnrecognizablePackageException(String message) {
        super(message);
    }

    /**
     * Constructor with cause. It calls the exception constructor indicating in
     * the message that the exception has been thrown and the cause.
     *
     * @param cause
     */
    public UnrecognizablePackageException(Throwable cause) {
        super("Error while reading package!", cause);
    }

    /**
     * Constructor with a context message and a cause. It calls the exception
     * constructor with the given arguments.
     *
     * @param message
     * @param cause
     */
    public UnrecognizablePackageException(String message, Throwable cause) {
        super(message, cause);
    }

}
