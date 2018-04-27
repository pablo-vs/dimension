package es.ucm.fdi.util;

import java.sql.*;

import es.ucm.fdi.exceptions.DAOError;

public class SQLUtil {
	
	private static final String DRIVER = "org.mariadb.jdbc.Driver";
	private static final String TABLE = "authors";
	private static final String DB = "dimension";
	private static final String HOST = "localhost";
	private static final String USER = "root";
	private static final String PASSWD = "";
	
	public static ResultSet executeQuery(String query) {
		try(Connection connection = DriverManager.getConnection
				("jdbc:mysql://" + HOST + "/" + DB, USER, PASSWD)) {

				try(Statement stat = connection.createStatement()) {
					try(ResultSet res = stat.executeQuery(query)) {
						return res;
					}
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
}
