package es.ucm.fdi.connectivity;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.trabajo.ProjectManagerAS;
import es.ucm.fdi.trabajo.ProjectTO;

/**
 * Application service to manage sharing and importing projects.
 *
 * @author Pablo Villalobos
 * @version 05.04.2018
 */
public class ShareAS {
	//Singleton pattern
	private ShareAS instance;
	private ProjectManagerAS projectMan = ProjectManagerAS.getManager();
	private UserManagerAS userMan = UserManagerAS.getManager(new UserManagerDAOHashTableImp);
	private SharedProjectDAO projectDB;
	private AuthorshipDAO authorDB;

	private ShareAS(SharedProjectDAO projDao, AuthorshipDAO authorDao) {
		projectDB = projDao;
		authorDB = authorDao;
	}

	public static SharedProjectDAO getInstance(SharedProjectDAO projDao, AuthorshipDAO authorDao) {
		if (instance == null) {
			instance = new SharedProjectDAO(projDao, authorDao);
		}
		return instance;
	}
	
	public void shareProject(String project, List<String> authors) {
		ProjectTO proj = projectMan.openProject(project);
		if(proj != null) {
			StringBuilder sb = new StringBuilder().append(proj.getID());
			ArrayList<AuthorshipBO> authorships = new ArrayList<>();
			for(String auth : authors) {
			        sb.append(auth);
				authorships.add(new AuthorshipBO(auth, proj.getID()));
			}
			String shareID = Integer.toString(sb.toString().hashCode());
			if(projectDB.findSharedProjects(shareID) == null) {
				SharedProjectTO sharedProj = new SharedProjectTO(shareID, proj.getID());
				projectDB.addSharedProject(sharedProj);
			} else {
				throw new IllegalArgumentException("Project " + project + " is already shared");
			}
		} else {
			throw new IllegalArgumentException("Project " + project + " does not exist");
		}
	}

}
