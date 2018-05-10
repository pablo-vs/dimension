package es.ucm.fdi.connectivity;

import java.sql.SQLException;
import java.sql.JDBCType;

import java.util.List;

import es.ucm.fdi.exceptions.DAOError;
import es.ucm.fdi.data.DAOSQLImp;

public class CommentDAOSQLImp extends DAOSQLImp<CommentBO> implements CommentDAO {

    private static final String TABLE = "comments";

    private static final String[] COLUMNS = {"id", "author", "project", "text"};

    private static final JDBCType[] COLUMN_TYPES = {JDBCType.VARCHAR, JDBCType.VARCHAR,
        JDBCType.VARCHAR, JDBCType.VARCHAR};

    public CommentDAOSQLImp() {
        super(TABLE, COLUMNS, COLUMN_TYPES);
    }

    /**
     * Adds a new comment to the database.
     *
     * @param auth The new comment as a CommentBO.
     */
    @Override
    public void addComment(CommentBO comment) throws DAOError {
        try {
            addRecord(comment);
        } catch (SQLException e) {
            throw new DAOError("Error while adding comment " + comment.getId()
                    + ".\n" + e.getMessage(), e);
        }
    }

    /**
     * Removes a comment from the database.
     *
     * @param auth The comment to remove.
     */
    @Override
    public void removeComment(CommentBO comment) throws DAOError {
        try {
            deleteRecord(comment.getId());
        } catch (SQLException e) {
            throw new DAOError("Error while removing comment " + comment.getId()
                    + ".\n" + e.getMessage(), e);
        }
    }

    /**
     * Find an comment in the database matching the given username.
     *
     * @param username The identifier of the user.
     * @return A List of comments where the author is the given user.
     */
    @Override
    public List<CommentBO> findByUser(String username) throws DAOError {
        List<CommentBO> result;
        try {
            result = findByVal(1, username);
        } catch (SQLException e) {
            throw new DAOError("Error while finding comments from " + username
                    + ".\n" + e.getMessage(), e);
        }
        return result;
    }

    /**
     * Find comments in the database matching the given project.
     *
     * @param project The identifier of the project.
     * @return A List of comments where the project is the given one.
     */
    @Override
    public List<CommentBO> findByProject(String project) throws DAOError {
        List<CommentBO> result;
        try {
            result = findByVal(2, project);
        } catch (SQLException e) {
            throw new DAOError("Error while finding comments of " + project
                    + ".\n" + e.getMessage(), e);
        }
        return result;
    }

    /**
     * Returns all the stored comments.
     *
     * @return A List of CommentBOs.
     */
    @Override
    public List<CommentBO> getComments() {
        List<CommentBO> result;
        try {
            result = getAllRecords();
        } catch (SQLException e) {
            throw new DAOError("Error while reading all comments.\n"
                    + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public CommentBO build(Object[] data) {
        if (data.length != 4) {
            throw new IllegalArgumentException("Constructor requires 4 objects, "
                    + data.length + " given");
        }
        if (!(data[0] instanceof String && data[1] instanceof String
                && data[2] instanceof String && data[3] instanceof String)) {
            throw new IllegalArgumentException("Invalid data type");
        }
        return new CommentBO((String) data[1],
                (String) data[2],
                (String) data[3]);
    }

    @Override
    public Object[] getData(CommentBO c) {
        Object[] data = {c.getId(), c.getAuthor(), c.getProj(), c.getText()};
        return data;
    }

}
