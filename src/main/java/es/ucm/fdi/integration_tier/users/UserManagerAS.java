package es.ucm.fdi.integration_tier.users;

import java.time.Period;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.security.AccessControlException;

import es.ucm.fdi.workspace.util.HashGenerator;
import es.ucm.fdi.integration_tier.connectivity.CommentBO;
import es.ucm.fdi.integration_tier.connectivity.CommentDAO;
import es.ucm.fdi.business_tier.exceptions.DuplicatedIDException;
import es.ucm.fdi.business_tier.exceptions.NotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Application service to manage user accounts and sessions.
 *
 * @author Pablo Villalobos, Inmaculada Pérez
 */
public class UserManagerAS {

    /**
     * Instance of singleton pattern
     */
    private static UserManagerAS instance;
    /**
     * User data access object
     */
    private UserDAO dao;
    /**
     * Hash generator
     */
    private HashGenerator hashgen = new HashGenerator();
    /**
     * Current active sessions
     */
    private HashMap<String, SessionBO> activeSessions = new HashMap<>();

    /**
     * Class constructor specifying user DAO
     *
     * @param dao Data access object from user
     */
    private UserManagerAS(UserDAO dao) {
        this.dao = dao;
    }

    /**
     * Gets the current manager or creates a new one if it does not exist, using
     * the given database.
     *
     * @param dao The UserDAO to use.
     * @return The User Manager.
     */
    public static UserManagerAS getManager(UserDAO dao) {
        if (instance == null) {
            instance = new UserManagerAS(dao);
        }
        return instance;
    }

    /**
     * Adds a new user to the database.
     *
     * @param user The user to add.
     */
    public void newUser(UserTransfer user) throws DuplicatedIDException, IllegalArgumentException {
        if (!existsUser(user.getID())) {
            if (validateAccountDetails(user)) {
                dao.addUser(user);

            } else {
                throw new IllegalArgumentException("Invalid account details");
            }
        } else {
            throw new DuplicatedIDException("User " + user.getID() + " already exists");
        }
    }

    /**
     * Removes a user from the database. Requires an active session.
     *
     * @param id The identifier of the user to be deleted.
     * @param session The session from which to perform the action.
     */
    public void removeUser(String id, SessionBO session) throws NotFoundException, AccessControlException {
        if (authenticate(id, session)) {
            if (existsUser(id)) {
                dao.removeUser(id);
            } else {
                throw new NotFoundException("User " + id + " does not exist");
            }
        } else {
            throw new AccessControlException("Invalid session");
        }
    }

    /**
     * Ban a user. This requires an active session.
     *
     * @param user The new account details.
     * @param session The session from which to perform the action.
     * @param banTime The ban period.
     * @param banNotification The notification which is sent to the user.
     */
    public void banUser(UserTransfer user, SessionBO session, Period banTime, String banNotification) throws AccessControlException, IllegalArgumentException {
        if (!banTime.isNegative()) {
            if (authenticate(user.getID(), session)) {
                if (validateAccountDetails(user)) 
            } else {
                throw new AccessControlException("Invalid session");
            }
        } else {
            throw new IllegalArgumentException("Invalid ban period");
        }
    }

    /**
     * Send a notification to a user. This requires an active session.
     *
     * @param user The new account details.
     * @param session The session from which to perform the action.
     * @param notification The notification which is sent to the user.
     */
    public void notifyUser(UserTO user, SessionBO session, String notification) throws AccessControlException, IllegalArgumentException {
        if (authenticate(user.getID(), session)) {
            if (validateAccountDetails(user)) {
                //user.getNotifications().add(notification);
                dao.modifyUser(user);
            }
        } else {
            throw new AccessControlException("Invalid session");
        }
    }

    /**
     * Modify a user's information in the database. This requires an active
     * session.
     *
     * @param user The new account details.
     * @param session The session from which to perform the action.
     */
    public void modifyUser(UserTO user, SessionBO session) throws AccessControlException, IllegalArgumentException {
        if (authenticate(user.getID(), session)) {
            try {
                if (validateAccountDetails(user)) {
                    dao.modifyUser(user);
                }
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid account details");
            }
        } else {
            throw new AccessControlException("Invalid session");
        }
    }

