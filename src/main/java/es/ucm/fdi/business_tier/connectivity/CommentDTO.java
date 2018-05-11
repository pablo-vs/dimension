package es.ucm.fdi.business_tier.connectivity;

/**
 * Represents a comment in a project.
 *
 * @author Brian Leiva
 */
public class CommentDTO {

    private final String author;
    private final String project;
    private final String text;
    private final String id;

    /**
     * Class constructor specifying the project, the author and the comment
     * itself (text)
     *
     * @param author
     * @param proj
     * @param text Text
     */
    public CommentDTO(String author, String proj, String text) {
        this.author = author;
        this.project = proj;
        this.text = text;
        id = author + proj + text.hashCode();
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
