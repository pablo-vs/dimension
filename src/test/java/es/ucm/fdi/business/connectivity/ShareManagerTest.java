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
import es.ucm.fdi.integration.connectivity.SharedProjectDAOSQLImp;
import es.ucm.fdi.integration.project.ProjectDAOHashTableImp;
import es.ucm.fdi.integration.project.ProjectDAOSQLImp;

import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

import es.ucm.fdi.business.workspace.project.ProjectDTO;
import es.ucm.fdi.business.users.SessionDTO;
import es.ucm.fdi.integration.users.UserDAOHashTableImp;
import es.ucm.fdi.integration.users.UserDAOSQLImp;
import es.ucm.fdi.business.users.UserManagerAS;
import es.ucm.fdi.business.users.UserDTO;
import es.ucm.fdi.business.util.HashGenerator;

public class ShareManagerTest {

    @Test
    public void shareImportTest() {
        HashGenerator hg = new HashGenerator();
        ShareManagerAS shareMgr = ShareManagerAS.getManager(
                new SharedProjectDAOHashTableImp(),
                new AuthorshipDAOHashTableImp());
        UserManagerAS userMgr = UserManagerAS.getManager(new UserDAOHashTableImp());
        char[] password = {'1', '2', '3', '4'};
        UserDTO user = new UserDTO("pepe", hg.hash(password));
        userMgr.addNewUser(user);
        UserDTO luis = new UserDTO("luis", hg.hash(password));
        UserDTO paco = new UserDTO("paco", hg.hash(password));
        userMgr.addNewUser(luis);
        userMgr.addNewUser(paco);
        List<String> authors = new ArrayList<String>();
        authors.add("pepe");
        authors.add("luis");
        authors.add("paco");
        SessionDTO session = userMgr.login("pepe", "1234");
        ProjectDTO project = new ProjectDTO("lineales");
        ProjectDTO project2 = new ProjectDTO("cuadraticas");
        // test the sharing of open projects
        try {
            shareMgr.shareOpenProject(project2, authors, session);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        // test the sharing of private projects
        try {
            shareMgr.sharePrivateProject(project, authors, authors, session);
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void findTest() {
        HashGenerator hg = new HashGenerator();
        char[] password = {'1', '2', '3', '4'};
        UserManagerAS userMgr = UserManagerAS.getManager(new UserDAOHashTableImp());
        ShareManagerAS shareMgr = ShareManagerAS.getManager(new SharedProjectDAOHashTableImp(), new AuthorshipDAOHashTableImp());
        UserDTO juan = new UserDTO("juan", hg.hash(password));
        userMgr.addNewUser(juan);
        SessionDTO juanSession = userMgr.login("juan", "1234");

        //test the rejection of unauthorised accesses
        try {
            shareMgr.importProject(shareMgr.findProjectByName("lineales", juanSession).get(0), juanSession);
            fail("Unauthorised access");
        } catch (Exception e) {
            //correcto
        }
        try {
            shareMgr.modifySharedProject(shareMgr.findProjectByName("lineales", juanSession).get(0), juanSession);
            fail("Unauthorised access");
        } catch (Exception e) {
            //correcto
        }
        try {
            shareMgr.modifySharedProject(shareMgr.findProjectByName("cuadraticas", juanSession).get(0), juanSession);
            fail("Unauthorised access");
        } catch (Exception e) {
            //correcto
        }
        SessionDTO session = userMgr.login("pepe", "1234");
        SessionDTO luis = userMgr.login("luis", "1234");

        //test the permission of legitimate accesses
        shareMgr.modifySharedProject(shareMgr.findProjectByName("lineales", session).get(0), session);
        shareMgr.importProject(shareMgr.findProjectByName("lineales", session).get(0), session);
        shareMgr.modifySharedProject(shareMgr.findProjectByName("lineales", luis).get(0), luis);
        shareMgr.importProject(shareMgr.findProjectByName("cuadraticas", juanSession).get(0), juanSession);
        List<SharedProjectDTO> found = shareMgr.findProjectByAuthor("pepe", session);
        for (SharedProjectDTO f : found) {
            shareMgr.modifySharedProject(f, session);
        }

    }

    @After
    public void clear() throws SQLException {
        (new ProjectDAOSQLImp("")).clear();
        (new SharedProjectDAOSQLImp()).clear();
        (new UserDAOSQLImp()).clear();
    }

}
