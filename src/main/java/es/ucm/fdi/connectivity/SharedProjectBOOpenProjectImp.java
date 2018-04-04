package es.ucm.fdi.connectivity;

import java.util.List;
import java.util.ArrayList;

/**
 * A shared project that can be read by everyone and modified by its authors.
 *
 * @author Pablo Villalobos
 * @version 04.04.2018
 */
public class SharedProjectBOOpenProjectImp extends SharedProjectBO {
	private List<String> authors;

	public SharedProjectBOOpenProjectImp(String project, List<String> auth) {
		super(project);
		authors = new ArrayList<>(auth);
	}

	public SharedProjectBOOpenProjectImp(String project, String author) {
		super(project);
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
