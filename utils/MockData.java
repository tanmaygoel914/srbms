package utils;

import entity.*;
import repository.ResourceRepository;
import repository.UserRepository;

public class MockData {
    public static void seedData(UserRepository userRepo, ResourceRepository resourceRepo) {
        
        userRepo.addUser(new Admin("admin@123", "admin1pass"));
        userRepo.addUser(new ResourceManager("manager@123", "manager1pass"));
        userRepo.addUser(new RegularUser("user1@123", "user1pass"));


        resourceRepo.addResource(new Resource("rich dad poor dad", "Book", 50.0,2));
        resourceRepo.addResource(new Resource("phone", "Equipment", 30.0,2));
        resourceRepo.addResource(new Resource("101 room", "Room", 40.0,3));
    }
}
