package es.ucm.fdi.connectivity;

import java.sql.SQLException;

import org.junit.Test;

public class DAOTest {
	
	@Test
	public void AuthDAOSQLTest() throws SQLException, ClassNotFoundException{
		//AuthorshipDAOSQLImp dao = new AuthorshipDAOSQLImp();
		AuthorshipBO auth = new AuthorshipBO("paco", "superProyecto");
		//dao.addAuthorship(auth);
	}
}