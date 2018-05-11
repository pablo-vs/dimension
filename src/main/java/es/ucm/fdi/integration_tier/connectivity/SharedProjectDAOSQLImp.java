package es.ucm.fdi.integration_tier.connectivity;

import es.ucm.fdi.business_tier.connectivity.SharedProjectDTO;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.List;

import es.ucm.fdi.integration_tier.data.DAOSQLImp;
import es.ucm.fdi.integration_tier.exceptions.DAOErrorException;

public class SharedProjectDAOSQLImp extends DAOSQLImp<SharedProjectDTO> implements
        SharedProjectDAO {

    private static final String TABLE = "projects";

    private static final String[] COLUMNS = {"id", "name", "data"};

    private static final JDBCType[] COLUMN_TYPES = {JDBCType.VARCHAR, JDBCType.VARCHAR,
        JDBCType.BLOB};

    public SharedProjectDAOSQLImp() {
        super(TABLE, COLUMNS, COLUMN_TYPES);
    }

    @Override
    public void addSharedProject(SharedProjectDTO proj) {
        try {
            addRecord(proj);
        } catch (SQLException e) {
            throw new DAOErrorException("Error while adding project " + proj.getID()
                    + ".\n" + e.getMessage(), e);
        }
    }

    @Override
    public void removeSharedProject(String id) {
        try {
            deleteRecord(id);
        } catch (SQLException e) {
            throw new DAOErrorException("Error while removing project " + id
                    + ".\n" + e.getMessage(), e);
        }
    }

    @Override
    public void modifySharedProject(SharedProjectDTO proj) {
        try {
            modifyRecord(proj);
        } catch (SQLException e) {
            throw new DAOErrorException("Error while modifying project " + proj.getID()
                    + ".\n" + e.getMessage(), e);
        }
    }

    @Override
    public SharedProjectDTO findSharedProject(String id) {
        SharedProjectDTO result = null;
        List<SharedProjectDTO> find;
        try {
            find = findByVal(0, id);
        } catch (SQLException e) {
            throw new DAOErrorException("Error while finding project " + id
                    + ".\n" + e.getMessage(), e);
        }
        if (find.size() == 1) {
            result = find.get(0);
        }
        return result;
    }

    @Override
    public List<SharedProjectDTO> findByName(String name) {
        List<SharedProjectDTO> result;
        try {
            result = findByVal(1, name);
        } catch (SQLException e) {
            throw new DAOErrorException("Error while finding projects called " + name
                    + ".\n" + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public List<SharedProjectDTO> getSharedProjects() {
        List<SharedProjectDTO> result;
        try {
            result = getAllRecords();
        } catch (SQLException e) {
            throw new DAOErrorException("Error while reading all users.\n"
                    + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public SharedProjectDTO build(List<Object> data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Object> getData(SharedProjectDTO obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
