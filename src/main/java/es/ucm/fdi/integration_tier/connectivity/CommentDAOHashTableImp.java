package es.ucm.fdi.integration_tier.connectivity;

import es.ucm.fdi.business_tier.connectivity.CommentDTO;
import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.integration_tier.data.MemoryDB;

/**
 * CommentDAO implementation using a HashTable-based database.
 *
 * @author Brian Leiva
 */
public class CommentDAOHashTableImp implements CommentDAO {

    /**
     * Database
     */
    private MemoryDB<CommentDTO> db = new MemoryDB<>();

    /**
     * Empty class constructor.
     */
    public CommentDAOHashTableImp() {
    }

    @Override
    public void addComment(CommentDTO comment) {
        db.insert(comment, comment.getId());
    }

    @Override
    public void removeComment(CommentDTO comment) {
        db.removeId(comment.getId());
    }

    @Override
    public List<CommentDTO> findByUser(String username) {
        ArrayList<CommentDTO> lista = new ArrayList<>();
        for (String id : db.getIds()) {
            CommentDTO aux = db.find(id);
            if (aux.getAuthor().equals(username)) {
                lista.add(aux);
            }
        }
        return lista;
    }

    @Override
    public List<CommentDTO> findByProject(String project) {
        ArrayList<CommentDTO> lista = new ArrayList<>();
        for (String id : db.getIds()) {
            CommentDTO aux = db.find(id);
            if (aux.getProj().equals(project)) {
                lista.add(aux);
            }
        }
        return lista;
    }

    @Override
    public List<CommentDTO> getComments() {
        ArrayList<CommentDTO> lista = new ArrayList<>();
        for (String id : db.getIds()) {
            lista.add(db.find(id));
        }
        return lista;
    }

}
