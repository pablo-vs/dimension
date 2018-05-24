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
import es.ucm.fdi.integration.exceptions.DAOErrorException;

import java.util.List;

/**
 * The DAO for notifications.
 *
 * @author Brian Leiva
 */
public interface NotificationDAO {

    /**
     * Adds a new notification to the database.
     *
     * @param user The new notification.
     */
    public void addNotification(NotificationDTO notif);

    /**
     * If there is a notification with the given identifier, removes it.
     *
     * @param notif The notification to remove.
     */
    public void removeNotification(NotificationDTO notif);

    /**
     * If there is a notification with the given identifier, changes its
     * details.
     *
     * @param notif A TO containing the new account details.
     */
    public void modifyNotification(NotificationDTO notif);

    /**
     * Returns the details of the notification for the given id.
     *
     * @param id Identifier of the notification.
     * @return A TO containing the relevant data.
     */
    public NotificationDTO findNotification(String id);

    /**
     * Find the notifications in the database matching the given username.
     *
     * @param username The identifier of the user.
     * @return A List of notifications sent to the user.
     */
    public List<NotificationDTO> findByUser(String username) throws DAOErrorException;
}
