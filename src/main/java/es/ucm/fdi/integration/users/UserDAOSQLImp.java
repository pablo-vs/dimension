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

import es.ucm.fdi.business.users.UserType;	
import es.ucm.fdi.business.users.UserDTO;
import java.sql.SQLException;
import java.sql.JDBCType;

import java.util.Date;
import java.time.ZonedDateTime;
import java.util.List;

import es.ucm.fdi.integration.exceptions.DAOErrorException;
import es.ucm.fdi.data.DAOSQLImp;
import java.util.ArrayList;

/**
 *
 * @author Inmaculada Pérez, Pablo Villalobos
 */
public class UserDAOSQLImp extends DAOSQLImp<UserDTO> implements UserDAO {

    private static final int REQUIERED_LENGTH = 10;

    private static final String TABLE = "users";

    private static final String[] COLUMNS = {"id", "name", "password", "date", "email", "telephone",
        "picture", "description", "type", "banTime"};

    private static final JDBCType[] COLUMN_TYPES
            = {JDBCType.VARCHAR, JDBCType.VARCHAR, JDBCType.VARCHAR,
                JDBCType.DATE, JDBCType.VARCHAR, JDBCType.VARCHAR,
                JDBCType.VARCHAR, JDBCType.VARCHAR, JDBCType.INTEGER,
                JDBCType.TIMESTAMP};

    /**
     * Class constructor.
     */
    public UserDAOSQLImp() {
        super(TABLE, COLUMNS, COLUMN_TYPES);
    }

    /**
     * Adds a new user to the database.
     *
     * @param user The new user as a UserDTO.
     */
    @Override
    public void addUser(UserDTO user) throws DAOErrorException {
        try {
            addRecord(user);
        } catch (SQLException e) {
            throw new DAOErrorException("Error while adding user " + user.getID()
                    + ".\n" + e.getMessage(), e);
        }
    }

    /**
     * Removes a user from the database.
     *
     * @param id The user to remove.
     */
    @Override
    public void removeUser(String id) {
        try {
            deleteRecord(id);
        } catch (SQLException e) {
            throw new DAOErrorException("Error while removing user " + id
                    + ".\n" + e.getMessage(), e);
        }
    }

    /**
     * Modifies a user.
     *
     * @param user The user to modify.
     */
    @Override
    public void modifyUser(UserDTO user) {
        try {
            modifyRecord(user);
        } catch (SQLException e) {
            throw new DAOErrorException("Error while modifying user " + user.getID()
                    + ".\n" + e.getMessage(), e);
        }
    }

    /**
     * Find an user in the database matching the given id.
     *
     * @param id The identifier of the user.
     * @return The user where the id is the given one.
     */
    @Override
    public UserDTO findUser(String id) {
        UserDTO result = null;
        List<UserDTO> find;
        try {
            find = findByValue(0, id);
        } catch (SQLException e) {
            throw new DAOErrorException("Error while finding user " + id
                    + ".\n" + e.getMessage(), e);
        }
        if (find.size() == 1) {
            result = find.get(0);
        }
        return result;
    }

    /**
     * Returns all the stored users.
     *
     * @return A List of UserTOs.
     */
    @Override
    public List<UserDTO> getUsers() {
        List<UserDTO> result;
        try {
            result = getAllRecords();
        } catch (SQLException e) {
            throw new DAOErrorException("Error while reading all users.\n"
                    + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public List<Object> getData(UserDTO u) {
        List<Object> data = new ArrayList<>();
        data.add(u.getID());
        data.add(u.getName());
        data.add(u.getPassword());
        data.add(u.getDate());
        data.add(u.getEmail());
        data.add(u.getTelephone());
        data.add(u.getPicture());
        data.add(u.getDescription());
        data.add(u.getType());
        data.add(u.getBanTime() == null ? null : u.getBanTime().toInstant());

        return data;
    }

    @Override
    public UserDTO build(List<Object> data) {
        if (data.size() != REQUIERED_LENGTH) {
            throw new IllegalArgumentException("Constructor requires 11 objects, "
                    + data.size() + " given");
        }
        if (!(data.get(0) instanceof String && (data.get(1) == null || data.get(1) instanceof String)
                && data.get(2) instanceof String
                && (data.get(3) == null || data.get(3) instanceof Date)
                && (data.get(4) == null || data.get(4) instanceof String)
                && (data.get(5) == null || data.get(5) instanceof String)
                && (data.get(6) == null || data.get(6) instanceof String)
                && (data.get(7) == null || data.get(7) instanceof String)
                && data.get(8) instanceof Integer
                && (data.get(4) == null || data.get(9) instanceof ZonedDateTime))) {
            throw new IllegalArgumentException("Invalid data type");
        }

        UserType type = UserType.fromInt((Integer) data.get(8));

        return new UserDTO((String) data.get(0), (String) data.get(1),
                (String) data.get(2), (Date) data.get(3), (String) data.get(4),
                (String) data.get(5), (String) data.get(6), (String) data.get(7),
                type, (ZonedDateTime) data.get(9));
    }

}
