package es.ucm.fdi.integration_tier.project;

import es.ucm.fdi.business_tier.workspace.project.ProjectDTO;
import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.integration_tier.data.MemoryDB;

/**
 * ProjectDAO implementation using a HashTable-based database.
 *
 * @author Eduardo Amaya, Inmapg
 */
public class ProjectDAOHashTableImp implements ProjectDAO {

    /**
     * Database
     */
    private MemoryDB<ProjectDTO> db = new MemoryDB<>();

    /**
     * Class constructor.
     */
    public ProjectDAOHashTableImp() {
    }

    @Override
    public void addProject(ProjectDTO proj) {
        db.insert(proj, proj.getID());
    }

    @Override
    public void removeProject(String ID) {
        db.removeId(ID);
    }

    @Override
    public void modifyProject(ProjectDTO proj) {
        if (findProject(proj.getID()) != null) {
            db.insert(proj, proj.getID());
        } else {
            throw new IllegalArgumentException("The project " + proj.getID() + " does not exist");
        }
    }

    @Override
    public ProjectDTO findProject(String id) {
        return db.find(id);
    }

    @Override
    public boolean containsProject(String id) {
        return db.getIds().contains(id);
    }

    @Override
    public List<ProjectDTO> getProjects() {
        ArrayList<ProjectDTO> lista = new ArrayList<>();
        db.getIds().forEach((id) -> {
            lista.add(db.find(id));
        });
        return lista;
    }

}
