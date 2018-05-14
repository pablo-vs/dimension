package es.ucm.fdi.integration;

import es.ucm.fdi.integration.connectivity.AuthorshipDAOSQLImp;
import es.ucm.fdi.business.connectivity.AuthorshipDTO;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;

public class DAOTest {

    @Test
    public void AuthDAOSQLTest() throws SQLException, ClassNotFoundException {
        AuthorshipDAOSQLImp dao = new AuthorshipDAOSQLImp();
        AuthorshipDTO auth1 = new AuthorshipDTO("paco", "superProyecto");
        AuthorshipDTO auth2 = new AuthorshipDTO("pepe", "superProyecto");
        AuthorshipDTO auth3 = new AuthorshipDTO("paco", "proyectoMediocre");
        ArrayList<AuthorshipDTO> results1 = new ArrayList<>(),
                results2 = new ArrayList<>();
        results1.add(auth3);
        results1.add(auth1);
        results2.add(auth1);
        results2.add(auth2);
        dao.addAuthorship(auth1);
        dao.addAuthorship(auth2);
        dao.addAuthorship(auth3);

        assertEquals("Invalid user search results", results1, dao.findByUser("paco"));
        assertEquals("Invalid project search results", results2, dao.findByProject("superProyecto"));
        dao.removeAuthorship(auth1);
        dao.removeAuthorship(auth2);
        dao.removeAuthorship(auth3);
    }
}
