/**
 * This file is part of Dimension.
 * Dimension is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Dimension is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Dimension.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.integration.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;

public class SQLDataSource {

    private static final String DRIVER = "org.mariadb.jdbc.Driver";
    private static final String DB = "dimension";
    private static final String HOST = "83.41.170.105";
    private static final String USER = "dimension";
    private static final String PASSWD = "dimension";

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
