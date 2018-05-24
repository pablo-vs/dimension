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

import es.ucm.fdi.integration.connectivity.CommentDAOSQLImp;
import es.ucm.fdi.business.connectivity.CommentDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

import org.junit.Test;
import static org.junit.Assert.*;

public class CommentDAOSQLTest {

    @Test
    public void CommentDAOTest() throws SQLException, ClassNotFoundException {
        CommentDAOSQLImp dao = new CommentDAOSQLImp();
        Date date1 = new Date(18, 5, 12), date2 = new Date(18, 5, 12), date3 = new Date(18, 4, 12), date4 = new Date(18, 4, 20);
        CommentDTO comment1 = new CommentDTO("paco", "superProyecto", "Este es mi proyecto", date1);
        CommentDTO comment2 = new CommentDTO("pepe", "superProyecto", "Es un buen proyecto", date2);
        CommentDTO comment3 = new CommentDTO("pepe", "superProyecto", "Es un buen proyecto", date3);
        CommentDTO comment4 = new CommentDTO("paco", "proyectoMediocre", "Es un proyecto mediocre", date4);
        ArrayList<CommentDTO> results1 = new ArrayList<>(), results2 = new ArrayList<>();
        results1.add(comment4);
        results1.add(comment1);
        results2.add(comment1);
        results2.add(comment2);
        results2.add(comment3);

        dao.addComment(comment1);
        dao.addComment(comment2);
        dao.addComment(comment3);
        dao.addComment(comment4);

        assertEquals("Invalid user search results", results1.getClass(), dao.findByUser("paco").getClass());
        assertEquals("Invalid project search results", results2, dao.findByProject("superProyecto"));

        dao.removeComment(comment1);
        dao.removeComment(comment2);
        dao.removeComment(comment3);
        dao.removeComment(comment4);
    }
}
