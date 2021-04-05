package model.storage;

import Exceptions.NotEnoughResourceException;
import Exceptions.ResourceNotPresentException;
import model.ResourceType;
import model.resource.Resource;

import java.lang.invoke.WrongMethodTypeException;
import java.util.List;

/**
 * This class represents a depot of the kind obtained after activating certain leader cards, providing the player with additional warehouse space.
 * This kind of depot can usually contain only two pieces of a single type of resource, and is not counted when determining resource blocking for basic depots.
 */
public class LeaderDepot implements ResourceDepot {
    /**
     * Attribute used to memorize the maximum amount of resource that can be stored
     */
    private final int size;
    /**
     * Attribute used to memorize the only type of resource that can be stored in this depot
     */
    private final ResourceType acceptedResource;
    /**
     * Attribute used to memorize the amount of resource stored
     */
    private int amount = 0;

    /**
     * The class constructor
     * @param size - the maximum number of resources the depot can contain
     */
    public LeaderDepot(int size, ResourceType resource) {
        this.size = size;
        this.acceptedResource = resource;
    }
    
    /**
     * Adds the given resource to storageContent
     * @param resource - the resource to be added
     * @param quantity - the amount of resource to add to the amount stored
     */
    public void addResource (ResourceType resource, int quantity) throws WrongMethodTypeException, NotEnoughResourceException {
        if (resource!=acceptedResource) {
            throw new WrongMethodTypeException();
        }
        int newQuantity = quantity + amount;
        if (newQuantity>size) {
            throw new NotEnoughResourceException();
        }
        quantity = newQuantity;
    }

    /**
     * Returns whether or not the depot, if it were empty, could contain the given amount of the given resource
     * @param resource - the resource to be stored
     * @param quantity - the amount to be stored of the given resource
     * @return true if the given resource and amount could be contained in the depot
     */
    @Override
    public boolean canHold (ResourceType resource, int quantity){
        //TODO
        return false;
    }

    /**
     * Returns false at all times, as this type of depot can never block a resource
     * @param resource - the resource that might be blocked
     * @return false
     */
    @Override
    public boolean isBlocking (ResourceType resource){
        //TODO
        return false;
    }

    /**
     * Returns the maximum number of resources that can be stored in the depot
     * @return the size of the depot
     */
    @Override
    public int getSize () {
        //TODO
        return 0;
    }

    /**
     * Empties the depot of its entire content
     */
    @Override
    public void empty(){
        //TODO
    }

    /**
     Remove a certain amount of the given resource from storage
     * @param resource - the resource to be decreased in quantity
     * @param quantity - the amount of resource to remove from the amount stored
     * @throws ResourceNotPresentException - if the given resource is not present in storage
     * @throws NotEnoughResourceException - if the given resource is present in storage in fewer quantity than the amount to be deleted
     */
    @Override
    public void removeResource (ResourceType resource, int quantity) throws ResourceNotPresentException, NotEnoughResourceException {
        //TODO
    }

    /**
     * Returns the stored amount of the given resource
     * @param resource - the resource the amount of which is asked
     * @return The amount of the given resource contained in storage
     */
    @Override
    public int getNumOfResource (ResourceType resource) {
        //TODO
        return 0;
    }

    /**
     * Returns the resources stored in storage
     * @return A List of the stored resource types (if there are no resources in storage, the list is empty)
     */
    @Override
    public List<ResourceType> getStoredResources () {
        //TODO
        return null;
    }
}
