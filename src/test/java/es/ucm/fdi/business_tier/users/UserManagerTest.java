package es.ucm.fdi.business_tier.users;

import es.ucm.fdi.business_tier.users.SessionDTO;
import es.ucm.fdi.business_tier.users.UserManagerAS;
import es.ucm.fdi.integration_tier.users.UserDAO;
import es.ucm.fdi.business_tier.users.UserDTO;
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
        UserDTO pepe = new UserDTO("pepito", hashgen.hash("1234".toCharArray()));
        UserDTO paco = new UserDTO("paquito", hashgen.hash("4321".toCharArray()));
        userMgr.newUser(pepe);
        userMgr.newUser(paco);
        SessionDTO sesionPepe = userMgr.login("pepito", "1234");
        SessionDTO sesionPaco = userMgr.login("paquito", "4321");
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
        UserDTO user = new UserDTO("pedro", passwd);
        UserManagerAS userMgr = UserManagerAS.getManager(dao);
        userMgr.newUser(user);
        try {
            SessionDTO sesion = userMgr.login("pedro", "12345");
            fail("La sesión no debería haberse iniciado");
        } catch (AccessControlException e) {
            //Todo correcto
        }

        SessionDTO sesion = new SessionDTO("pedro", ZonedDateTime.now());
        assertFalse(userMgr.validateSession(sesion));
    }
}
