package es.ucm.fdi.connectivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A shared project that can be read only by a list of people allowed and modified by its authors.
 *
 * @author Javier Galiana
 * @version 05.04.2018
 */
public class SharedProjectBOPrivateProjectImp extends SharedProjectBO {

	private List<String> authors;
	private List<String> viewers;
	
	public SharedProjectBOPrivateProjectImp(String ID, String name, List<String> auth, List<String> view) {
		super(ID, name);
		authors = new ArrayList<>(auth);
		viewers = new ArrayList<>(view);
	}
	
	public SharedProjectBOPrivateProjectImp(String ID, String name, String author) {
		super(ID, name);
		authors = new ArrayList<>();
		authors.add(author);
		viewers.add(author);
	}

	@Override
	public boolean hasReadAccess(String username) {
		return viewers.contains(username);
	}

	@Override
	public boolean hasWriteAccess(String username) {
		return authors.contains(username);
	}

}
