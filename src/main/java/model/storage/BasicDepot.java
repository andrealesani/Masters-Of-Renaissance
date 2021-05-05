package model.storage;

import exceptions.*;
import model.ResourceType;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a depot of the basic type, which are present in the warehouse at the start of the game.
 * This kind of depot can only contain one type of resource, it can only contain a finite amount of that resource, and no two depots of this kind can contain the same resource at the same time.
 */
public class BasicDepot implements ResourceDepot {
    /**
     * Attribute used to memorize the warehouse that contains this depot, in order to make checks on other depots
     */
    private final Warehouse warehouse;
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

    //CONSTRUCTORS

    /**
     * The class constructor
     *
     * @param warehouse the warehouse that holds the depot
     * @param size      the maximum number of resources the depot can contain
     */
    public BasicDepot(Warehouse warehouse, int size) {
        if (warehouse == null || size <= 0) {
            throw new ParametersNotValidException();
        }
        this.warehouse = warehouse;
        this.size = size;
    }

    //PUBLIC METHODS

    /**
     * Adds the given resource to the storage
     *
     * @param resource the resource to be added
     * @param quantity the amount of resource to add to the amount stored
     * @throws WrongResourceInsertionException if the type of the resource to be added cannot (currently) be added to the storage
     * @throws NotEnoughSpaceException         if the quantity of the resource to be added plus the amount already stored exceeds the maximum capacity
     * @throws BlockedResourceException        if the resource is being blocked by a different depot
     */
    @Override
    public void addResource(ResourceType resource, int quantity) throws WrongResourceInsertionException, NotEnoughSpaceException, BlockedResourceException {
        if (quantity < 0) {
            throw new ParametersNotValidException();
        }
        if (resource != null && quantity > 0) {
            int newQuantity = amount + quantity;
            if (newQuantity > size) {
                throw new NotEnoughSpaceException();
            }

            if (amount == 0) {
                List<ResourceDepot> exclusions = new ArrayList<>();
                exclusions.add(this);
                if (warehouse.isResourceBlocked(resource, exclusions)) {
                    throw new BlockedResourceException(resource);
                }
                storedResource = resource;
            } else {
                if (resource != storedResource) {
                    throw new WrongResourceInsertionException();
                }
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
        if (depot == null) {
            return false;
        }
        List<ResourceType> depotResourcesList = depot.getStoredResources();
        if (depotResourcesList.isEmpty())
            return true;

        ResourceType depotResource = depotResourcesList.get(0);
        List<ResourceDepot> exclusions = new ArrayList<>();
        exclusions.add(this);
        exclusions.add(depot);
        if (warehouse.isResourceBlocked(depotResource, exclusions)) {
            return false;
        }

        int depotQuantity = depot.getNumOfResource(depotResource);
        return depotQuantity <= size;
    }

    /**
     * Returns whether or not the depot is blocking a certain resource, meaning that no other depot of the basic type can contain it
     *
     * @param resource the resource that might be blocked
     * @return true if the given resource is blocked by this depot
     */
    @Override
    public boolean isBlocking(ResourceType resource) {
        if (amount == 0 || resource == null) {
            return false;
        }
        return resource == storedResource;
    }

    /**
     * Remove a certain amount of the given resource from storage
     *
     * @param resource the resource to be decreased in quantity
     * @param quantity the amount of resource to remove from the amount stored
     * @throws NotEnoughResourceException  if the given resource is present in storage in fewer quantity than the amount to be deleted
     */
    @Override
    public void removeResource(ResourceType resource, int quantity) throws NotEnoughResourceException {
        if (quantity < 0) {
            throw new ParametersNotValidException();
        }
        if (resource != null && quantity > 0) {
            if (amount == 0 || resource != storedResource) {
                throw new NotEnoughResourceException();
            }
            int newQuantity = amount - quantity;
            if (newQuantity < 0) {
                throw new NotEnoughResourceException();
            } else {
                if (newQuantity == 0) {
                    storedResource = null;
                }
                amount = newQuantity;
            }
        }
    }

    /**
     * Empties the depot of its entire content
     */
    @Override
    public void clear() {
        amount = 0;
        storedResource = null;
    }

    // GETTERS

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
        if (amount != 0 && resource == storedResource) {
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
        if (amount > 0) resourceList.add(storedResource);
        return resourceList;
    }
}
