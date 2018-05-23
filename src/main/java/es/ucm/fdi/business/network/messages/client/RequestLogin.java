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

import java.io.Serializable;

/**
 * A RequestLogin object represents a package received from the client. The class
 * extends from ClientMessage and it adds two fields containing the password
 * and the username of the user requesting access to the server. 
 * @see ClientMessage
 * @author Arturo Acuaviva
 */
public class RequestLogin extends ClientMessage implements Serializable {
    /**
     * Username of the user requesting access
     */
    public String username;
     /**
     * Password of the user requesting access
     */
    public String password;
    
   /**
     * Class constructor. A RequestLogin constructor calls the super class 
     * constructor using REQUEST_LOGIN type as an argument. 
     * @see ClientMessage
     */
    public RequestLogin(){
        super(ClientMessage.REQUEST_LOGIN);
    }
    
}
