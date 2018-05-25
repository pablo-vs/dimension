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
import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.data.MemoryDB;

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

    @Override
    public void addSharedProject(SharedProjectDTO proj) {
        database.insert(proj, proj.getSharedID());
    }

    @Override
    public void removeSharedProject(String id) {
        database.removeId(id);
    }

    @Override
    public void modifySharedProject(SharedProjectDTO proj) {
        database.insert(proj, proj.getSharedID());
    }

    @Override
    public SharedProjectDTO findSharedProject(String id) {
        return database.find(id);
    }

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

    @Override
    public List<SharedProjectDTO> getSharedProjects() {
        ArrayList<SharedProjectDTO> results = new ArrayList<>();
        database.getIds().forEach((s) -> {
            results.add(database.find(s));
        });
        return results;
    }
}