    /**
     * Validates the given data and returns a new session.
     *
     * @param username The identifier of the user.
     * @param passwd The password provided
     * @return The new session.
     */
    public SessionBO login(String username, String passwd) throws AccessControlException, NotFoundException {
        //Looks for this username in the database.
        if (existsUser(username)) {
            UserTO user = dao.findUser(username);
            //If the introduced password matches the given one
            //create and return a new session
            if (hashgen.authenticate(passwd.toCharArray(), user.getPassword())) {
                return createNewSession(username);
            } else {
                throw new AccessControlException("Incorrect password");
            }
        } else {
            throw new NotFoundException("User " + username + " does not exist");
        }
    }

    /**
     * Checks if a session is valid.
     *
     * @param session The session to check.
     * @return if the session is valid.
     */
    public boolean validateSession(SessionBO session) {
        return activeSessions.containsKey(session.getID());
    }

    /**
     * Checks if the given session is an active session with privileges over the
     * given user account.
     *
     * @param username The claimed user name.
     * @param session The session to check.
     * @return if the session has been validated
     */
    public boolean authenticate(String username, SessionBO session) {
        if (validateSession(session)) {
            String sessionUser = session.getUser();
            return sessionUser.equals(username)
                    || dao.findUser(sessionUser).getType() == UserType.ADMIN;
        } else {
            return false;
        }
    }

    /**
     * Closes the given session.
     *
     * @param session The session to close.
     */
    public void logout(SessionBO session) {
        activeSessions.remove(session.getID());
    }

    /**
     *
     * @param username The name to check.
     * @return if the user exists
     */
    public boolean existsUser(String username) {
        return dao.findUser(username) != null;
    }

    /**
     * Creates a new session for a given user.
     *
     * @param user User
     * @return the new session
     * @throws IllegalArgumentException if the user is already logged in.
     */
    private SessionBO createNewSession(String user) throws IllegalArgumentException {
        SessionBO sesion = null;
        if (activeSessions.get(user) != null) {
            throw new IllegalArgumentException("This user is already logged in");
        } else {
            sesion = new SessionBO(user, ZonedDateTime.now());
            activeSessions.put(sesion.getID(), sesion);
        }
        return sesion;
    }

    /**
     * Validates the e-mail.
     *
     * @param email User's e-mail
     * @return true if e-mail is valid
     */
    public static boolean validEmail(String email) {
        if (email == null) {
            return false;
        }
        // Input the string for validation
        // String email = "xyz@.com";
        // Set the email pattern string
        Pattern p = Pattern.compile("[^@]+@[^@]+\\.[a-z]+");

        // Match the given string with the pattern
        Matcher m = p.matcher(email);

        // check whether match is found
        boolean matchFound = m.matches();

        StringTokenizer st = new StringTokenizer(email, ".");
        String lastToken = null;
        while (st.hasMoreTokens()) {
            lastToken = st.nextToken();
        }

        return matchFound && lastToken.length() >= 2
                && email.length() - 1 != lastToken.length(); // validate the country code
    }

    /**
     * See the user's comments.
     *
     * @param comments List of comments
     * @param username User ID
     * @return The list of comments made by the user
     */
    public List<CommentBO> seeComments(CommentDAO comments, String username) {
        return comments.findByUser(username);
    }

    /**
     * Validates a word format.
     *
     * @param word Word to validate
     * @return wether the word is valid
     */
    private static boolean validString(String word) {
        return word != null && word.matches("[a-zA-Z1-9_]+");
    }

    /**
     * Validates a number format.
     *
     * @param num Number to validate
     * @return wether the word is valid
     */
    private static boolean validInt(String num) {
        return num != null && num.matches("[0-9]+");
    }

    /**
     * Validates an url format.
     *
     * @param num Url to validate
     * @return wether the url is valid
     */
    private static boolean validUrl(String site) {
        boolean r = true;
        try {
            URL url = new URL(site);
            URLConnection conn = url.openConnection();
            conn.connect();
        } catch (MalformedURLException e) {
            // the URL is not in a valid form
            r = false;
        } catch (IOException e) {
            // the connection couldn't be established
            r = false;
        }
        return r;
    }

    /**
     * Validates an account.
     *
     * @param user User
     * @return if the account has been validated
     */
    private boolean validateAccountDetails(UserTO user) {
        if (validString(user.getID())
                && HashGenerator.checkFormat(user.getPassword())
                && (user.getName() == null || validString(user.getName()))
                && (user.getDescription() == null || validString(user.getDescription()))
                && (user.getEmail() == null || validEmail(user.getEmail()))
                && (user.getTelephone() == null || validInt(user.getTelephone()))
                && (user.getPicture() == null || validUrl(user.getPicture()))) {
            return true;
        }
        throw new IllegalArgumentException("");
    }

}
