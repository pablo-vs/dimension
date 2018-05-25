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

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.security.AccessControlException;

import es.ucm.fdi.business.workspace.project.ProjectManagerAS;
import es.ucm.fdi.business.workspace.project.ProjectDTO;
import es.ucm.fdi.business.users.SessionDTO;
import es.ucm.fdi.business.users.UserManagerAS;
import es.ucm.fdi.integration.users.UserDAOHashTableImp;
import es.ucm.fdi.business.exceptions.NotFoundException;
import es.ucm.fdi.integration.connectivity.AuthorshipDAO;
import es.ucm.fdi.integration.connectivity.CommentDAO;
import es.ucm.fdi.integration.connectivity.SharedProjectDAO;

/**
 * Application service to manage sharing and importing projects.
 *
 * @author Pablo Villalobos
 */
public class ShareManagerAS {
    // Singleton pattern

    private static ShareManagerAS instance;
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
     * @param proj The project to share.
     * @param authors The list of authors.
     * @param session The session to validate this operation.
     */
    public void shareOpenProject(ProjectDTO proj, List<String> authors,
            SessionDTO session) throws AccessControlException {

        if (validateAuthorList(authors, session)) {
            SharedProjectDTOOpenProjectImp shared = new SharedProjectDTOOpenProjectImp(
                    createSharedProjectID(proj.getID(), authors), proj, authors);
            store(shared, authors);
        } else {
            throw new AccessControlException("Invalid session");
        }
    }

    /**
     * Shares the given project as a Private Project.
     *
     * @param proj The project to share.
     * @param authors The list of authors.
     * @param users The list of users which can view the project.
     * @param session The session to validate this operation.
     */
    public void sharePrivateProject(ProjectDTO proj, List<String> authors,
            List<String> users, SessionDTO session)
            throws AccessControlException {

        if (validateAuthorList(authors, session)) {
            SharedProjectDTOPrivateProjectImp shared = new SharedProjectDTOPrivateProjectImp(
                    createSharedProjectID(proj.getID(), authors), proj,
                    authors, users);
            store(shared, authors);
        } else {
            throw new AccessControlException("Invalid session");
        }
    }

    /**
     * Deletes a Shared Project.
     *
     * @param id The project to delete.
     * @param session The session to validate this operation.
     */
    public void removeProject(String id, SessionDTO session)
            throws AccessControlException {

        if (userMan.authenticate(session.getUser(), session)) {
            SharedProjectDTO proj = projectDB.findSharedProject(id);
            if (proj != null) {
                if (proj.hasWriteAccess(session.getUser())) {
                    projectDB.removeSharedProject(proj.getSharedID());
                } else {
                    throw new AccessControlException("User "
                            + session.getUser() + " cannot modify "
                            + proj.getID());
                }
            }
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
    public void modifySharedProject(SharedProjectDTO proj, SessionDTO session)
            throws NotFoundException, AccessControlException {

        SharedProjectDTO old = projectDB.findSharedProject(proj.getSharedID());
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
     * Adds a addComment to a shared project.
     *
     * @param projID The project ID.
     * @param session The session to validate this operation.
     * @param comment
     */
    public void addComment(String projID, SessionDTO session,
            String comment) {
        SharedProjectDTO project = projectDB.findSharedProject(projID);
        if (project != null) {
            if (userMan.authenticate(session.getUser(), session)) {
                if (project.hasReadAccess(session.getUser())) {
                    commentDB.addComment(new CommentDTO(session.getUser(),
                            projID, comment, new Date()));
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
    public void importProject(SharedProjectDTO proj, SessionDTO session)
            throws NotFoundException, AccessControlException {

        if (userMan.authenticate(session.getUser(), session)) {
            if (proj.hasReadAccess(session.getUser())) {
                ProjectManagerAS projMan = new ProjectManagerAS(session.getUser());
                projMan.newProject(new ProjectDTO(proj), session);
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
    public List<SharedProjectDTO> findProjectByAuthor(String author,
            SessionDTO session) throws AccessControlException {

        ArrayList<SharedProjectDTO> results = new ArrayList<>();
        if (userMan.authenticate(session.getUser(), session)) {
            for (AuthorshipDTO authorship : authorDB.findByUser(author)) {
                SharedProjectDTO proj = projectDB.findSharedProject(authorship
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
    public List<SharedProjectDTO> findProjectByName(String name,
            SessionDTO session) throws AccessControlException {

        ArrayList<SharedProjectDTO> results = new ArrayList<>();
        if (userMan.authenticate(session.getUser(), session)) {
            for (SharedProjectDTO proj : projectDB.findByName(name)) {
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
    private void store(SharedProjectDTO proj, List<String> authors) {
        projectDB.addSharedProject(proj);
        for (String author : authors) {
            AuthorshipDTO authorship = new AuthorshipDTO(author,
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
    private boolean validateAuthorList(List<String> authors, SessionDTO session)
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
