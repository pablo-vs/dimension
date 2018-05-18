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

import es.ucm.fdi.integration.users.UserDAOHashTableImp;
import es.ucm.fdi.business.users.NotificationDTO;

import java.sql.Date;
import java.util.ArrayList;

import org.junit.Test;

import static org.junit.Assert.*;

/**
* JUnit test for NotificationDAOHashTableImp class.
*
* @see NotificationDAOHashTableImp
* @author Brian Leiva
*/
public class NotificationDAOHashTableImpTest {

  @Test
  public void NotificationDAOHashTableTest() {

	  NotificationDAOHashTableImp dao = new NotificationDAOHashTableImp();
      NotificationDTO notif1 = new NotificationDTO("paco", "Has sido baneado", new Date(18, 5, 16));
      NotificationDTO notif2 = new NotificationDTO("pepe", "Has creado 10 proyectos", new Date(18, 3, 12));
      NotificationDTO notif3 = new NotificationDTO("marta", "Has colaborado en tu primer proyecto", new Date(18, 4, 20));
      NotificationDTO notif4 = new NotificationDTO("paco", "Ya no estas baneado", new Date(18, 5, 18));
      ArrayList<NotificationDTO> results1 = new ArrayList<>(), results2 = new ArrayList<>();

      results1.add(notif4);
      results2.add(notif2);
      results2.add(notif3);
      results1.add(notif1);

      dao.addNotification(notif1);
      dao.addNotification(notif2);
      dao.addNotification(notif3);
      dao.addNotification(notif4);

      assertEquals("Invalid user search results", results1, dao.findByUser("paco"));

      dao.removeNotification(notif1);
      dao.removeNotification(notif2);
      dao.removeNotification(notif3);
      dao.removeNotification(notif4);
  }
}