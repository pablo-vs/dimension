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

package es.ucm.fdi.model;

import es.ucm.fdi.model.observer.DimensionObservable;
import es.ucm.fdi.connectivity.AuthorshipDAOHashTableImp;
import es.ucm.fdi.connectivity.CommentDAOHashTableImp;
import es.ucm.fdi.connectivity.ShareManagerAS;
import es.ucm.fdi.connectivity.SharedProjectDAOHashTableImp;
import es.ucm.fdi.exceptions.NotFoundException;
import es.ucm.fdi.users.UserDAOHashTableImp;
import es.ucm.fdi.users.UserManagerAS;
import es.ucm.fdi.users.SessionBO;
import es.ucm.fdi.workspace.project.ProjectTO;
import java.security.AccessControlException;

/**
 *
 * @author Arturacu
 */
public class DimensionModel extends DimensionObservable {
    
    // Databases
    private final UserDAOHashTableImp userDB;
    private final SharedProjectDAOHashTableImp sharedprojectDB ;
    private final AuthorshipDAOHashTableImp authorshipDB;
    private final CommentDAOHashTableImp commentsDB;
    private final UserManagerAS userManager;
    private final ShareManagerAS projectManager;
    private SessionBO currentSession = null;
    private ProjectTO currentProject = null;
    
    public DimensionModel(){
        // we don't have an external database, this model creates 
        // its own volatile database each time it is created
        userDB = new UserDAOHashTableImp(); 
        sharedprojectDB = new SharedProjectDAOHashTableImp();
        authorshipDB = new AuthorshipDAOHashTableImp();
        commentsDB = new CommentDAOHashTableImp();
        
        
        projectManager = ShareManagerAS.getManager(sharedprojectDB, authorshipDB);
        userManager = UserManagerAS.getManager(userDB);
    }
    
    public void login(String username, String password){
        try{
            currentSession = userManager.login(username, password);
        } catch(AccessControlException | NotFoundException e){
            notifyException(e);
        }
        notifyLogin(username);
    }
    
    public void logout(){
        if(currentSession != null){
            userManager.logout(currentSession);
            notifyLogout(currentSession.getUser());
        } else{
            notifyException(new NotFoundException("No session was available!"));
        } 
    }
    
    
    public void commentProject(String projID, String message){
        try{
            // projectManager.comment(commentsDB, projID, currentSession, message);
        } catch(AccessControlException | NotFoundException e){
            this.notifyObservers(e);
        }
    }
   
    
    
}