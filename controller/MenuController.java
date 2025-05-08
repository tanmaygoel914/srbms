package controller;

import entity.*;
import services.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MenuController {

    private UserService userService;
    private ResourceService resourceService;
    private BookingService bookingService;
    private ReportService reportService;
    private User currentUser;
    Scanner scanner = new Scanner(System.in);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public MenuController(UserService userService, ResourceService resourceService, BookingService bookingService,
            ReportService reportService) {
        this.userService = userService;
        this.resourceService = resourceService;
        this.bookingService = bookingService;
        this.reportService = reportService;
    }

    public void start() {
        while (true) {
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");

            int choice = getValidIntInput("Choose an option: ");
            scanner.nextLine();

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    System.out.println("Thank you for using");
                    return;
                default:
                    System.out.println("galat choice Please try again.");
                    waitForEnter();
            }
        }
    }

    private int getValidIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    private double getValidDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return scanner.nextDouble();
            } catch (Exception e) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    private void waitForEnter() {
        System.out.print("\nPress Enter");
        scanner.nextLine();
    }

    private void login() {
        System.out.println("Login");
        System.out.println("Enter email:");
        String email = scanner.nextLine();
        System.out.println("Enter Password:");
        String password = scanner.nextLine();

        currentUser = userService.login(email, password);
        if (currentUser == null) {
            System.out.println("Invalid email or password.");
            waitForEnter();
            return;
        }

        handleUserSession();
    }

    private void register() {
        System.out.println("Register");
        System.out.println("Enter email:");
        String email = scanner.nextLine();
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            System.out.println("Invalid email format.");
            waitForEnter();
            return;
        }
        System.out.println("Enter Password:");
        String password = scanner.nextLine();
        if (password.length() < 8 || !password.matches(".*[a-zA-Z].*") || !password.matches(".*\\d.*")) {
            System.out.println("Password should be at least 8 characters and should have number and characters");
            waitForEnter();
            return;
        }
        System.out.println("Select Role:");
        System.out.println("1. Regular User");
        System.out.println("2. Resource Manager");

        int roleChoice = getValidIntInput("Choose role: ");
        scanner.nextLine();

        String role = "REGULAR_USER";
        if (roleChoice == 2) {
            role = "RESOURCE_MANAGER";
        }

        boolean success = userService.register(email, password, role);
        if (success) {
            System.out.println("Registration successful! You can now login.");
        } else {
            System.out.println("email already exists. Please try again.");
        }
        waitForEnter();
    }

    private void handleUserSession() {
        while (currentUser != null) {
            System.out.println("\nhey: " + currentUser.getEmail());
            System.out.println();

            currentUser.displayMenu();
            int choice = getValidIntInput("Choose an option: ");
            scanner.nextLine();

            switch (currentUser.getRole()) {
                case "ADMIN":
                    handleAdminMenu(choice);
                    break;
                case "RESOURCE_MANAGER":
                    handleResourceManagerMenu(choice);
                    break;
                case "REGULAR_USER":
                    handleUserMenu(choice);
                    break;
            }

            if (currentUser != null) {
                waitForEnter();
            }
        }
    }

    private void handleAdminMenu(int choice) {
        switch (choice) {
            case 1:
                System.out.println("All Bookings");
                List<Booking> allBookings = bookingService.getAllBookings();
                if (allBookings.isEmpty()) {
                    System.out.println("No bookings found.");
                } else {
                    for (Booking booking : allBookings) {
                        System.out.println(booking);
                    }
                }
                break;
            case 2:
                System.out.println("Reports");
                reportService.generateReport(bookingService.getAllBookings());
                break;
            case 3:
                
                System.out.println("Logging out");
                currentUser = null;
                return;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void handleResourceManagerMenu(int choice) {
        switch (choice) {
            case 1:
                System.out.println("Add New Resource ");
                System.out.println("Enter Resource Name:");
                String name = scanner.nextLine();
                if(name.isEmpty()) {
                    System.out.println("Resource name cannot be empty.");
                    return;
                }

                System.out.println("Choose Resource Type:");
                System.out.println("1. Book");
                System.out.println("2. Equipments");
                System.out.println("3. Room");
                int typeChoice = getValidIntInput("Select type ");
                scanner.nextLine();

                String type;
                switch (typeChoice) {
                    case 1:
                        type = "Book";
                        break;
                    case 2:
                        type = "Equipment";
                        break;
                    case 3:
                        type = "Room";
                        break;
                    default:
                        type = "Other";
                }

                double costPerHour = getValidDoubleInput("Enter Cost per Hour: ");
                if(costPerHour<0 ){
                    System.out.println("cost can not be negative");
                    return;
                }
                int quantity = getValidIntInput("Enter Quantity: ");
                if(quantity<=0 ){
                    System.out.println("quantity can not be negative or zero");
                    return;
                }
                scanner.nextLine();

                boolean isNewResource = resourceService.addResource(name, type, costPerHour, quantity);
                if (isNewResource) {
                    System.out.println("New resource added successfully!");
                } else {
                    System.out.println("Resource already exists. Quantity has been updated!");
                }
                break;

            case 2:
                System.out.println("Available Resources");
                displayResourceList(resourceService.getAllResources());
                break;

            case 3:
                System.out.println("Logging out");
                currentUser = null;
                return;

            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void handleUserMenu(int choice) {
        switch (choice) {
            case 1:
                System.out.println(" Available Resources");
                viewResourceList(resourceService.getAllResources());
                break;

            case 2:
                System.out.println("Book a Resource");

            
                System.out.println("\nEnter dates in format:yyyy-MM-dd HH:mm");
                System.out.println("Enter start time:");
                String startDate = scanner.nextLine();
                System.out.println("Enter end time:");
                String endDate = scanner.nextLine();

                try {
                    LocalDateTime start = LocalDateTime.parse(startDate, formatter);
                    LocalDateTime end = LocalDateTime.parse(endDate, formatter);

                    if (end.isBefore(start) || end.isEqual(start)) {
                        System.out.println("End time must be after start time.");
                        return;
                    }

                    DateTimeRange range = new DateTimeRange(start, end);

                 
                    List<Resource> availableResources = bookingService.getAvailableResources(
                            resourceService.getAllResources(), range);

                    System.out.println("Available Resources for Selected Time");
                    if (availableResources.isEmpty()) {
                        System.out.println("No resources available for the selected time period.");
                        return;
                    }

                    displayResourceList(availableResources);

                    int resourceId = getValidIntInput("Enter Resource ID to book: ");
                    scanner.nextLine();

                    if (resourceId == -1) {
                        System.out.println("Booking canceled.");
                        return;
                    }

                    Resource resource = resourceService.getById(resourceId);
                    if (resource != null) {
                        System.out.println(
                                "Selected: " + resource.getName()+" " +  resource.getCostPerHour() + "/hr");

                        List<Resource> available = bookingService.getAvailableResources(List.of(resource), range);
                        if (available.isEmpty()) {
                            System.out.println("This resource is no longer available for the selected time.");
                            return;
                        }

                        int maxAvailable = available.get(0).getAvailableQuantity();
                        System.out.println("Available quantity: " + maxAvailable); 

                        int bookQuantity = getValidIntInput("Enter quantity to book (Available " + maxAvailable + "): ");
                        scanner.nextLine();

                        if (bookQuantity <= 0 || bookQuantity > maxAvailable) {
                            System.out.println("Invalid quantity. Booking canceled.");
                            return;
                        }

                        if (bookingService.book(currentUser, resource, range, bookQuantity)) {
                            double cost = Calculator.calculateCost(resource, range, bookQuantity);
                            System.out.printf("Booking Done Total cost: %.2f\n", cost); 
                        } else {
                            System.out.println("Booking failed. The resource might be finished");
                        }
                    } else {
                        System.out.println("Resource not found.");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid date format or input. Please try again. Error: " + e.getMessage());
                }
                break;

            case 3:
                System.out.println("My Bookings");
                List<Booking> userBookings = bookingService.getUserBookings(currentUser);
                if (userBookings.isEmpty()) {
                    System.out.println("You have no bookings.");
                } else {
                    for (Booking booking : userBookings) {
                        System.out.println(booking);
                    }
                }
                break;

            case 4:
                System.out.println("Logging out");
                currentUser = null;
                return;

            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void displayResourceList(List<Resource> resources) {
        if (resources.isEmpty()) {
            System.out.println("No resources available.");
        } else {
            System.out.println("ID | Name | Type | Cost/Hour | Available/Total");
            System.out.println("-------------------------------------------------");
            for (Resource r : resources) {
                System.out.printf("%d | %s | %s | %.2f | %d/%d\n",
                        r.getId(), r.getName(), r.getType(), r.getCostPerHour(),
                        r.getAvailableQuantity(), r.getQuantity());
            }
            System.out.println();
        }
    } 

    private void viewResourceList(List<Resource> resources) {
        if (resources.isEmpty()) {
            System.out.println("No resources available.");
        } else {
            System.out.println("ID | Name | Type | Cost/Hour | Available/Total");
            System.out.println("------------------------------------------------");
            for (Resource r : resources) {
                System.out.printf("%d | %s | %s | %.2f | %d/%d\n",
                        r.getId(), r.getName(), r.getType(), r.getCostPerHour(),
                        r.getAvailableQuantity(), r.getQuantity());
            }
            System.out.println();
        }
    }
}
