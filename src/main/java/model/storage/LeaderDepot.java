package model.storage;

import model.resource.Resource;

/**
 * This class represents a depot of the kind obtained after activating certain leader cards, providing the player with additional warehouse space.
 * This kind of depot can usually contain only two pieces of a single type of resource, and is not counted when determining resource blocking for basic depots.
 */
public class LeaderDepot extends ResourceStash implements ResourceDepot {
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
    public boolean canHold (Resource resource, int quantity){
        //TODO
        return false;
    }

    /**
     * Returns false at all times, as this type of depot can never block a resource
     * @param resource - the resource that might be blocked
     * @return false
     */
    public boolean isBlocking (Resource resource){
        //TODO
        return false;
    }

    /**
     * Empties the depot of its entire content
     */
    public void empty(){
        //TODO
    }
}
