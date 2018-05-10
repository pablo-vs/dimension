package es.ucm.fdi.connectivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.exceptions.DAOError;
import es.ucm.fdi.util.SQLUtil;

public class CommentDAOSQLImp implements CommentDAO {

    private static final String DRIVER = "org.mariadb.jdbc.Driver";
    private static final String TABLE = "comments";
    private static final String DEFAULT_DATABASE = "dimension";
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWD = "";

    private String host, db, user, passwd;

    /**
     * Class constructor.
     * 
     * @param host
     * @param db
     * @param user
     * @param password
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public CommentDAOSQLImp(String host, String db, String user, String password)
            throws SQLException, ClassNotFoundException {
        // Load the JDBC driver
        Class.forName(DRIVER);

        this.host = host;
        this.db = db;
        this.user = user;
        this.passwd = password;

        // Try to connect
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + db, user, password)) {
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Class constructor.
     * 
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public CommentDAOSQLImp() throws SQLException, ClassNotFoundException {
        this(DEFAULT_HOST, DEFAULT_DATABASE, DEFAULT_USER, DEFAULT_PASSWD);
    }

    /**
     * Adds a new comment to the database.
     *
     * @param comment The new comment as a CommentBO.
     */
    @Override
    public void addComment(CommentBO comment) throws DAOError {
        String[] values = {comment.getId(), comment.getAuthor(), comment.getProj(), comment.getText()};
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ");
        query.append(TABLE);
        query.append(" VALUES ");
        query.append(commaList(values));
        query.append(";");
        try (Statement stat = SQLUtil.getStatement()) {
            stat.executeQuery(query.toString());
        } catch (SQLException e) {
            throw new DAOError("DAO Error:\n" + e.getMessage(), e);
        }
    }

    /**
     * Removes a comment from the database.
     *
     * @param comment The comment to remove.
     */
    @Override
    public void removeComment(CommentBO comment) throws DAOError {
        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM ");
        query.append(TABLE);
        query.append(" WHERE id = '");
        query.append(comment.getId());
        query.append("';");
        try (Statement stat = SQLUtil.getStatement()) {
            stat.executeQuery(query.toString());
        } catch (SQLException e) {
            throw new DAOError("DAO Error:\n" + e.getMessage(), e);
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
        ArrayList<CommentBO> result = new ArrayList<>();
        try (Statement stat = SQLUtil.getStatement()) {
            ResultSet rs = SQLUtil.getStatement().executeQuery("SELECT * FROM authors WHERE author = '" + username + "';");
            rs.first();
            while (!rs.isAfterLast()) {
                CommentBO comment = new CommentBO(rs.getString("author"), rs.getString("project"), rs.getString("text"));
                result.add(comment);
                rs.next();
            }

        } catch (SQLException e) {
            throw new DAOError("DAO Error:\n" + e.getMessage(), e);
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
        ArrayList<CommentBO> result = new ArrayList<>();
        try (Statement stat = SQLUtil.getStatement()) {
            ResultSet rs = SQLUtil.getStatement().executeQuery("SELECT * FROM authors WHERE project = '" + project + "';");

            rs.first();
            while (!rs.isAfterLast()) {
                CommentBO comment = new CommentBO(rs.getString("author"), rs.getString("project"), rs.getString("text"));
                result.add(comment);
                rs.next();
            }

        } catch (SQLException e) {
            throw new DAOError("DAO Error:\n" + e.getMessage(), e);
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
        ArrayList<CommentBO> result = new ArrayList<>();
        try (Statement stat = SQLUtil.getStatement()) {
            ResultSet rs = SQLUtil.getStatement().executeQuery("SELECT * FROM comments;");
            rs.first();
            while (!rs.isAfterLast()) {
                CommentBO comment = new CommentBO(rs.getString("author"), rs.getString("project"), rs.getString("text"));
                result.add(comment);
                rs.next();
            }
        } catch (SQLException e) {
            throw new DAOError("DAO Error:\n" + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 
     * @param values
     * @return the values of the list splitted with commas
     */
    private String commaList(String[] values) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (String s : values) {
            sb.append("'");
            sb.append(s);
            sb.append("', ");
        }
        sb.setCharAt(sb.length() - 2, ')');
        return sb.substring(0, sb.length() - 1);
    }

}
