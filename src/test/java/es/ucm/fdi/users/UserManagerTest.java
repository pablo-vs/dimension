package es.ucm.fdi.users;

import es.ucm.fdi.integration_tier.users.SessionBO;
import es.ucm.fdi.integration_tier.users.UserManagerAS;
import es.ucm.fdi.integration_tier.users.UserDAO;
import es.ucm.fdi.integration_tier.users.UserTransfer;
import es.ucm.fdi.integration_tier.users.UserDAOHashTableImp;
import java.time.ZonedDateTime;
import java.security.AccessControlException;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Assert;

import es.ucm.fdi.workspace.util.HashGenerator;
import es.ucm.fdi.business_tier.exceptions.NotFoundException;

public class UserManagerTest {

    @Test
    public void userManagementTest() {
        UserManagerAS userMgr = UserManagerAS.getManager(new UserDAOHashTableImp());
        HashGenerator hashgen = new HashGenerator();
        UserTransfer pepe = new UserTransfer("pepito", hashgen.hash("1234".toCharArray()));
        UserTO paco = new UserTransfer("paquito", hashgen.hash("4321".toCharArray()));
        userMgr.newUser(pepe);
        userMgr.newUser(paco);
        SessionBO sesionPepe = userMgr.login("pepito", "1234"),
                sesionPaco = userMgr.login("paquito", "4321");
        try {
            userMgr.removeUser("pepito", sesionPaco);
            fail("Illegal account access!!");
        } catch (AccessControlException e) {
            //System.out.println("Authentication works");
        }

        userMgr.removeUser("pepito", sesionPepe);
        userMgr.logout(sesionPepe);
        try {
            sesionPepe = userMgr.login("pepito", "1234");
            fail("Logged in to nonexistent account!");
        } catch (NotFoundException e) {
            //Todo correcto
        }
    }

    @Test
    public void invalidSessionTest() {
        HashGenerator hashgen = new HashGenerator();
        UserDAO dao = new UserDAOHashTableImp();
        String passwd = hashgen.hash("1234".toCharArray());
        UserTransfer user = new UserTransfer("pedro", passwd);
        UserManagerAS userMgr = UserManagerAS.getManager(dao);
        userMgr.newUser(user);
        try {
            SessionBO sesion = userMgr.login("pedro", "12345");
            fail("La sesión no debería haberse iniciado");
        } catch (AccessControlException e) {
            //Todo correcto
        }

        SessionBO sesion = new SessionBO("pedro", ZonedDateTime.now());
        assertFalse(userMgr.validateSession(sesion));
    }
}
