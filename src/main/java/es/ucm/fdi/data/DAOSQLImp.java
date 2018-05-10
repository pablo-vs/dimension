package es.ucm.fdi.data;

import java.sql.JDBCType;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.data.SQLDataSource;


public abstract class DAOSQLImp<T> {

    private String table;
    
    private String [] columns;

    private JDBCType [] columnTypes;

    public DAOSQLImp(String table, String[] columns, JDBCType [] columnTypes) {
	this.table = table;
	this.columns = columns;
	this.columnTypes = columnTypes;
    }

    public void addRecord(T obj) throws SQLException {
	Object [] data = getData(obj);
	StringBuilder sb = new StringBuilder();
	sb.append("(?");
	for(int i = 1; i < data.length; ++i) {
	    sb.append(",?");
	}
	sb.append(")");
	try (PreparedStatement stmt = SQLDataSource.getStatement("INSERT INTO " + table + " VALUES "
								 + sb.toString())) {
	    for(int i = 0; i < data.length; ++i) {
		stmt.setObject(i+1, data[i], columnTypes[i]);
	    }
	    stmt.execute();
	} catch(IllegalArgumentException |SQLException e) {
	    throw new SQLException("Could not add record to table:\n" + e.getMessage(), e);
	}
    }

    public void deleteRecord(String id) throws SQLException {
	try (PreparedStatement stmt = SQLDataSource.getStatement
	     ("DELETE FROM " + table + " WHERE " + columns[0] + " = ?")) {
	    stmt.setString(1, id);
	    stmt.execute();
	} catch(SQLException e) {
	    throw new SQLException("Could not remove record from table:\n" + e.getMessage(), e);
	}
    }

     public void modifyRecord(T obj) throws SQLException {
	Object [] data = getData(obj);
	StringBuilder sb = new StringBuilder();
	sb.append(columns[0] + " = ?");
	for(int i = 1; i < data.length; ++i) {
	    sb.append(", " + columns[i] + " = ?");
	}
	try (PreparedStatement stmt = SQLDataSource.getStatement
	     ("UPDATE TABLE " + table + " SET " + sb.toString() + " WHERE " + columns[0] + " = ?")) {
	    for(int i = 0; i < data.length; ++i) {
		stmt.setObject(i+1, data[i], columnTypes[i]);
	    }
	    stmt.setObject(data.length+1, data[0], columnTypes[0]);
	    stmt.execute();
	} catch(IllegalArgumentException | SQLException e) {
	    throw new SQLException("Could not modify record in table:\n" + e.getMessage(), e);
	}
     }
    
    public List<T> findByVal(int col, Object val) throws SQLException {
	String query;
	if(col < 0 || col >= columns.length) {
	    throw new SQLException("No column with index " + col + " in table " + table);
	}
	if(val == null) {
	    query = "SELECT * FROM " + table;
	} else {
	    query = "SELECT * FROM " + table + " WHERE " + columns[col] + " = ?";
	}
	ArrayList<T> results = new ArrayList<T>();
	
	try (PreparedStatement stmt = SQLDataSource.getStatement(query)) {
	    
	    if(val != null) stmt.setObject(1, val, columnTypes[col]);
	    ResultSet rs = stmt.executeQuery();

	    rs.first();
            while (!rs.isAfterLast()) {
		Object [] data = readData(rs);
		
                T obj = build(data);
                results.add(obj);
                rs.next();
            }

	} catch(IllegalArgumentException | SQLException e) {
	    throw new SQLException("Could not retrieve results from table:\n" + e.getMessage(), e);
	}

	return results;
    }

    public List<T> getAllRecords() throws SQLException {
	return findByVal(0, null);
    }

    private Object [] readData (ResultSet rs) throws SQLException {
	Object [] data = new Object[columns.length];

	for(int i = 0; i < columns.length; ++i) {
	    switch(columnTypes[i]) {
	    case VARCHAR: {
		data[i] = rs.getString(i);
		break;
	    }
	    case INTEGER: {
		data[i] = rs.getInt(i);
		break;
	    }
	    case DATE: {
		data[i] = rs.getDate(i);
		break;
	    }
	    default:
		data[i] = null;
	    }
	}
	
	return data;
    }

    public abstract Object [] getData(T obj);

    public abstract T build(Object [] data);
    
}
