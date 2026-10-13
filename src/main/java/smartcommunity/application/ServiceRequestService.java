
package smartcommunity.application;

import smartcommunity.domain.*;
import smartcommunity.persistence.ServiceRequestRepository;

import java.util.List;
import java.util.UUID;

public class ServiceRequestService {

    private final ServiceRequestRepository repository;

    public ServiceRequestService(ServiceRequestRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository is required.");
        }
        this.repository = repository;
    }

    // Resident submits a new issue
    public ServiceRequest submitRequest(
            Resident resident,
            String category,
            String description,
            Location location) {

        if (resident == null) {
            throw new IllegalArgumentException("Resident is required.");
        }

        String requestId = "REQ-" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        ServiceRequest request = new ServiceRequest(
                requestId, resident, category, description, location);

        repository.save(request);
        return request;
    }

    // Council employee reviews and prioritises a request
    public void reviewRequest(
            CouncilEmployee employee,
            ServiceRequest request,
            RequestPriority priority) {

        requireEmployee(employee);
        requireStoredRequest(request);

        if (request.getStatus() != RequestStatus.SUBMITTED) {
            throw new IllegalStateException(
                    "Only submitted requests can be reviewed.");
        }

        request.setPriority(priority);
        request.setManagedBy(employee);
        request.updateStatus(RequestStatus.UNDER_REVIEW);

        repository.save(request);
    }

    // Council employee assigns a maintenance team
    public void assignRequest(
            CouncilEmployee employee,
            ServiceRequest request,
            MaintenanceTeam team) {

        requireEmployee(employee);
        requireStoredRequest(request);

        if (request.getStatus() != RequestStatus.UNDER_REVIEW) {
            throw new IllegalStateException(
                    "Request must be reviewed before assignment.");
        }

        if (team == null || team.getMembers().isEmpty()) {
            throw new IllegalArgumentException(
                    "A maintenance team with members is required.");
        }

        request.assignTeam(team);
        request.setManagedBy(employee);
        request.updateStatus(RequestStatus.ASSIGNED);

        repository.save(request);
    }

    // Assigned maintenance staff update progress
    public void updateProgress(
            MaintenanceStaff staff,
            ServiceRequest request,
            RequestStatus status) {

        if (staff == null) {
            throw new IllegalArgumentException("Maintenance staff is required.");
        }

        requireStoredRequest(request);

        MaintenanceTeam team = request.getAssignedTeam();

        if (team == null || !team.hasMember(staff)) {
            throw new SecurityException(
                    "Staff member is not assigned to this request.");
        }

        if (status == null) {
            throw new IllegalArgumentException("Status is required.");
        }

        RequestStatus current = request.getStatus();

        boolean validTransition =
                (current == RequestStatus.ASSIGNED
                        && status == RequestStatus.IN_PROGRESS)
                || (current == RequestStatus.IN_PROGRESS
                        && status == RequestStatus.COMPLETED);

        if (!validTransition) {
            throw new IllegalStateException(
                    "Invalid request status transition: "
                            + current + " -> " + status);
        }

        request.updateStatus(status);
        repository.save(request);
    }

    // Resident provides feedback after completion
    public Feedback submitFeedback(
            Resident resident,
            ServiceRequest request,
            int rating,
            String comment) {

        if (resident == null) {
            throw new IllegalArgumentException("Resident is required.");
        }

        requireStoredRequest(request);

        if (!request.getResident().getUserId()
                .equals(resident.getUserId())) {
            throw new SecurityException(
                    "Only the reporting resident can provide feedback.");
        }

        if (request.getStatus() != RequestStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Feedback is allowed only after completion.");
        }

        if (request.getFeedback() != null) {
            throw new IllegalStateException(
                    "Feedback has already been submitted.");
        }

        Feedback feedback = new Feedback(
                "FB-" + UUID.randomUUID().toString()
                        .substring(0, 8).toUpperCase(),
                rating,
                comment);

        request.setFeedback(feedback);
        repository.save(request);

        return feedback;
    }

    // Retrieve the status of a request
    public RequestStatus trackRequest(String requestId) {
        return getRequest(requestId).getStatus();
    }

    // Retrieve full request details
    public ServiceRequest getRequest(String requestId) {
        return repository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Request not found: " + requestId));
    }

    // Retrieve all requests for staff dashboards
    public List<ServiceRequest> getAllRequests() {
        return repository.findAll();
    }

    // Validate council employee
    private void requireEmployee(CouncilEmployee employee) {
        if (employee == null) {
            throw new SecurityException(
                    "Only council employees can perform this operation.");
        }
    }

    // Ensure the request belongs to the repository
    private void requireStoredRequest(ServiceRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request is required.");
        }

        ServiceRequest stored = repository
                .findById(request.getRequestId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Request does not exist."));

        if (stored != request) {
            throw new IllegalArgumentException(
                    "Request does not match the stored record.");
        }
    }
}
