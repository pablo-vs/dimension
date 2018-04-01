package es.ucm.fdi.usuarios;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.datos.BDMemoria;

public class CuentaUsuarioDAO {
	private BDMemoria<CuentaUsuarioTO> bd;

	public void addUsuario(CuentaUsuarioTO user) {
		bd.insert(user, user.getID());
	}

	public void eliminarUsuario(String id) {
		bd.removeId(id);
	}

	public void modificarUsuario(CuentaUsuarioTO user) {
		bd.insert(user, user.getID());
	}

	public CuentaUsuarioTO buscarUsuario(String id) {
		return bd.find(id);
	}

	public List<CuentaUsuarioTO> getUsuarios() {
		ArrayList<CuentaUsuarioTO> lista = new ArrayList<>();
		for (String id : bd.getIds()) {
			lista.add(bd.find(id));
		}
		return lista;
	}
}
