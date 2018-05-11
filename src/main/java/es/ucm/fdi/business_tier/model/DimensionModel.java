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
package es.ucm.fdi.business_tier.model;

import es.ucm.fdi.business_tier.model.observer.DimensionObservable;
import es.ucm.fdi.integration_tier.connectivity.AuthorshipDAOHashTableImp;
import es.ucm.fdi.integration_tier.connectivity.CommentDAOHashTableImp;
import es.ucm.fdi.integration_tier.connectivity.ShareManagerAS;
import es.ucm.fdi.integration_tier.connectivity.SharedProjectDAOHashTableImp;
import es.ucm.fdi.business_tier.exceptions.NotFoundException;
import es.ucm.fdi.integration_tier.users.UserDAOHashTableImp;
import es.ucm.fdi.integration_tier.users.UserManagerAS;
import es.ucm.fdi.integration_tier.users.SessionBO;
import es.ucm.fdi.integration_tier.project.ProjectTransfer;
import java.security.AccessControlException;

/**
 *
 * @author Arturacu
 */
public class DimensionModel extends DimensionObservable {

    // Databases
    private final UserDAOHashTableImp userDB = new UserDAOHashTableImp();
    private final SharedProjectDAOHashTableImp sharedprojectDB = new SharedProjectDAOHashTableImp();
    private final AuthorshipDAOHashTableImp authorshipDB = new AuthorshipDAOHashTableImp();
    private final CommentDAOHashTableImp commentsDB = new CommentDAOHashTableImp();
    private final UserManagerAS userManager;
    private final ShareManagerAS projectManager;
    private SessionBO currentSession = null;
    private ProjectTransfer currentProject = null;

    /**
     * Class constructor. We don't have an external database, this model creates
     * its own volatile database each time it is created
     */
    public DimensionModel() {
        projectManager = ShareManagerAS.getManager(sharedprojectDB, authorshipDB);
        userManager = UserManagerAS.getManager(userDB);
    }

    /**
     * Implements the login of a user.
     *
     * @param username
     * @param password
     */
    public void login(String username, String password) {
        try {
            currentSession = userManager.login(username, password);
        } catch (AccessControlException | NotFoundException e) {
            notifyException(e);
        }
        notifyLogin(username);
    }

    /**
     * Implements the logout of the session.
     */
    public void logout() {
        if (currentSession != null) {
            userManager.logout(currentSession);
            notifyLogout(currentSession.getUser());
        } else {
            notifyException(new NotFoundException("No session was available!"));
        }
    }

    /**
     * Adds a comment to the project.
     *
     * @param projID
     * @param message
     */
    public void commentProject(String projID, String message) {
        try {
            // projectManager.comment(commentsDB, projID, currentSession, message);
        } catch (AccessControlException | NotFoundException e) {
            this.notifyObservers(e);
        }
    }

}
