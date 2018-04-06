package es.ucm.fdi.connectivity;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.workspace.project.ProjectTO;

/**
 * A shared project that can be read only by a list of people allowed and modified by its authors.
 *
 * @author Javier Galiana
 * @version 05.04.2018
 */
public class SharedProjectBOPrivateProjectImp extends SharedProjectBO {

	private List<String> authors;
	private List<String> viewers;
	
	public SharedProjectBOPrivateProjectImp(String ID, ProjectTO proj, List<String> auth, List<String> view) {
		super(ID, proj);
		authors = new ArrayList<>(auth);
		viewers = new ArrayList<>(view);
	}
	
	public SharedProjectBOPrivateProjectImp(String ID, ProjectTO proj, String author) {
		super(ID, proj);
		authors = new ArrayList<>();
		authors.add(author);
	}

	@Override
	public boolean hasReadAccess(String username) {
		return authors.contains(username) || viewers.contains(username);
	}

	@Override
	public boolean hasWriteAccess(String username) {
		return authors.contains(username);
	}

}
