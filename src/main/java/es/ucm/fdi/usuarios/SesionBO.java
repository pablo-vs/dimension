package es.ucm.fdi.usuarios;

public class SesionBO {
	private String sessionID;
	private CuentaUsuarioTO usuario;
	
	public SesionBO(CuentaUsuarioTO usu, String id) {
		usuario = usu;
		sessionID = id;
	}

	public String getID() {
		return sessionID;
	}

	public CuentaUsuarioTO getUsuario() {
		return usuario;
	}
}
