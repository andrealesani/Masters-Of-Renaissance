package model.storage;

import model.resource.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the player board's warehouse, intended as the collection of all of its depots (both the basic, starting ones and the ones obtained through other means).
 */
public class Warehouse {
    /**
     * List used to store and index the various depots available to the player
     */
    private final List<ResourceDepot> depots = new ArrayList<>();

    /**
     * Adds the given depot to the list of available depots
     * @param depot - the depot to be added
     */
    public void addNewDepot (ResourceDepot depot){
        //TODO
    }

    /**
     * Adds the given quantity of the given resource to the given depot
     * @param depotNumber - the index of the depot to which to add the resource
     * @param resource - the resource to be added
     * @param quantity - the quantity of the resource to add
     */
    public void addToDepot (int depotNumber, Resource resource, int quantity){
        //TODO
    }

    /**
     * Removes the given quantity of the given resource to the given depot
     * @param depotNumber - the index of the depot from which to remove the resource
     * @param resource - the resource to be removed
     * @param quantity - the quantity of the resource to remove
     */
    public void removeFromDepot (int depotNumber, Resource resource, int quantity) {
        //TODO
    }

    /**
     * Swaps the contents of two warehouse depots
     * @param depot1 - the first depot
     * @param depot2 - the second depot
     */
    public void swapDepotContent (int depot1, int depot2) {
        //TODO
    }

    /**
     * Returns the total stored amount of the given resource among all warehouse depots
     * @param resource - the resource the amount of which is asked
     * @return The sum of the amounts of the given resource contained within all depots
     */
    public int getNumOfResource (Resource resource) {
        //TODO
        return 0;
    }

    /**
     * Returns the resources present in the warehouse
     * @return A List of the resources stored in the warehouse
     */
    public List<Resource> getStoredResources () {
        //TODO
        return null;
    }
}
