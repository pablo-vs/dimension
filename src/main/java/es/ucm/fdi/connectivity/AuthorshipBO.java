package es.ucm.fdi.connectivity;

import java.util.Objects;

/**
 * Represents a relationship of authorship between a user and a shared project.
 *
 * @author Pablo Villalobos
 */
public class AuthorshipBO {

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
     * @param author Author
     * @param project Shared project
     */
    public AuthorshipBO(String author, String project) {
        this.author = author;
        this.project = project;
        id = author + project;
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.author);
        hash = 47 * hash + Objects.hashCode(this.project);
        hash = 47 * hash + Objects.hashCode(this.id);
        return hash;
    }
}
