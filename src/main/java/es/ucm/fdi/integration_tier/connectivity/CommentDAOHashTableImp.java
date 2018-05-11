package es.ucm.fdi.integration_tier.connectivity;

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
    private MemoryDB<CommentBO> db = new MemoryDB<>();

    /**
     * Empty class constructor.
     */
    public CommentDAOHashTableImp() {
    }

    @Override
    public void addComment(CommentBO comment) {
        db.insert(comment, comment.getId());
    }

    @Override
    public void removeComment(CommentBO comment) {
        db.removeId(comment.getId());
    }

    @Override
    public List<CommentBO> findByUser(String username) {
        ArrayList<CommentBO> lista = new ArrayList<>();
        for (String id : db.getIds()) {
            CommentBO aux = db.find(id);
            if (aux.getAuthor().equals(username)) {
                lista.add(aux);
            }
        }
        return lista;
    }

    @Override
    public List<CommentBO> findByProject(String project) {
        ArrayList<CommentBO> lista = new ArrayList<>();
        for (String id : db.getIds()) {
            CommentBO aux = db.find(id);
            if (aux.getProj().equals(project)) {
                lista.add(aux);
            }
        }
        return lista;
    }

    @Override
    public List<CommentBO> getComments() {
        ArrayList<CommentBO> lista = new ArrayList<>();
        for (String id : db.getIds()) {
            lista.add(db.find(id));
        }
        return lista;
    }

}
