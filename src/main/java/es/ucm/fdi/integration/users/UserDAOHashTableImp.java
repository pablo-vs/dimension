package es.ucm.fdi.integration.users;

import es.ucm.fdi.business.users.UserDTO;
import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.integration.data.MemoryDB;

/**
 * UserDAO implementation using a HashTable-based database.
 *
 * @author Pablo Villalobos
 */
public class UserDAOHashTableImp implements UserDAO {

    /**
     * Database
     */
    private MemoryDB<UserDTO> db = new MemoryDB<>();

    /**
     * Empty class constructor.
     */
    public UserDAOHashTableImp() {
    }

    @Override
    public void addUser(UserDTO user) {
        db.insert(user, user.getID());
    }

    @Override
    public void removeUser(String id) {
        db.removeId(id);
    }

    @Override
    public void modifyUser(UserDTO user) {
        if (findUser(user.getID()) != null) {
            db.insert(user, user.getID());
        } else {
            throw new IllegalArgumentException("The user " + user.getID()
                    + " does not exist");
        }
    }

    @Override
    public UserDTO findUser(String id) {
        return db.find(id);
    }

    @Override
    public List<UserDTO> getUsers() {
        ArrayList<UserDTO> lista = new ArrayList<>();
        db.getIds().forEach((id) -> {
            lista.add(db.find(id));
        });
        return lista;
    }

    @Override
    public void banUser(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
