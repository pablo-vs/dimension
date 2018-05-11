package es.ucm.fdi.integration_tier.connectivity;

import java.util.List;
import java.util.ArrayList;
import java.security.AccessControlException;

import es.ucm.fdi.integration_tier.project.ProjectDAOHashTableImp;
import es.ucm.fdi.business_tier.workspace.project.ProjectManagerAS;
import es.ucm.fdi.integration_tier.project.ProjectTransfer;
import es.ucm.fdi.integration_tier.users.SessionBO;
import es.ucm.fdi.integration_tier.users.UserManagerAS;
import es.ucm.fdi.integration_tier.users.UserDAOHashTableImp;
import es.ucm.fdi.business_tier.exceptions.NotFoundException;

/**
 * Application service to manage sharing and importing projects.
 *
 * @author Pablo Villalobos
 */
public class ShareManagerAS {
    // Singleton pattern

    private static ShareManagerAS instance;
    private final ProjectManagerAS localProjMan = ProjectManagerAS
            .getManager(new ProjectDAOHashTableImp());
    private final UserManagerAS userMan = UserManagerAS
            .getManager(new UserDAOHashTableImp());
    private final SharedProjectDAO projectDB;
    private final AuthorshipDAO authorDB;
    private CommentDAO commentDB;

    /**
     * Class constructor specifying shared project and author.
     *
     * @param projectDB
     * @param authorDB
     */
    private ShareManagerAS(SharedProjectDAO projectDB, AuthorshipDAO authorDB) {
        this.projectDB = projectDB;
        this.authorDB = authorDB;
    }

    /**
     * Returns the manager instance.
     *
     * @param projDao
     * @param authorDao
     * @return
     */
    public static ShareManagerAS getManager(SharedProjectDAO projDao,
            AuthorshipDAO authorDao) {
        if (instance == null) {
            instance = new ShareManagerAS(projDao, authorDao);
        }
        return instance;
    }

    /**
     * Shares the given project as an Open Project.
     *
     * @param proj The identifier of the project to share.
     * @param authors The list of authors.
     * @param session The session to validate this operation.
     */
    public void shareOpenProject(ProjectTransfer proj, List<String> authors,
            SessionBO session) throws AccessControlException {

        if (validateAuthorList(authors, session)) {
            SharedProjectBOOpenProjectImp shared = new SharedProjectBOOpenProjectImp(
                    createSharedProjectID(proj.getID(), authors), proj, authors);
            store(shared, authors);
        } else {
            throw new AccessControlException("Invalid session");
        }
    }

    /**
     * Shares the given project as a Private Project.
     *
     * @param proj The identifier of the project to share.
     * @param authors The list of authors.
     * @param users The list of users which can view the project.
     * @param session The session to validate this operation.
     */
    public void sharePrivateProject(ProjectTransfer proj, List<String> authors,
            List<String> users, SessionBO session)
            throws AccessControlException {

        if (validateAuthorList(authors, session)) {
            SharedProjectBOPrivateProjectImp shared = new SharedProjectBOPrivateProjectImp(
                    createSharedProjectID(proj.getID(), authors), proj,
                    authors, users);
            store(shared, authors);
        } else {
            throw new AccessControlException("Invalid session");
        }
    }

    /**
     * Modifies a shared project.
     *
     * @param proj The new project data.
     * @param session The session to validate this operation.
     */
    public void modifySharedProject(SharedProjectBO proj, SessionBO session)
            throws NotFoundException, AccessControlException {

        SharedProjectBO old = projectDB.findSharedProject(proj.getSharedID());
        if (old != null) {
            if (userMan.authenticate(session.getUser(), session)) {
                if (proj.hasWriteAccess(session.getUser())) {
                    projectDB.modifySharedProject(proj);
                } else {
                    throw new AccessControlException("User "
                            + session.getUser() + " cannot modify "
                            + proj.getID());
                }
            } else {
                throw new AccessControlException("Invalid session");
            }
        } else {
            throw new NotFoundException("Project " + proj.getSharedID()
                    + " not found");
        }
    }

