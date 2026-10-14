# Smart Community Services Platform

Sajan Basnet - 71445
Jannatun Naim Ankhi - 70612
Manisha Balagoni - 70506
Abhilash Alle - 68406

ICT602 – Software Engineering  
Assignment 3 – Part A

## About the Project

This project is based on the Smart Community Services Platform for Greenfield City Council.

The overall system is designed to help residents access council services, report local issues, book facilities and participate in community events.

For this assignment, we have implemented the **Service Request Management** module in Java. It is a console-based application where residents can report issues and council staff can manage them until they are completed.

The UML diagrams for the overall system and the Java implementation are also included in this repository.

## Features

The Java application currently supports:

- Login for residents, council employees, maintenance staff and administrators
- Submitting service requests with location details
- Viewing and tracking requests
- Reviewing requests and setting priorities
- Assigning requests to maintenance teams
- Updating request progress
- Submitting feedback after a request is completed
- Viewing a summary of requests as an administrator

## Technologies Used

- Java 21
- Object-Oriented Programming (OOP)
- Layered architecture
- In-memory data storage
- PlantUML for UML diagrams

## How to Run

Make sure Java 21 or later is installed.

Open the terminal in the project folder and compile the Java files:

```bash
mkdir -p out/classes

javac -d out/classes \
  src/main/java/smartcommunity/domain/*.java \
  src/main/java/smartcommunity/persistence/*.java \
  src/main/java/smartcommunity/application/*.java \
  src/main/java/smartcommunity/presentation/*.java \
  src/main/java/smartcommunity/Main.java
```

Then run:

```bash
java -cp out/classes smartcommunity.Main
```

The application will start in the terminal and display the login menu.

## Demo Accounts

The following accounts are already created for testing the application.

| Role | Email | Password |
|---|---|---|
| Resident | resident@demo.com | Password123 |
| Council Employee | employee@demo.com | Password123 |
| Maintenance Staff | staff@demo.com | Password123 |
| Administrator | admin@demo.com | Password123 |

## How to Test the Application

To try the complete service request process:

1. Log in as a resident and submit a new request.
2. Copy the request ID shown after submission.
3. Log in as a council employee, review the request and assign it to the maintenance team.
4. Log in as maintenance staff and update the request to `IN_PROGRESS` and then `COMPLETED`.
5. Log in as the resident again to track the request and submit feedback.
6. Log in as an administrator to view the request summary.

The request moves through these statuses:

`SUBMITTED → UNDER_REVIEW → ASSIGNED → IN_PROGRESS → COMPLETED`

## Project Structure

The Java source code is organised into four packages:

- `domain` – Classes such as User, ServiceRequest, Location and MaintenanceTeam
- `application` – Handles login and service request operations
- `persistence` – Stores and retrieves requests
- `presentation` – Handles the console menus and user input

The UML diagrams are available in the `docs/uml` folder.

## Notes

This is a prototype developed for the assignment, so it uses in-memory storage instead of a database. All submitted requests will be lost when the application is closed.

Only the Service Request Management module has been implemented in Java. The other modules are covered in the overall system design but are not part of the working application.
