/**
 * This file is part of Dimension.
 * Dimension is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Dimension is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Dimension.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.business.connectivity;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.business.workspace.project.ProjectDTO;

/**
 * A shared project that can be read by everyone and modified by its authors.
 *
 * @author Pablo Villalobos
 */
public class SharedProjectDTOOpenProjectImp extends SharedProjectDTO {

    /**
     * List of authors
     */
    private List<String> authors = new ArrayList<>();

    /**
     * Class constructor specifying id, existing project and list of authors.
     *
     * @param ID Identifier
     * @param proj Project
     * @param authors List of authors
     */
    public SharedProjectDTOOpenProjectImp(String ID, ProjectDTO proj, List<String> authors) {
        super(ID, proj);
        this.authors = new ArrayList<>(authors);
    }

    /**
     * Class constructor specifying id, existing project and an author.
     *
     * @param ID Identifier
     * @param proj Project
     * @param author Owner
     */
    public SharedProjectDTOOpenProjectImp(String ID, ProjectDTO proj, String author) {
        super(ID, proj);
        authors.add(author);
    }

    @Override
    public boolean hasReadAccess(String username) {
        return true;
    }

    @Override
    public boolean hasWriteAccess(String username) {
        return authors.contains(username);
    }
}
