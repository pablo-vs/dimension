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
package es.ucm.fdi.integration.connectivity;

import es.ucm.fdi.integration.connectivity.AuthorshipDAOHashTableImp;
import es.ucm.fdi.business.connectivity.AuthorshipDTO;
import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit test for AuthorshipDAOHashTableImp class.
 *
 * @see AuthorshipDAOHashTableImp
 * @author Javier Galiana
 */
public class AuthorshipDAOHashTableImpTest {

    @Test
    public void AuthorDAOHashTableTest() {
        AuthorshipDAOHashTableImp dao = new AuthorshipDAOHashTableImp();
        AuthorshipDTO auth1 = new AuthorshipDTO("paco", "superProyecto");
        AuthorshipDTO auth2 = new AuthorshipDTO("pepe", "superProyecto");
        AuthorshipDTO auth3 = new AuthorshipDTO("paco", "proyectoMediocre");
        ArrayList<AuthorshipDTO> results1 = new ArrayList<>(),
                results2 = new ArrayList<>(),
                results3 = new ArrayList<>();
        results1.add(auth1);
        results1.add(auth3);
        results2.add(auth2);
        results2.add(auth1);
        results3.add(auth2);
        results3.add(auth1);
        results3.add(auth3);

        dao.addAuthorship(auth1);
        dao.addAuthorship(auth2);
        dao.addAuthorship(auth3);

        assertEquals("Invalid user search results", results1, dao.findByUser("paco"));
        assertEquals("Invalid project search results", results2, dao.findByProject("superProyecto"));
        assertEquals("The Authorships cannot be loaded", results3, dao.getAuthorships());

        dao.removeAuthorship(auth1);
        dao.removeAuthorship(auth2);
        dao.removeAuthorship(auth3);
    }
}
