package es.ucm.fdi.connectivity;

/**
 * Represents a relationship of authorship between a user and a shared project.
 *
 * @author Pablo Villalobos
 * @version 04.04.2018
 */
public class AuthorshipBO {
	private String author, project, id;

	public AuthorshipBO(String user, String proj) {
		author = user;
		project = proj;
		id = user + project;
	}

	public String getId() {
		return id;
	}
	
	public String getAuthor() {
		return author;
	}

	public String getProject() {
		return project;
	}
}
