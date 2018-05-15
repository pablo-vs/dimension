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
package es.ucm.fdi.integration.exceptions;

/**
 * Controls the exceptions thrown associated with a DAO Error.
 *
 * @author Inmaculada Pérez
 */
public class DAOErrorException extends RuntimeException {

    /**
     * Serial version
     */
    static final long serialVersionUID = 1003L;

    /**
     *
     * Class constructor specifying error message and exception.
     *
     * @param message Error message
     * @param e Exception
     */
    public DAOErrorException(String message, Throwable e) {
        super(message, e);
    }

    /**
     * Class constructor specifying error message.
     *
     * @param message Error message
     */
    public DAOErrorException(String message) {
        super(message);
    }
}
