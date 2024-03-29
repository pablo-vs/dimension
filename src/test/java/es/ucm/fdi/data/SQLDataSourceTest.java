package es.ucm.fdi.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Test;
import static org.junit.Assert.*;

public class SQLDataSourceTest {

    @Test
    public void statementTest() throws SQLException {
        try (PreparedStatement stmt = SQLDataSource.getStatement("SHOW TABLES")) {
            assertTrue(stmt.execute());
        } catch (SQLException e) {
            throw e;
        }
    }
}
