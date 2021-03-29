package model.storage;

import model.resource.Resource;

/**
 * This class represents a depot of the basic kind, which are present in the warehouse at the start of the game.
 * This kind of depot can only contain one type of resource, it can only contain a finite amount of that resource, and no two depots of this kind can contain the same resource at the same time.
 */
public class BasicDepot extends ResourceStash implements ResourceDepot{
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
     * Returns whether or not the depot is blocking a certain resource, meaning that no other depot of the basic type can contain it
     * @param resource - the resource that might be blocked
     * @return true if the given resource is blocked by this depot
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
