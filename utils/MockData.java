package utils;

import entity.*;
import repository.ResourceRepository;
import repository.UserRepository;

public class MockData {
    public static void seedData(UserRepository userRepo, ResourceRepository resourceRepo) {
        
        userRepo.addUser(new Admin("admin@123", "12345678a"));
        userRepo.addUser(new ResourceManager("manager@123", "87654321a"));
        userRepo.addUser(new RegularUser("user@123", "user123456"));

        resourceRepo.addResource(new Resource("rich dad poor dad", "Book", 50.0,2));
        resourceRepo.addResource(new Resource("phone", "Equipment", 100.0,2));
        resourceRepo.addResource(new Resource("ps5", "GamingConsole", 120.0,3));
    }  
}
