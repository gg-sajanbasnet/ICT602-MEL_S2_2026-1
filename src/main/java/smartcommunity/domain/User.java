
package smartcommunity.domain;

public abstract class User {
    private final String userId;
    private String name;
    private String email;
    private final String passwordHash;

    public User(String userId, String name, String email, String passwordHash) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public void updateProfile(String name, String email) {
        if (name == null || name.isBlank() || email == null || email.isBlank()) {
            throw new IllegalArgumentException("Name and email cannot be empty.");
        }
        this.name = name;
        this.email = email;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
}
