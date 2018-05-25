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
import es.ucm.fdi.business.users.UserManagerAS;

/**
 * RegisterProtocol provides a way of dealing with register request packages
 * sent by a client. Given a UserManagerAS, RegisterProtocol tries to parse the
 * ClientMessage received by checking if the package contains the fields
 * required to register a new user in the system. If the package is incorrectly
 * created or does not match a request register package type, a
 * ProtocolException is thrown. If the register proccess is carried out
 * correctly because a correct package was provided, the method apply should
 * return a true statement. If the register proccess cannot be carried out due
 * to incorrect fields filling, the method apply return a false statement.
 *
 * @author Arturacu
 */
public final class RegisterProtocol implements Protocol {

    /**
     * Application Service to manage the users in the system.
     */
    private final UserManagerAS manager;

    /**
     * Class constructor. A RegisterProtocol needs an Application Service to
     * manage the users in the system.
     *
     * @param manager
     */
    public RegisterProtocol(UserManagerAS manager) {
        this.manager = manager;
    }

    /**
     * Not implemented yet.
     *
     * @param msg
     * @return
     * @throws ProtocolException
     */
    @Override
    public boolean apply(ClientMessage msg) throws ProtocolException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
