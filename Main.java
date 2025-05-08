import entity.*;
import repository.*;
import services.*;
import controller.MenuController;
import utils.MockData; 
public class Main {
    public static void main(String[] args) {
        UserRepository userRepo = new UserRepository();
        ResourceRepository resourceRepo = new ResourceRepository();
        BookingRepository bookingRepo = new BookingRepository();

       
        MockData.seedData(userRepo, resourceRepo);

        UserService userService = new UserService(userRepo);
        ResourceService resourceService = new ResourceService(resourceRepo);
        BookingService bookingService = new BookingService(bookingRepo);
        ReportService reportService = new ReportService();

        MenuController menuController = new MenuController(userService, resourceService, bookingService, reportService);

        menuController.start();
    }
}