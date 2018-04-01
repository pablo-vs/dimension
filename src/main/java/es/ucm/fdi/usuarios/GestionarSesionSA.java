package es.ucm.fdi.usuarios;

import java.lang.IllegalArgumentException;

import es.ucm.fdi.util.HashGenerator;


public class GestionarSesionSA {
	private CuentaUsuarioDAO BDUsuarios;
	private HashGenerator hashgen;

	public GestionarSesionSA(CuentaUsuarioDAO bd) {
		BDUsuarios = bd;
		hashgen = new HashGenerator();
	}
	
	public SesionBO iniciarSesion(String nombre, String passwd) {
		CuentaUsuarioTO user = BDUsuarios.buscarUsuario(nombre);
		if(user != null) {
			if(hashgen.authenticate(passwd.toCharArray(), user.getContrasenya())) {
				String ID = hashgen.hash(nombre.toCharArray());
				return new SesionBO(user, ID);
			}
			else {
				throw new IllegalArgumentException("Contraseña incorrecta");
			}
		}
		else {
			throw new IllegalArgumentException("No existe ese usuario");
		}
	}
}
