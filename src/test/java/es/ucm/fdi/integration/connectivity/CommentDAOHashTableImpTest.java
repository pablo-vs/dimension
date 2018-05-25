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

import org.junit.Test;

import es.ucm.fdi.business.connectivity.CommentDTO;

import java.sql.Date;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * JUnit test for CommentDAOHashTableImp class.
 *
 * @see CommentDAOHashTableImp
 * @author Javier Galiana
 */
public class CommentDAOHashTableImpTest {

    @Test
    public void CommentDAOHashTableTest() {
        CommentDAOHashTableImp dao = new CommentDAOHashTableImp();
        CommentDTO comm1 = new CommentDTO("juan", "superProyecto", "Me gusta tu superProyecto", new Date(18, 5, 17));
        CommentDTO comm2 = new CommentDTO("maria", "superProyecto", "Me gusta tu superProyecto", new Date(18, 5, 17));
        CommentDTO comm3 = new CommentDTO("juan", "proyectoMediocre", "No me gusta tu proyectoMediocre", new Date(18, 4, 12));
        ArrayList<CommentDTO> results1 = new ArrayList<>(),
                results2 = new ArrayList<>(),
                results3 = new ArrayList<>();
        results1.add(comm1);
        results1.add(comm3);
        results2.add(comm1);
        results2.add(comm2);
        results3.add(comm1);
        results3.add(comm2);
        results3.add(comm3);

        dao.addComment(comm1);
        dao.addComment(comm2);
        dao.addComment(comm3);

        assertEquals("Invalid user search results", results1, dao.findByUser("juan"));
        assertEquals("Invalid project search results", results2, dao.findByProject("superProyecto"));
        assertEquals("The Comments cannot be obtained", results3, dao.getComments());

        dao.removeComment(comm1);
        dao.removeComment(comm2);
        dao.removeComment(comm3);
    }
}
