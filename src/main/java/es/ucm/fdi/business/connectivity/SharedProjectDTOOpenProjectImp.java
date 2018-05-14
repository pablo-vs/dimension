package es.ucm.fdi.business.connectivity;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.business.workspace.project.ProjectDTO;

/**
 * A shared project that can be read by everyone and modified by its authors.
 *
 * @author Pablo Villalobos
 */
public class SharedProjectDTOOpenProjectImp extends SharedProjectDTO {

    /**
     * List of authors
     */
    private List<String> authors = new ArrayList<>();

    /**
     * Class constructor specifying id, existing project and list of authors.
     *
     * @param ID Identifier
     * @param proj Project
     * @param authors List of authors
     */
    public SharedProjectDTOOpenProjectImp(String ID, ProjectDTO proj, List<String> authors) {
        super(ID, proj);
        this.authors = new ArrayList<>(authors);
    }

    /**
     * Class constructor specifying id, existing project and an author.
     *
     * @param ID Identifier
     * @param proj Project
     * @param author Owner
     */
    public SharedProjectDTOOpenProjectImp(String ID, ProjectDTO proj, String author) {
        super(ID, proj);
        authors.add(author);
    }

    @Override
    public boolean hasReadAccess(String username) {
        return true;
    }

    @Override
    public boolean hasWriteAccess(String username) {
        return authors.contains(username);
    }
}
