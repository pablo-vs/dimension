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
package es.ucm.fdi.integration.project;

import es.ucm.fdi.business.workspace.project.ProjectDTO;
import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.integration.data.MemoryDB;

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
    private final String user;

    /**
     * Class constructor.
     */
    public ProjectDAOHashTableImp(String user) {
        this.user = user;
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