    /**
     * Adds a comment to a shared project.
     *
     * @param projID The project ID.
     * @param session The session to validate this operation.
     * @param comment
     */
    public void comment(String projID, SessionBO session,
            String comment) {
        SharedProjectBO project = projectDB.findSharedProject(projID);
        if (project != null) {
            if (userMan.authenticate(session.getUser(), session)) {
                if (project.hasReadAccess(session.getUser())) {
                    commentDB.addComment(new CommentBO(session.getUser(),
                            projID, comment));
                } else {
                    throw new AccessControlException("User "
                            + session.getUser() + " cannot add a comment in "
                            + projID);
                }
            } else {
                throw new AccessControlException("Invalid session");
            }
        } else {
            throw new NotFoundException("Project " + projID + " not found");
        }
    }

    /**
     * Imports a shared project into local storage.
     *
     * @param proj The project to import.
     * @param session The session to validate this operation.
     */
    public void importProject(SharedProjectBO proj, SessionBO session)
            throws NotFoundException, AccessControlException {

        if (userMan.authenticate(session.getUser(), session)) {
            if (proj.hasReadAccess(session.getUser())) {
                localProjMan.newProject(new ProjectTransfer(proj));
            } else {
                throw new AccessControlException("User " + session.getUser()
                        + " cannot modify " + proj.getID());
            }
        } else {
            throw new AccessControlException("Invalid session");
        }
    }

    /**
     * Returns a list of projects whose authors include the given one.
     *
     * @param author
     * @param session The session to validate this operation.
     * @return
     */
    public List<SharedProjectBO> findProjectByAuthor(String author,
            SessionBO session) throws AccessControlException {

        ArrayList<SharedProjectBO> results = new ArrayList<>();
        if (userMan.authenticate(session.getUser(), session)) {
            for (AuthorshipTransfer authorship : authorDB.findByUser(author)) {
                SharedProjectBO proj = projectDB.findSharedProject(authorship
                        .getProject());

                if (proj.hasReadAccess(session.getUser())) {
                    results.add(proj);
                }
            }
        } else {
            throw new AccessControlException("Invalid session");
        }

        return results;
    }

    /**
     * Returns a list of projects whose name matches the given one.
     *
     * @param name The name to find.
     * @param session The session to validate this operation.
     * @return
     */
    public List<SharedProjectBO> findProjectByName(String name,
            SessionBO session) throws AccessControlException {

        ArrayList<SharedProjectBO> results = new ArrayList<>();
        if (userMan.authenticate(session.getUser(), session)) {
            for (SharedProjectBO proj : projectDB.findByName(name)) {
                if (proj.hasReadAccess(session.getUser())) {
                    results.add(proj);
                }
            }
        } else {
            throw new AccessControlException("Invalid session");
        }

        return results;
    }

    private String createSharedProjectID(String project, List<String> authors) {
        // Create a unique shared project id
        StringBuilder sb = new StringBuilder().append(project);
        authors.forEach((author) -> {
            sb.append(author);
        });
        return Integer.toString(sb.toString().hashCode());
    }

    /**
     * Does the actual storing of the project and the authorships.
     *
     * @param proj
     * @param authors
     */
    private void store(SharedProjectBO proj, List<String> authors) {
        projectDB.addSharedProject(proj);
        for (String author : authors) {
            AuthorshipTransfer authorship = new AuthorshipTransfer(author,
                    proj.getSharedID());
            authorDB.addAuthorship(authorship);
        }

    }

    /**
     * Checks that all authors exist and the session corresponds to one of them.
     *
     * @param authors
     * @param session
     * @return true if valid
     * @throws NotFoundException
     */
    private boolean validateAuthorList(List<String> authors, SessionBO session)
            throws NotFoundException {
        boolean valid = false;
        for (String author : authors) {
            if (!userMan.existsUser(author)) {
                throw new NotFoundException("User " + author
                        + " does not exist");
            } else {
                if (userMan.authenticate(author, session)) {
                    valid = true;
                    break;
                }
            }
        }
        return valid;
    }

}
