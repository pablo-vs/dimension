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

import java.sql.SQLException;
import java.sql.Date;
import java.sql.JDBCType;

import java.util.List;

import es.ucm.fdi.integration.exceptions.DAOErrorException;
import es.ucm.fdi.integration.data.DAOSQLImp;
import java.util.ArrayList;

/**
* The SQL implementation for AuthorshipDAO.
*
* @author Brian Leiva
*/
public class NotificationDAOSQLImp extends DAOSQLImp<NotificationDTO> implements NotificationDAO {

  private static final int REQUIERED_LENGTH = 4;

  private static final String TABLE = "notifications";

  private static final String[] COLUMNS = {"id", "user", "notification", "date"};

  private static final JDBCType[] COLUMN_TYPES = {JDBCType.VARCHAR, JDBCType.VARCHAR,
      JDBCType.VARCHAR, JDBCType.DATE};

  public NotificationDAOSQLImp() {
      super(TABLE, COLUMNS, COLUMN_TYPES);
  }

  @Override
  public void addNotification(NotificationDTO notif) throws DAOErrorException {
      try {
          addRecord(notif);
      } catch (SQLException e) {
          throw new DAOErrorException("Error while adding authorship " + notif.getId()
                  + ".\n" + e.getMessage(), e);
      }
  }

  @Override
  public void removeNotification(NotificationDTO notif) throws DAOErrorException {
      try {
          deleteRecord(notif.getId());
      } catch (SQLException e) {
          throw new DAOErrorException("Error while removing Nnotification " + notif.getId()
                  + ".\n" + e.getMessage(), e);
      }
  }

  @Override
  public void modifyNotification(NotificationDTO notif) {
      try {
          modifyRecord(notif);
      } catch (SQLException e) {
          throw new DAOErrorException("Error while modifying notification " + notif.getUser()
                  + ".\n" + e.getMessage(), e);
      }
  }

  @Override
  public NotificationDTO findNotification(String id) {
	  NotificationDTO result = null;
      List<NotificationDTO> find;
      try {
          find = findByValue(0, id);
      } catch (SQLException e) {
          throw new DAOErrorException("Error while finding notification " + id
                  + ".\n" + e.getMessage(), e);
      }
      if (find.size() == 1) {
          result = find.get(0);
      }
      return result;
  }

  @Override
  public List<NotificationDTO> findByUser(String username) throws DAOErrorException {
      List<NotificationDTO> result;
      try {
          result = findByValue(1, username);
      } catch (SQLException e) {
          throw new DAOErrorException("Error while finding notifications from " + username
                  + ".\n" + e.getMessage(), e);
      }
      return result;
  }

  @Override
  public NotificationDTO build(List<Object> data) {
      if (data.size() != REQUIERED_LENGTH) {
          throw new IllegalArgumentException("Constructor requires 4 objects, "
                  + data.size() + " given");
      }
      if (!(data.get(0) instanceof String && data.get(1) instanceof String
              && data.get(2) instanceof String && data.get(3) instanceof Date)) {
          throw new IllegalArgumentException("Invalid data type");
      }
      return new NotificationDTO((String) data.get(1),
              (String) data.get(2), (Date) data.get(3));
  }

  @Override
  public List<Object> getData(NotificationDTO notif) {
      List<Object> data = new ArrayList<>();
      data.add(notif.getId());
      data.add(notif.getUser());
      data.add(notif.getNotification());
      data.add(notif.getDate());
      return data;
  }

}