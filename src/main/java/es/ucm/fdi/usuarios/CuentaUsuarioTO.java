package es.ucm.fdi.usuarios;

import java.util.Date;

/**
 * 
 * @author Eduardo Amaya
 * @author Javier Galiana
 *
 */

public class CuentaUsuarioTO {
	
	private String ID;
	private String nombre;
	private String contrasenya;
	private Date Fecha;
	private String email;
	private int telefono;
	private String foto;
	private String formacion;
	private String descripcion;
	private TipoUsuario tipo;
	private int tiempoSancion;

	public String getID() {
		return ID;
	}

	public String getContrasenya() {
		return contrasenya;
	}
}
