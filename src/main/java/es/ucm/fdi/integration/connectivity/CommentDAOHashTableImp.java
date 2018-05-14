/**
 * This file is part of Dimension.
 * Dimension is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Dimension is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Dimension.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.integration.connectivity;

import es.ucm.fdi.business.connectivity.CommentDTO;
import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.integration.data.MemoryDB;

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
