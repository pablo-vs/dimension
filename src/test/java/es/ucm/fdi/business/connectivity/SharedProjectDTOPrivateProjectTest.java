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

import es.ucm.fdi.business.workspace.Visualization;
import es.ucm.fdi.business.workspace.project.ProjectDTO;
import static org.junit.Assert.*;

/**
 * JUnit test for SharedProjectDTOPrivateProject class.
 *
 * @see SharedProjectDTOPrivateProject
 * @author Javier Galiana
 */

public class SharedProjectDTOPrivateProjectTest {

	@Test
	public void SharedProjectDTOPrivateTest() {

		ProjectDTO proj1 = new ProjectDTO("proj1");
		ArrayList<String> authors = new ArrayList<>(), viewers = new ArrayList<>();

		authors.add("marta");
		authors.add("paco");
		authors.add("juan");

		viewers.add("pepe");
		viewers.add("sara");
		viewers.add("juan");

		SharedProjectDTOPrivateProjectImp privateShared = new SharedProjectDTOPrivateProjectImp(
				proj1.getID(), proj1, authors, viewers);

		assertEquals("This user is allowed to read the project", true,
				privateShared.hasReadAccess("sara"));
		assertEquals("This user is not allowed to read the project", false,
				privateShared.hasReadAccess("jose"));
		assertEquals("This user is allowed to modify the project", true,
				privateShared.hasWriteAccess("paco"));
		assertEquals("This user is not allowed to modify the project", false,
				privateShared.hasWriteAccess("jose"));
	}
}
