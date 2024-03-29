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

import java.util.Objects;

/**
 * Represents a relationship of authorship between a user and a shared project.
 *
 * @author Pablo Villalobos
 */
public class AuthorshipDTO {

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
    public AuthorshipDTO(String author, String project) {
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
        if (!(other instanceof AuthorshipDTO)) {
            return false;
        } else {
            return id.equals(((AuthorshipDTO) other).getId());
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
