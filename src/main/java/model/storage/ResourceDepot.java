package model.storage;

import Exceptions.BlockedResourceException;
import Exceptions.NotEnoughSpaceException;
import Exceptions.WrongResourceTypeException;
import model.ResourceType;

/**
 * This interface indicates that the implementing class can be used as a depot in the player board warehouse.
 * It has methods for evaluating the depot's capacity or whether it is blocking a specific resource, and to empty the depot, as well as the standard methods for a ResourceStash.
 */
public interface ResourceDepot extends ResourceStash {
    /**
     * Adds the given resource to the storage
     * @param resource - the resource to be added
     * @param quantity - the amount of resource to add to the amount stored
     * @throws WrongResourceTypeException - if the type of the resource to be added cannot (currently) be added to the storage
     * @throws NotEnoughSpaceException - if the quantity of the resource to be added plus the amount already stored exceeds the maximum capacity
     * @throws BlockedResourceException - if the depot is affected by resource blocking and the resource is being blocked by a different depot
     */
    void addResource (ResourceType resource, int quantity) throws WrongResourceTypeException, NotEnoughSpaceException, BlockedResourceException;

    /**
     * Returns whether or not the depot, if it were empty, could contain the given amount of the given resource
     * @param resource - the resource to be stored
     * @param quantity - the amount to be stored of the given resource
     * @return true if the given resource and amount could be contained in the depot
     */
    boolean canHold (ResourceType resource, int quantity);

    /**
     * Returns whether or not the depot is blocking a certain resource, meaning that no other depot of the basic type can contain it
     * @param resource - the resource that might be blocked
     * @return true if the given resource is blocked by this depot
     */
    boolean isBlocking (ResourceType resource);

    /**
     * Returns the maximum number of resources that can be stored in the depot
     * @return the size of the depot
     */
    int getSize ();

    /**
     * Empties the depot of its entire content
     */
    void empty();
}
