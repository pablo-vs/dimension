package es.ucm.fdi.connectivity;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.workspace.project.ProjectTO;

/**
 * A shared project that can be read by everyone and modified by its authors.
 *
 * @author Pablo Villalobos
 * @version 04.04.2018
 */
public class SharedProjectBOOpenProjectImp extends SharedProjectBO {
        /**
         * List of authors
         */
	private List<String> authors;
        
        /**
         * Class constructor specifying id, existing project and list of authors.
         * @param ID Identifier
         * @param proj Project
         * @param auth List of authors
         */
	public SharedProjectBOOpenProjectImp(String ID, ProjectTO proj, List<String> auth) {
		super(ID, proj);
		authors = new ArrayList<>(auth);
	}
        
        /**
         * Class constructor specifying id, existing project and an author.
         * @param ID Identifier
         * @param proj Project
         * @param author Owner
         */
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
