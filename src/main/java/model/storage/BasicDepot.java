package model.storage;

import Exceptions.*;
import model.ResourceType;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a depot of the basic kind, which are present in the warehouse at the start of the game.
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

    /**
     * The class constructor
     * @param size - the maximum number of resources the depot can contain
     */
    public BasicDepot(Warehouse warehouse, int size) {
        this.warehouse = warehouse;
        this.size = size;
    }

    /**
     * Adds the given resource to the storage
     * @param resource - the resource to be added
     * @param quantity - the amount of resource to add to the amount stored
     * @throws WrongResourceTypeException - if the type of the resource to be added cannot (currently) be added to the storage
     * @throws NotEnoughSpaceException - if the quantity of the resource to be added plus the amount already stored exceeds the maximum capacity
     * @throws BlockedResourceException - if the resource is being blocked by a different depot
     */
    @Override
    public void addResource(ResourceType resource, int quantity) throws WrongResourceTypeException, NotEnoughSpaceException, BlockedResourceException {
        if (amount==0) {
            amount = quantity;
            storedResource = resource;
        } else {
            if (resource!=storedResource) {
                throw new WrongResourceTypeException();
            }
            int newQuantity = amount + quantity;
            if (newQuantity>size) {
                throw new NotEnoughSpaceException();
            }
            amount=newQuantity;
        }

    }

    /**
     * Returns whether or not the depot, if it were empty, could contain the given amount of the given resource
     * @param resource - the resource to be stored
     * @param quantity - the amount to be stored of the given resource
     * @return true if the given resource and amount could be contained in the depot
     */
    @Override
    public boolean canHold (ResourceType resource, int quantity){
        return quantity<=size;
    }

    /**
     * Returns whether or not the depot is blocking a certain resource, meaning that no other depot of the basic type can contain it
     * @param resource - the resource that might be blocked
     * @return true if the given resource is blocked by this depot
     */
    @Override
    public boolean isBlocking (ResourceType resource){
        if (amount==0) {
            return false;
        }
        return resource==storedResource;
    }

    /**
     * Returns the maximum number of resources that can be stored in the depot
     * @return the size of the depot
     */
    @Override
    public int getSize () {
        return size;
    }

    /**
     * Empties the depot of its entire content
     */
    @Override
    public void empty(){
        amount=0;
        storedResource=null;
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
        if (amount==0 || resource!=storedResource) {
            throw new ResourceNotPresentException();
        }
        int newQuantity = amount - quantity;
        if (newQuantity<0) {
            throw new NotEnoughResourceException();
        } else {
            if (newQuantity==0) {
                storedResource = null;
            }
            amount = newQuantity;
        }
    }

    /**
     * Returns the stored amount of the given resource
     * @param resource - the resource the amount of which is asked
     * @return The amount of the given resource contained in storage
     */
    @Override
    public int getNumOfResource (ResourceType resource) {
        if (amount!=0 && resource==storedResource) {
            return amount;
        }
        return 0;
    }

    /**
     * Returns the resources stored in storage
     * @return A List of the stored resource types (if there are no resources in storage, the list is empty)
     */
    @Override
    public List<ResourceType> getStoredResources () {
        List<ResourceType> resourceList = new ArrayList<>();
        if (amount>0) resourceList.add(storedResource);
        return resourceList;
    }
}
