package es.ucm.fdi.business_tier.connectivity;

import es.ucm.fdi.integration_tier.connectivity.AuthorshipDAOHashTableImp;
import es.ucm.fdi.business_tier.connectivity.ShareManagerAS;
import es.ucm.fdi.integration_tier.connectivity.SharedProjectDAOHashTableImp;
import es.ucm.fdi.business_tier.connectivity.SharedProjectDTO;
import java.util.ArrayList;

import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

import es.ucm.fdi.business_tier.workspace.project.ProjectManagerAS;
import es.ucm.fdi.business_tier.workspace.project.ProjectDTO;
import es.ucm.fdi.business_tier.users.SessionDTO;
import es.ucm.fdi.integration_tier.users.UserDAOHashTableImp;
import es.ucm.fdi.business_tier.users.UserManagerAS;
import es.ucm.fdi.business_tier.users.UserDTO;
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
        UserDTO user = new UserDTO("pepe", hg.hash(password));
        userMgr.newUser(user);
        UserDTO luis = new UserDTO("luis", hg.hash(password));
        UserDTO paco = new UserDTO("paco", hg.hash(password));
        userMgr.newUser(luis);
        userMgr.newUser(paco);
        List<String> authors = new ArrayList<String>();
        authors.add("pepe");
        authors.add("luis");
        authors.add("paco");
        SessionDTO session = userMgr.login("pepe", "1234");
        ProjectDTO project = new ProjectDTO("lineales");
        ProjectDTO project2 = new ProjectDTO("cuadraticas");
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
        UserDTO juan = new UserDTO("juan", hg.hash(password));
        userMgr.newUser(juan);
        SessionDTO juanSession = userMgr.login("juan", "1234");

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
        SessionDTO session = userMgr.login("pepe", "1234");
        SessionDTO luis = userMgr.login("luis", "1234");

        //test the permission of legitimate accesses
        try {
            shareMgr.modifySharedProject(shareMgr.findProjectByName("lineales", session).get(0), session);
            shareMgr.importProject(shareMgr.findProjectByName("lineales", session).get(0), session);
            shareMgr.modifySharedProject(shareMgr.findProjectByName("lineales", luis).get(0), luis);
            shareMgr.importProject(shareMgr.findProjectByName("cuadraticas", juanSession).get(0), juanSession);
            List<SharedProjectDTO> found = shareMgr.findProjectByAuthor("pepe", session);
            for (SharedProjectDTO f : found) {
                shareMgr.modifySharedProject(f, session);
            }
        } catch (Exception e) {
            fail("Not accepting legitimate access");
        }
    }

}
