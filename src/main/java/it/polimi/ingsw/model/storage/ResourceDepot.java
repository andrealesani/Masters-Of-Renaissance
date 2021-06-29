package it.polimi.ingsw.model.storage;

import it.polimi.ingsw.Exceptions.BlockedResourceException;
import it.polimi.ingsw.Exceptions.NotEnoughSpaceException;
import it.polimi.ingsw.Exceptions.WrongResourceInsertionException;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * This interface indicates that the implementing class can be used as a depot in the player board warehouse.
 * It has methods for evaluating the depot's capacity or whether it is blocking a specific resource, and to empty the depot, as well as the standard methods for a ResourceStash.
 */
public interface ResourceDepot extends ResourceStash {

    //PUBLIC METHODS

    /**
     * Adds the given resource to the storage
     *
     * @param resource the resource to be added
     * @param quantity the amount of resource to add to the amount stored
     * @throws WrongResourceInsertionException if the type of the resource to be added cannot (currently) be added to the storage
     * @throws NotEnoughSpaceException    if the quantity of the resource to be added plus the amount already stored exceeds the maximum capacity
     * @throws BlockedResourceException   if the depot is affected by resource blocking and the resource is being blocked by a different depot
     */
    void addResource(ResourceType resource, int quantity) throws WrongResourceInsertionException, NotEnoughSpaceException, BlockedResourceException;

    /**
     * Returns whether or not the depot is blocking a certain resource, meaning that no other depot of the basic type can contain it
     *
     * @param resource the resource that might be blocked
     * @return true if the given resource is blocked by this depot
     */
    boolean isBlocking(ResourceType resource);

    //GETTERS

    /**
     * Returns the maximum number of resources that can be stored in the depot
     *
     * @return the size of the depot
     */
    int getSize();

    /**
     * Returns the id of the card that activated this depot
     *
     * @return the leader card id, if the depot is not a leader depot, returns 0
     */
    int getCardId();

    /**
     * Returns the type of resource this depot can currently accept
     *
     * @return the currently accepted ResourceType, 'null' if any type is accepted
     */
    ResourceType getAcceptedResource();
}
