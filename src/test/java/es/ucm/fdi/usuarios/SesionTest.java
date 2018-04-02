package es.ucm.fdi.usuarios;

import org.junit.Test;
import static org.junit.Assert.*;

import es.ucm.fdi.util.HashGenerator;

public class SesionTest {

	@Test
	public void testSesionValida() {
		HashGenerator hashgen = new HashGenerator();
		CuentaUsuarioDAO dao = new CuentaUsuarioDAOHashTableImp();
		String passwd = hashgen.hash("1234".toCharArray());
		CuentaUsuarioTO user = new CuentaUsuarioTO("pepe", passwd);
		dao.addUsuario(user);
		GestionarSesionSA sessionMgr = new GestionarSesionSA(dao);
		SesionBO sesion = sessionMgr.iniciarSesion("pepe", "1234");
		assertTrue(sessionMgr.validarSesion(sesion.getID()));
		sessionMgr.cerrarSesion(sesion.getID());
	}
}
