package es.ucm.fdi.usecases;

import static org.junit.Assert.*;

import java.security.AccessControlException;
import java.time.ZonedDateTime;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import es.ucm.fdi.business.users.NotificationDTO;
import es.ucm.fdi.business.users.SessionDTO;
import es.ucm.fdi.business.users.UserDTO;
import es.ucm.fdi.business.users.UserManagerAS;
import es.ucm.fdi.business.users.UserType;
import es.ucm.fdi.business.util.HashGenerator;
import es.ucm.fdi.integration.users.NotificationDAOHashTableImp;
import es.ucm.fdi.integration.users.UserDAOHashTableImp;

/**
 * Use case test: sanctioning or notifying something to a user. More information
 * on how UserManagerAS handles the registration process can be found on
 * UserManagerAS test class:
 *
 * @see UserManagerASTest
 * @author Eduardo Amaya
 */
public class NotifySanctionUCTest {

    /**
     * Database to be used during the tests
     */
    private UserDAOHashTableImp database;
    /**
     * Database for notifications
     */
    private NotificationDAOHashTableImp notDatabase;
    /**
     * User manager Application Service
     */
    private UserManagerAS manager;
    /**
     * Hash generator for passwords creation
     */
    private HashGenerator hashgen;
    /**
     * User for testing
     */
    private UserDTO user;
    /**
     * Session for previous user
     */
    private SessionDTO session;
    /**
     * Another user
     */
    private UserDTO newUser;
    /**
     * Session for previous user
     */
    private SessionDTO newSession;
    /**
     * An admin
     */
    private UserDTO admin;
    /**
     * Session for admin
     */
    private SessionDTO adminSession;

    /**
     * Admin test id
     */
    private final static String ADMIN_ID = "creador";
    /**
     * Admin test password
     */
    private final static String ADMIN_PASSWORD = "mando";

    /**
     * User test id
     */
    private final static String USER_ID = "Edu";
    /**
     * User test password
     */
    private final static String USER_PASSWORD = "0000";
    /**
     * New user test id
     */
    private final static String NEW_USER_ID = "Jositoh";
    /**
     * New user test password
     */
    private final static String NEW_USER_PASSWORD = "0901";

    /**
     * Initializes all the values to be used during the tests.
     */
    @Before
    public void setUp() {
        database = new UserDAOHashTableImp();
        notDatabase = new NotificationDAOHashTableImp();
        hashgen = new HashGenerator();
        user = new UserDTO(USER_ID, hashgen.hash(USER_PASSWORD.toCharArray()));
        newUser = new UserDTO(NEW_USER_ID, hashgen.hash(NEW_USER_PASSWORD
                .toCharArray()));
        admin = new UserDTO(ADMIN_ID, hashgen.hash(ADMIN_PASSWORD.toCharArray()), UserType.ADMIN);
        database.addUser(user);
        database.addUser(newUser);
        database.addUser(admin);
        manager = UserManagerAS.getManager(database);
        session = manager.login(USER_ID, USER_PASSWORD);
        newSession = manager.login(NEW_USER_ID, NEW_USER_PASSWORD);
        adminSession = manager.login(ADMIN_ID, ADMIN_PASSWORD);
    }

    /**
     * Testing the functionality of adding a notification to a user
     */
    @Test
    public void addNotificationTest() {
        manager.notifyUser(notDatabase, user, session, "Hello world!");
        assertEquals("Notification different from expected", new NotificationDTO(NEW_USER_ID, "Hello world!", new Date()).getNotification(),
                manager.getNotifications(notDatabase, session).get(0).getNotification());

        try {
            manager.logout(newSession);
            manager.notifyUser(notDatabase, user, newSession, "Wrong session");
            fail("Wrong session taken as correct");
        } catch (AccessControlException e) {
            //correct
        }
        newSession = manager.login(NEW_USER_ID, NEW_USER_PASSWORD);
    }

    /**
     * Testing the functionality of sanctioning a user
     */
    @Test
    public void sanctionUser() {
        ZonedDateTime now = ZonedDateTime.now();
        manager.banUser(user, adminSession, now, "you are banned");
        assertEquals(now, user.getBanTime());
    }
}
