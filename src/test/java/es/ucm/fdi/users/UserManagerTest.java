package es.ucm.fdi.users;

import java.time.ZonedDateTime;
import java.security.AccessControlException;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Assert;

import es.ucm.fdi.util.HashGenerator;
import es.ucm.fdi.exceptions.NotFoundException;

public class UserManagerTest {

	@Test
	public void userManagementTest() {
		UserManagerAS userMgr = UserManagerAS.getManager(new UserDAOHashTableImp());
		HashGenerator hashgen = new HashGenerator();
		UserTO pepe = new UserTO("pepe", hashgen.hash("1234".toCharArray())),
			paco = new UserTO("paco", hashgen.hash("4321".toCharArray()));
		userMgr.newUser(pepe);
		userMgr.newUser(paco);
		SessionBO sesionPepe = userMgr.login("pepe", "1234"),
			sesionPaco = userMgr.login("paco", "4321");
		try {
			userMgr.removeUser("pepe", sesionPaco);
			fail("Illegal account access!!");
		} catch(AccessControlException e) {
			//System.out.println("Authentication works");
		}

		userMgr.removeUser("pepe", sesionPepe);
		userMgr.logout(sesionPepe);
		try {
			sesionPepe = userMgr.login("pepe", "1234");
			fail("Logged in to nonexistent account!");
		} catch(NotFoundException e) {
			//Todo correcto
		}
	}
		
	@Test
	public void validSessionTest() {
		HashGenerator hashgen = new HashGenerator();
		UserDAO dao = new UserDAOHashTableImp();
		String passwd = hashgen.hash("1234".toCharArray());
		UserTO user = new UserTO("pedro", passwd);
		dao.addUser(user);
		UserManagerAS userMgr = UserManagerAS.getManager(dao);
		SessionBO sesion = userMgr.login("pedro", "1234");
		assertTrue(userMgr.validateSession(sesion));
		userMgr.logout(sesion);
		assertFalse(userMgr.validateSession(sesion));
	}

	@Test
	public void invalidSessionTest() {
		HashGenerator hashgen = new HashGenerator();
		UserDAO dao = new UserDAOHashTableImp();
		String passwd = hashgen.hash("1234".toCharArray());
		UserTO user = new UserTO("pedro", passwd);
		dao.addUser(user);
		UserManagerAS userMgr = UserManagerAS.getManager(dao);
		try{
			SessionBO sesion = userMgr.login("pedro", "12345");
			fail("La sesión no debería haberse iniciado");
		} catch(AccessControlException e) {
			//Todo correcto
		}

		SessionBO sesion = new SessionBO("pedro", ZonedDateTime.now());
		assertFalse(userMgr.validateSession(sesion));
	}
}
