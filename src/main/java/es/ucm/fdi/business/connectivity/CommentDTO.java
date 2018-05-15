/*
  This file is part of Dimension.
  Dimension is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  Dimension is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  You should have received a copy of the GNU General Public License
  along with Dimension.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.business.connectivity;

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
    public String getIDProject() {
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
