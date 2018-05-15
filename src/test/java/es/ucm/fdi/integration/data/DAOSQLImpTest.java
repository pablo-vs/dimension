package es.ucm.fdi.integration.data;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.JDBCType;
import java.sql.SQLException;

import org.junit.Test;
import static org.junit.Assert.*;

import es.ucm.fdi.integration.data.DAOSQLImp;

public class DAOSQLImpTest extends DAOSQLImp<DataType> {

    private static final String TABLE = "test";
    private static final String [] COLUMNS = {"id", "num", "date"};
    private static final JDBCType [] COLUMN_TYPES = {
	JDBCType.VARCHAR, JDBCType.INTEGER, JDBCType.DATE};

    public DAOSQLImpTest() {
	super(TABLE, COLUMNS, COLUMN_TYPES);
    }
    
    @Test
    public void addFindRemoveTest() throws SQLException {
	ArrayList<DataType> results = new ArrayList<DataType>(),
	    results2 = new ArrayList<DataType>();
	
	results.add(new DataType("Record1", 1, new Date(1, 1, 1)));
	results.add(new DataType("Record2", 2, new Date(2, 2, 2)));

	results2.add(results.get(0));
	try {
	    addRecord(results.get(0));
	    addRecord(results.get(1));
	    assertEquals("Search result does not match expected", results2, findByValue(0, "Record1"));
	    assertEquals("Search result does not match expected", results2, findByValue(1, 1));
	    DataType modif = results.get(1);
	    modif.num = 1;
	    results.set(1, modif);
	    modifyRecord(modif);
	    assertEquals("Search result does not match expected", results, findByValue(1, 1));
	    deleteRecord("Record2");
	    assertEquals("Search result does not match expected", results2, getAllRecords());
	    clear();
	} catch(SQLException e) {
	    throw e;
	}
    }

    @Override
    public List<Object> getData(DataType obj) {
	ArrayList<Object> data = new ArrayList<Object>();
	data.add(obj.ID);
	data.add(obj.num);
	data.add(obj.date);
	return data;
    }

    @Override
    public DataType build(List<Object> data) {
	if(data.size() != 3) throw new IllegalArgumentException("Expected 3 argument");
	if(!(data.get(0) instanceof String && data.get(1) instanceof Integer
	     && data.get(2) instanceof Date))
	    throw new IllegalArgumentException("String expected");
	else return new DataType((String)data.get(0), (Integer)data.get(1), (Date)data.get(2));
    }
}

class DataType {

    public String ID;
    public int num;
    public Date date;

    public DataType(String ID, int num, Date date) {
	this.ID = ID;
	this.num = num;
	this.date = date;
    }

    @Override
    public boolean equals(Object other) {
	return other instanceof DataType && ((DataType)other).ID.equals(ID) &&
	    ((DataType)other).num == num && ((DataType)other).date.equals(date);
    }
}
