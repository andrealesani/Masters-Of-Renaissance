package it.polimi.ingsw.model.storage;

import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.model.Observable;
import it.polimi.ingsw.model.Observer;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents the player board's warehouse, intended as the collection of all of its depots (both the basic, starting ones and the ones obtained through other means).
 */
public class Warehouse implements Observable {
    /**
     * List used to store and index the various depots available to the player
     */
    private final List<ResourceDepot> depots = new ArrayList<>();
    /**
     * Attribute used to store the number of basic depots
     */
    private final int numOfBasicDepots;

    //CONSTRUCTORS

    /**
     * The class constructor
     *
     * @param numOfBasicDepots the number of basic depots in the warehouse
     */
    public Warehouse(int numOfBasicDepots) {
        if (numOfBasicDepots < 0) {
            throw new ParametersNotValidException();
        }
        this.numOfBasicDepots = numOfBasicDepots;
        for (int i = 1; i <= numOfBasicDepots; i++) {
            depots.add(new BasicDepot(this, i));
        }

        notifyObservers();
    }

    //PUBLIC METHODS

    /**
     * Adds the given depot to the list of available depots
     *
     * @param depot the depot to be added
     */
    public void addNewDepot(ResourceDepot depot) {
        if (depot == null) {
            throw new ParametersNotValidException();
        }
        depots.add(depot);

        notifyObservers();
    }

    /**
     * Adds the given quantity of the given resource to the given depot
     *
     * @param depotNumber the number of the depot to which to add the resource
     * @param resource    the resource to be added
     * @param quantity    the quantity of the resource to add
     * @throws DepotNotPresentException        if the number of the target depot does not correspond to any depot in the warehouse
     * @throws WrongResourceInsertionException if the type of the resource to be added cannot (currently) be added to the target depot
     * @throws NotEnoughSpaceException         if the quantity of the resource to be added plus the amount already stored in the target depot exceeds the depot's maximum capacity
     * @throws BlockedResourceException        if the depot is affected by resource blocking and the resource is being blocked by a different depot
     */
    public void addToDepot(int depotNumber, ResourceType resource, int quantity) throws DepotNotPresentException, NotEnoughSpaceException, WrongResourceInsertionException, BlockedResourceException {
        if (depotNumber < 1) {
            throw new ParametersNotValidException();
        }
        if (depotNumber > depots.size()) {
            throw new DepotNotPresentException(depotNumber);
        }

        depots.get(depotNumber - 1).addResource(resource, quantity);

        notifyObservers();
    }

    /**
     * Removes the given quantity of the given resource to the given depot
     *
     * @param depotNumber the index of the depot from which to remove the resource
     * @param resource    the resource to be removed
     * @param quantity    the quantity of the resource to remove
     * @throws DepotNotPresentException   if the number of the target depot does not correspond to any depot in the warehouse
     * @throws NotEnoughResourceException if the given resource is not present in the target depot in the amount to be deleted
     */
    public void removeFromDepot(int depotNumber, ResourceType resource, int quantity) throws DepotNotPresentException, NotEnoughResourceException {
        if (depotNumber < 1) {
            throw new ParametersNotValidException();
        }
        if (depotNumber > depots.size()) {
            throw new DepotNotPresentException(depotNumber);
        }

        depots.get(depotNumber - 1).removeResource(resource, quantity);

        notifyObservers();
    }

