package es.ucm.fdi.users;

/**
 * Represents a friendship between two users.
 *
 * @author Pablo Villalobos
 * @version 04.04.2018
 */
public class FriendshipBO {
	private String user1, user2;

	public FriendshipBO(String u1, String u2) {
		user1 = u1;
		user2 = u2;
	}
}
