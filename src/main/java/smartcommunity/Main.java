
package smartcommunity;

import smartcommunity.application.AuthenticationService;
import smartcommunity.application.ServiceRequestService;
import smartcommunity.domain.*;
import smartcommunity.persistence.InMemoryServiceRequestRepository;
import smartcommunity.presentation.ConsoleUI;

public class Main {

    public static void main(String[] args) {

        var repository = new InMemoryServiceRequestRepository();
        var requestService = new ServiceRequestService(repository);
        var authenticationService = new AuthenticationService();

        String demoHash =
                AuthenticationService.hashPassword("Password123");

        Resident resident = new Resident(
                "U001", "Sajan Basnet Resident", "resident@demo.com",
                demoHash, "Greenfield City", "0400000001");

        CouncilEmployee employee = new CouncilEmployee(
                "U002", "Janatun Council Employee", "employee@demo.com",
                demoHash, "E001", "Public Works");

        MaintenanceStaff staff = new MaintenanceStaff(
                "U003", "Manisha Maintenance", "staff@demo.com",
                demoHash, "M001");

        Administrator administrator = new Administrator(
                "U004", "Abhilas Administrator", "admin@demo.com",
                demoHash, "A001");

        authenticationService.registerDemoUser(resident);
        authenticationService.registerDemoUser(employee);
        authenticationService.registerDemoUser(staff);
        authenticationService.registerDemoUser(administrator);

        MaintenanceTeam team = new MaintenanceTeam(
                "TEAM001", "Road Maintenance Team");
        team.addMember(staff);

        ConsoleUI console = new ConsoleUI(
                authenticationService, requestService, team);

        console.start();
    }
}