    /**
     * Swaps the contents of two warehouse depots
     *
     * @param depotNumber1 the number of the first depot
     * @param depotNumber2 the number of the second depot
     * @throws DepotNotPresentException    if one of the depot numbers given does not correspond with any depot
     * @throws ParametersNotValidException if the two inputs are the same number or below 1
     * @throws SwapNotValidException       if the content of one or both of the depots cannot be transferred to the other
     */
    public void swapDepotContent(int depotNumber1, int depotNumber2) throws DepotNotPresentException, ParametersNotValidException, SwapNotValidException {
        //Checks the input parameters
        if (depotNumber1 < 1 || depotNumber2 < 1 || depotNumber1 == depotNumber2) {
            throw new ParametersNotValidException();
        }
        if (depotNumber1 > depots.size()) {
            throw new DepotNotPresentException(depotNumber1);
        }
        if (depotNumber2 > depots.size()) {
            throw new DepotNotPresentException(depotNumber2);
        }

        //Stores the affected depots in variables
        ResourceDepot depot1 = depots.get(depotNumber1 - 1);
        ResourceDepot depot2 = depots.get(depotNumber2 - 1);

        //Temporarily stores the contents of the two depots into variables
        List<ResourceType> resourceList1 = depot1.getStoredResources();
        List<ResourceType> resourceList2 = depot2.getStoredResources();
        ResourceType resource1 = null;
        ResourceType resource2 = null;
        int amount1 = 0;
        int amount2 = 0;
        if (!resourceList1.isEmpty()) {
            resource1 = resourceList1.get(0);
            amount1 = depot1.getNumOfResource(resource1);
            depot1.clear();
        }
        if (!resourceList2.isEmpty()) {
            resource2 = resourceList2.get(0);
            amount2 = depot2.getNumOfResource(resource2);
            depot2.clear();
        }

        //Completes the exchange
        try {
            if (amount2 != 0)
                depot1.addResource(resource2, amount2);
            if (amount1 != 0)
                depot2.addResource(resource1, amount1);

        } catch (WrongResourceInsertionException | NotEnoughSpaceException | BlockedResourceException ex) {
            //Attempts to revert the changes
            try {
                depot1.clear();
                depot2.clear();
                if (amount1 != 0)
                    depot1.addResource(resource1, amount1);
                if (amount2 != 0)
                    depot2.addResource(resource2, amount2);

            } catch (WrongResourceInsertionException | NotEnoughSpaceException | BlockedResourceException ex2) {
                //This should never happen
                System.err.print("There was an error when reinserting resources as part of a failed depot swap");
                ex2.printStackTrace();
            }

            throw new SwapNotValidException(depotNumber1, depotNumber2);
        }

        notifyObservers();
    }

    /**
     * Moves a certain amount of resource from one depot to another
     *
     * @param depotNumberTake the number of the depot from which to take the resources
     * @param depotNumberGive the number of the depot to which to move the resources
     * @param resource        the resource to move between the two depots
     * @param quantity        the quantity of the resource to move
     * @throws DepotNotPresentException        if one of the depot numbers given does not correspond with any depot
     * @throws ParametersNotValidException     if the two inputs are the same number or below 1
     * @throws NotEnoughResourceException      if the given resource is not present in the providing depot in the amount to be deleted
     * @throws WrongResourceInsertionException if the type of the resource to be added cannot (currently) be added to the receiving depot
     * @throws NotEnoughSpaceException         if the quantity of the resource to be added plus the amount already stored in the receiving depot exceeds the depot's maximum capacity
     * @throws BlockedResourceException        if the receiving depot is affected by resource blocking and the resource is being blocked by a different depot
     */
    public void moveDepotContent(int depotNumberTake, int depotNumberGive, ResourceType resource, int quantity) throws DepotNotPresentException, NotEnoughResourceException, BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
        //Checks the input parameters
        if (depotNumberTake < 1 || depotNumberGive < 1 || depotNumberTake == depotNumberGive) {
            throw new ParametersNotValidException();
        }
        if (depotNumberTake > depots.size() || depotNumberGive > depots.size()) {
            throw new DepotNotPresentException(depotNumberTake);
        }

        //Checks that the providing depot contains them in sufficient quantity
        if (depots.get(depotNumberTake - 1).getNumOfResource(resource) < quantity) {
            throw new NotEnoughResourceException(resource);
        }

        //Removes the resources from the providing depot
        depots.get(depotNumberTake - 1).removeResource(resource, quantity);

