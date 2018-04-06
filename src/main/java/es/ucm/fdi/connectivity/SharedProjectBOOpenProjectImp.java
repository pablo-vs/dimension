package es.ucm.fdi.connectivity;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.connectivity.SharedProjectBO;
import es.ucm.fdi.trabajo.ProjectTO;

/**
 * A shared project that can be read by everyone and modified by its authors.
 *
 * @author Pablo Villalobos
 * @version 04.04.2018
 */
public class SharedProjectBOOpenProjectImp extends SharedProjectBO {
	private List<String> authors;

	public SharedProjectBOOpenProjectImp(String ID, ProjectTO proj, List<String> auth) {
		super(ID, proj);
		authors = new ArrayList<>(auth);
	}

	public SharedProjectBOOpenProjectImp(String ID, ProjectTO proj, String author) {
		super(ID, proj);
		authors = new ArrayList<>();
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
