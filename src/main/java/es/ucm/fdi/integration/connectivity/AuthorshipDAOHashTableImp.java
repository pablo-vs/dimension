package es.ucm.fdi.integration.connectivity;

import es.ucm.fdi.business.connectivity.AuthorshipDTO;
import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.integration.data.MemoryDB;
import es.ucm.fdi.integration.exceptions.DAOErrorException;

/**
 * AuthorshipDAO implementation using a HashTable-based database.
 *
 * @author Javier Galiana
 */
public class AuthorshipDAOHashTableImp implements AuthorshipDAO {

    /**
     * Database
     */
    private MemoryDB<AuthorshipDTO> db = new MemoryDB<>();

    /**
     * Empty class constructor
     *
     * @throws DAOErrorException
     */
    public AuthorshipDAOHashTableImp() {
    }

    @Override
    public void addAuthorship(AuthorshipDTO auth) throws DAOErrorException {
        db.insert(auth, auth.getId());
    }

    @Override
    public void removeAuthorship(AuthorshipDTO auth) throws DAOErrorException {
        db.removeId(auth.getId());
    }

    @Override
    public List<AuthorshipDTO> findByUser(String username) throws DAOErrorException {
        ArrayList<AuthorshipDTO> lista = new ArrayList<>();
        for (String id : db.getIds()) {
            AuthorshipDTO aux = db.find(id);
            if (aux.getAuthor().equals(username)) {
                lista.add(aux);
            }
        }
        return lista;
    }

    @Override
    public List<AuthorshipDTO> findByProject(String project) throws DAOErrorException {
        ArrayList<AuthorshipDTO> lista = new ArrayList<>();
        for (String id : db.getIds()) {
            AuthorshipDTO aux = db.find(id);
            if (aux.getProject().equals(project)) {
                lista.add(aux);
            }
        }
        return lista;
    }

    @Override
    public List<AuthorshipDTO> getAuthorships() throws DAOErrorException {
        ArrayList<AuthorshipDTO> lista = new ArrayList<>();
        for (String id : db.getIds()) {
            lista.add(db.find(id));
        }
        return lista;
    }

}
