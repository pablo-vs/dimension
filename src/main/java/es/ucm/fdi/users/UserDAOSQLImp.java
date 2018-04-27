package es.ucm.fdi.users;

import java.util.List;
import java.sql.*;
import java.time.Period;

import es.ucm.fdi.connectivity.AuthorshipBO;
import es.ucm.fdi.exceptions.DAOError;
import es.ucm.fdi.util.SQLUtil;

public class UserDAOSQLImp implements UserDAO{
	private static final String DRIVER = "org.mariadb.jdbc.Driver";
	private static final String TABLE = "users";
	private static final String DEFAULT_DATABASE = "dimension";
	private static final String DEFAULT_HOST = "localhost";
	private static final String DEFAULT_USER = "root";
	private static final String DEFAULT_PASSWD = "";

	private String host, db, user, passwd;

	public UserDAOSQLImp(String host, String db, String user, String password)
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
	
	public UserDAOSQLImp() throws SQLException, ClassNotFoundException {
		this(DEFAULT_HOST, DEFAULT_DATABASE, DEFAULT_USER, DEFAULT_PASSWD);
	}

	/**
     * Adds a new user to the database.
     *
     * @param auth The new user as a UserTO.
     */
	@Override
    public void addUser(UserTO user) throws DAOError {
		String[] values1 = {user.getID(), user.getName(), user.getPassword()};
		String[] values2 = {user.getEmail(), user.getTelephone(), user.getPicture(),
							user.getDescription(), String.valueOf(user.getType())};
		Date date = (Date) user.getDate();
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO ");
		query.append(TABLE);
		query.append(" VALUES ");
		query.append(commaList(values1));
		query.append(date);
		query.append(user.getBanTime());
		query.append(commaList(values2));
		query.append(";");	
		SQLUtil.executeQuery(query.toString());
    }

	@Override
	public void removeUser(String id) {
		StringBuilder query = new StringBuilder();
		query.append("DELETE FROM ");
		query.append(TABLE);
		query.append(" WHERE id = ");
		query.append(id);
		query.append(";");
		SQLUtil.executeQuery(query.toString());
	}
	
	private String modify(String column, String update, String id) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(TABLE);
		query.append(" SET ");
		query.append(column);
		query.append(" = ");
		query.append(update);
		query.append(" WHERE id = ");
		query.append(id);
		query.append(";");
		return query.toString();
	}

	@Override
	public void modifyUser(UserTO user) {
		StringBuilder query = new StringBuilder();
		query.append(modify("name", user.getName(), user.getID()) + "\n");
		query.append(modify("password", user.getPassword(), user.getID()) + "\n");
		query.append(modify("date", user.getDate(), user.getID()) + "\n");
		query.append(modify("email", user.getEmail(), user.getID()) + "\n");
		query.append(modify("telephone", user.getTelephone(), user.getID()) + "\n");
		query.append(modify("picture", user.getPicture(), user.getID()) + "\n");
		query.append(modify("description", user.getDescription(), user.getID()) + "\n");
		query.append(modify("type", String.valueOf(user.getType()), user.getID()) + "\n");
		query.append(modify("ban time", user.getBanTime(), user.getID()));
		SQLUtil.executeQuery(query.toString());
	}

	@Override
	public UserTO findUser(String id) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM ");
		query.append(TABLE);
		query.append(" WHERE id = ");
		query.append(id);
		query.append(";");
		ResultSet res = SQLUtil.executeQuery(query.toString());
		return (UserTO) res;
	}

	@Override
	public List<UserTO> getUsers() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM ");
		query.append(TABLE);
		ResultSet res = SQLUtil.executeQuery(query.toString());
		return (List<UserTO>) res;
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
