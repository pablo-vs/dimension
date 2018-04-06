package es.ucm.fdi.trabajo;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.datos.MemoryDB;
import es.ucm.fdi.workspace.project.ProjectDAO;
import es.ucm.fdi.workspace.project.ProjectTO;


/**
 * ProjectDAO implementation using a HashTable-based database.
 *
 * @author Eduardo Amaya
 * @version 05.04.2018
 */

public class ProjectDAOHashTableImp implements ProjectDAO {

	private MemoryDB<ProjectTO> bd;
	
	public ProjectDAOHashTableImp() {
		bd = new MemoryDB<ProjectTO>();
	}
	
	public void addProject(ProjectTO proj) {
		bd.insert(proj, proj.getID());
	}
	
	public void removeProject(String ID) {
		bd.removeId(ID);
	}
	
	public void modifyProject(ProjectTO proj) {
		if (findProject(proj.getID()) != null) {
			bd.insert(proj, proj.getID());
		} else {
			throw new IllegalArgumentException("The project " + proj.getID() + " does not exist");
		}
	}
	
	public ProjectTO findProject(String id) {
		return bd.find(id);
	}

	public boolean containsProject(String id) {
		return bd.getIds().contains(id);
	}
	
	public List<ProjectTO> getProjects() {
		ArrayList<ProjectTO> lista = new ArrayList<>();
		for (String id : bd.getIds()) {
			lista.add(bd.find(id));
		}
		return lista;
	}


	
}
