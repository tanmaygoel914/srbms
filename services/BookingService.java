package services;

import entity.*;
import repository.BookingRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingService {
    private BookingRepository repo;

    public BookingService(BookingRepository repo) {
        this.repo = repo;
    } 

    public List<Resource> getAvailableResources(List<Resource> allResources, DateTimeRange range) {
        List<Resource> availableResources = new ArrayList<>();
       
        Map<Integer, Integer> bookedQuantities = new HashMap<>();
        
      
        for (Booking booking : repo.getAllBookings()) {
            if (booking.getTimeRange().overlaps(range)) {
                int resourceId = booking.getResource().getId();
                bookedQuantities.put(resourceId, 
                    bookedQuantities.getOrDefault(resourceId, 0) + booking.getQuantity());
            }
        }
        
       
        for (Resource resource : allResources) {
           
            int bookedQty = bookedQuantities.getOrDefault(resource.getId(), 0);
            int availableQty = resource.getQuantity() - bookedQty;
            
            if (availableQty > 0) {
            
                resource.setAvailableQuantity(availableQty);
                availableResources.add(resource);
            }
        }
        
        return availableResources;
    }

    public boolean book(User user, Resource resource, DateTimeRange range, int quantity) {
        
        List<Resource> availableResources = getAvailableResources(List.of(resource), range);
        
        if (availableResources.isEmpty() || availableResources.get(0).getAvailableQuantity() < quantity) {
            return false;
        }
        
        double cost = Calculator.calculateCost(resource, range, quantity);
        Booking booking = new Booking(user, resource, range, cost, quantity);
        repo.addBooking(booking);
        return true;
    }

    public List<Booking> getAllBookings() {
        return repo.getAllBookings();
    }
    
    public List<Booking> getUserBookings(User user) {
        List<Booking> userBookings = new ArrayList<>();
        for (Booking booking : repo.getAllBookings()) {
            if (booking.getUser().getEmail().equals(user.getEmail())) {
                userBookings.add(booking);
            }
        }
        return userBookings;
    }
}