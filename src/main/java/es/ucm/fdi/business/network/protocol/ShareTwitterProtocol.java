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
import es.ucm.fdi.business.network.operations.twitter.TwitterAS;
import es.ucm.fdi.business.users.SessionDTO;

/**
 * ShareTwitterProtocol provides a way of dealing with tweeting request from a
 * client. Given a TwitterAS (Application Service) a ShareTwitterProtocol tries
 * to parse a received message. If the message is SHARE_TWITTER type, then the
 * Protocol is applied by checking if the session is on and the user has an
 * active twitter account In case these match the protocol returns true, if they
 * don't instead a false statement is returned.
 *
 * @see TwitterAS
 * @see Protocol
 * @author Arturo Acuaviva
 */
public class ShareTwitterProtocol implements Protocol {

    /**
     * Application Service to manage the twitter accounts in the system.
     */
    private final TwitterAS manager;

    /**
     * Session from the user who logged in
     */
    private final SessionDTO session;

    /**
     * Class constructor. A ShareTwitterProtocol is built by providing a
     * TwitterAS and an active session.
     *
     * @param manager the Application Service the protocol will apply
     * @param session
     */
    public ShareTwitterProtocol(TwitterAS manager, SessionDTO session) {
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
