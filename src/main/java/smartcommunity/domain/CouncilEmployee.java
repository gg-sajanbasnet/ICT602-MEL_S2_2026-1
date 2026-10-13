
package smartcommunity.domain;

public class CouncilEmployee extends User {
    private final String employeeId;
    private final String department;

    public CouncilEmployee(String userId, String name, String email,
                           String passwordHash, String employeeId, String department) {
        super(userId, name, email, passwordHash);
        this.employeeId = employeeId;
        this.department = department;
    }

    public String getEmployeeId() { return employeeId; }
    public String getDepartment() { return department; }
}
