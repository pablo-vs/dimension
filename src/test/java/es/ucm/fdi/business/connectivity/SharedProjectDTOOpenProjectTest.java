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

import java.util.ArrayList;

import org.junit.Test;

import es.ucm.fdi.business.workspace.project.ProjectDTO;
import static org.junit.Assert.assertEquals;

/**
 * JUnit test for SharedProjectDTOOpenProject class.
 *
 * @see SharedProjectDTOOpenProject
 * @author Javier Galiana
 */
public class SharedProjectDTOOpenProjectTest {

    @Test
    public void SharedProjectDTOOpenTest() {

        ProjectDTO proj1 = new ProjectDTO("proj1");
        SharedProjectDTOOpenProjectImp openShared1 = new SharedProjectDTOOpenProjectImp(
                proj1.getID(), proj1, "Peter");
        ArrayList<String> authors = new ArrayList<>();
        authors.add("Peter");
        authors.add("Tim");
        authors.add("Josh");
        SharedProjectDTOOpenProjectImp openShared2 = new SharedProjectDTOOpenProjectImp(
                proj1.getID(), proj1, authors);

        assertEquals("Everyone can read a Open Project", true,
                openShared1.hasReadAccess("Josh") && openShared1.hasReadAccess("Tim")
                && openShared1.hasReadAccess("Peter"));
        assertEquals("This user is allowed to modify the project", true,
                openShared2.hasWriteAccess("Peter"));
        assertEquals("This user is not allowed to modify the project", false,
                openShared2.hasWriteAccess("Nate"));
    }
}
