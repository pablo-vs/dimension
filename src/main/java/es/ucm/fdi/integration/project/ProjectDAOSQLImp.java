/**
 *
 */
package es.ucm.fdi.integration.project;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.JDBCType;
import java.sql.SQLException;

import es.ucm.fdi.business.workspace.project.ProjectDTO;
import es.ucm.fdi.data.DAOSQLImp;
import es.ucm.fdi.integration.exceptions.DAOErrorException;

/**
 * @author usuario_local
 *
 */
public class ProjectDAOSQLImp extends DAOSQLImp<ProjectDTO> implements ProjectDAO {

    private static final String TABLE = "projects";
    private static final String[] COLUMNS = {"id", "user", "name", "data"};
    private static final JDBCType[] COLUMN_TYPES = {JDBCType.VARCHAR, JDBCType.VARCHAR,
        JDBCType.VARCHAR, JDBCType.VARCHAR};
    private static final int REQUIRED_LENGTH = 4;
    private final String user;

    public ProjectDAOSQLImp(String user) {
        super(TABLE, COLUMNS, COLUMN_TYPES);
        this.user = user;
    }

    /**
     * Adds a new project to the database.
     *
     * @param project The new project as a ProjectDTO.
     */
    @Override
    public void addProject(ProjectDTO project) throws DAOErrorException {
        try {
            addRecord(project);
        } catch (SQLException e) {
            throw new DAOErrorException("Error while adding project " + project.getID()
                    + ".\n" + e.getMessage(), e);
        }
    }

    /**
     * Removes a project from the database.
     *
     * @param id The project to remove.
     */
    @Override
    public void removeProject(String id) {
        try {
            deleteRecord(nameToID(id));
        } catch (SQLException e) {
            throw new DAOErrorException("Error while removing project " + id
                    + ".\n" + e.getMessage(), e);
        }
    }

    /**
     * Modifies a project.
     *
     * @param project The project to modify.
     */
    @Override
    public void modifyProject(ProjectDTO project) {
        try {
            modifyRecord(project);
        } catch (SQLException e) {
            throw new DAOErrorException("Error while modifying project " + project.getID()
                    + ".\n" + e.getMessage(), e);
        }
    }

    /**
     * Find an project in the database matching the given id.
     *
     * @param id The identifier of the project.
     * @return The project where the id is the given one.
     */
    @Override
    public ProjectDTO findProject(String id) {
        ProjectDTO result = null;
        List<ProjectDTO> find;
        try {
            find = findByValue(0, nameToID(id));
        } catch (SQLException e) {
            throw new DAOErrorException("Error while finding project " + id
                    + ".\n" + e.getMessage(), e);
        }
        if (find.size() == 1) {
            result = find.get(0);
        }
        return result;
    }

    /**
     * Returns all the stored projects.
     *
     * @return A List of projectTOs.
     */
    @Override
    public List<ProjectDTO> getProjects() {
        List<ProjectDTO> result;
        try {
            result = findByValue(1, user);
        } catch (SQLException e) {
            throw new DAOErrorException("Error while reading all projects.\n"
                    + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public List<Object> getData(ProjectDTO proj) {
        try {
            List<Object> data = new ArrayList<>();
            ByteArrayOutputStream str = new ByteArrayOutputStream();
            JAXBContext.newInstance("es.ucm.fdi.business.workspace.project:"
                    + "es.ucm.fdi.business.workspace.function.types.binary:"
                    + "es.ucm.fdi.business.workspace.function.types.unary"
            ).createMarshaller().marshal(proj, str);

            data.add(nameToID(proj.getID()));
            data.add(user);
            data.add(proj.getID());
            data.add(str.toString());

            return data;
        } catch (JAXBException e) {
            throw new DAOErrorException("Could not marshall project.\n" + e.getMessage(), e);
        }
    }

    @Override
    public ProjectDTO build(List<Object> data) {
        if (data.size() != REQUIRED_LENGTH) {
            throw new IllegalArgumentException("Constructor requires 4 objects, "
                    + data.size() + " given");
        }
        if (!(data.get(0) instanceof String && (data.get(1) == null || data.get(1) instanceof String)
                && data.get(2) instanceof String && data.get(3) instanceof String)) {
            throw new IllegalArgumentException("Invalid data type");
        }

        try {
            return (ProjectDTO) JAXBContext.newInstance("es.ucm.fdi.business.workspace.project:"
                    + "es.ucm.fdi.business.workspace.function.types.binary:"
                    + "es.ucm.fdi.business.workspace.function.types.unary"
            ).createUnmarshaller().unmarshal(new ByteArrayInputStream(((String) data.get(3)).getBytes()));
        } catch (JAXBException e) {
            throw new DAOErrorException("Could not unmarshal project.\n" + e.getMessage(), e);
        }
    }

    private String nameToID(String name) {
        return user + "/" + name;
    }

    @Override
    public boolean containsProject(String id) {
        // TODO Auto-generated method stub
        return false;
    }

}
