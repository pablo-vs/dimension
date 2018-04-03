package es.ucm.fdi.usuarios;

import java.time.ZonedDateTime;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Assert;

import es.ucm.fdi.util.HashGenerator;

public class UserManagerTest {

	@Test
	public void userManagementTest() {
		UserManagerAS userMgr = UserManagerAS.getManager(new CuentaUsuarioDAOHashTableImp());
		HashGenerator hashgen = new HashGenerator();
		CuentaUsuarioTO pepe = new CuentaUsuarioTO("pepe", hashgen.hash("1234".toCharArray())),
			paco = new CuentaUsuarioTO("paco", hashgen.hash("4321".toCharArray()));
		userMgr.newUser(pepe);
		userMgr.newUser(paco);
		String sesionPepe = userMgr.login("pepe", "1234"),
			sesionPaco = userMgr.login("paco", "4321");
		try {
			userMgr.removeUser("pepe", sesionPaco);
			fail("Illegal account access!!");
		} catch(IllegalArgumentException e) {
			//System.out.println("Authentication works");
		}

		userMgr.removeUser("pepe", sesionPepe);
		userMgr.logout(sesionPepe);
		try {
			sesionPepe = userMgr.login("pepe", "1234");
			fail("Logged in to nonexistent account!");
		} catch(IllegalArgumentException e) {
			//Todo correcto
		}
	}
		
	@Test
	public void validSessionTest() {
		HashGenerator hashgen = new HashGenerator();
		CuentaUsuarioDAO dao = new CuentaUsuarioDAOHashTableImp();
		String passwd = hashgen.hash("1234".toCharArray());
		CuentaUsuarioTO user = new CuentaUsuarioTO("pedro", passwd);
		dao.addUsuario(user);
		UserManagerAS userMgr = UserManagerAS.getManager(dao);
		String sesion = userMgr.login("pedro", "1234");
		assertTrue(userMgr.validateSession(sesion));
		userMgr.logout(sesion);
		assertFalse(userMgr.validateSession(sesion));
	}

	@Test
	public void invalidSessionTest() {
		HashGenerator hashgen = new HashGenerator();
		CuentaUsuarioDAO dao = new CuentaUsuarioDAOHashTableImp();
		String passwd = hashgen.hash("1234".toCharArray());
		CuentaUsuarioTO user = new CuentaUsuarioTO("pedro", passwd);
		dao.addUsuario(user);
		UserManagerAS userMgr = UserManagerAS.getManager(dao);
		try{
			String sesion = userMgr.login("pedro", "12345");
			fail("La sesión no debería haberse iniciado");
		} catch(IllegalArgumentException e) {
			//Todo correcto
		}

		SesionBO sesion = new SesionBO("pedro", ZonedDateTime.now());
		assertFalse(userMgr.validateSession(sesion.getID()));
	}
}
