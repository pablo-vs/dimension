package es.ucm.fdi.connectivity;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.trabajo.ProjectManagerAS;
import es.ucm.fdi.trabajo.ProjectTO;
import es.ucm.fdi.user.SessionBO;

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
	
	private String createShareProjectID(String project, List<String> authors) {
		StringBuilder sb = new StringBuilder().append(proj.getID());
		for(String auth : authors) {
			sb.append(auth);
		}
		return Integer.toString(sb.toString().hashCode());
	}

	public void shareOpenProject(ProjectTO proj, List<String> authors, SessionBO session) {
		
	}

	public void sharePrivateProject(ProjectTO proj, List<String> authors, List<String> users, SessionBO session) {

	}

	public void modifySharedProject(SharedProjectBO proj, SessionBO session) {
		
	}
	
	public ProjectTO importProject(SharedProjectTO proj, SessionBO session) {

	}

	public List<SharedProjectTO> findProjectByAuthor(String author) {

	}

	public List<SharedProjectTO> findProjectByName(String name) {

	}

}
