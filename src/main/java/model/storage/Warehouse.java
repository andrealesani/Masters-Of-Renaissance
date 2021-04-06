package model.storage;

import Exceptions.*;
import model.ResourceType;
import model.card.leadercard.DepotDecorator;
import model.resource.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents the player board's warehouse, intended as the collection of all of its depots (both the basic, starting ones and the ones obtained through other means).
 */
public class Warehouse {
    /**
     * List used to store and index the various depots available to the player
     */
    private final List<ResourceDepot> depots = new ArrayList<>();

    /**
     * The class constructor
     * * @param numOfDepots - the number of basic depots in the warehouse
     */
    public Warehouse(int numOfDepots) {
        for (int i = 1; i<=numOfDepots; i++) {
            depots.add(new BasicDepot(this, i));
        }
    }

    /**
     * Adds the given depot to the list of available depots
     * @param depot - the depot to be added
     */
    public void addNewDepot(ResourceDepot depot){
        depots.add(depot);
    }

    /**
     * Returns the number of depots currently accessible in the warehouse
     * @return the number of depots
     */
    public int getNumOfDepots() {
        return depots.size();
    }

    /**
     * Adds the given quantity of the given resource to the given depot
     * @param depotNumber - the number of the depot to which to add the resource
     * @param resource - the resource to be added
     * @param quantity - the quantity of the resource to add
     * @throws DepotNotPresentException - if the number of the target depot does not correspond to any depot in the warehouse
     * @throws WrongResourceTypeException - if the type of the resource to be added cannot (currently) be added to the target depot
     * @throws NotEnoughSpaceException - if the quantity of the resource to be added plus the amount already stored in the target depot exceeds the depot's maximum capacity
     * @throws BlockedResourceException - if the depot is affected by resource blocking and the resource is being blocked by a different depot
     */
    public void addToDepot (int depotNumber, ResourceType resource, int quantity) throws DepotNotPresentException, NotEnoughSpaceException, WrongResourceTypeException, BlockedResourceException {
        if (depotNumber<1 || depotNumber>depots.size()) {
            throw new DepotNotPresentException();
        }
        depots.get(depotNumber-1).addResource(resource, quantity);
    }

    /**
     * Removes the given quantity of the given resource to the given depot
     * @param depotNumber - the index of the depot from which to remove the resource
     * @param resource - the resource to be removed
     * @param quantity - the quantity of the resource to remove
     * @throws DepotNotPresentException - if the number of the target depot does not correspond to any depot in the warehouse
     * @throws ResourceNotPresentException - if the given resource is not present in the target depot
     * @throws NotEnoughResourceException - if the given resource is present in the target depot in fewer quantity than the amount to be deleted
     */
    public void removeFromDepot (int depotNumber, ResourceType resource, int quantity) throws DepotNotPresentException, ResourceNotPresentException, NotEnoughResourceException {
        if (depotNumber<1 || depotNumber>depots.size()) {
            throw new DepotNotPresentException();
        }
        depots.get(depotNumber-1).removeResource(resource, quantity);
    }

    /**
     * Swaps the contents of two warehouse depots
     * @param depotNumber1 - the number of the first depot
     * @param depotNumber2 - the number of the second depot
     */
    public void swapDepotContent (int depotNumber1, int depotNumber2) throws DepotNotPresentException, SameDepotException {
        if (depotNumber1==depotNumber2) {
            throw new SameDepotException();
        }
        if (depotNumber1<1 || depotNumber1>depots.size() || depotNumber2<1 || depotNumber2>depots.size()) {
            throw new DepotNotPresentException();
        }
        ResourceDepot depot1 = depots.get(depotNumber1);
        ResourceDepot depot2 = depots.get(depotNumber2);
        ResourceType resource1 = depot1.getStoredResources().get(0);
        ResourceType resource2 = depot2.getStoredResources().get(0);
        int amount1 = depot1.getNumOfResource(resource1);
        int amount2 = depot2.getNumOfResource(resource2);

        for (int i=0; i<depots.size(); i++) {
            if (i!=depotNumber1-1 && i!=depotNumber2-1) {
                ResourceDepot depot = depots.get(i);
                if (depot.isBlocking(resource1) || depot.isBlocking(resource2)) {

                }
            }
        }
    }

    /**
     * Returns whether or not the given resource is blocked by one of the depots in the warehouse, excluding those provided in the exclusions list
     * @param resource - the resource that might be blocked
     * @param exclusions - the depots exempted from the search
     * @return - true if one of the depots is blocking the resource
     */
    public boolean isResourceBlocked (ResourceType resource, List<ResourceDepot> exclusions) {
        for (ResourceDepot depot : depots) {
            if (!exclusions.contains(depot) && depot.isBlocking(resource)){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the total stored amount of the given resource among all warehouse depots
     * @param resource - the resource the amount of which is asked
     * @return The sum of the amounts of the given resource contained within all depots
     */
    public int getNumOfResource (ResourceType resource) {
        int numOfResource = 0;
        for (ResourceDepot depot : depots) {
            numOfResource += depot.getNumOfResource(resource);
        }
        return numOfResource;
    }

    /**
     * Returns the resources present in the warehouse
     * @return A List of the resources stored in the warehouse
     */
    public List<ResourceType> getStoredResources () {
        List<ResourceType> resourceList = new ArrayList<>();
        for (ResourceDepot depot : depots) {
            resourceList.addAll(depot.getStoredResources());
        }
        return resourceList.stream().distinct().collect(Collectors.toList());
    }
}
