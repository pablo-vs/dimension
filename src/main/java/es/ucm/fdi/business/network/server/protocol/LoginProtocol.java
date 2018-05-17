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
package es.ucm.fdi.business.network.server.protocol;

import es.ucm.fdi.business.exceptions.NotFoundException;
import es.ucm.fdi.business.exceptions.network.ProtocolException;
import es.ucm.fdi.business.network.server.codes.ClientMessages;
import es.ucm.fdi.business.users.UserManagerAS;
import java.security.AccessControlException;

/**
 *
 * @author Arturacu
 */
public final class LoginProtocol implements Protocol {

    private static UserManagerAS manager;
    
    
    //Instead, the DAO database should be given as a parameter to initialize the manager
    public LoginProtocol(){
     //
     manager = null;
    }
    
    /**
     *
     * @param msg
     * @return
     * @throws ProtocolException
     */
    @Override
    public boolean apply(ClientMessages msg) throws ProtocolException {
        boolean result = false;
        if(msg == null || msg.field1 == null || msg.field2 == null ||
           msg.field1.isEmpty() || msg.field2.isEmpty()){
            throw new ProtocolException("Error while client login protocol,"
                    + " non valid package received");
        }
        if(msg.getType() == ClientMessages.LOG_IN){
            try{
                manager.login(msg.field1, msg.field2);
                result = true; //Login successfully 
            } catch(AccessControlException | NotFoundException e){
                // The user was not in the database or the information given
                // about the username and password was incorrect
            }
            
        }
        return result;
    }

}
