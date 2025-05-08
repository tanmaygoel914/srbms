package services;

import entity.Resource;
import repository.ResourceRepository;

import java.util.List;

public class ResourceService {
    private ResourceRepository repo;

    public ResourceService(ResourceRepository repo) {
        this.repo = repo;
    }

    public boolean addResource(String name, String type, double costPerHour, int quantity) {
     
        Resource existingResource = repo.findSimilarResource(name, type);
        
        if (existingResource != null) {
           
            existingResource.incrementQuantity(quantity);
            return false; 
        } else {
         
            Resource resource = new Resource(name, type, costPerHour, quantity);
            repo.addResource(resource);
            return true;
        }
    }

    public List<Resource> getAllResources() {
        return repo.getAllResources();
    }

    public Resource getById(int id) {
        return repo.getResourceById(id);
    }
}