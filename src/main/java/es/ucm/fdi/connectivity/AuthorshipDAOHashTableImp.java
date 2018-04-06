package es.ucm.fdi.connectivity;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.datos.BDMemoria;
import es.ucm.fdi.users.UserTO;

/**
 * AuthorShipDAO implementation using a HashTable-based database.
 *
 * @author Javier Galiana
 * @version 05.04.2018
 */
public class AuthorshipDAOHashTableImp implements AuthorshipDAO {
	
	private BDMemoria<AuthorshipBO> bd;
	
	@Override
	public void addAuthorship(AuthorshipBO auth) {
		bd.insert(auth, auth.getId());
	}

	@Override
	public void removeAuthorship(AuthorshipBO auth) {
		bd.removeId(auth.getId());
	}

	@Override
	public List<AuthorshipBO> findByUser(String username) {
		ArrayList<AuthorshipBO> lista = new ArrayList<>();
		for (String id : bd.getIds()) {
			AuthorshipBO aux = bd.find(id);
			if(aux.getAuthor() == username) {
				lista.add(aux);
			}
		}
		return lista;
	}

	@Override
	public List<AuthorshipBO> findByProject(String project) {
		ArrayList<AuthorshipBO> lista = new ArrayList<>();
		for (String id : bd.getIds()) {
			AuthorshipBO aux = bd.find(id);
			if(aux.getProject() == project) {
				lista.add(aux);
			}
		}
		return lista;
	}

	@Override
	public List<AuthorshipBO> getAuthorships() {
		ArrayList<AuthorshipBO> lista = new ArrayList<>();
		for (String id : bd.getIds()) {
			lista.add(bd.find(id));
		}
		return lista;
	}

}
