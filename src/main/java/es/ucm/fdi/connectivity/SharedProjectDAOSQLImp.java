package es.ucm.fdi.connectivity;

import java.sql.JDBCType;	
import java.sql.SQLException;
import java.util.List;

import es.ucm.fdi.data.DAOSQLImp;
import es.ucm.fdi.exceptions.DAOError;

public class SharedProjectDAOSQLImp extends DAOSQLImp<SharedProjectBO> implements
		SharedProjectDAO {

	private static final String TABLE = "projects";

    private static final String[] COLUMNS = {"id", "name", "data"};

    private static final JDBCType[] COLUMN_TYPES = {JDBCType.VARCHAR, JDBCType.VARCHAR,
        JDBCType.BLOB};
	
	public SharedProjectDAOSQLImp() {
		super(TABLE, COLUMNS, COLUMN_TYPES);
	}

	@Override
	public void addSharedProject(SharedProjectBO proj) {
		try {
            addRecord(proj);
        } catch (SQLException e) {
            throw new DAOError("Error while adding project " + proj.getID()
                    + ".\n" + e.getMessage(), e);
        }
	}

	@Override
	public void removeSharedProject(String id) {
		try {
            deleteRecord(id);
        } catch (SQLException e) {
            throw new DAOError("Error while removing project " + id
                    + ".\n" + e.getMessage(), e);
        }
	}

	@Override
	public void modifySharedProject(SharedProjectBO proj) {
		 try {
	            modifyRecord(proj);
	        } catch (SQLException e) {
	            throw new DAOError("Error while modifying project " + proj.getID()
	                    + ".\n" + e.getMessage(), e);
	        }
	}

	@Override
	public SharedProjectBO findSharedProject(String id) {
		SharedProjectBO result = null;
        List<SharedProjectBO> find;
        try {
            find = findByVal(0, id);
        } catch (SQLException e) {
            throw new DAOError("Error while finding project " + id
                    + ".\n" + e.getMessage(), e);
        }
        if (find.size() == 1) {
            result = find.get(0);
        }
        return result;
	}

	@Override
	public List<SharedProjectBO> findByName(String name) {
		List<SharedProjectBO> result;
        try {
            result = findByVal(1, name);
        } catch (SQLException e) {
            throw new DAOError("Error while finding projects called " + name
                    + ".\n" + e.getMessage(), e);
        }
        return result;
	}

	@Override
	public List<SharedProjectBO> getSharedProjects() {
		List<SharedProjectBO> result;
        try {
            result = getAllRecords();
        } catch (SQLException e) {
            throw new DAOError("Error while reading all users.\n"
                    + e.getMessage(), e);
        }
        return result;
	}

	@Override
	public Object[] getData(SharedProjectBO obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SharedProjectBO build(Object[] data) {
		// TODO Auto-generated method stub
		return null;
	}

}
