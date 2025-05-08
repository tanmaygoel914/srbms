package services;

import entity.*;
import repository.UserRepository;

public class UserService {
    private UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public boolean register(String email, String password, String role) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return false; 
        }

        if (userRepo.exists(email)) return false;

        User user;
        switch (role.toUpperCase()) {
            case "ADMIN":
                user = new Admin(email, password);
                break;
            case "RESOURCE_MANAGER":
                user = new ResourceManager(email, password);
                break;
            case "REGULAR_USER":
                user = new RegularUser(email, password);
                break;
            default:
                return false;
        }

        userRepo.addUser(user);
        return true;
    }


    public User login(String email, String password) {
        User user = userRepo.getUser(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
