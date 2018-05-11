package es.ucm.fdi.integration_tier.connectivity;

import es.ucm.fdi.business_tier.connectivity.SharedProjectDTO;
import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.integration_tier.data.MemoryDB;

/**
 * Represents a hashtable that contains the shared projects.
 *
 * @author Eduardo Amaya, Pablo Villalobos
 */
public class SharedProjectDAOHashTableImp implements SharedProjectDAO {

    private MemoryDB<SharedProjectDTO> database = new MemoryDB<>();

    /**
     * Empty class constructor.
     */
    public SharedProjectDAOHashTableImp() {
    }

    /**
     * Adds a new project to the database.
     *
     * @param proj The new project as a SharedProjectDTO.
     */
    @Override
    public void addSharedProject(SharedProjectDTO proj) {
        database.insert(proj, proj.getSharedID());
    }

    /**
     * Removes a project from the database.
     *
     * @param id The identifier of the project.
     */
    @Override
    public void removeSharedProject(String id) {
        database.removeId(id);
    }

    /**
     * If there is a project with the same identifier as the given one, replaces
     * it.
     *
     * @param proj A SharedProjectDTO containing the new data of the project.
     */
    @Override
    public void modifySharedProject(SharedProjectDTO proj) {
        database.insert(proj, proj.getSharedID());
    }

    /**
     * Finds a project in the database.
     *
     * @param id The identifier of the project.
     * @return A SharedProjectDTO containing the data of the project, or null if
 no project was found.
     */
    @Override
    public SharedProjectDTO findSharedProject(String id) {
        return database.find(id);
    }

    /**
     * Find a project by name.
     *
     * @param name The name of the project.
     * @return the A List of projects with that name.
     */
    @Override
    public List<SharedProjectDTO> findByName(String name) {
        ArrayList<SharedProjectDTO> results = new ArrayList<>();
        for (String s : database.getIds()) {
            SharedProjectDTO proj = database.find(s);
            if (proj.getID().equals(name)) {
                results.add(proj);
            }
        }
        return results;
    }

    /**
     * Returns all the stored projects.
     *
     * @return A List of SharedProjectBOs.
     */
    @Override
    public List<SharedProjectDTO> getSharedProjects() {
        ArrayList<SharedProjectDTO> results = new ArrayList<>();
        database.getIds().forEach((s) -> {
            results.add(database.find(s));
        });
        return results;
    }

}
