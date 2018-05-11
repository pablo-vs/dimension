package es.ucm.fdi.integration_tier.connectivity;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.integration_tier.data.MemoryDB;
import es.ucm.fdi.business_tier.exceptions.DAOError;

/**
 * AuthorshipDAO implementation using a HashTable-based database.
 *
 * @author Javier Galiana
 */
public class AuthorshipDAOHashTableImp implements AuthorshipDAO {

    /**
     * Database
     */
    private MemoryDB<AuthorshipBO> db = new MemoryDB<>();

    /**
     * Empty class constructor
     *
     * @throws DAOError
     */
    public AuthorshipDAOHashTableImp() {
    }

    @Override
    public void addAuthorship(AuthorshipBO auth) throws DAOError {
        db.insert(auth, auth.getId());
    }

    @Override
    public void removeAuthorship(AuthorshipBO auth) throws DAOError {
        db.removeId(auth.getId());
    }

    @Override
    public List<AuthorshipBO> findByUser(String username) throws DAOError {
        ArrayList<AuthorshipBO> lista = new ArrayList<>();
        for (String id : db.getIds()) {
            AuthorshipBO aux = db.find(id);
            if (aux.getAuthor().equals(username)) {
                lista.add(aux);
            }
        }
        return lista;
    }

    @Override
    public List<AuthorshipBO> findByProject(String project) throws DAOError {
        ArrayList<AuthorshipBO> lista = new ArrayList<>();
        for (String id : db.getIds()) {
            AuthorshipBO aux = db.find(id);
            if (aux.getProject().equals(project)) {
                lista.add(aux);
            }
        }
        return lista;
    }

    @Override
    public List<AuthorshipBO> getAuthorships() throws DAOError {
        ArrayList<AuthorshipBO> lista = new ArrayList<>();
        for (String id : db.getIds()) {
            lista.add(db.find(id));
        }
        return lista;
    }

}
