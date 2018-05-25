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
package es.ucm.fdi.usecases;

import es.ucm.fdi.business.exceptions.DuplicatedIDException;
import es.ucm.fdi.business.exceptions.NotFoundException;
import es.ucm.fdi.business.users.SessionDTO;
import es.ucm.fdi.business.users.UserDTO;
import es.ucm.fdi.business.users.UserManagerAS;
import es.ucm.fdi.business.util.HashGenerator;
import es.ucm.fdi.integration.users.UserDAOHashTableImp;
import java.security.AccessControlException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Use case test: registering a new user in the system. This test could be taken
 * as an integration test using a UserManagerAS. More information on how
 * UserManagerAS handles the registration process can be found on UserManagerAS
 * test class:
 *
 * @see UserManagerAS
 * @author Inmaculada Pérez
 */
public class LoginUCTest {
    private static final String USERNAME = "Mathew";
    private static final String PASSWORD = "1234";
    private UserDAOHashTableImp database;
    private HashGenerator hashGenerator;
    private UserManagerAS manager;
    private static final String INVALID_USERNAME = "James";
    private static final String INVALID_PASSWORD = "lemon1234";
    private UserDTO user;
    
    @Before
    public void initialize(){
        database = new UserDAOHashTableImp();
        hashGenerator = new HashGenerator();
        user = new UserDTO(USERNAME,
                hashGenerator.hash(PASSWORD.toCharArray()));
        database.addUser(user);
        manager = UserManagerAS.getManager(database);
    }
    
    @Test
    public void correctLoginUCTest(){
        System.out.println("Use case valid login test");  
        SessionDTO session = null;
        try {
            session = manager.login(USERNAME, PASSWORD);
        } catch (NotFoundException | AccessControlException ex) {
            fail("Unexpected exception thrown at login test");
        }
        assertTrue("The new user has not correctly logged in", manager.validateSession(session));
    }
    
    @Test
    public void incorrectLoginUCTest(){
        System.out.println("Use case invalid login test");  
        SessionDTO session = null;
        try {
            session = manager.login(INVALID_USERNAME, INVALID_PASSWORD);
            fail("Not expected to arrive here!");
        } catch (NotFoundException | AccessControlException ex) {
            // Expected
        }
        assertEquals("The new user has correctly logged in", null, session); 
    }
}
