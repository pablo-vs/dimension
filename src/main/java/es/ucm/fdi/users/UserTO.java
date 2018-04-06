package es.ucm.fdi.users;

import java.util.Date;
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
	 * Constructor mínimo.
	 */
	public UserTO(String ID, String password) {
		this.ID = ID;
		this.password = password;
	}

	public String getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public Date getDate() {
		return date;
	}

	public String getEmail() {
		return email;
	}

	public String getTelephone() {
		return telephone;
	}

	public String getPicture() {
		return picture;
	}

	public String getDescription() {
		return description;
	}

	public UserType getType() {
		return type;
	}

	public Period getBanTime() {
		return banTime;
	}
}
