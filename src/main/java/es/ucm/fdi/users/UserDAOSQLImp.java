package es.ucm.fdi.users;

import java.sql.SQLException;
import java.sql.JDBCType;

import java.util.Date;
import java.time.Period;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

import twitter4j.auth.AccessToken;

import es.ucm.fdi.exceptions.DAOError;
import es.ucm.fdi.data.DAOSQLImp;

public class UserDAOSQLImp extends DAOSQLImp<UserTO> implements UserDAO {

    private static final String TABLE = "users";

    private static final String[] COLUMNS = {"id", "name", "passwd", "date", "email", "telephone",
        "picture", "description", "type", "banTime", "twitter"};

    private static final JDBCType[] COLUMN_TYPES
            = {JDBCType.VARCHAR, JDBCType.VARCHAR, JDBCType.VARCHAR,
                JDBCType.DATE, JDBCType.VARCHAR, JDBCType.VARCHAR,
                JDBCType.VARCHAR, JDBCType.VARCHAR, JDBCType.VARCHAR,
                JDBCType.TIMESTAMP, JDBCType.VARCHAR};

    public UserDAOSQLImp() {
        super(TABLE, COLUMNS, COLUMN_TYPES);
    }

    /**
     * Adds a new user to the database.
     *
     * @param user The new user as a UserTO.
     */
    @Override
    public void addUser(UserTO user) throws DAOError {
        try {
            addRecord(user);
        } catch (SQLException e) {
            throw new DAOError("Error while adding user " + user.getID()
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
            throw new DAOError("Error while removing user " + id
                    + ".\n" + e.getMessage(), e);
        }
    }

    /**
     * Modifies a user.
     *
     * @param user The user to modify.
     */
    @Override
    public void modifyUser(UserTO user) {
        try {
            modifyRecord(user);
        } catch (SQLException e) {
            throw new DAOError("Error while modifying user " + user.getID()
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
    public UserTO findUser(String id) {
        UserTO result = null;
        List<UserTO> find;
        try {
            find = findByVal(0, id);
        } catch (SQLException e) {
            throw new DAOError("Error while finding user " + id
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
    public List<UserTO> getUsers() {
        List<UserTO> result;
        try {
            result = getAllRecords();
        } catch (SQLException e) {
            throw new DAOError("Error while reading all users.\n"
                    + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public Object[] getData(UserTO u) {
        try {
            ByteArrayOutputStream str = new ByteArrayOutputStream();
            ObjectOutputStream ostr = new ObjectOutputStream(str);
            ostr.writeObject(u.getTwitterAccess());
            String twitterAccess = str.toString("UTF-8");
            Object[] data = {u.getID(), u.getName(), u.getPassword(), u.getDate(),
                u.getEmail(), u.getTelephone(), u.getPicture(),
                u.getDescription(), u.getType(), u.getBanTime(), twitterAccess};
            return data;
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not serialize AccessToken for user "
                    + u.getID());
        }
    }

    @Override
    public UserTO build(Object[] data) {
        if (data.length != 11) {
            throw new IllegalArgumentException("Constructor requires 11 objects, "
                    + data.length + " given");
        }
        if (!(data[0] instanceof String && data[1] instanceof String && data[2] instanceof String
                && data[3] instanceof Date && data[4] instanceof String && data[5] instanceof String
                && data[6] instanceof String && data[7] instanceof String && data[8] instanceof Integer
                && data[9] instanceof Period && data[10] instanceof String)) {
            throw new IllegalArgumentException("Invalid data type");
        }

        Object token;
        try {
            ByteArrayInputStream str = new ByteArrayInputStream(((String) data[10]).getBytes("UTF-8"));
            token = new ObjectInputStream(str).readObject();
            if (!(token instanceof AccessToken)) {
                throw new IllegalArgumentException("Could not read access token");
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new IllegalArgumentException("Could not read access token");
        }

        UserType type = UserType.fromInt((Integer) data[8]);

        return new UserTO((String) data[0],
                (String) data[1],
                (String) data[2],
                (Date) data[3],
                (String) data[4],
                (String) data[5],
                (String) data[6],
                (String) data[7],
                type,
                (Period) data[9],
                (AccessToken) token);

    }

}
