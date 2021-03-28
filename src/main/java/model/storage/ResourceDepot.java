package model.storage;

import model.resource.Resource;

/**
 * This interface indicates that the implementing class can be used as a depot in the player board warehouse.
 */
public interface ResourceDepot {
    /**
     * Returns whether or not the depot, if it were empty, could contain the given amount of the given resource
     * @param resource - the resource to be stored
     * @param quantity - the amount to be stored of the given resource
     * @return true if the given resource and amount could be contained in the depot
     */
    boolean canHold (Resource resource, int quantity);

    /**
     * Returns whether or not the depot is blocking a certain resource, meaning that no other depot of the basic type can contain it
     * @param resource - the resource that might be blocked
     * @return true if the given resource is blocked by this depot
     */
    boolean isBlocking (Resource resource);

    /**
     * Empties the depot of its entire content
     */
    void empty();
}
