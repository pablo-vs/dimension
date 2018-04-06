package es.ucm.fdi.connectivity;

import java.util.List;

import es.ucm.fdi.workspace.project.ProjectTO;

/**
 * Represents a project which has been shared.
 *
 * @author Pablo Villalobos
 * @version 04.04.2018
 */
public abstract class SharedProjectBO extends ProjectTO{
	private String sharedID;
	
	public SharedProjectBO(String ID, String name) {
		super(name);
		sharedID = ID;
	}

	public SharedProjectBO(String ID, ProjectTO proj) {
		super(proj);
		sharedID = ID;
	}
	
	/**
	 * Check if a user can read the project.
	 *
	 * @param username Identifier of the user to check.
	 * @returns True if this user has read access to the project.
	 */
	public abstract boolean hasReadAccess(String username);

	/**
	 * Check if a user can modify the project.
	 *
	 * @param username Identifier of the user to check.
	 * @returns True if this user has write access to the project.
	 */
	public abstract boolean hasWriteAccess(String username);

	public String getSharedID() {
		return sharedID;
	}
}
