package demo.sql;

import java.sql.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class SQLTest {

    @Test
    public void SQLTest() throws SQLException, ClassNotFoundException {
        // Load the JDBC driver
        Class.forName("org.mariadb.jdbc.Driver");
        System.out.println("Driver loaded");

        // Try to connect
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/dimension", "root", "");

        System.out.println("It works!");

        connection.close();
    }
}
