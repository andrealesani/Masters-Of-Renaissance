package model.storage;

import Exceptions.*;
import model.ResourceType;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a depot of the type obtained after activating certain leader cards, providing the player with additional warehouse space.
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

    //CONSTRUCTORS

    /**
     * The class constructor
     *
     * @param size     the maximum number of resources the depot can contain
     * @param resource the only type of resource the depot can contain
     */
    public LeaderDepot(int size, ResourceType resource) {
        this.size = size;
        this.acceptedResource = resource;
    }

    //PUBLIC METHODS

    /**
     * Adds the given resource to storageContent
     *
     * @param resource the resource to be added
     * @param quantity the amount of resource to add to the amount stored
     * @throws WrongResourceInsertionException if the resource is not the kind accepted by this depot
     * @throws NotEnoughSpaceException    if the quantity of the resource to be added plus the amount already stored exceeds the maximum capacity
     * @throws BlockedResourceException   under no circumstance, because this type of depot is not affected by resource blocking
     */
    public void addResource(ResourceType resource, int quantity) throws WrongResourceInsertionException, NotEnoughSpaceException, BlockedResourceException {
        if (resource!=null && quantity>0) {
            if (resource != acceptedResource) {
                throw new WrongResourceInsertionException();
            }
            int newQuantity = quantity + amount;
            if (newQuantity > size) {
                throw new NotEnoughSpaceException();
            }
            amount = newQuantity;
        }
    }

    /**
     * Returns whether or not the depot, if it were empty, could hold the contents of the given depot
     *
     * @param depot the depot the contents of which need to be stored
     * @return true if the given resource and amount could be contained in the depot
     */
    @Override
    public boolean canHoldContentOf(ResourceDepot depot) {
        if (depot==null) {
            return false;
        }
        List<ResourceType> depotResourcesList = depot.getStoredResources();
        if (depotResourcesList.isEmpty())
            return true;

        ResourceType depotResource = depotResourcesList.get(0);
        int depotQuantity = depot.getNumOfResource(depotResource);
        return depotResource == acceptedResource && depotQuantity <= size;
    }

    /**
     * Returns false at all times, as this type of depot can never block a resource
     *
     * @param resource the resource that might be blocked
     * @return false
     */
    @Override
    public boolean isBlocking(ResourceType resource) {
        return false;
    }

    /**
     * Empties the depot of its entire content
     */
    @Override
    public void clear() {
        amount = 0;
    }

    /**
     * Remove a certain amount of the given resource from storage
     *
     * @param resource the resource to be decreased in quantity
     * @param quantity the amount of resource to remove from the amount stored
     */
    @Override
    public void removeResource(ResourceType resource, int quantity) throws NotEnoughResourceException {
        if (resource!=null && quantity>0) {
            if (resource != acceptedResource) {
                throw new NotEnoughResourceException();
            }
            int newQuantity = amount - quantity;
            if (newQuantity < 0) {
                throw new NotEnoughResourceException();
            }
            amount = newQuantity;
        }
    }

    //GETTERS

    /**
     * Returns the maximum number of resources that can be stored in the depot
     *
     * @return the size of the depot
     */
    @Override
    public int getSize() {
        return size;
    }

    /**
     * Returns the stored amount of the given resource
     *
     * @param resource the resource the amount of which is asked
     * @return the amount of the given resource contained in storage
     */
    @Override
    public int getNumOfResource(ResourceType resource) {
        if (resource == acceptedResource) {
            return amount;
        }
        return 0;
    }

    /**
     * Returns the resources stored in storage
     *
     * @return a List of the stored resource types (if there are no resources in storage, the list is empty)
     */
    @Override
    public List<ResourceType> getStoredResources() {
        List<ResourceType> resourceList = new ArrayList<>();
        if (amount > 0) resourceList.add(acceptedResource);
        return resourceList;
    }
}
