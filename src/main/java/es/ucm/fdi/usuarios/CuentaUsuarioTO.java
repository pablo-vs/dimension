package es.ucm.fdi.usuarios;

import java.util.Date;

/**
 * Objeto que contiene los datos de una cuenta de usuario; 
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

	/**
	 * Constructor mínimo para hacer tests.
	 */
	public CuentaUsuarioTO(String ID, String contrasenya) {
		this.ID = ID;
		this.contrasenya = contrasenya;
	}
	
	public String getID() {
		return ID;
	}

	public String getContrasenya() {
		return contrasenya;
	}
}
