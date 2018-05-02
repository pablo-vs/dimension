package es.ucm.fdi.users;

/**
 * Represents a friendship between two users.
 *
 * @author Pablo Villalobos
 * @version 04.04.2018
 */
public class FriendshipBO {
        /**
         * User 1
         */
	private final String user1;
        /**
         * User 2
         */
        private final String user2;

        /**
         * Class constructor specifying both users
         * @param u1 User 1
         * @param u2 User 2
         */
	public FriendshipBO(String u1, String u2) {
		user1 = u1;
		user2 = u2;
	}
}
