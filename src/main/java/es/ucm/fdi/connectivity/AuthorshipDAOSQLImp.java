package es.ucm.fdi.connectivity;

import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.exceptions.DAOError;
import es.ucm.fdi.data.DAOSQLImp;

public class AuthorshipDAOSQLImp extends DAOSQLImp implements AuthorshipDAO {

    private static final String TABLE = "authors";
    
    private String [] COLUMNS = {"id", "author", "project"};

    private JDBCType [] columnTypes = {JDBCTypes.VARCHAR, JDBCTypes.VARCHAR, JDBCTypes.VARCHAR};

    public AuthorshipDAOSQLImp() {
        super(TABLE);
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
	    removeRecord(auth);
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
	    result = findByVal(2, username);
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

}
