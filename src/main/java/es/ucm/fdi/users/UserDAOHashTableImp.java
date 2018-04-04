package es.ucm.fdi.users;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.datos.BDMemoria;

/**
 * UserDAO implementation using a HashTable-based database.
 *
 * @author Pablo Villalobos
 * @version 01.04.2018
 */
public class UserDAOHashTableImp implements UserDAO {
	private BDMemoria<UserTO> bd;

	public UserDAOHashTableImp() {
		bd = new BDMemoria<UserTO>();
	}
	
	public void addUser(UserTO user) {
		bd.insert(user, user.getID());
	}

	public void removeUser(String id) {
		bd.removeId(id);
	}

	public void modifyUser(UserTO user) {
		if (findUser(user.getID()) != null) {
			bd.insert(user, user.getID());
		} else {
			throw new IllegalArgumentException("The user " + user.getID() + " does not exist");
		}
	}

	public UserTO findUser(String id) {
		return bd.find(id);
	}

	public List<UserTO> getUsers() {
		ArrayList<UserTO> lista = new ArrayList<>();
		for (String id : bd.getIds()) {
			lista.add(bd.find(id));
		}
		return lista;
	}
}
