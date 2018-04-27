package es.ucm.fdi.connectivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.exceptions.DAOError;
import es.ucm.fdi.util.SQLUtil;

public class AuthorshipDAOSQLImp implements AuthorshipDAO{

	private static final String DRIVER = "org.mariadb.jdbc.Driver";
	private static final String TABLE = "authors";
	private static final String DEFAULT_DATABASE = "dimension";
	private static final String DEFAULT_HOST = "localhost";
	private static final String DEFAULT_USER = "root";
	private static final String DEFAULT_PASSWD = "";

	private String host, db, user, passwd;

	public AuthorshipDAOSQLImp(String host, String db, String user, String password)
	throws SQLException, ClassNotFoundException {
		// Load the JDBC driver
		Class.forName(DRIVER);

		this.host = host;
		this.db = db;
		this.user = user;
		this.passwd = passwd;

		// Try to connect
		try(Connection connection = DriverManager.getConnection
			("jdbc:mysql://" + host + "/" + db, user, password)) {}
		catch(SQLException e) {
			throw e;
		}
	}

	public AuthorshipDAOSQLImp() throws SQLException, ClassNotFoundException {
		this(DEFAULT_HOST, DEFAULT_DATABASE, DEFAULT_USER, DEFAULT_PASSWD);
	}

	/**
     * Adds a new authorship to the database.
     *
     * @param auth The new authorship as a AuthorshipBO.
     */
	@Override
    public void addAuthorship(AuthorshipBO auth) throws DAOError {
    	String[] values = {auth.getId(), auth.getAuthor(), auth.getProject()};
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO ");
		query.append(TABLE);
		query.append(" VALUES ");
		query.append(commaList(values));
		query.append(";");
		try(Statement stat = SQLUtil.getStatement()) {
			stat.executeQuery(query.toString());
		} catch (SQLException e) {
			throw new DAOError("DAO Error:\n" + e.getMessage(), e);
		}
    }

    /**
     * Removes a authorship from the database.
     *
     * @param auth The authorship to remove.
     */
    @Override
    public void removeAuthorship(AuthorshipBO auth) throws DAOError {
		StringBuilder query = new StringBuilder();
		query.append("DELETE FROM ");
		query.append(TABLE);
		query.append(" WHERE id = '");
		query.append(auth.getId());
		query.append("';");
		try(Statement stat = SQLUtil.getStatement()) {
			stat.executeQuery(query.toString());
		} catch (SQLException e) {
			throw new DAOError("DAO Error:\n" + e.getMessage(), e);
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
    	ArrayList<AuthorshipBO> result = new ArrayList<AuthorshipBO>();
    	try(Statement stat = SQLUtil.getStatement()) {
    		ResultSet rs = SQLUtil.getStatement().executeQuery("SELECT * FROM authors WHERE author = '" + username + "';");

			rs.first();
			while(!rs.isAfterLast()) {
				AuthorshipBO auth = new AuthorshipBO(rs.getString("author"), rs.getString("project"));
				result.add(auth);
				rs.next();
			}
			
    	} catch (SQLException e) {
			throw new DAOError("DAO Error:\n" + e.getMessage(), e);
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
    	ArrayList<AuthorshipBO> result = new ArrayList<AuthorshipBO>();
    	try(Statement stat = SQLUtil.getStatement()) {
    		ResultSet rs = SQLUtil.getStatement().executeQuery("SELECT * FROM authors WHERE project = '" + project + "';");

			rs.first();
			while(!rs.isAfterLast()) {
				AuthorshipBO auth = new AuthorshipBO(rs.getString("author"), rs.getString("project"));
				result.add(auth);
				rs.next();
			}
			
    	} catch (SQLException e) {
			throw new DAOError("DAO Error:\n" + e.getMessage(), e);
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
    	return null;
    }

    
    private String commaList(String[] values) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("(");
    	for(String s : values) {
    		sb.append("'");
			sb.append(s);
			sb.append("', ");
		}
		sb.setCharAt(sb.length()-2, ')');
		return sb.substring(0, sb.length()-1);
    }

}