package es.ucm.fdi.users;

import java.util.Date;
import java.util.List;
import java.time.Period;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import twitter4j.auth.AccessToken;

/**
 * Contains a user data.
 *
 * @author Eduardo Amaya, Javier Galiana, Inmaculada Pérez
 *
 */
public class UserTO implements Serializable {

    /**
     * Identifier
     */
    private String ID;
    /**
     * Username
     */
    private String name;
    /**
     * Password
     */
    private String password;
    /**
     * Date of login
     */
    private Date date;
    /**
     * E-mail
     */
    private String email;
    /**
     * Phone number
     */
    private String telephone;
    /**
     * Icon
     */
    private String picture;
    /**
     * Profile description
     */
    private String description;
    /**
     * Range
     */
    private UserType type;
    /**
     * Banned period
     */
    private Period banTime;
    /**
     * Notifications
     */
    private List<String> notifications;

    /**
     * Representing authorized Access Twitter Token which is passed to the
     * service provider in order to access protected resources
     */
    private AccessToken twitterAccess = null;

    /**
     * Class constructor specifying id and password
     *
     * @param ID Identifier
     * @param password Password
     */
    public UserTO(String ID, String password) {
        this.ID = ID;
        this.password = password;
    }

    /**
     * Class constructor specifying more fields
     *
     * @param ID Identifier
     * @param password Password
     * @param name Name
     * @param date Date
     * @param email email
     * @param telephone Telephone
     * @param picture Picture
     * @param description Description
     * @param type Type
     * @param banTime Ban time
     * @throws ParseException
     */
    public UserTO(String ID, String password, String name, String date,
            String email, String telephone, String picture, String description,
            String type, String banTime) throws ParseException {
        this.ID = ID;
        this.password = password;
        this.name = name;
        SimpleDateFormat formatter = new SimpleDateFormat(date);
        this.date = formatter.parse(date);
        this.email = email;
        this.telephone = telephone;
        this.picture = picture;
        this.description = description;
        if ("USER".equals(type)) {
            this.type = UserType.USER;
        } else {
            this.type = UserType.ADMIN;
        }
        this.banTime = Period.parse(banTime);

    }

    /**
     * Class constructor specifying more fields including authentication tokens.
     *
     * @param ID Identifier
     * @param password Password
     * @param name Name
     * @param date Date
     * @param email email
     * @param telephone Telephone
     * @param picture Picture
     * @param description Description
     * @param type Type
     * @param banTime Ban time
     * @param twitterAccessToken access token to authorizes access to twitter
     * @param twitterAccessTokenSecret access secret token to authorizes access
     * to twitter
     * @throws ParseException
     */
    public UserTO(String ID, String password, String name, String date,
            String email, String telephone, String picture, String description,
            String type, String banTime, String twitterAccessToken, String twitterAccessTokenSecret)
            throws ParseException {
        this.ID = ID;
        this.password = password;
        this.name = name;
        SimpleDateFormat formatter = new SimpleDateFormat(date);
        this.date = formatter.parse(date);
        this.email = email;
        this.telephone = telephone;
        this.picture = picture;
        this.description = description;
        this.twitterAccess = new AccessToken(twitterAccessToken, twitterAccessTokenSecret);
        if ("USER".equals(type)) {
            this.type = UserType.USER;
        } else {
            this.type = UserType.ADMIN;
        }
        this.banTime = Period.parse(banTime);

    }

    /**
     *
     * @return the user's identifier
     */
    public String getID() {
        return ID;
    }

    /**
     *
     * @return the username
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @return the login date
     */
    public Date getDate() {
        return date;
    }

    /**
     *
     * @return the user's e-mail
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return the user's phone number
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     *
     * @return the url from the user's icon
     */
    public String getPicture() {
        return picture;
    }

    /**
     *
     * @return the user's profile description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return the user's range
     */
    public UserType getType() {
        return type;
    }

    /**
     *
     * @return the ban period
     */
    public Period getBanTime() {
        return banTime;
    }

    /**
     *
     * @return the list of notifications
     */
    public List<String> getNotifications() {
        return notifications;
    }

    /**
     *
     * @param newBanTime The new ban period
     */
    public void setBanTime(Period newBanTime) {
        banTime = newBanTime;
    }

    /**
     *
     * @return twitter access token
     */
    public AccessToken getTwitterAccess() {
        return twitterAccess;
    }

    /**
     *
     * @param twitterAccessToken new access token
     */
    public void setTwitterAccess(AccessToken twitterAccessToken) {
        this.twitterAccess = twitterAccessToken;
    }

    /**
     * @return null when no twitter access provided
     */
    public boolean hasTwitterAccess() {
        return (null == twitterAccess);
    }

}