        //Attempts to add the resources to the receiving depot
        try {
            depots.get(depotNumberGive - 1).addResource(resource, quantity);
        } catch (Exception ex) {

            //Attempts to revert the changes
            try {
                depots.get(depotNumberTake - 1).addResource(resource, quantity);
            } catch (WrongResourceInsertionException | NotEnoughSpaceException | BlockedResourceException ex2) {
                //This should never happen
                System.err.print("There was an error when reinserting resources as part of a failed move depot content");
                ex2.printStackTrace();
            }

            throw ex;
        }

        notifyObservers();
    }

    /**
     * Returns whether or not the given resource is blocked by one of the depots in the warehouse, excluding those provided in the exclusions list
     *
     * @param resource   the resource that might be blocked
     * @param exclusions the depots exempted from the search
     * @return true if one of the depots is blocking the resource
     */
    public boolean isResourceBlocked(ResourceType resource, List<ResourceDepot> exclusions) {
        if (resource == null) {
            return false;
        }
        if (exclusions == null) {
            exclusions = new ArrayList<>();
        }
        for (ResourceDepot depot : depots) {
            if (!exclusions.contains(depot) && depot.isBlocking(resource)) {
                return true;
            }
        }
        return false;
    }

    //GETTERS

    /**
     * Returns the number of depots currently accessible in the warehouse
     *
     * @return the number of depots
     */
    public int getNumOfDepots() {
        return depots.size();
    }

    /**
     * Returns the depot corresponding to the given number
     *
     * @param depotNumber the number of the depot to get (STARTS FROM 1)
     * @return the requested depot
     */
    public ResourceDepot getDepot(int depotNumber) {
        return depots.get(depotNumber - 1);
    }

    /**
     * Returns the total stored amount of the given resource among all warehouse depots
     *
     * @param resource the resource the amount of which is asked
     * @return the sum of the amounts of the given resource contained within all depots
     */
    public int getNumOfResource(ResourceType resource) {
        int numOfResource = 0;
        for (ResourceDepot depot : depots) {
            numOfResource += depot.getNumOfResource(resource);
        }
        return numOfResource;
    }

    /**
     * Returns the resources present in the warehouse
     *
     * @return a List of the resources stored in the warehouse
     */
    public List<ResourceType> getStoredResources() {
        List<ResourceType> resourceList = new ArrayList<>();
        for (ResourceDepot depot : depots) {
            resourceList.addAll(depot.getStoredResources());
        }
        return resourceList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Returns an array of the depot's card id, or 0 if they are not leader depots
     *
     * @return an array of the id of the cards that activated the depots.
     */
    public int[] getDepotCardId() {
        int[] cardIds = new int[depots.size()];
        for (int i = 0; i < depots.size(); i++) {
            cardIds[i] = depots.get(i).getCardId();
        }
        return cardIds;
    }

    // PERSISTENCE METHODS

    /**
     * Restores the contents of the warehouse's depots
     *
     * @param depotType     the type of resource stored in each depot
     * @param depotQuantity the amount of resource stored in each depot
     */
    public void restoreDepots(ResourceType[] depotType, int[] depotQuantity) {
        for (int i = 0; i < depotType.length; i++) {
            if (depotType[i] != null)
                try {
                    getDepot(i + 1).addResource(depotType[i], depotQuantity[i]);
                } catch (Exception e) {
                    System.out.println("Warning: Something went wrong restoring a warehouse depot\n" + e.getMessage());
                    System.out.println("Tried to add " + depotType[i] + " " + depotQuantity[i] + " to depot n. " + (i + 1));
                }
        }
    }

    // OBSERVABLE ATTRIBUTES AND METHODS

    /**
     * List of observers that need to get updated when the object state changes
     */
    private final List<Observer> observers = new ArrayList<>();

    /**
     * This method calls the update() on every object observing this object
     */
    public void notifyObservers() {
        observers.forEach(observer -> observer.update(this));
    }

    /**
     * Adds an observer to the list of this object's observers
     *
     * @param observer the Observer to be added
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
        notifyObservers();
    }

    /**
     * Returns the list of this object's observers
     *
     * @return a List of the Observers
     */
    public List<Observer> getObservers() {
        return observers;
    }
}
