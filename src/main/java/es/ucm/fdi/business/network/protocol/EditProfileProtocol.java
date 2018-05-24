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
import es.ucm.fdi.business.users.SessionDTO;
import es.ucm.fdi.business.users.UserManagerAS;

/**
 * EditProfileProtocol provides a way of dealing with profile editing request
 * from a client. Given a UserManagerAS (Application Service) a
 * EditProfileProtocol tries to parse a received message. If the message is
 * EDIT_PROFILE type, then the Protocol is applied by checking if the session is
 * on and the filled up valid fields correctly. In case these statements are
 * true the protocol returns true, if they don't instead false is returned.
 *
 * @see UserManagerAS
 * @see Protocol
 * @author Arturo Acuaviva
 */
public class EditProfileProtocol implements Protocol {

    /**
     * Application Service to manage profiles of the users in the system.
     */
    private final UserManagerAS manager;

    /**
     * Session from the user who logged in
     */
    private final SessionDTO session;

    /**
     * Class constructor. A EditProfileProtocol is built by providing a
     * UserManagerAS.
     *
     * @param manager the Application Service the protocol will apply
     * @param session
     */
    public EditProfileProtocol(UserManagerAS manager, SessionDTO session) {
        this.manager = manager;
        this.session = session;
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
