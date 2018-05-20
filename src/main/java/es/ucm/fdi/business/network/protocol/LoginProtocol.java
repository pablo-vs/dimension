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

import es.ucm.fdi.business.exceptions.NotFoundException;
import es.ucm.fdi.business.exceptions.network.ProtocolException;
import es.ucm.fdi.business.network.messages.client.ClientMessage;
import es.ucm.fdi.business.network.messages.client.RequestLogin;
import es.ucm.fdi.business.users.SessionDTO;
import es.ucm.fdi.business.users.UserManagerAS;
import java.security.AccessControlException;

/**
 * LoginProtocol provides a way of dealing with login request from a client.
 * Given a UserManagerAS (Application Service) a LoginProtocol tries to parse a
 * received message. If the message is a REQUEST_LOGIN type, then the Protocol
 * is applied by checking if the username and the password matches the ones in
 * the database. In case they match the protocol returns true, if they don't
 * instead a false statement is returned.
 *
 * @see UserManagerAS
 * @see Protocol
 * @author Arturo Acuaviva
 */
public final class LoginProtocol implements Protocol {

    /**
     * Application Service to manage the users in the system.
     */
    private final UserManagerAS manager;

    /**
     * Session from the user who logged in
     */
    private SessionDTO session;

    /**
     * Class constructor. A LoginProtocol is built by providing a UserManagerAS.
     *
     * @param manager the Application Service the protocol will apply
     */
    public LoginProtocol(UserManagerAS manager) {
        this.manager = manager;
        session = null;
    }

    /**
     * Applies the protocol using a message package from the client. Given a
     * ClientMessage, this method checks whether or not the package matches a
     * request login packages and that the fields are correctly set up, if it
     * does not match the package type or fields are not filled up then a
     * ProtocolException is thrown. If the package is correctly created it is
     * check whether or not it contains the values for a current user to log-in,
     * using the ApplicationService to check if the user exists and the password
     * matches the one from the user.
     *
     * @param msg
     * @return true if the user can login with the provided information
     * @throws ProtocolException when package provided is not a request login
     * package
     */
    @Override
    public boolean apply(ClientMessage msg) throws ProtocolException {
        boolean result = false;
        if (msg == null || msg.getType() != ClientMessage.REQUEST_LOGIN) {
            throw new ProtocolException("Error while client login protocol,"
                    + " not a request login received");
        }
        if (((RequestLogin) msg).username == null
                || ((RequestLogin) msg).password == null
                || ((RequestLogin) msg).username.isEmpty()
                || ((RequestLogin) msg).password.isEmpty()) {
            throw new ProtocolException("Error while client login protocol,"
                    + " non valid login package received");
        }
        if (msg.getType() == ClientMessage.REQUEST_LOGIN) {
            try {
                session = manager.login(((RequestLogin) msg).username,
                        ((RequestLogin) msg).password);
                result = true; //Login successfully 
            } catch (AccessControlException | NotFoundException e) {
                // The user was not in the database or the information given
                // about the username and password was incorrect
            }

        }
        return result;
    }

    /**
     * Get the session if the user has logged in correctly otherwise returns
     * nulls.
     *
     * @return the session of the user or null if no user logged in
     */
    public SessionDTO getSession() {
        return session;
    }

}
