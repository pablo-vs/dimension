package es.ucm.fdi.connectivity;

import es.ucm.fdi.data.SQLDescribable;

/**
 * Represents a relationship of authorship between a user and a shared project.
 *
 * @author Pablo Villalobos
 * @version 04.04.2018
 */
public class AuthorshipBO extends SQLDescribable {

    /**
     * Author of the project
     */
    private final String author;
    /**
     * Shared project
     */
    private final String project;
    /**
     * Authorship id
     */
    private final String id;

    /**
     * Class constructor specifying author and shared project. Id is made by the
     * joint of user and project
     *
     * @param user Author
     * @param proj Shared project
     */
    public AuthorshipBO(String user, String proj) {
        author = user;
        project = proj;
        id = user + project;
    }

    @Override
    public AuthorshipBO(Object [] data) {
	if(data.length != 3) {
	    throw new IllegalArgumentException("Constructor requires 3 objects, "
					       + data.length + " given");
	}
	if(!(data[0] instanceof String && data[1] instanceof String && data[2] instanceof String)) {
	    throw new IllegalArgumentException("Invalid data type");
	}
	id = (String) data[0];
	author = (String) data[1];
	project = (String) data[2];
    }
    
    /**
     *
     * @return the id of the authorship
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @return the author of the project
     */
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @return the shared project
     */
    public String getProject() {
        return project;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof AuthorshipBO)) {
            return false;
        } else {
            return id.equals(((AuthorshipBO) other).getId());
        }
    }
}
