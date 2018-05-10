package es.ucm.fdi.connectivity;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.data.MemoryDB;

/**
 * CommentDAO implementation using a HashTable-based database.
 *
 * @author Brian Leiva
 * @version 20.04.2018
 */
public class CommentDAOHashTableImp implements CommentDAO {

    /**
     * Database
     */
    private MemoryDB<CommentBO> db;

    public CommentDAOHashTableImp() {
        db = new MemoryDB<CommentBO>();
    }

    public void addComment(CommentBO comment) {
        db.insert(comment, comment.getId());
    }

    public void removeComment(CommentBO comment) {
        db.removeId(comment.getId());
    }

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

    public List<CommentBO> getComments() {
        ArrayList<CommentBO> lista = new ArrayList<>();
        for (String id : db.getIds()) {
            lista.add(db.find(id));
        }
        return lista;
    }

}
