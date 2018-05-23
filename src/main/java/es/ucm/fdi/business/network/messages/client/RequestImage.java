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
 * A RequestImage object represents a package received from the client. This class
 * extends from ClientMessage and adds a new field function containing the string
 * describing the function provided by the client. 
 * @see ClientMessage
 * @author Arturo Acuaviva
 */
public class RequestImage extends ClientMessage implements Serializable {
    
    /**
     * A description of the function to be drawn up
     */
    public String function;
    
    /**
     * Class constructor. A RequestImage constructor calls the super class 
     * constructor using REQUEST_IMAGE type as an argument. 
     * @see ClientMessage
     */
    public RequestImage(){
        super(ClientMessage.REQUEST_IMAGE);
    }
}
