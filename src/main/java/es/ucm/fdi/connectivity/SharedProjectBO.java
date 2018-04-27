package es.ucm.fdi.connectivity;


import es.ucm.fdi.workspace.project.ProjectTO;


/**
 * Represents a shared project by an author.
 *
 * @author Pablo Villalobos
 * @version 04.04.2018
 * @see ProjectTO
 */
public abstract class SharedProjectBO extends ProjectTO{
        /**
         * Project id
         */
	private String sharedID;
	
        /**
         * Class constructor specifying id and author name.
         * @param ID Identifier
         * @param name Author
         */
	public SharedProjectBO(String ID, String name) {
		super(name);
		sharedID = ID;
	}
        
        /**
         * Class constructor specifying id and existing project
         * @param ID Identifier
         * @param proj Existing project
         */
	public SharedProjectBO(String ID, ProjectTO proj) {
		super(proj);
		sharedID = ID;
	}
	
	/**
	 * Checks if a user can read the project.
	 *
	 * @param username Identifier of the user to check.
	 * @return True if this user has read access to the project.
	 */
	public abstract boolean hasReadAccess(String username);

	/**
	 * Checks if a user can modify the project.
	 *
	 * @param username Identifier of the user to check.
	 * @return True if this user has write access to the project.
	 */
	public abstract boolean hasWriteAccess(String username);

	public String getSharedID() {
		return sharedID;
	}
	
}
