package es.ucm.fdi.connectivity;

import java.util.List;
import java.util.ArrayList;
import java.security.AccessControlException;

import es.ucm.fdi.trabajo.ProjectDAOHashTableImp;
import es.ucm.fdi.trabajo.ProjectManagerAS;
import es.ucm.fdi.trabajo.ProjectTO;
import es.ucm.fdi.users.SessionBO;
import es.ucm.fdi.users.UserManagerAS;
import es.ucm.fdi.users.UserDAOHashTableImp;
import es.ucm.fdi.exceptions.NotFoundException;

/**
 * Application service to manage sharing and importing projects.
 *
 * @author Pablo Villalobos
 * @version 05.04.2018
 */
public class ShareAS {
	/**
         * Instance of singleton pattern
         */
	private static ShareAS instance;
        /***
         * Local project manager
         */
	private ProjectManagerAS localProjMan = ProjectManagerAS.getManager(new ProjectDAOHashTableImp());
        /**
         * User manager
         */
	private UserManagerAS userMan = UserManagerAS.getManager(new UserDAOHashTableImp());
        /**
         * Shared project on db
         */
	private SharedProjectDAO projectDB;
        /**
         * Author on db
         */
	private AuthorshipDAO authorDB;
        
        /**
         * Private class constructor specifying DAOproject and DAOauthor.
         * @param projDao Shared project
         * @param authorDao Owner
         */
	private ShareAS(SharedProjectDAO projDao, AuthorshipDAO authorDao) {
		projectDB = projDao;
		authorDB = authorDao;
	}
        
        /**
         * Given a project and an author, returns their instance
         * @param projDao Shared project
         * @param authorDao Owner
         * @return instance
         */
	public static ShareAS getInstance(SharedProjectDAO projDao, AuthorshipDAO authorDao) {
		if (instance == null) {
			instance = new ShareAS(projDao, authorDao);
		}
		return instance;
	}
	
        /**
         * Creates an unique id from a project and a list of authors.
         * @param project Project
         * @param authors Authors list
         * @return shared project id
         */
	private String createSharedProjectID(String project, List<String> authors) {
		StringBuilder sb = new StringBuilder().append(project);
                authors.forEach((auth) -> {
                    sb.append(auth);
                });
		return Integer.toString(sb.toString().hashCode());
	}
        
        /**
         * Stores the project and the authorships.
         * @param proj Shared project
         * @param authors Authors list
         */
	private void store(SharedProjectBO proj, List<String> authors) {
		projectDB.addSharedProject(proj);
		for(String author : authors) {
			AuthorshipBO authorship = new AuthorshipBO(author, proj.getSharedID());
			authorDB.addAuthorship(authorship);
		}		
	}
        
        /**
         * Validates the authors list
         * @param authors List of authors
         * @param session User session
         * @return if the authors list is valid
         * @throws NotFoundException  if an author does not exist
         */
	private boolean validateAuthorList(List<String> authors, SessionBO session) throws NotFoundException {
		//Check that all authors exist and the session corresponds to one of them
		boolean valid = false;
		for(String author: authors) {
			if(!userMan.existsUser(author)) {
				throw new NotFoundException("User " + author + " does not exist");
			} else {
				if(userMan.authenticate(author, session)) {
					valid = true;
					break;
				}
			}
		}
		return valid;
	}
	
	/**
	 * Shares the given project as an Open Project.
	 *
	 * @param proj The identifier of the project to share.
	 * @param authors The list of authors.
	 * @param session The session to validate this operation.
	 */
	public void shareOpenProject(ProjectTO proj, List<String> authors, SessionBO session)
		throws AccessControlException {
		
		if(validateAuthorList(authors, session)) {
			SharedProjectBOOpenProjectImp shared =
				new SharedProjectBOOpenProjectImp(createSharedProjectID(proj.getID(),authors), proj, authors);
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
	public void sharePrivateProject(ProjectTO proj, List<String> authors, List<String> users,
					SessionBO session) throws AccessControlException {
		
		if(validateAuthorList(authors, session)) {
			SharedProjectBOPrivateProjectImp shared =
				new SharedProjectBOPrivateProjectImp(createSharedProjectID(proj.getID(),authors), proj, authors, users);
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
		if(old != null) {
			if(userMan.authenticate(session.getUser(), session)) {
				if(proj.hasWriteAccess(session.getUser())) {
					projectDB.modifySharedProject(proj);
				} else {
					throw new AccessControlException("User " + session.getUser() + " cannot modify " + proj.getID());
				}
			} else {
				throw new AccessControlException("Invalid session");
			}
		} else {
			throw new NotFoundException("Project " + proj.getSharedID() + " not found");
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

		if(userMan.authenticate(session.getUser(), session)) {
			if(proj.hasReadAccess(session.getUser())) {
				localProjMan.newProject(new ProjectTO(proj));
			} else {
				throw new AccessControlException("User " + session.getUser() + " cannot modify " + proj.getID());
			}
		} else {
			throw new AccessControlException("Invalid session");
		}
	}

	/**
	 * Returns a list of projects whose authors includes the given one.
	 *
	 * @param author The author to find.
	 * @param session The session to validate this operation.
         * @return List of projects
	 */
	public List<SharedProjectBO> findProjectByAuthor(String author, SessionBO session)
		throws AccessControlException {

		ArrayList<SharedProjectBO> results = new ArrayList<>();
		if(userMan.authenticate(session.getUser(), session)) {
			for(AuthorshipBO authorship : authorDB.findByUser(author)) {
				SharedProjectBO proj =
					projectDB.findSharedProject(authorship.getProject());
			
				if(proj.hasReadAccess(session.getUser())) {
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
         * @return projects list
	 */
	public List<SharedProjectBO> findProjectByName(String name, SessionBO session)
		throws AccessControlException {

		ArrayList<SharedProjectBO> results = new ArrayList<>();
		if(userMan.authenticate(session.getUser(), session)) {
			for(SharedProjectBO proj : projectDB.findByName(name)) {
				if(proj.hasReadAccess(session.getUser())) {
					results.add(proj);
				}
			}
		} else {
			throw new AccessControlException("Invalid session");
		}
		
		return results;
	}

}
