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

/**
 * Represents a friendship between two users.
 *
 * @author Pablo Villalobos
 */
public class FriendshipDTO {

    private final String user1;
    private final String user2;

    /**
     * Class constructor specifying both users.
     *
     * @param user1 User 1
     * @param user2 User 2
     */
    public FriendshipDTO(String user1, String user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    /**
     * 
     * @return the first user
     */
    public String getFirstUser() {
        return user1;
    }

    /**
     * 
     * @return the secondo user
     */
    public String getSecondUser() {
        return user2;
    }
}
