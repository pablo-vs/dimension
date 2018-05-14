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
package es.ucm.fdi.business.users;

import java.time.ZonedDateTime;
import java.io.Serializable;

/**
 * Represents a user session.
 *
 * @author Pablo Villalobos
 */
public class SessionDTO implements Serializable {

    private final String username;
    /**
     * Date of login
     */
    private final ZonedDateTime date;

    /**
     * Class constructor specifying username and date.
     *
     * @param username Username
     * @param date Date of login
     */
    public SessionDTO(String username, ZonedDateTime date) {
        this.username = username;
        this.date = date;
    }

    /**
     * Returns an unique session identifier.
     *
     * @return a string which uniquely determines the session.
     */
    public String getID() {
        return Integer.toString((username + date.toString()).hashCode());
    }

    /**
     * @return the username of whom started the session
     */
    public String getUser() {
        return username;
    }

    /**
     *
     * @return A ZonedDateTime representing the date.
     */
    public ZonedDateTime getDate() {
        return date;
    }
}
