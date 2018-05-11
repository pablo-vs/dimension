package es.ucm.fdi.integration_tier.connectivity;

import es.ucm.fdi.integration_tier.project.ProjectTransfer;

/**
 * Represents a shared project by an author.
 *
 * @author Pablo Villalobos
 * @see ProjectTransfer
 */
public abstract class SharedProjectBO extends ProjectTransfer {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2809118441039530565L;
	/**
     * Project id
     */
    private String sharedID;

    /**
     * Class constructor specifying id and author name.
     *
     * @param sharedID Identifier
     * @param name Author
     */
    public SharedProjectBO(String sharedID, String name) {
        super(name);
        this.sharedID = sharedID;
    }

    /**
     * Class constructor specifying id and existing project
     *
     * @param sharedID Identifier
     * @param proj Existing project
     */
    public SharedProjectBO(String sharedID, ProjectTransfer proj) {
        super(proj);
        this.sharedID = sharedID;
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

    /**
     *
     * @return the shared ID
     */
    public String getSharedID() {
        return sharedID;
    }

}
