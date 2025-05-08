package services;

import entity.DateTimeRange;
import entity.Resource;
public class Calculator {
    public static double calculateCost(Resource resource, DateTimeRange timeRange, int quantity) {
        long minutes = java.time.Duration.between(timeRange.getStart(), timeRange.getEnd()).toMinutes();
        long hours = minutes / 60;
        if (minutes % 60 != 0) {
            hours++; 
        }

        return resource.getCostPerHour() * hours * quantity;
    }
}
