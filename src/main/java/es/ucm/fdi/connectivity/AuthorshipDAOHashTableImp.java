package es.ucm.fdi.connectivity;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.datos.MemoryDB;

/**
 * AuthorshipDAO implementation using a HashTable-based database.
 *
 * @author Javier Galiana
 * @version 05.04.2018
 */
public class AuthorshipDAOHashTableImp implements AuthorshipDAO {
        /**
         * Database
         */
        private MemoryDB<AuthorshipBO> db;

        @Override
        public void addAuthorship(AuthorshipBO auth) {
                db.insert(auth, auth.getId());
        }

        @Override
        public void removeAuthorship(AuthorshipBO auth) {
                db.removeId(auth.getId());
        }

        @Override
        public List<AuthorshipBO> findByUser(String username) {
            ArrayList<AuthorshipBO> lista = new ArrayList<>();
            for (String id : db.getIds()) {
                    AuthorshipBO aux = db.find(id);
                    if(aux.getAuthor().equals(username)) {
                            lista.add(aux);
                    }
            }
            return lista;
        }

        @Override
        public List<AuthorshipBO> findByProject(String project) {
                ArrayList<AuthorshipBO> lista = new ArrayList<>();
                for (String id : db.getIds()) {
                        AuthorshipBO aux = db.find(id);
                        if(aux.getProject().equals(project)) {
                                lista.add(aux);
                        }
                }
                return lista;
        }

        @Override
        public List<AuthorshipBO> getAuthorships() {
                ArrayList<AuthorshipBO> lista = new ArrayList<>();
                for (String id : db.getIds()) {
                        lista.add(db.find(id));
                }
                return lista;
        }

}
