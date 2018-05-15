/**
 * This file is part of Dimension.
 * Dimension is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Dimension is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Dimension.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.business.users;

import es.ucm.fdi.integration.users.UserDAO;
import es.ucm.fdi.integration.users.UserDAOHashTableImp;
import java.time.ZonedDateTime;
import java.security.AccessControlException;

import org.junit.Test;
import static org.junit.Assert.*;

import es.ucm.fdi.business.util.HashGenerator;
import es.ucm.fdi.business.exceptions.NotFoundException;

public class UserManagerTest {

    @Test
    public void userManagementTest() {
        UserManagerAS userMgr = UserManagerAS.getManager(new UserDAOHashTableImp());
        HashGenerator hashgen = new HashGenerator();
        userMgr.newUser(new UserDTO("Peter", hashgen.hash("1234".toCharArray())));
        userMgr.newUser(new UserDTO("Tim", hashgen.hash("4321".toCharArray())));
        SessionDTO peterSession = userMgr.login("Peter", "1234");
        SessionDTO timSession = userMgr.login("Tim", "4321");
        try {
            userMgr.removeUser("Peter", timSession);
            fail("Illegal account access!");
        } catch (AccessControlException e) {
            //System.out.println("Authentication works");
        }

        userMgr.removeUser("Peter", peterSession);
        userMgr.logout(peterSession);
        try {
            peterSession = userMgr.login("Peter", "1234");
            fail("Logged in to nonexistent account!");
        } catch (NotFoundException e) {
            // OK
        }
    }

    @Test
    public void invalidSessionTest() {
        HashGenerator hashgen = new HashGenerator();
        UserDAO dao = new UserDAOHashTableImp();
        String passwd = hashgen.hash("1234".toCharArray());
        UserDTO user = new UserDTO("Peter", passwd);
        UserManagerAS userMgr = UserManagerAS.getManager(dao);
        userMgr.newUser(user);
        try {
            SessionDTO sesion = userMgr.login("Peter", "12345");
            fail("Session should have not begun.");
        } catch (AccessControlException e) {
            // OK
        }

        SessionDTO sesion = new SessionDTO("Peter", ZonedDateTime.now());
        assertFalse(userMgr.validateSession(sesion));
    }
}
