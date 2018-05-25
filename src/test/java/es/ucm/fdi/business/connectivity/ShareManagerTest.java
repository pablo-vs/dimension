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
package es.ucm.fdi.business.connectivity;

import es.ucm.fdi.integration.connectivity.AuthorshipDAOHashTableImp;
import es.ucm.fdi.integration.connectivity.SharedProjectDAOHashTableImp;

import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import es.ucm.fdi.business.workspace.project.ProjectDTO;
import es.ucm.fdi.business.workspace.project.ProjectManagerAS;
import es.ucm.fdi.business.users.SessionDTO;
import es.ucm.fdi.integration.users.UserDAOHashTableImp;
import es.ucm.fdi.business.users.UserManagerAS;
import es.ucm.fdi.business.users.UserDTO;
import es.ucm.fdi.business.util.HashGenerator;
import java.security.AccessControlException;

/**
 * JUnit test for ShareManager class.
 *
 * @see ShareManager
 * @author Inmaculada Pérez
 */
public class ShareManagerTest {

    private static final HashGenerator HASH_GENERATOR = new HashGenerator();
    private static final char[] PASSWORD = {'1', '2', '3', '4'};
    private static final ProjectDTO PROJECT_1 = new ProjectDTO("linear");
    private static final ProjectDTO PROJECT_2 = new ProjectDTO("quadratic");
    private static ShareManagerAS shareManager = ShareManagerAS.getManager(
            new SharedProjectDAOHashTableImp(),
            new AuthorshipDAOHashTableImp());
    private static UserManagerAS userManager = UserManagerAS.getManager(new UserDAOHashTableImp());
    private static UserDTO kate;
    private static UserDTO william;
    private static UserDTO john;
    private static UserDTO nate;
    private static SessionDTO kateSession;
    private static SessionDTO johnSession;
    private static SessionDTO nateSession;
    private static SessionDTO williamSession;
    private static List<String> authors = new ArrayList<>();

    @BeforeClass
    public static void initialize() {
        kate = new UserDTO("kate", HASH_GENERATOR.hash(PASSWORD));
        william = new UserDTO("william", HASH_GENERATOR.hash(PASSWORD));
        nate = new UserDTO("nate", HASH_GENERATOR.hash(PASSWORD));
        john = new UserDTO("john", HASH_GENERATOR.hash(PASSWORD));
        userManager.addNewUser(kate);
        userManager.addNewUser(john);
        userManager.addNewUser(william);
        userManager.addNewUser(nate);
        authors.add("kate");
        authors.add("william");
        authors.add("nate");
        kateSession = userManager.login("kate", "1234");
        johnSession = userManager.login("john", "1234");
        nateSession = userManager.login("nate", "1234");
        williamSession = userManager.login("william", "1234");
    }

    /**
     * Tests the sharing of open/private projects.
     */
    @Test
    public void shareImportTest() {
    	
    	//Share
        try {
            shareManager.shareOpenProject(PROJECT_1, authors, kateSession);
        } catch (AccessControlException e) {
            fail(e.getMessage());
        }
        try {
            shareManager.sharePrivateProject(PROJECT_2, authors, authors, kateSession);
        } catch (AccessControlException e) {
            fail(e.getMessage());
        }
        
        ProjectManagerAS projMan = new ProjectManagerAS("john");
        ProjectManagerAS projMan2 = new ProjectManagerAS("nate");
        
        //Import
        shareManager.importProject(shareManager.findProjectByName("linear", johnSession).get(0), johnSession);
        assertTrue("Incorrect imported project", PROJECT_1.equals(projMan.openProject("linear", johnSession)));
        
        shareManager.importProject(shareManager.findProjectByName("quadratic", nateSession).get(0), nateSession);
        assertTrue("Incorrect imported project", PROJECT_2.equals(projMan2.openProject("quadratic", nateSession)));
        
        
        //Illegal access
        try {
            shareManager.modifySharedProject(shareManager.findProjectByName("linear", johnSession).get(0), johnSession);
            fail("Unauthorized access at modify shared project");
        } catch (AccessControlException e) {
            // OK
        }
        
        projMan.removeProject("linear", johnSession);
        projMan2.removeProject("quadratic", nateSession);
    }

    /**
     * Tests project finding
     */
    @Test
    public void findTest() {
    	shareManager.shareOpenProject(PROJECT_1, authors, kateSession);
    	shareManager.sharePrivateProject(PROJECT_2, authors, authors, kateSession);
    	
        assertTrue(PROJECT_1.equals(shareManager.findProjectByName("linear", williamSession).get(0)));
        
        assertTrue(PROJECT_1.equals(shareManager.findProjectByAuthor("kate", williamSession).get(0)));
        assertTrue(PROJECT_2.equals(shareManager.findProjectByAuthor("kate", williamSession).get(1)));
        
        assertTrue("Found private projects!", shareManager.findProjectByName("quadratic", johnSession).isEmpty());
        
        List<SharedProjectDTO> found = shareManager.findProjectByAuthor("kate", kateSession);
        found.forEach((f) -> {
            shareManager.modifySharedProject(f, kateSession);
        });
        
        
    }

    @AfterClass
    public static void clear() throws SQLException {
    	shareManager.removeProject("linear", kateSession);
        shareManager.removeProject("quadratic", kateSession);
    	
    	userManager.removeUser("kate", kateSession);
    	userManager.removeUser("john", johnSession);
    	userManager.removeUser("nate", nateSession);
    	userManager.removeUser("william", williamSession);
        userManager.logout(kateSession);
        userManager.logout(johnSession);
        userManager.logout(nateSession);
        userManager.logout(williamSession);
    }

}
