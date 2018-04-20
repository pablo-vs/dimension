package demo.sql;

import java.sql.*;

public class SQLTest {
	@Test
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		// Load the JDBC driver
		Class.forName("org.mariadb.jdbc.Driver");
		System.out.println("Driver loaded");

		// Try to connect
		Connection connection = DriverManager.getConnection
			("jdbc:mysql://83.41.170.105/dimension", "dimension", "dimension");

		System.out.println("It works!");

		connection.close();
	}
}
