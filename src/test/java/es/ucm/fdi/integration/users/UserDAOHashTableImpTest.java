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
package es.ucm.fdi.integration.users;

import es.ucm.fdi.integration.users.UserDAOHashTableImp;
import es.ucm.fdi.business.users.UserDTO;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.ArrayList;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * JUnit test for UserDAOHashTableImp class.
 *
 * @see UserDAOHashTableImp
 * @author Javier Galiana
 */
public class UserDAOHashTableImpTest {

    @Test
    public void UserDAOHashTableTest() {

        UserDAOHashTableImp dao = new UserDAOHashTableImp();
        UserDTO user1 = new UserDTO("paco", "clavesecreta");
        UserDTO user2 = new UserDTO("pepe", "password");
        UserDTO user3 = new UserDTO("marta", "nadielavaaadivinar");
        ArrayList<UserDTO> results1 = new ArrayList<>();

        results1.add(user3);
        results1.add(user1);
        results1.add(user2);

        dao.addUser(user1);
        dao.addUser(user2);
        dao.addUser(user3);

        assertEquals("Invalid user search results", user1, dao.findUser("paco"));
        assertEquals("The Users cannot be loaded", results1, dao.getUsers());

        Period newBanTime = Period.between(LocalDate.of(2018, Month.DECEMBER, 9), LocalDate.now());
        user1.setBanTime(newBanTime);;

        dao.modifyUser(user1);

        assertEquals("The user found is not the expected", user1, dao.findUser(user1.getID()));

        dao.removeUser(user1.getID());
        dao.removeUser(user2.getID());
        dao.removeUser(user3.getID());
    }
}
