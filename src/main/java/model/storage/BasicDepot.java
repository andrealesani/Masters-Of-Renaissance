package model.storage;

import Exceptions.NotEnoughResourceException;
import Exceptions.ResourceNotPresentException;
import model.ResourceType;
import model.resource.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a depot of the basic kind, which are present in the warehouse at the start of the game.
 * This kind of depot can only contain one type of resource, it can only contain a finite amount of that resource, and no two depots of this kind can contain the same resource at the same time.
 */
public class BasicDepot implements ResourceDepot {
    /**
     * Attribute used to memorize the maximum amount of resource that can be stored
     */
    private final int size;
    /**
     * Attribute used to memorize the type of resource stored
     */
    private ResourceType storedResource;
    /**
     * Attribute used to memorize the amount of resource stored
     */
    private int amount = 0;

    /**
     * The class constructor
     * @param size - the maximum number of resources the depot can contain
     */
    public BasicDepot(int size) {
        this.size = size;
    }

    /**
     * Adds the given resource to storageContent
     * @param resource - the resource to be added
     * @param quantity - the amount of resource to add to the amount stored
     */
    public void addResource (Resource resource, int quantity){
        //TODO
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
     * Returns whether or not the depot is blocking a certain resource, meaning that no other depot of the basic type can contain it
     * @param resource - the resource that might be blocked
     * @return true if the given resource is blocked by this depot
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
