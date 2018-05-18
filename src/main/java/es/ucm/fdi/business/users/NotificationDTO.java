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
package es.ucm.fdi.business.users;

import java.sql.Date;

/**
* Represents a notification to a user
* @author Brian Leiva
*/
public class NotificationDTO {

	/**
     * User
     */
    private final String user;
    /**
     * Notification text
     */
    private final String notification;
    /**
     * Date
     */
    private final Date date;
    /**
     * Notification id
     */
    private final String id;


  /**
   * Class constructor specifying both users.
   *
   * @param user User
   * @param notification Notification text
   */
  public NotificationDTO(String user, String notification, Date date) {
      this.user = user;
      this.notification = notification;
      this.date = date;
      this.id = user + notification.hashCode() + date.hashCode();
  }

  /**
   *
   * @return the user
   */
  public String getUser() {
      return user;
  }

  /**
   *
   * @return the notification text
   */
  public String getNotification() {
      return notification;
  }
  
  /**
  *
  * @return the notification date
  */
  public Date getDate() {
     return date;
  }
  
  /**
  *
  * @return the id of the notification
  */
  public String getId() {
	  return id;
  }
 
  @Override
  public boolean equals(Object other) {
	  if (!(other instanceof NotificationDTO)) {
		  return false;
	  } else {
		  return id.equals(((NotificationDTO) other).getId());
	  }
  }

}