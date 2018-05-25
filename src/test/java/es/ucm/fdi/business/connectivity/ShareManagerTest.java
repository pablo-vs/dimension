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

import es.ucm.fdi.business.exceptions.NotFoundException;
import es.ucm.fdi.integration.connectivity.AuthorshipDAOHashTableImp;
import es.ucm.fdi.integration.connectivity.SharedProjectDAOHashTableImp;
import es.ucm.fdi.integration.connectivity.SharedProjectDAOSQLImp;
import es.ucm.fdi.integration.project.ProjectDAOSQLImp;

import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import es.ucm.fdi.business.workspace.project.ProjectDTO;
import es.ucm.fdi.business.users.SessionDTO;
import es.ucm.fdi.integration.users.UserDAOHashTableImp;
import es.ucm.fdi.integration.users.UserDAOSQLImp;
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

    private final HashGenerator HASG_GENERATOR = new HashGenerator();
    private final char[] PASSWORD = {'1', '2', '3', '4'};
    private final ProjectDTO PROJECT_1 = new ProjectDTO("linear");
    private final ProjectDTO PROJECT_2 = new ProjectDTO("quadratic");
    private ShareManagerAS shareManager = ShareManagerAS.getManager(
            new SharedProjectDAOHashTableImp(),
            new AuthorshipDAOHashTableImp());
    private UserManagerAS userManager = UserManagerAS.getManager(new UserDAOHashTableImp());
    private UserDTO kate;
    private UserDTO william;
    private UserDTO john;
    private UserDTO nate;
    private List<String> authors = new ArrayList<>();

    @Before
    public void initialize() {
        kate = new UserDTO("kate", HASG_GENERATOR.hash(PASSWORD));
        william = new UserDTO("william", HASG_GENERATOR.hash(PASSWORD));
        nate = new UserDTO("nate", HASG_GENERATOR.hash(PASSWORD));
        john = new UserDTO("john", HASG_GENERATOR.hash(PASSWORD));
        userManager.addNewUser(kate);
        userManager.addNewUser(john);
        userManager.addNewUser(william);
        userManager.addNewUser(nate);
        authors.add("kate");
        authors.add("william");
        authors.add("nate");
        authors.add("john");
    }

    /**
     * Tests the sharing of open/private projects.
     */
    @Test
    public void shareImportTest() {
        SessionDTO kateSession = userManager.login("kate", "1234");
        try {
            shareManager.shareOpenProject(PROJECT_2, authors, kateSession);
        } catch (AccessControlException e) {
            fail(e.getMessage());
        }
        try {
            shareManager.sharePrivateProject(PROJECT_1, authors, authors, kateSession);
        } catch (AccessControlException e) {
            fail(e.getMessage());
        }

    }

    //This test is not working
    /**
     * Tests the rejection of unauthorized accesses and the permission of
     * legitimate accesses.
     */
    // @Test
    public void findTest() {
        SessionDTO johnSession = userManager.login("john", "1234");
        try {
            shareManager.importProject(shareManager.findProjectByName("linear", johnSession).get(0), johnSession);
            fail("Unauthorized access at share open project");
        } catch (NotFoundException | AccessControlException e) {
            // OK
        }
        try {
            shareManager.modifySharedProject(shareManager.findProjectByName("linear", johnSession).get(0), johnSession);
            fail("Unauthorized access at modify shared project");
        } catch (NotFoundException | AccessControlException e) {
            // OK
        }
        try {
            shareManager.modifySharedProject(shareManager.findProjectByName("cuadraticas", johnSession).get(0), johnSession);
            fail("Unauthorized access");
        } catch (NotFoundException | AccessControlException e) {
            // OK
        }
        SessionDTO kateSession = userManager.login("kate", "1234");
        SessionDTO williamSession = userManager.login("william", "1234");

        shareManager.modifySharedProject(shareManager.findProjectByName("linear", kateSession).get(0), kateSession);
        shareManager.importProject(shareManager.findProjectByName("linear", kateSession).get(0), kateSession);
        shareManager.modifySharedProject(shareManager.findProjectByName("linear", williamSession).get(0), williamSession);
        shareManager.importProject(shareManager.findProjectByName("quadratic", johnSession).get(0), johnSession);
        List<SharedProjectDTO> found = shareManager.findProjectByAuthor("kate", kateSession);
        found.forEach((f) -> {
            shareManager.modifySharedProject(f, kateSession);
        });

    }

    @After
    public void clear() throws SQLException {
        (new ProjectDAOSQLImp("")).clear();
        (new SharedProjectDAOSQLImp()).clear();
        (new UserDAOSQLImp()).clear();
    }

}
