package es.ucm.fdi.connectivity;

/**
 * Represents a comment in a project.
 *
 * @author Brian Leiva
 */
public class CommentBO {
    private final String author;
    private final String project;
    private final String text;
    private String id;

    /**
     * Class constructor specifying the project, the author and the comment
     * itself (text)
     *
     * @param author Author
     * @param project Project
     * @param text Text
     */
    public CommentBO(String author, String project, String text) {
        this.author = author;
        this.project = project;
        this.text = text;
    }

    /**
     *
     * @return the author of the comment
     */
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @return the id of the project
     */
    public String getProj() {
        return project;
    }

    /**
     *
     * @return the comment text
     */
    public String getText() {
        return text;
    }

    /**
     *
     * @return the id of the comment
     */
    public String getId() {
        return id;
    }
}
