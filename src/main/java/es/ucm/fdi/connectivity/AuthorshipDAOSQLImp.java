package es.ucm.fdi.connectivity;

import java.sql.SQLException;
import java.sql.JDBCType;

import java.util.List;

import es.ucm.fdi.exceptions.DAOError;
import es.ucm.fdi.data.DAOSQLImp;

public class AuthorshipDAOSQLImp extends DAOSQLImp<AuthorshipBO> implements AuthorshipDAO {

    private static final String TABLE = "authors";

    private static final String[] COLUMNS = {"id", "author", "project"};

    private static final JDBCType[] COLUMN_TYPES = {JDBCType.VARCHAR, JDBCType.VARCHAR,
        JDBCType.VARCHAR};

    public AuthorshipDAOSQLImp() {
        super(TABLE, COLUMNS, COLUMN_TYPES);
    }

    /**
     * Adds a new authorship to the database.
     *
     * @param auth The new authorship as a AuthorshipBO.
     */
    @Override
    public void addAuthorship(AuthorshipBO auth) throws DAOError {
        try {
            addRecord(auth);
        } catch (SQLException e) {
            throw new DAOError("Error while adding authorship " + auth.getId()
                    + ".\n" + e.getMessage(), e);
        }
    }

    /**
     * Removes a authorship from the database.
     *
     * @param auth The authorship to remove.
     */
    @Override
    public void removeAuthorship(AuthorshipBO auth) throws DAOError {
        try {
            deleteRecord(auth.getId());
        } catch (SQLException e) {
            throw new DAOError("Error while removing authorship " + auth.getId()
                    + ".\n" + e.getMessage(), e);
        }
    }

    /**
     * Find an autorship in the database matching the given username.
     *
     * @param username The identifier of the user.
     * @return A List of authorships where the author is the given user.
     */
    @Override
    public List<AuthorshipBO> findByUser(String username) throws DAOError {
        List<AuthorshipBO> result;
        try {
            result = findByVal(1, username);
        } catch (SQLException e) {
            throw new DAOError("Error while finding authorships from " + username
                    + ".\n" + e.getMessage(), e);
        }
        return result;
    }

    /**
     * Find autorships in the database matching the given project.
     *
     * @param project The identifier of the project.
     * @return A List of authorships where the project is the given one.
     */
    @Override
    public List<AuthorshipBO> findByProject(String project) throws DAOError {
        List<AuthorshipBO> result;
        try {
            result = findByVal(2, project);
        } catch (SQLException e) {
            throw new DAOError("Error while finding authorships of " + project
                    + ".\n" + e.getMessage(), e);
        }
        return result;
    }

    /**
     * Returns all the stored authorships.
     *
     * @return A List of AuthorshipBOs.
     */
    @Override
    public List<AuthorshipBO> getAuthorships() throws DAOError {
        List<AuthorshipBO> result;
        try {
            result = getAllRecords();
        } catch (SQLException e) {
            throw new DAOError("Error while reading all authorships.\n"
                    + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public AuthorshipBO build(Object[] data) {
        if (data.length != 3) {
            throw new IllegalArgumentException("Constructor requires 3 objects, "
                    + data.length + " given");
        }
        if (!(data[0] instanceof String && data[1] instanceof String && data[2] instanceof String)) {
            throw new IllegalArgumentException("Invalid data type");
        }
        return new AuthorshipBO((String) data[1],
                (String) data[2]);
    }

    @Override
    public Object[] getData(AuthorshipBO auth) {
        Object[] data = {auth.getId(), auth.getAuthor(), auth.getProject()};
        return data;
    }

}
