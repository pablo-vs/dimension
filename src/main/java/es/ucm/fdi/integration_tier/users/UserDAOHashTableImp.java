package es.ucm.fdi.integration_tier.users;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.integration_tier.data.MemoryDB;

/**
 * UserDAO implementation using a HashTable-based database.
 *
 * @author Pablo Villalobos
 */
public class UserDAOHashTableImp implements UserDAO {

    /**
     * Database
     */
    private MemoryDB<UserTransfer> db = new MemoryDB<>();

    /**
     * Empty class constructor.
     */
    public UserDAOHashTableImp() {
    }

    @Override
    public void addUser(UserTransfer user) {
        db.insert(user, user.getID());
    }

    @Override
    public void removeUser(String id) {
        db.removeId(id);
    }

    @Override
    public void modifyUser(UserTransfer user) {
        if (findUser(user.getID()) != null) {
            db.insert(user, user.getID());
        } else {
            throw new IllegalArgumentException("The user " + user.getID()
                    + " does not exist");
        }
    }

    @Override
    public UserTransfer findUser(String id) {
        return db.find(id);
    }

    @Override
    public List<UserTransfer> getUsers() {
        ArrayList<UserTransfer> lista = new ArrayList<>();
        db.getIds().forEach((id) -> {
            lista.add(db.find(id));
        });
        return lista;
    }
}
