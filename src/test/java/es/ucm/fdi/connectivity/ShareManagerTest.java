package es.ucm.fdi.connectivity;

import es.ucm.fdi.integration_tier.connectivity.AuthorshipDAOHashTableImp;
import es.ucm.fdi.integration_tier.connectivity.ShareManagerAS;
import es.ucm.fdi.integration_tier.connectivity.SharedProjectDAOHashTableImp;
import es.ucm.fdi.integration_tier.connectivity.SharedProjectBO;
import java.util.ArrayList;

import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

import es.ucm.fdi.business_tier.workspace.project.ProjectManagerAS;
import es.ucm.fdi.integration_tier.project.ProjectTransfer;
import es.ucm.fdi.integration_tier.users.SessionBO;
import es.ucm.fdi.integration_tier.users.UserDAOHashTableImp;
import es.ucm.fdi.integration_tier.users.UserManagerAS;
import es.ucm.fdi.integration_tier.users.UserTransfer;
import es.ucm.fdi.workspace.util.HashGenerator;

public class ShareManagerTest {

    @Test
    public void shareImportTest() {
        HashGenerator hg = new HashGenerator();
        ShareManagerAS shareMgr = ShareManagerAS.getManager(
                new SharedProjectDAOHashTableImp(),
                new AuthorshipDAOHashTableImp());
        UserManagerAS userMgr = UserManagerAS.getManager(new UserDAOHashTableImp());
        char[] password = {'1', '2', '3', '4'};
        UserTransfer user = new UserTransfer("pepe", hg.hash(password));
        userMgr.newUser(user);
        UserTransfer luis = new UserTransfer("luis", hg.hash(password));
        UserTransfer paco = new UserTransfer("paco", hg.hash(password));
        userMgr.newUser(luis);
        userMgr.newUser(paco);
        List<String> authors = new ArrayList<String>();
        authors.add("pepe");
        authors.add("luis");
        authors.add("paco");
        SessionBO session = userMgr.login("pepe", "1234");
        ProjectTransfer project = new ProjectTransfer("lineales");
        ProjectTransfer project2 = new ProjectTransfer("cuadraticas");
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
        char[] password = {'1', '2', '3', '4'};
        UserManagerAS userMgr = UserManagerAS.getManager(new UserDAOHashTableImp());
        ShareManagerAS shareMgr = ShareManagerAS.getManager(new SharedProjectDAOHashTableImp(), new AuthorshipDAOHashTableImp());
        UserTransfer juan = new UserTransfer("juan", hg.hash(password));
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
            for (SharedProjectBO f : found) {
                shareMgr.modifySharedProject(f, session);
            }
        } catch (Exception e) {
            fail("Not accepting legitimate access");
        }
    }

}
