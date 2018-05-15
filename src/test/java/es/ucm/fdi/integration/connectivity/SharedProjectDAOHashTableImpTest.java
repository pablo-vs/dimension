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

import org.junit.Test;

import es.ucm.fdi.business.connectivity.SharedProjectDTO;
import es.ucm.fdi.business.connectivity.SharedProjectDTOOpenProjectImp;
import es.ucm.fdi.business.workspace.Visualization;
import es.ucm.fdi.business.workspace.project.ProjectDTO;
import es.ucm.fdi.business.workspace.project.ProjectManagerAS;
import es.ucm.fdi.business.workspace.project.WorkAS;
import es.ucm.fdi.integration.connectivity.SharedProjectDAOHashTableImp;
import es.ucm.fdi.integration.project.ProjectDAOHashTableImp;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * JUnit test for SharedProjectDAOHashTableImp class.
 *
 * @see SharedProjectDAOHashTableImp
 * @author Javier Galiana
 */
public class SharedProjectDAOHashTableImpTest {

	@Test
	public void SharedProjectDAOHashTableTest() {

		SharedProjectDAOHashTableImp dao = new SharedProjectDAOHashTableImp();
		ProjectDTO exponencial = new ProjectDTO("exponential");
		ProjectDTO logaritmica = new ProjectDTO("logaritmica");
		ProjectDTO trigonometrica = new ProjectDTO("trigonometrica");
		WorkAS proj = new WorkAS(exponencial);

		SharedProjectDTO shared1 = new SharedProjectDTOOpenProjectImp(
				"superProyecto", exponencial, "pepe");
		SharedProjectDTO shared2 = new SharedProjectDTOOpenProjectImp(
				"megaProyecto", logaritmica, "Maria");
		SharedProjectDTO shared3 = new SharedProjectDTOOpenProjectImp(
				"proyectoMediocre", trigonometrica, "Mar");
		
		ArrayList<SharedProjectDTO> results1 = new ArrayList<>(), 
									results2 = new ArrayList<>();
		
		results1.add(shared1);
		results2.add(shared3);
		results2.add(shared1);
		results2.add(shared2);

		dao.addSharedProject(shared1);
		dao.addSharedProject(shared2);
		dao.addSharedProject(shared3);

		assertEquals("Invalid sharedProject search results", results1,
				dao.findByName("exponential"));
		assertEquals("The sharedProjects cannot be obtained", results2,
				dao.getSharedProjects());

		Visualization views = new Visualization();
		proj.addVisualizationBO(views);
		dao.modifySharedProject(shared1);

		assertEquals("The sharedProject found is not the expected", shared1,
				dao.findSharedProject(shared1.getSharedID()));

		dao.removeSharedProject(shared1.getSharedID());
		dao.removeSharedProject(shared2.getSharedID());
		dao.removeSharedProject(shared3.getSharedID());
	}
}