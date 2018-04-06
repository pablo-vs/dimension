package es.ucm.fdi.users;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.datos.MemoryDB;

/**
 * UserDAO implementation using a HashTable-based database.
 *
 * @author Pablo Villalobos
 * @version 01.04.2018
 */
public class UserDAOHashTableImp implements UserDAO {
        /**
         * Database
         */
	private MemoryDB<UserTO> db;

        /**
         * Class constructor that initializes the db.
         */
	public UserDAOHashTableImp() {
		db = new MemoryDB<UserTO>();

	}
	
        @Override
	public void addUser(UserTO user) {
		db.insert(user, user.getID());
	}

        @Override
	public void removeUser(String id) {
		db.removeId(id);
	}

        @Override
	public void modifyUser(UserTO user) {
		if (findUser(user.getID()) != null) {
			db.insert(user, user.getID());
		} else {
			throw new IllegalArgumentException("The user " + user.getID() + " does not exist");
		}
	}

        @Override
	public UserTO findUser(String id) {
		return db.find(id);
	}

        @Override
	public List<UserTO> getUsers() {
		ArrayList<UserTO> lista = new ArrayList<>();
                db.getIds().forEach((id) -> {
                    lista.add(db.find(id));
                });
		return lista;
	}
}
