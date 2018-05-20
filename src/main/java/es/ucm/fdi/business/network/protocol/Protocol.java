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

package es.ucm.fdi.business.network.protocol;

import es.ucm.fdi.business.exceptions.network.ProtocolException;
import es.ucm.fdi.business.network.messages.client.ClientMessage;

/**
 * Protocol defines an interface to apply whenever a message is received from
 * a client. A Protocol class should implements a method apply which receives a 
 * message and tries to parse it, then it updates the state of the app using
 * different application services provided. Whenever a Protocol cannot be applied
 * correctly due to errors in the message ProtocolException is thrown. 
 * @see ClientMessage
 * @see ProtocolException
 * @author Arturo Acuaviva
 */
public interface Protocol {
    
    /**
     * Applies the current protocol. The method receives a ClientMessage
     * and it updates the state of the app using an application service.
     * @param msg Client package message providing information
     * @return true whenever the protocol was applied as aspected given a message
     * @throws ProtocolException whenever the protocol cannot be applied due to 
     * errors in the message. 
     */
    boolean apply(ClientMessage msg) throws ProtocolException;
    
}
