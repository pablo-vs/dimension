package es.ucm.fdi.integration_tier.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;

import es.ucm.fdi.business_tier.exceptions.DAOError;

public class SQLDataSource {

    private static final String DRIVER = "org.mariadb.jdbc.Driver";
    private static final String DB = "dimension";
    private static final String HOST = "localhost";
    private static final String USER = "root";
    private static final String PASSWD = "";

    private static SQLDataSource instance;
    private BasicDataSource dataSource;

    private SQLDataSource() throws SQLException {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName(DRIVER);
        dataSource.setUrl("jdbc:mysql://" + HOST + "/" + DB);
        dataSource.setUsername(USER);
        dataSource.setPassword(PASSWD);
    }

    public static PreparedStatement getStatement(String stmt) throws SQLException {
        if (instance == null) {
            instance = new SQLDataSource();
        }
        return instance.dataSource.getConnection().prepareStatement(stmt);
    }
}
