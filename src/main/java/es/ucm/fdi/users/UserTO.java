package es.ucm.fdi.users;

import java.util.Date;
import java.time.Period;
import java.io.Serializable;
import java.text.ParseException;
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
     * Class constructor specifying more fields.
     *
     * @param ID 
     * @param password 
     * @param name 
     * @param date 
     * @param email 
     * @param telephone 
     * @param picture 
     * @param description 
     * @param type 
     * @param banTime
     * @param twitterAccess
     */
    public UserTO(String ID, String name, String password, Date date,
            String email, String telephone, String picture, String description,
            UserType type, Period banTime, AccessToken twitterAccess) {
        this.ID = ID;
        this.password = password;
        this.name = name;
        this.date = date;
        this.email = email;
        this.telephone = telephone;
        this.picture = picture;
        this.description = description;
        this.type = type;
        this.banTime = banTime;
        this.twitterAccess = twitterAccess;
    }

    /**
     * Class constructor specifying more fields including authentication tokens.
     *
     * @param ID 
     * @param password 
     * @param name 
     * @param date 
     * @param email 
     * @param telephone 
     * @param picture 
     * @param description 
     * @param type 
     * @param banTime 
     * @param twitterAccessToken access token to authorizes access to twitter
     * @param twitterAccessTokenSecret access secret token to authorizes access
     * to twitter
     * @throws ParseException
     */
    public UserTO(String ID, String name, String password, Date date,
            String email, String telephone, String picture, String description,
            UserType type, Period banTime, String twitterAccessToken, String twitterAccessTokenSecret)
            throws ParseException {
        this(ID, name, password, date, email, telephone, picture, description, type,
                banTime, new AccessToken(twitterAccessToken, twitterAccessTokenSecret));
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
