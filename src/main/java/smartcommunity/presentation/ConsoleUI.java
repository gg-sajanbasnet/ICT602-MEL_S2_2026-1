
package smartcommunity.presentation;

import smartcommunity.application.AuthenticationService;
import smartcommunity.application.ServiceRequestService;
import smartcommunity.domain.*;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {

    private final AuthenticationService authentication;
    private final ServiceRequestService requests;
    private final MaintenanceTeam maintenanceTeam;
    private final Scanner scanner = new Scanner(System.in);

    private User currentUser;

    public ConsoleUI(
            AuthenticationService authentication,
            ServiceRequestService requests,
            MaintenanceTeam maintenanceTeam) {
        this.authentication = authentication;
        this.requests = requests;
        this.maintenanceTeam = maintenanceTeam;
    }

    public void start() {
        System.out.println("======================================");
        System.out.println(" SMART COMMUNITY SERVICES PLATFORM");
        System.out.println("======================================");

        while (true) {
            System.out.println("\n1. Login");
            System.out.println("0. Exit");

            String choice = input("Choose an option: ");

            if (choice.equals("0")) {
                System.out.println("Goodbye!");
                return;
            }

            if (!choice.equals("1")) {
                System.out.println("Invalid option.");
                continue;
            }

            try {
                login();
                showRoleMenu();
            } catch (IllegalArgumentException |
                     IllegalStateException |
                     SecurityException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                logout();
            }
        }
    }

    private void login() {
        String email = input("Email: ");
        String password = input("Password: ");

        currentUser = authentication.authenticate(email, password);

        System.out.println("\nWelcome, " + currentUser.getName());
    }

    private void logout() {
        if (currentUser != null) {
            authentication.logout(currentUser);
            currentUser = null;
            System.out.println("Logged out.");
        }
    }

    private void showRoleMenu() {
        while (currentUser != null) {
            try {
                if (currentUser instanceof Resident resident) {
                    residentMenu(resident);
                } else if (currentUser instanceof CouncilEmployee employee) {
                    employeeMenu(employee);
                } else if (currentUser instanceof MaintenanceStaff staff) {
                    maintenanceMenu(staff);
                } else if (currentUser instanceof Administrator) {
                    administratorMenu();
                }
            } catch (IllegalArgumentException |
                     IllegalStateException |
                     SecurityException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void residentMenu(Resident resident) {
        System.out.println("\n--- Resident Menu ---");
        System.out.println("1. Report an issue");
        System.out.println("2. View my requests");
        System.out.println("3. Track request");
        System.out.println("4. Submit feedback");
        System.out.println("0. Logout");

        switch (input("Choose: ")) {
            case "1" -> submitIssue(resident);
            case "2" -> myRequests(resident);
            case "3" -> trackRequest(resident);
            case "4" -> submitFeedback(resident);
            case "0" -> logout();
            default -> System.out.println("Invalid option.");
        }
    }

    private void employeeMenu(CouncilEmployee employee) {
        System.out.println("\n--- Council Employee Menu ---");
        System.out.println("1. View all requests");
        System.out.println("2. Review and prioritise request");
        System.out.println("3. Assign maintenance team");
        System.out.println("0. Logout");

        switch (input("Choose: ")) {
            case "1" -> printRequests(requests.getAllRequests());
            case "2" -> reviewRequest(employee);
            case "3" -> assignRequest(employee);
            case "0" -> logout();
            default -> System.out.println("Invalid option.");
        }
    }

    private void maintenanceMenu(MaintenanceStaff staff) {
        System.out.println("\n--- Maintenance Staff Menu ---");
        System.out.println("1. View assigned requests");
        System.out.println("2. Update request progress");
        System.out.println("0. Logout");

        switch (input("Choose: ")) {
            case "1" -> printRequests(assignedRequests(staff));
            case "2" -> updateProgress(staff);
            case "0" -> logout();
            default -> System.out.println("Invalid option.");
        }
    }

    private void administratorMenu() {
        System.out.println("\n--- Administrator Menu ---");
        System.out.println("1. View request summary");
        System.out.println("0. Logout");

        switch (input("Choose: ")) {
            case "1" -> {
                List<ServiceRequest> all = requests.getAllRequests();
                System.out.println("Total requests: " + all.size());

                for (RequestStatus status : RequestStatus.values()) {
                    long count = all.stream()
                            .filter(r -> r.getStatus() == status)
                            .count();
                    System.out.println(status + ": " + count);
                }
            }
            case "0" -> logout();
            default -> System.out.println("Invalid option.");
        }
    }

    private void submitIssue(Resident resident) {
        String category = input("Issue category: ");
        String description = input("Description: ");
        String address = input("Location address: ");
        double latitude = readDouble("Latitude: ");
        double longitude = readDouble("Longitude: ");

        Location location = new Location(
                latitude, longitude, address);

        ServiceRequest request = requests.submitRequest(
                resident, category, description, location);

        System.out.println("Request submitted successfully.");
        System.out.println("Request ID: " + request.getRequestId());
        System.out.println("Status: " + request.getStatus());
    }

    private void myRequests(Resident resident) {
        List<ServiceRequest> mine = requests.getAllRequests()
                .stream()
                .filter(r -> r.getResident().getUserId()
                        .equals(resident.getUserId()))
                .toList();

        printRequests(mine);
    }

    private void trackRequest(Resident resident) {
        ServiceRequest request = ownRequest(resident);

        System.out.println("Request ID: " + request.getRequestId());
        System.out.println("Category: " + request.getCategory());
        System.out.println("Description: " + request.getDescription());
        System.out.println("Status: " + request.getStatus());
        System.out.println("Priority: " + request.getPriority());
    }

    private void submitFeedback(Resident resident) {
        ServiceRequest request = ownRequest(resident);
        int rating = readInt("Rating (1-5): ");
        String comment = input("Comment: ");

        Feedback feedback = requests.submitFeedback(
                resident, request, rating, comment);

        System.out.println(
                "Feedback submitted: " + feedback.getFeedbackId());
    }

    private void reviewRequest(CouncilEmployee employee) {
        ServiceRequest request = findRequest();
        System.out.println("Priorities: LOW, MEDIUM, HIGH, URGENT");

        RequestPriority priority = RequestPriority.valueOf(
                input("Priority: ").toUpperCase());

        requests.reviewRequest(employee, request, priority);
        System.out.println("Request reviewed successfully.");
    }

    private void assignRequest(CouncilEmployee employee) {
        ServiceRequest request = findRequest();

        requests.assignRequest(
                employee, request, maintenanceTeam);

        System.out.println("Assigned to: "
                + maintenanceTeam.getTeamName());
    }

    private void updateProgress(MaintenanceStaff staff) {
        ServiceRequest request = findRequest();

        System.out.println("Available: IN_PROGRESS, COMPLETED");

        RequestStatus status = RequestStatus.valueOf(
                input("New status: ").toUpperCase());

        requests.updateProgress(staff, request, status);

        System.out.println("Updated status: " + request.getStatus());
    }

    private ServiceRequest ownRequest(Resident resident) {
        ServiceRequest request = findRequest();

        if (!request.getResident().getUserId()
                .equals(resident.getUserId())) {
            throw new SecurityException(
                    "You can only access your own requests.");
        }

        return request;
    }

    private ServiceRequest findRequest() {
        return requests.getRequest(input("Request ID: "));
    }

    private List<ServiceRequest> assignedRequests(
            MaintenanceStaff staff) {

        return requests.getAllRequests().stream()
                .filter(r -> r.getAssignedTeam() != null)
                .filter(r -> r.getAssignedTeam().hasMember(staff))
                .toList();
    }

    private void printRequests(List<ServiceRequest> list) {
        if (list.isEmpty()) {
            System.out.println("No requests found.");
            return;
        }

        for (ServiceRequest request : list) {
            System.out.printf(
                    "%s | %s | %s | %s%n",
                    request.getRequestId(),
                    request.getCategory(),
                    request.getStatus(),
                    request.getPriority());
        }
    }

    private String input(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        return Integer.parseInt(input(prompt));
    }

    private double readDouble(String prompt) {
        return Double.parseDouble(input(prompt));
    }
}
