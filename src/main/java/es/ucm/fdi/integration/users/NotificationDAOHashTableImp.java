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
package es.ucm.fdi.integration.users;

import es.ucm.fdi.business.users.NotificationDTO;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.integration.data.MemoryDB;
import es.ucm.fdi.integration.exceptions.DAOErrorException;

/**
 * NotificationDAO implementation using a HashTable-based database.
 *
 * @author Brian Leiva
 */
public class NotificationDAOHashTableImp implements NotificationDAO {

    /**
     * Database
     */
    private MemoryDB<NotificationDTO> db = new MemoryDB<>();

    /**
     * Empty class constructor
     *
     * @throws DAOErrorException
     */
    public NotificationDAOHashTableImp() {
    }

    @Override
    public void addNotification(NotificationDTO notif) throws DAOErrorException {
        db.insert(notif, notif.getId());
    }

    @Override
    public void removeNotification(NotificationDTO notif) throws DAOErrorException {
        db.removeId(notif.getId());
    }

    @Override
    public void modifyNotification(NotificationDTO notif) {
        if (findNotification(notif.getId()) != null) {
            db.insert(notif, notif.getId());
        } else {
            throw new IllegalArgumentException("The user " + notif.getId()
                    + " does not exist");
        }
    }

    @Override
    public NotificationDTO findNotification(String id) {
        return db.find(id);
    }

    @Override
    public List<NotificationDTO> findByUser(String username) throws DAOErrorException {
        ArrayList<NotificationDTO> lista = new ArrayList<>();
        for (String id : db.getIds()) {
            NotificationDTO aux = db.find(id);
            if (aux.getUser().equals(username)) {
                lista.add(aux);
            }
        }
        return lista;
    }

}
