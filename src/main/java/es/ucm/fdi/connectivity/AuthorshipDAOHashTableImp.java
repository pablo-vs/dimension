package es.ucm.fdi.connectivity;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.datos.MemoryDB;
import es.ucm.fdi.exceptions.DAOError;

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

        public AuthorshipDAOHashTableImp() throws DAOError{
			db = new MemoryDB<>();
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
                    if(aux.getAuthor().equals(username)) {
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
                        if(aux.getProject().equals(project)) {
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
