/*
  This file is part of Dimension.
  Dimension is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  Dimension is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  You should have received a copy of the GNU General Public License
  along with Dimension.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.integration.connectivity;

import es.ucm.fdi.business.connectivity.SharedProjectDTO;

import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import es.ucm.fdi.integration.data.DAOSQLImp;
import es.ucm.fdi.integration.exceptions.DAOErrorException;

public class SharedProjectDAOSQLImp extends DAOSQLImp<SharedProjectDTO> implements
        SharedProjectDAO {

    private static final int REQUIRED_LENGTH = 3;

    private static final String TABLE = "sharedprojects";

    private static final String[] COLUMNS = {"id", "name", "data"};

    private static final JDBCType[] COLUMN_TYPES = {JDBCType.VARCHAR, JDBCType.VARCHAR,
        JDBCType.VARCHAR};

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
            find = findByValue(0, id);
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
            result = findByValue(1, name);
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
        if (data.size() != REQUIRED_LENGTH) {
            throw new IllegalArgumentException("Constructor requires 3 objects, "
                    + data.size() + " given");
        }
        if (!(data.get(0) instanceof String && data.get(1) instanceof String
                && data.get(2) instanceof String)) {
            throw new IllegalArgumentException("Invalid data type");
        }
        try {
			return (SharedProjectDTO) JAXBContext.newInstance("es.ucm.fdi.business.connectivity:"
	        		+ "es.ucm.fdi.business.workspace.project:"
            		+ "es.ucm.fdi.business.workspace.function.types.binary:"
            		+ "es.ucm.fdi.business.workspace.function.types.unary"
            		).createUnmarshaller().unmarshal(new ByteArrayInputStream(((String)data.get(2)).getBytes()));
		} catch (JAXBException e) {
			throw new DAOErrorException("Could not unmarshal project.\n" + e.getMessage(), e);
		}
    }

    @Override
    public List<Object> getData(SharedProjectDTO obj) {
        ArrayList<Object> data = new ArrayList<Object>();
        try {
	        ByteArrayOutputStream str = new ByteArrayOutputStream();
	        JAXBContext.newInstance("es.ucm.fdi.business.connectivity:"
	        		+ "es.ucm.fdi.business.workspace.project:"
	        		+ "es.ucm.fdi.business.workspace.function.types.binary:"
	        		+ "es.ucm.fdi.business.workspace.function.types.unary"
	        		).createMarshaller().marshal(obj, str);
	        
	        data.add(obj.getSharedID());
	        data.add(obj.getID());
	        data.add(str.toString());
        } catch (JAXBException e) {
            throw new DAOErrorException("Could not marshall project.\n" + e.getMessage(), e);
        }
        return data;
    }

}
