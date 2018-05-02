package es.ucm.fdi.users;

import java.util.Date;
import java.util.List;
import java.time.Period;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
	 * Twitter access token
	 */
	private String twitterToken;
	
	/**
	 * Twitter access secret token
	 */
	private String twitterTokenSecret;
	
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
	     * Class constructor specifying more fields
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
		if (type == "USER") this.type = UserType.USER;
		else this.type = UserType.ADMIN;
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
	
	/**
	 * Sets the value of twitter token.
	 * @param twitterToken New twitter token value
	 */
	public void setTwitterToken(String twitterToken){
		this.twitterToken = twitterToken;
	}
	
	/**
	 * @return the twitter token
	 */
	public String getTwitterToken(){
		return twitterToken;
	}
	
	/**
	 * Sets the value of twitter secret token.
	 * @param twitterTokenSecret New twitter secret token value
	 */
	public void setTwitterTokenSecret(String twitterTokenSecret){
		this.twitterTokenSecret = twitterTokenSecret;
	}
	
	/**
	 * @return the twitter secret token
	 */
	public String getTwitterTokenSecret(){
		return twitterTokenSecret;
	}
}
