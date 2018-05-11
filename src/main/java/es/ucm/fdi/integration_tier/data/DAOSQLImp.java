package es.ucm.fdi.integration_tier.data;

import java.sql.JDBCType;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

/**
 * 
 * @author Inmaculada Pérez, Pablo Villalobos
 * @param <T> 
 */
public abstract class DAOSQLImp<T> {

    private String table;

    private String[] columns;

    private JDBCType[] columnTypes;

    public DAOSQLImp(String table, String[] columns, JDBCType[] columnTypes) {
        this.table = table;
        this.columns = columns;
        this.columnTypes = columnTypes;
    }

    public void addRecord(T obj) throws SQLException {
        List data = getData(obj);
        StringBuilder sb = new StringBuilder("(?");
        for (int i = 1; i < data.size(); ++i) {
            sb.append(",?");
        }
        sb.append(")");
        try (PreparedStatement stmt = SQLDataSource.getStatement("INSERT INTO "
                + table + " VALUES " + sb.toString())) {
            for (int i = 0; i < data.size(); ++i) {
                stmt.setObject(i + 1, data.get(i), columnTypes[i]);
            }
            stmt.execute();
        } catch (IllegalArgumentException | SQLException e) {
            throw new SQLException("Could not add record to table:\n" + e.getMessage(), e);
        }
    }

    public void deleteRecord(String id) throws SQLException {
        try (PreparedStatement stmt = SQLDataSource.getStatement("DELETE FROM "
                + table + " WHERE " + columns[0] + " = ?")) {
            stmt.setString(1, id);
            stmt.execute();
        } catch (SQLException e) {
            throw new SQLException("Could not remove record from table:\n" + e.getMessage(), e);
        }
    }

    public void modifyRecord(T obj) throws SQLException {
        List data = getData(obj);
        StringBuilder sb = new StringBuilder();
        sb.append(columns[0]).append(" = ?");
        for (int i = 1; i < data.size(); ++i) {
            sb.append(", ").append(columns[i]).append(" = ?");
        }
        try (PreparedStatement stmt = SQLDataSource.getStatement("UPDATE TABLE "
                + table + " SET " + sb.toString() + " WHERE " + columns[0] + " = ?")) {
            for (int i = 0; i < data.size(); ++i) {
                stmt.setObject(i + 1, data.get(i), columnTypes[i]);
            }
            stmt.setObject(data.size() + 1, data.get(0), columnTypes[0]);
            stmt.execute();
        } catch (IllegalArgumentException | SQLException e) {
            throw new SQLException("Could not modify record in table:\n" + e.getMessage(), e);
        }
    }

    public List<T> findByVal(int col, Object val) throws SQLException {
        StringBuilder query = new StringBuilder();
        if (col < 0 || col >= columns.length) {
            throw new SQLException("No column with index " + col + " in table " + table);
        }
        if (val == null) {
            query.append("SELECT * FROM ").append(table);
        } else {
            query.append("SELECT * FROM ").append(table).append(" WHERE ")
                    .append(columns[col]).append(" = ?");
        }
        ArrayList<T> results = new ArrayList<>();
        try (PreparedStatement stmt = SQLDataSource.getStatement(query.toString())) {
            if (val != null) {
                stmt.setObject(1, val, columnTypes[col]);
            }
            ResultSet rs = stmt.executeQuery();
            rs.first();
            while (!rs.isAfterLast()) {
                T obj = build(readData(rs));
                results.add(obj);
                rs.next();
            }
        } catch (IllegalArgumentException | SQLException e) {
            throw new SQLException("Could not retrieve results from table:\n" +
                    e.getMessage(), e);
        }
        return results;
    }

    public List<T> getAllRecords() throws SQLException {
        return findByVal(0, null);
    }

    private List<Object> readData(ResultSet rs) throws SQLException {
        List<Object> data = new ArrayList<>();
        for (int i = 0; i < columns.length; ++i) {
            switch (columnTypes[i]) {
                case VARCHAR: {
                    data.add(rs.getString(i));
                    break;
                }
                case INTEGER: {
                    data.add(rs.getInt(i));
                    break;
                }
                case DATE: {
                    data.add(rs.getDate(i));
                    break;
                }
                default:
                    data.add(null);
            }
        }

        return data;
    }

    public abstract List<Object> getData(T obj);

    public abstract T build(List<Object> data);

}
