/*
  This file is part of Dimension.
  Dimension is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  Dimension is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  You should have received a copy of the GNU General Public License
  along with Dimension.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.business.users;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.security.AccessControlException;
import java.util.Date;

import es.ucm.fdi.business.util.HashGenerator;
import es.ucm.fdi.business.connectivity.CommentDTO;
import es.ucm.fdi.integration.connectivity.CommentDAO;
import es.ucm.fdi.business.exceptions.DuplicatedIDException;
import es.ucm.fdi.business.exceptions.NotFoundException;
import es.ucm.fdi.integration.users.NotificationDAO;
import es.ucm.fdi.integration.users.NotificationDAOSQLImp;
import es.ucm.fdi.integration.users.UserDAO;
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
    private final UserDAO dao;
    /**
     * Hash generator
     */
    private final HashGenerator hashgen = new HashGenerator();
    /**
     * Current active sessions
     */
    private final HashMap<String, SessionDTO> activeSessions = new HashMap<>();

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
    public void addNewUser(UserDTO user) throws DuplicatedIDException, IllegalArgumentException {
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
    public void removeUser(String id, SessionDTO session) throws NotFoundException, AccessControlException {
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
     * Bans a user. This requires an active session.
     *
     * @param user The new account details.
     * @param session The session from which to perform the action.
     * @param banTime The ban period.
     * @param banNotification The notification which is sent to the user.
     */
    public void banUser(UserDTO user, SessionDTO session, ZonedDateTime banTime, String banNotification) throws AccessControlException, IllegalArgumentException {
        if (!banTime.isAfter(ZonedDateTime.now())) {
            if (authenticate(session.getUser(), session)) {
                UserDTO banner = dao.findUser(session.getID());
                if (banner.getType() == UserType.ADMIN) {
                    modifyUser(user, session);
                    notifyUser(new NotificationDAOSQLImp(), user, session, banNotification);
                } else {
                    throw new AccessControlException("Restricted operation");
                }
            } else {
                throw new AccessControlException("Invalid session");
            }
        } else {
            throw new IllegalArgumentException("Invalid ban period");
        }
    }

    /**
     * Sends a notification to a user. This requires an active session.
     *
     * @param notifications Notifications data access object.
     * @param user The new account details.
     * @param session The session from which to perform the action.
     * @param notification The notification which is sent to the user.
     */
    public void notifyUser(NotificationDAO notifications, UserDTO user, SessionDTO session, String notification) throws AccessControlException, IllegalArgumentException {
        if (authenticate(session.getUser(), session)) {
            notifications.addNotification(new NotificationDTO(user.getID(), notification, new Date()));
        } else {
            throw new AccessControlException("Invalid session");
        }
    }

    /**
     * Sends a notification to a user. This requires an active session.
     *
     * @param notifications Notifications data access object.
     * @param session The session from which to perform the action.
     * @return A list of notifications.
     */
    public List<NotificationDTO> getNotifications(NotificationDAO notifications, SessionDTO session) throws AccessControlException, IllegalArgumentException {
        if (authenticate(session.getUser(), session)) {
            return notifications.findByUser(session.getUser());
        } else {
            throw new AccessControlException("Invalid session");
        }
    }

    /**
     * Modifies a user's information in the database. This requires an active
     * session.
     *
     * @param user The new account details.
     * @param session The session from which to perform the action.
     */
    public void modifyUser(UserDTO user, SessionDTO session) throws AccessControlException, IllegalArgumentException {
        if (authenticate(user.getID(), session)) {
            try {
                if (validateAccountDetails(user)) {
                    if (user.getType() != dao.findUser(user.getID()).getType()) {
                        throw new IllegalArgumentException("The type of user "
                                + "cannot be modified!");
                    }
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
    public SessionDTO login(String username, String passwd) throws AccessControlException, NotFoundException {
        //Looks for this username in the database.
        if (existsUser(username)) {
            UserDTO user = dao.findUser(username);
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
    public boolean validateSession(SessionDTO session) {
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
    public boolean authenticate(String username, SessionDTO session) {
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
    public void logout(SessionDTO session) {
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
    private SessionDTO createNewSession(String user) throws IllegalArgumentException {
        SessionDTO sesion = null;
        if (activeSessions.get(user) != null) {
            throw new IllegalArgumentException("This user is already logged in");
        } else {
            sesion = new SessionDTO(user, ZonedDateTime.now());
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
    public List<CommentDTO> seeComments(CommentDAO comments, String username) {
        return comments.findByUser(username);
    }

    /**
     * Validates a word format.
     *
     * @param word Word to validate
     * @return wether the word is valid
     */
    private static boolean validString(String word) {
        return word != null && word.matches("[a-zA-Z1-9_ ]+");
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
    private boolean validateAccountDetails(UserDTO user) {
        if (!validString(user.getID())) {
            throw new IllegalArgumentException("Invalid user ID provided: " + user.getID());
        }
        if (!HashGenerator.checkFormat(user.getPassword())) {
            throw new IllegalArgumentException("Invalid user password provided");
        }
        if (!(user.getName() == null || validString(user.getName()))) {
            throw new IllegalArgumentException("Invalid user password provided");
        }
        if (!(user.getDescription() == null || validString(user.getDescription()))) {
            throw new IllegalArgumentException("Invalid user description provided: "
                    + user.getDescription());
        }
        if (!(user.getEmail() == null || validEmail(user.getEmail()))) {
            throw new IllegalArgumentException("Invalid user email provided: "
                    + user.getEmail());
        }
        if (!(user.getTelephone() == null || validInt(user.getTelephone()))) {
            throw new IllegalArgumentException("Invalid user telephone provided: "
                    + user.getTelephone());
        }
        if (!(user.getPicture() == null || validUrl(user.getPicture()))) {
            throw new IllegalArgumentException("Invalid user picture provided: "
                    + user.getPicture());
        }
        return true;
    }

    /**
     * Returns the information from an specified user given its id.
     *
     * @param userID
     * @param session The session from which to perform the action.
     * @return
     */
    public UserDTO getUserInformation(String userID, SessionDTO session) throws AccessControlException, IllegalArgumentException {
        if (authenticate(userID, session)) {
            UserDTO user = dao.findUser(userID);
            if (user != null) {
                return user;
            }
            throw new IllegalArgumentException("The userID requested does not exists!");
        } else {
            throw new AccessControlException("Invalid session");
        }
    }
}
