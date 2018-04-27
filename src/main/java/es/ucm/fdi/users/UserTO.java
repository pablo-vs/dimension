package es.ucm.fdi.users;

import java.util.Date;
import java.util.List;
import java.time.Period;
import java.io.Serializable;

/**
 * Contains a user data.
 *
 * @author Eduardo Amaya
 * @author Javier Galiana
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
         * Class constructor specifying id and password
         * @param ID Identifier
         * @param password Password
         */
	public UserTO(String ID, String password) {
		this.ID = ID;
		this.password = password;
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
	public List<String> getNotifications(){
		return notifications;
	}
	
	    /**
	     * 
	     * @param newBanTime The new ban period 
	     */
	public void setBanTime(Period newBanTime) {
		banTime = newBanTime;
	}
}
