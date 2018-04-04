package es.ucm.fdi.users;

import java.util.Date;
import java.time.Period;
import java.io.Serializable;

/**
 * Contains a user's data.
 *
 * @author Eduardo Amaya
 * @author Javier Galiana
 *
 */

public class UserTO implements Serializable {
	
	private String ID;
	private String name;
	private String password;
	private Date date;
	private String email;
	private String telephone;
	private String picture;
	private String description;
	private UserType type;
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
