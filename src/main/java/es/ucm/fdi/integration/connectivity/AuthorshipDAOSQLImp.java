package es.ucm.fdi.integration.connectivity;

import es.ucm.fdi.business.connectivity.AuthorshipDTO;
import java.sql.SQLException;
import java.sql.JDBCType;

import java.util.List;

import es.ucm.fdi.integration.exceptions.DAOErrorException;
import es.ucm.fdi.integration.data.DAOSQLImp;
import java.util.ArrayList;

/**
 *
 * @author Inmaculada Pérez, Pablo Villalobos
 */
public class AuthorshipDAOSQLImp extends DAOSQLImp<AuthorshipDTO> implements AuthorshipDAO {

    private static final int REQUIERED_LENGTH = 3;

    private static final String TABLE = "authorships";

    private static final String[] COLUMNS = {"id", "author", "project"};

    private static final JDBCType[] COLUMN_TYPES = {JDBCType.VARCHAR, JDBCType.VARCHAR,
        JDBCType.VARCHAR};

    public AuthorshipDAOSQLImp() {
        super(TABLE, COLUMNS, COLUMN_TYPES);
    }

    /**
     * Adds a new authorship to the database.
     *
     * @param auth The new authorship as a AuthorshipDTO.
     */
    @Override
    public void addAuthorship(AuthorshipDTO auth) throws DAOErrorException {
        try {
            addRecord(auth);
        } catch (SQLException e) {
            throw new DAOErrorException("Error while adding authorship " + auth.getId()
                    + ".\n" + e.getMessage(), e);
        }
    }

    /**
     * Removes a authorship from the database.
     *
     * @param auth The authorship to remove.
     */
    @Override
    public void removeAuthorship(AuthorshipDTO auth) throws DAOErrorException {
        try {
            deleteRecord(auth.getId());
        } catch (SQLException e) {
            throw new DAOErrorException("Error while removing authorship " + auth.getId()
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
    public List<AuthorshipDTO> findByUser(String username) throws DAOErrorException {
        List<AuthorshipDTO> result;
        try {
            result = findByVal(1, username);
        } catch (SQLException e) {
            throw new DAOErrorException("Error while finding authorships from " + username
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
    public List<AuthorshipDTO> findByProject(String project) throws DAOErrorException {
        List<AuthorshipDTO> result;
        try {
            result = findByVal(2, project);
        } catch (SQLException e) {
            throw new DAOErrorException("Error while finding authorships of " + project
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
    public List<AuthorshipDTO> getAuthorships() throws DAOErrorException {
        List<AuthorshipDTO> result;
        try {
            result = getAllRecords();
        } catch (SQLException e) {
            throw new DAOErrorException("Error while reading all authorships.\n"
                    + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public AuthorshipDTO build(List<Object> data) {
        if (data.size() != REQUIERED_LENGTH) {
            throw new IllegalArgumentException("Constructor requires 3 objects, "
                    + data.size() + " given");
        }
        if (!(data.get(0) instanceof String && data.get(1) instanceof String
                && data.get(2) instanceof String)) {
            throw new IllegalArgumentException("Invalid data type");
        }
        return new AuthorshipDTO((String) data.get(1),
                (String) data.get(2));
    }

    @Override
    public List<Object> getData(AuthorshipDTO auth) {
        List<Object> data = new ArrayList<>();
        data.add(auth.getId());
        data.add(auth.getAuthor());
        data.add(auth.getProject());
        return data;
    }

}
