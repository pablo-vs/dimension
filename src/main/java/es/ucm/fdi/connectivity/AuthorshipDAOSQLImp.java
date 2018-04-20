package es.ucm.fdi.connectivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;

import java.util.List;

import es.ucm.fdi.exceptions.DAOError;

public class AuthorshipDAOSQLImp implements AuthorshipDAO{

	private static final String DRIVER = "org.mariadb.jdbc.Driver";
	private static final String TABLE = "users";
	private static final String DEFAULT_DATABASE = "dimension";
	private static final String DEFAULT_HOST = "83.41.170.105";
	private static final String DEFAULT_USER = "dimension";
	private static final String DEFAULT_PASSWD = "dimension";

	private String host, db, user, passwd;

	public AuthorshipDAOSQLImp(String host, String db, String user, String password)
	throws SQLException, ClassNotFoundException {
		// Load the JDBC driver
		Class.forName(DRIVER);

		this.host = host;
		this.db = db;
		this.user = user;
		this.passwd = passwd;

		System.err.println("jdbc:mysql://" + host + "/" + db);
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
    	try(Connection connection = DriverManager.getConnection
			("jdbc:mysql://" + host + "/" + db, user, passwd)) {

	    	String[] values = {auth.getId(), auth.getAuthor(), auth.getProject()};
			try(Statement stat = connection.createStatement()) {
				StringBuilder query = new StringBuilder();
				query.append("INSERT INTO ");
				query.append(TABLE);
				query.append(" VALUES ");
				query.append(commaList(values));
				query.append(";");

				try(ResultSet res = stat.executeQuery(query.toString())) {}
				catch(SQLException e) {
					throw new DAOError("DAO error: " + e.getMessage(), e);
				}
			} catch(SQLException e) {
				throw new DAOError("DAO error: " + e.getMessage(), e);
			}
		} catch(SQLException e) {
			throw new DAOError("DAO error: " + e.getMessage(), e);
		}

			
		
    }

    /**
     * Removes a authorship from the database.
     *
     * @param auth The authorship to remove.
     */
    @Override
    public void removeAuthorship(AuthorshipBO auth) throws DAOError {

    }

    /**
     * Find an autorship in the database matching the given username.
     *
     * @param username The identifier of the user.
     * @return A List of authorships where the author is the given user.
     */
    @Override
    public List<AuthorshipBO> findByUser(String username) throws DAOError {
    	return null;
    }

    /**
     * Find autorships in the database matching the given project.
     *
     * @param project The identifier of the project.
     * @return A List of authorships where the project is the given one.
     */
    @Override
    public List<AuthorshipBO> findByProject(String project) throws DAOError {
    	return null;
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