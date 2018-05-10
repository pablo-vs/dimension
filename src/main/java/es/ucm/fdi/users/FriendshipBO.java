package es.ucm.fdi.users;

/**
 * Represents a friendship between two users.
 *
 * @author Pablo Villalobos
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
     * Class constructor specifying both users.
     *
     * @param user1 User 1
     * @param user2 User 2
     */
    public FriendshipBO(String user1, String user2) {
        this.user1 = user1;
        this.user2 = user2;
    }
}
