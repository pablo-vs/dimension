/*
  This file is part of Dimension.
  Dimension is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  Dimension is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  You should have received a copy of the GNU General Public License
  along with Dimension.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.integration.connectivity;

import es.ucm.fdi.business.connectivity.AuthorshipDTO;
import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.data.MemoryDB;
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
