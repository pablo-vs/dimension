package es.ucm.fdi.connectivity;

import java.util.ArrayList;

import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

import es.ucm.fdi.trabajo.ProjectTO;
import es.ucm.fdi.users.SessionBO;
import es.ucm.fdi.users.UserDAOHashTableImp;
import es.ucm.fdi.users.UserManagerAS;
import es.ucm.fdi.users.UserTO;
import es.ucm.fdi.util.HashGenerator;


public class ShareManagerTest {

	@Test
	public void shareImportTest() {
		HashGenerator hg = new HashGenerator();
		ShareManagerAS shareMgr = ShareManagerAS.getManager(
				new SharedProjectDAOHashTableImp(),
				new AuthorshipDAOHashTableImp());
		UserManagerAS userMgr = UserManagerAS.getManager(new UserDAOHashTableImp());
		char[] password = {'1','2','3','4'};
		UserTO user = new UserTO("pepe", hg.hash(password));
		userMgr.newUser(user);
		UserTO luis = new UserTO("luis", hg.hash(password));
		UserTO paco = new UserTO("paco", hg.hash(password));
		userMgr.newUser(luis);
		userMgr.newUser(paco);
		List<String> authors = new ArrayList<String>();
		authors.add("pepe");
		authors.add("luis");
		authors.add("paco");
		SessionBO session = userMgr.login("pepe", "1234");
		ProjectTO project = new ProjectTO("lineales");
		ProjectTO project2 = new ProjectTO("cuadraticas");
		// test the sharing of open projects
		try {
			shareMgr.shareOpenProject(project2, authors, session);	
		} catch (Exception e) {
			fail(e.getMessage());
		}
		// test the sharing of private projects
		try {
			shareMgr.sharePrivateProject(project, authors, authors, session);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
	}

	@Test
	public void findTest() {
		HashGenerator hg = new HashGenerator();
		char[] password = {'1','2','3','4'};
		UserManagerAS userMgr = UserManagerAS.getManager(new UserDAOHashTableImp());
		ShareManagerAS shareMgr = ShareManagerAS.getManager(new SharedProjectDAOHashTableImp(), new AuthorshipDAOHashTableImp());
		UserTO juan = new UserTO("juan", hg.hash(password));
		userMgr.newUser(juan);
		SessionBO juanSession = userMgr.login("juan", "1234");
		
		//test the rejection of unauthorised accesses
		try {
			shareMgr.importProject(shareMgr.findProjectByName("lineales", juanSession).get(0), juanSession);
			fail("Unauthorised access");
		} catch (Exception e) {
			//correcto
		}
		try {
			shareMgr.modifySharedProject(shareMgr.findProjectByName("lineales", juanSession).get(0), juanSession);
			fail("Unauthorised access");
		} catch (Exception e) {
			//correcto
		}
		try {
			shareMgr.modifySharedProject(shareMgr.findProjectByName("cuadraticas", juanSession).get(0), juanSession);
			fail("Unauthorised access");
		} catch (Exception e) {
			//correcto
		}
		SessionBO session = userMgr.login("pepe", "1234");
		SessionBO luis = userMgr.login("luis", "1234");
		
		//test the permission of legitimate accesses
		try {
			shareMgr.modifySharedProject(shareMgr.findProjectByName("lineales", session).get(0), session);
			shareMgr.importProject(shareMgr.findProjectByName("lineales", session).get(0), session);
			shareMgr.modifySharedProject(shareMgr.findProjectByName("lineales", luis).get(0), luis);
			shareMgr.importProject(shareMgr.findProjectByName("cuadraticas", juanSession).get(0), juanSession);
			List<SharedProjectBO> found = shareMgr.findProjectByAuthor("pepe", session);
			for(SharedProjectBO f : found) {
			shareMgr.modifySharedProject(f, session);
			}
		} catch (Exception e) {
			fail("Not accepting legitimate access");
		}
	}

}
