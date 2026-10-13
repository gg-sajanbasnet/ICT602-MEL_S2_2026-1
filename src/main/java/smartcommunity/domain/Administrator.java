
package smartcommunity.domain;

public class Administrator extends User {
    private final String adminId;

    public Administrator(String userId, String name, String email,
                         String passwordHash, String adminId) {
        super(userId, name, email, passwordHash);
        this.adminId = adminId;
    }

    public String getAdminId() { return adminId; }
}
