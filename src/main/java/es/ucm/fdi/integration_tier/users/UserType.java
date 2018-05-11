package es.ucm.fdi.integration_tier.users;

/**
 * Represents the distinct types of users: User and admin.
 */
public enum UserType {
    USER(1), ADMIN(0);

    private final int num;

    UserType(int num) {
        this.num = num;
    }

    public static UserType fromInt(int num) {
        if (num == 0) {
            return ADMIN;
        } else {
            return USER;
        }
    }
}
