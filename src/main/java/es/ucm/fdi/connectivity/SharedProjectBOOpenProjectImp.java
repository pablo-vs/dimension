package es.ucm.fdi.connectivity;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.connectivity.SharedProjectBO;

/**
 * A shared project that can be read by everyone and modified by its authors.
 *
 * @author Pablo Villalobos
 * @version 04.04.2018
 */
public class SharedProjectBOOpenProjectImp extends SharedProjectBO {
	private List<String> authors;

	public SharedProjectBOOpenProjectImp(String ID, String name, List<String> auth) {
		super(ID, name);
		authors = new ArrayList<>(auth);
	}

	public SharedProjectBOOpenProjectImp(String ID, String name, String author) {
		super(ID, name);
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
