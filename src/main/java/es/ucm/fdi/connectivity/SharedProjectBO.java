package es.ucm.fdi.connectivity;

/**
 * Represents a project which has been shared.
 *
 * @author Pablo Villalobos
 * @version 04.04.2018
 */
public abstract class SharedProjectBO {
	private String sharedID;
	private String projectName;
	
	public SharedProjectBO(String ID, String name) {
		sharedID = ID;
		this.projectName = name;
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
}
