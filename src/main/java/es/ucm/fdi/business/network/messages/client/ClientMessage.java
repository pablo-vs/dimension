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
package es.ucm.fdi.business.network.messages.client;

/**
 * Abstract package received from a client. A ClientMessage defines a package
 * that a client sends, the type of the message is an integer value. A message
 * could contain an internal string msg or relegate to job of defining the
 * values contained to another class which extends this.
 *
 * @author Arturo Acuaviva
 */
public abstract class ClientMessage {

    /**
     * Type requesting login package to the server
     */
    public static final int REQUEST_LOGIN = 1;
    /**
     * Type requesting logout package from the server
     */
    public static final int REQUEST_LOGOUT = 2;
    /**
     * Type requesting new account package to access to the server
     */
    public static final int REQUEST_REGISTER = 3;
    /**
     * Type requesting new function image package from the server
     */
    public static final int REQUEST_IMAGE = 25;
    /**
     * Type of the package message
     */
    private final int type;
    /**
     * Message in the package
     */
    private final String msg;

    /**
     * Class constructor specifying the type of package message. The message in
     * the package is set up as an empty message.
     *
     * @param type
     */
    public ClientMessage(int type) {
        this.type = type;
        this.msg = "";
    }

    /**
     * Class constructor specifying the type of package message and the message.
     *
     * @param type of package message
     * @param msg message saved in the package
     */
    public ClientMessage(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    /**
     * Getter method
     *
     * @return returns the type of the package message.
     */
    public int getType() {
        return type;
    }

    /**
     * Getter method
     *
     * @return returns the message in the package message.
     */
    public String getMessage() {
        return msg;
    }

}
