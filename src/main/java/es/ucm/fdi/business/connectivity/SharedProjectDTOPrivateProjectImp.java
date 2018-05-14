package es.ucm.fdi.business.connectivity;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.business.workspace.project.ProjectDTO;

/**
 * A shared project that can be read only by a list of people allowed and
 * modified by its authors.
 *
 * @author Javier Galiana
 */
public class SharedProjectDTOPrivateProjectImp extends SharedProjectDTO {

    /**
     * List of authors
     */
    private List<String> authors;
    /**
     * List of viewers
     */
    private List<String> viewers;

    /**
     * Class constructor specifying id, project and list of authors and viewers.
     *
     * @param ID Identifier
     * @param proj Shared project
     * @param auth List of authors
     * @param view List of viewers
     */
    public SharedProjectDTOPrivateProjectImp(String ID, ProjectDTO proj, List<String> auth, List<String> view) {
        super(ID, proj);
        authors = new ArrayList<>(auth);
        viewers = new ArrayList<>(view);
    }

    /**
     * Class constructor specifying id, project and author.
     *
     * @param ID Identifier
     * @param proj Shared projects
     * @param author List of authors
     */
    public SharedProjectDTOPrivateProjectImp(String ID, ProjectDTO proj, String author) {
        super(ID, proj);
        authors = new ArrayList<>();
        authors.add(author);
    }

    @Override
    public boolean hasReadAccess(String username) {
        return authors.contains(username) || viewers.contains(username);
    }

    @Override
    public boolean hasWriteAccess(String username) {
        return authors.contains(username);
    }

}
