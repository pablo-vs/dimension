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

import es.ucm.fdi.business.connectivity.CommentDTO;
import java.sql.SQLException;
import java.sql.JDBCType;

import java.util.List;

import es.ucm.fdi.integration.exceptions.DAOErrorException;
import es.ucm.fdi.integration.data.DAOSQLImp;
import java.util.ArrayList;
import java.sql.Date;

/**
 * The SQL implementation for CommentDAO.
 *
 * @author Inmaculada Pérez, Pablo Villalobos
 */
public class CommentDAOSQLImp extends DAOSQLImp<CommentDTO> implements CommentDAO {

    private static final int REQUIRED_LENGTH = 5;

    private static final String TABLE = "comments";

    private static final String[] COLUMNS = {"id", "author", "project", "text", "date"};

    private static final JDBCType[] COLUMN_TYPES = {JDBCType.VARCHAR, JDBCType.VARCHAR,
        JDBCType.VARCHAR, JDBCType.VARCHAR, JDBCType.DATE};

    public CommentDAOSQLImp() {
        super(TABLE, COLUMNS, COLUMN_TYPES);
    }
    
    @Override
    public void addComment(CommentDTO comment) throws DAOErrorException {
        try {
            addRecord(comment);
        } catch (SQLException e) {
            throw new DAOErrorException("Error while adding comment " + comment.getId()
                    + ".\n" + e.getMessage(), e);
        }
    }

    @Override
    public void removeComment(CommentDTO comment) throws DAOErrorException {
        try {
            deleteRecord(comment.getId());
        } catch (SQLException e) {
            throw new DAOErrorException("Error while removing comment " + comment.getId()
                    + ".\n" + e.getMessage(), e);
        }
    }

    @Override
    public List<CommentDTO> findByUser(String username) throws DAOErrorException {
        List<CommentDTO> result;
        try {
            result = findByValue(1, username);
        } catch (SQLException e) {
            throw new DAOErrorException("Error while finding comments from " + username
                    + ".\n" + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public List<CommentDTO> findByProject(String project) throws DAOErrorException {
        List<CommentDTO> result;
        try {
            result = findByValue(2, project);
        } catch (SQLException e) {
            throw new DAOErrorException("Error while finding comments of " + project
                    + ".\n" + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public List<CommentDTO> getComments() {
        List<CommentDTO> result;
        try {
            result = getAllRecords();
        } catch (SQLException e) {
            throw new DAOErrorException("Error while reading all comments.\n"
                    + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public CommentDTO build(List<Object> data) {
        if (data.size() != REQUIRED_LENGTH) {
            throw new IllegalArgumentException("Constructor requires 5 objects, "
                    + data.size() + " given");
        }
        if (!(data.get(0) instanceof String && data.get(1) instanceof String
                && data.get(2) instanceof String && data.get(3) instanceof String
                && data.get(4) instanceof Date)) {
            throw new IllegalArgumentException("Invalid data type");
        }
        return new CommentDTO((String) data.get(1),
                (String) data.get(2),
                (String) data.get(3),
                (Date) data.get(4));
    }

    @Override
    public List<Object> getData(CommentDTO c) {
        List<Object> data = new ArrayList<>();
        data.add(c.getId());
        data.add(c.getAuthor());
        data.add(c.getIDProject());
        data.add(c.getText());
        data.add(c.getDate());
        return data;
    }
}
