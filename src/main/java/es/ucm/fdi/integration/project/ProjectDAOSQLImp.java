/**
 * 
 */
package es.ucm.fdi.integration.project;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;	
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.JDBCType;
import java.sql.SQLException;

import es.ucm.fdi.business.workspace.project.ProjectDTO;
import es.ucm.fdi.business.workspace.project.ProjectDTO;
import es.ucm.fdi.integration.data.DAOSQLImp;
import es.ucm.fdi.integration.exceptions.DAOErrorException;

/**
 * @author usuario_local
 *
 */
public class ProjectDAOSQLImp extends DAOSQLImp<ProjectDTO> implements ProjectDAO {

	
	private static final String TABLE = "projects";
	private static final String [] COLUMNS = {"id", "user", "name", "data"};
	private static final JDBCType [] COLUMN_TYPES = {JDBCType.VARCHAR, JDBCType.VARCHAR,
		JDBCType.VARCHAR, JDBCType.VARCHAR};
	private static final int REQUIRED_LENGTH = 4;
	
	public ProjectDAOSQLImp() {
		super(TABLE, COLUMNS, COLUMN_TYPES);
	}
	
	/**
     * Adds a new project to the database.
     *
     * @param project The new project as a ProjectDTO.
     */
    @Override
    public void addProject(ProjectDTO project, String user) throws DAOErrorException {
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
            deleteRecord(id);
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

    /**
     * Returns all the stored projects.
     *
     * @return A List of projectTOs.
     */
    @Override
    public List<ProjectDTO> getProjects() {
        List<ProjectDTO> result;
        try {
            result = getAllRecords();
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
            data.add(proj.getID() + proj.get());
            data.add(proj.getID());
            
            data.add(proj.getPassword());
            data.add(proj.getDate());
            data.add(proj.getEmail());
            data.add(proj.getTelephone());
            data.add(proj.getPicture());
            data.add(proj.getDescription());
            data.add(proj.getType());
            data.add(proj.getBanTime() == null ? null : u.getBanTime().toInstant());

            return data;
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not serialize AccessToken for project "
                    + u.getID());
        }
    }

    @Override
    public ProjectDTO build(List<Object> data) {
        if (data.size() != REQUIRED_LENGTH) {
            throw new IllegalArgumentException("Constructor requires 11 objects, "
                    + data.size() + " given");
        }
        if (!(data.get(0) instanceof String && (data.get(1) == null || data.get(1) instanceof String)
	      && data.get(2) instanceof String
	      && (data.get(3) == null || data.get(3) instanceof Date)
	      && (data.get(4) == null || data.get(4) instanceof String)
	      && (data.get(5) == null || data.get(5) instanceof String)
	      && (data.get(6) == null || data.get(6) instanceof String)
	      && (data.get(7) == null || data.get(7) instanceof String)
	      && data.get(8) instanceof Integer
	      && (data.get(4) == null || data.get(9) instanceof ZonedDateTime))) {
            throw new IllegalArgumentException("Invalid data type");
        }

	projectType type = projectType.fromInt((Integer) data.get(8));

        return new ProjectDTO();
    }

}
