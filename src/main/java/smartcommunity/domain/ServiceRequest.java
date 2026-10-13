
package smartcommunity.domain;

import java.time.LocalDateTime;

public class ServiceRequest {
    private final String requestId;
    private final String category;
    private final String description;
    private final LocalDateTime reportedAt;

    private RequestStatus status;
    private RequestPriority priority;

    private final Resident resident;
    private final Location location;

    private CouncilEmployee managedBy;
    private MaintenanceTeam assignedTeam;
    private Feedback feedback;

    public ServiceRequest(String requestId, Resident resident,
                          String category, String description, Location location) {
        if (resident == null || location == null) {
            throw new IllegalArgumentException("Resident and location are required.");
        }
        if (category == null || category.isBlank()
                || description == null || description.isBlank()) {
            throw new IllegalArgumentException("Category and description are required.");
        }

        this.requestId = requestId;
        this.resident = resident;
        this.category = category;
        this.description = description;
        this.location = location;
        this.reportedAt = LocalDateTime.now();
        this.status = RequestStatus.SUBMITTED;
        this.priority = RequestPriority.MEDIUM;
    }

    public void updateStatus(RequestStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }
        this.status = status;
    }

    public void setPriority(RequestPriority priority) {
        if (priority == null) {
            throw new IllegalArgumentException("Priority cannot be null.");
        }
        this.priority = priority;
    }

    public void assignTeam(MaintenanceTeam team) {
        if (team == null) {
            throw new IllegalArgumentException("Maintenance team is required.");
        }
        this.assignedTeam = team;
    }

    public void setManagedBy(CouncilEmployee employee) {
        this.managedBy = employee;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public String getRequestId() { return requestId; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public LocalDateTime getReportedAt() { return reportedAt; }
    public RequestStatus getStatus() { return status; }
    public RequestPriority getPriority() { return priority; }
    public Resident getResident() { return resident; }
    public Location getLocation() { return location; }
    public CouncilEmployee getManagedBy() { return managedBy; }
    public MaintenanceTeam getAssignedTeam() { return assignedTeam; }
    public Feedback getFeedback() { return feedback; }
}
