
package smartcommunity.domain;

public class MaintenanceStaff extends User {
    private final String staffId;

    public MaintenanceStaff(String userId, String name, String email,
                            String passwordHash, String staffId) {
        super(userId, name, email, passwordHash);
        this.staffId = staffId;
    }

    public String getStaffId() { return staffId; }
}
