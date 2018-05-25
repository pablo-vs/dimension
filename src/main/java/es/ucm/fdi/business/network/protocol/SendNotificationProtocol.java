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
 * SendNotificationProtocol provides a way of dealing with notifications request
 * from a client. Typically this user is an admin. Given a UserManagerAS
 * (Application Service) a SendNotificationProtocol tries to parse a received
 * message. If the message is NOTIFY_USER type, then the Protocol is applied by
 * checking if the session of the user is on, if the user type matches the rank
 * required to apply the notification and if the user name in the request exists
 * in the database. In case these statements are true, the protocol returns true
 * if they don't instead a false statement is returned.
 *
 * @see UserManagerAS
 * @see Protocol
 * @author Arturo Acuaviva
 */
public class SendNotificationProtocol implements Protocol {

    /**
     * Application Service to manage notifications in the system.
     */
    private final UserManagerAS manager;

    /**
     * Session from the user who logged in
     */
    private final SessionDTO session;

    /**
     * Class constructor. A SendNotificationProtocol is built by providing a
     * UserManagerAS and a client active session.
     *
     * @param manager the Application Service the protocol will apply
     * @param session
     */
    public SendNotificationProtocol(UserManagerAS manager, SessionDTO session) {
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
