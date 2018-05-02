package es.ucm.fdi.connectivity;

/**
 * Represents a comment in a project.
 *
 * @author Brian Leiva
 * @version 20.04.2018
 */
public class CommentBO {

    /**
     * Author
     */
    private final String author;
    /**
     * Project
     */
    private final String project;
    /**
     * Text
     */
    private final String text;
    /**
     * Comment id
     */
    private String id;

    /**
     * Class constructor specifying the project, the author and the comment
     * itself (text)
     *
     * @param auth Author
     * @param proj Project
     * @param commentText Text
     */
    public CommentBO(String auth, String proj, String commentText) {
        author = auth;
        project = proj;
        text = commentText;
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
