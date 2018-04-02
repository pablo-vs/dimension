package es.ucm.fdi.usuarios;

import java.util.List;

/**
 * Interfaz para gestionar la persistencia de datos de usuarios.
 *
 * @author Pablo Villalobos
 * @version 01.04.2018
 */
public interface CuentaUsuarioDAO {

	/**
	 * Añade un usuario a la base de datos.
	 *
	 * @param user El usuario que se desea añadir.
	 */
	public void addUsuario(CuentaUsuarioTO user);

	/**
	 * Elimina el usuario con el identificador dado, si existe.
	 *
	 * @param id El identificador del usuario a eliminar.
	 */
	public void eliminarUsuario(String id);

	/**
	 * Si el usuario dado ya existe, cambia los datos de su cuenta por los dados.
	 *
	 * @param user El TO que contiene los nuevos datos del usuario.
	 */
	public void modificarUsuario(CuentaUsuarioTO user);

	/**
	 * Devuelve el usuario con el identificador dado.
	 *
	 * @param id Identificador del usuario que se busca.
	 * @return Un TO con los datos del usuario correspondiente.
	 */
	public CuentaUsuarioTO buscarUsuario(String id);

	/**
	 * Devuelve una lista con todos los usuarios registrados
	 *
	 * @return La lista de usuarios.
	 */
	public List<CuentaUsuarioTO> getUsuarios();
}
