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

import java.util.Date;
import java.time.ZonedDateTime;
import java.io.Serializable;
import java.text.ParseException;
import twitter4j.auth.AccessToken;

/**
 * Contains a user data.
 *
 * @author Eduardo Amaya, Javier Galiana, Inmaculada Pérez
 */
public class UserDTO implements Serializable {

    private String ID;
    private String name;
    private String password;
    /**
     * Date of login
     */
    private Date date;
    private String email;
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
    private ZonedDateTime banTime;

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
    public UserDTO(String ID, String password) {
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
    public UserDTO(String ID, String name, String password, Date date,
            String email, String telephone, String picture, String description,
            UserType type, ZonedDateTime banTime) {
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
    public UserDTO(String ID, String name, String password, Date date,
            String email, String telephone, String picture, String description,
            UserType type, ZonedDateTime banTime, String twitterAccessToken, String twitterAccessTokenSecret)
            throws ParseException {
        this(ID, name, password, date, email, telephone, picture, description, type,
                banTime);
        twitterAccess = new AccessToken(twitterAccessToken, twitterAccessTokenSecret);
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
    public ZonedDateTime getBanTime() {
        return banTime;
    }

    /**
     *
     * @param newBanTime The new ban period
     */
    public void setBanTime(ZonedDateTime newBanTime) {
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
     * @return false when no twitter access provided
     */
    public boolean hasTwitterAccess() {
        return (null != twitterAccess);
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof UserDTO) && ((UserDTO) other).ID.equals(ID);
    }

}
