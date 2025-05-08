package repository;

import entity.*;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private Map<String, User> users = new HashMap<>();

    public void addUser(User user) {
        users.put(user.getEmail(), user);
    }

    public User getUser(String email) {
        return users.get(email);
    }

    public boolean exists(String email) {
        return users.containsKey(email);
    }

    public Map<String, User> getAllUsers() {
        return users;
    }
}
