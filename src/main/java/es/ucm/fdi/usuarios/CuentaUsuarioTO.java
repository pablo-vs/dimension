package es.ucm.fdi.usuarios;

import java.util.Date;
import java.time.Period;
import java.io.Serializable;

/**
 * Objeto que contiene los datos de una cuenta de usuario; 
 *
 * @author Eduardo Amaya
 * @author Javier Galiana
 *
 */

public class CuentaUsuarioTO implements Serializable {
	
	private String ID;
	private String nombre;
	private String contrasenya;
	private Date fecha;
	private String email;
	private String telefono;
	private String foto;
	private String formacion;
	private String descripcion;
	private TipoUsuario tipo;
	private Period tiempoSancion;

	/**
	 * Constructor mínimo.
	 */
	public CuentaUsuarioTO(String ID, String contrasenya) {
		this.ID = ID;
		this.contrasenya = contrasenya;
	}

	public String getID() {
		return ID;
	}

	public String getNombre() {
		return nombre;
	}

	public String getContrasenya() {
		return contrasenya;
	}

	public Date getFecha() {
		return fecha;
	}

	public String getEmail() {
		return email;
	}

	public String getTelefono() {
		return telefono;
	}

	public String getFoto() {
		return foto;
	}

	public String getFormacion() {
		return formacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public TipoUsuario getTipo() {
		return tipo;
	}

	public Period getTiempoSancion() {
		return tiempoSancion;
	}
}
