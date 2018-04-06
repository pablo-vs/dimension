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
		UserTO pepe = new UserTO("pepito", hashgen.hash("1234".toCharArray())),
			paco = new UserTO("paquito", hashgen.hash("4321".toCharArray()));
		userMgr.newUser(pepe);
		userMgr.newUser(paco);
		SessionBO sesionPepe = userMgr.login("pepito", "1234"),
			sesionPaco = userMgr.login("paquito", "4321");
		try {
			userMgr.removeUser("pepito", sesionPaco);
			fail("Illegal account access!!");
		} catch(AccessControlException e) {
			//System.out.println("Authentication works");
		}

		userMgr.removeUser("pepito", sesionPepe);
		userMgr.logout(sesionPepe);
		try {
			sesionPepe = userMgr.login("pepito", "1234");
			fail("Logged in to nonexistent account!");
		} catch(NotFoundException e) {
			//Todo correcto
		}
	}
		
	@Test
	public void invalidSessionTest() {
		HashGenerator hashgen = new HashGenerator();
		UserDAO dao = new UserDAOHashTableImp();
		String passwd = hashgen.hash("1234".toCharArray());
		UserTO user = new UserTO("pedro", passwd);
		UserManagerAS userMgr = UserManagerAS.getManager(dao);
		userMgr.newUser(user);
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
