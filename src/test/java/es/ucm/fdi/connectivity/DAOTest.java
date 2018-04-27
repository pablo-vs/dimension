package es.ucm.fdi.connectivity;

import java.sql.SQLException;	
import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;

public class DAOTest {
	
	@Test
	public void AuthDAOSQLTest() throws SQLException, ClassNotFoundException{
		AuthorshipDAOSQLImp dao = new AuthorshipDAOSQLImp();
		AuthorshipBO auth1 = new AuthorshipBO("paco", "superProyecto"),
				auth2 = new AuthorshipBO("pepe", "superProyecto"),
				auth3 = new AuthorshipBO("paco", "proyectoMediocre");
		ArrayList<AuthorshipBO> results1 = new ArrayList<>(),
				results2 = new ArrayList<>();
		results1.add(auth3); results1.add(auth1);
		results2.add(auth1); results2.add(auth2);
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