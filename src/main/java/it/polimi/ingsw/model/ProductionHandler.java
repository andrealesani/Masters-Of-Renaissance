package it.polimi.ingsw.model;

import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceJolly;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class was made to simplify the interaction between PlayerBoard and Production classes during a single turn.
 * It holds the information about every Production the player can activate during his turn so that the player can
 * select all the Productions he wants to activate and then all the selected Productions can be activated at the same
 * time at the end of the turn
 */
public class ProductionHandler implements Observable {
    /**
     * This list contains all of the player's currently available productions
     */
    private final List<Production> productions;
    /**
     * This list contains the cumulative input requirements for all currently selected productions
     */
    private final List<Resource> currentInput;
    /**
     * This list contains the cumulative output for all currently selected productions
     */
    private final List<Resource> currentOutput;

    //CONSTRUCTORS

    /**
     * Constructor
     */
    public ProductionHandler() {
        productions = new ArrayList<>();
        currentInput = new ArrayList<>();
        currentOutput = new ArrayList<>();
    }

    //PUBLIC METHODS

    /**
     * Adds the specified Production to the player's ProductionHandler
     *
     * @param production specifies the Production to be added
     */
    public void addProduction(Production production) {
        if (production == null)
            throw new ParametersNotValidException();
        productions.add(production);

        notifyObservers();
    }

    /**
     * Removes the specified Production from the player's ProductionHandler
     *
     * @param production specifies the Production to be removed
     * @throws ProductionNotPresentException if there is no production corresponding to the give number
     */
    public void removeProduction(Production production) throws ProductionNotPresentException {
        if (production == null)
            throw new ParametersNotValidException();
        if (!productions.contains(production))
            throw new ProductionNotPresentException();

        productions.remove(production);

        notifyObservers();
    }

    /**
     * Removes the first ResourceJolly it finds in currentInput and replaces it with the specified Resource.
     * Throws RuntimeException if it doesn't find any ResourceJolly.
     *
     * @param resource specifies the Resource that is going to replace the ResourceJolly
     * @throws NotEnoughResourceException if the productions' input does not contain any more jollies
     */
    public void chooseJollyInput(Resource resource) throws NotEnoughResourceException {
        if (resource == null || !resource.getType().canBeStored())
            throw new ParametersNotValidException();

        if (currentInput.remove(new ResourceJolly()))
            currentInput.add(resource);
        else throw new NotEnoughResourceException(ResourceType.JOLLY);

        notifyObservers();
    }

    /**
     * Removes the first ResourceJolly it finds in currentOutput and replaces it with the specified Resource.
     * Throws RuntimeException if it doesn't find any ResourceJolly.
     *
     * @param resource specifies the Resource that is going to replace the ResourceJolly
     * @throws NotEnoughResourceException if the productions' input does not contain any more jollies
     */
    public void chooseJollyOutput(Resource resource) throws NotEnoughResourceException {
        if (resource == null || !resource.getType().canBeStored())
            throw new ParametersNotValidException();

        if (currentOutput.remove(new ResourceJolly()))
            currentOutput.add(resource);
        else throw new NotEnoughResourceException(ResourceType.JOLLY);

        notifyObservers();
    }

    /**
     * Marks the specified Production as selected and updates currentInput and currentOutput lists.
     * At the end of the turn, only selected Productions will be activated
     *
     * @param productionNumber indicates the position of the required production  inside the list
     * @throws ProductionNotPresentException if there is no production corresponding to the given number
     */
    public void selectProduction(int productionNumber) throws ProductionNotPresentException {
        if (productionNumber < 1)
            throw new ParametersNotValidException();
        if (productionNumber > productions.size())
            throw new ProductionNotPresentException(productionNumber);

        productions.get(productionNumber - 1).select();

        updateCurrentInput();
        updateCurrentOutput();
    }

    /**
     * Marks the specified Production as selected and updates currentInput and currentOutput lists.
     * At the end of the turn, only selected Productions will be activated
     *
     * @param productionID indicates the ID of the required production
     * @throws ProductionNotPresentException if there is no production corresponding to the given id
     */
    public void selectProductionByID(int productionID) throws ProductionNotPresentException {

        for (Production production : productions) {
            if (production.getId() == productionID) {
                production.select();
                updateCurrentInput();
                updateCurrentOutput();
                return;
            }
        }

        throw new ProductionNotPresentException();
    }

    /**
     * Unselects all the Productions in the ProductionHandler and clears currentInput and currentOutput lists.
     */
    public void resetProductionChoice() {
        for (Production production : productions) {
            production.unselect();
        }

        updateCurrentInput();
        updateCurrentOutput();

        notifyObservers();
    }

    /**
     * Returns whether or not the player has enough resources to activate the currently selected productions.
     * If the player does not have the required resources, throws an exception so as to be able to communicate which is missing
     *
     * @param playerBoard specifies which PlayerBoard to control in order to ensure that it has enough Resources
     * @return true if the player has enough resources (considering the entirety of his storage) to activate all the Productions he selected
     * @throws UndefinedJollyException if there are still UnknownResources in input or output lists (the exception message will specify if they're in input or output)
     * @throws NotEnoughResourceException if the player does not have enough resources
     */
    public boolean arePlayerResourcesEnough(PlayerBoard playerBoard) throws UndefinedJollyException, NotEnoughResourceException {

        if (getCurrentInput().contains(new ResourceJolly()))
            throw new UndefinedJollyException("input");
        if (getCurrentOutput().contains(new ResourceJolly()))
            throw new UndefinedJollyException("output");

        //Turns the input into a map, and makes a set of the different resource types it contains
        List<ResourceType> input = getCurrentInput().stream().map(Resource::getType).collect(Collectors.toList());
        Map<ResourceType, Long> inputQuantities =
                input.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        Set<ResourceType> inputCostMap = inputQuantities.keySet();

        //Controls that for each ResourceType in the map the player has enough Resources to pay up the cost
        for (ResourceType resourceType : inputCostMap) {
            int resourceCost = Math.toIntExact(inputQuantities.get(resourceType));
            if (playerBoard.getNumOfResource(resourceType) < resourceCost)
                throw new NotEnoughResourceException(resourceType);
        }

        return true;
    }

    /**
     * Adds to the player's waiting room the resources in the active productions input
     *
     * @param playerBoard the player's board
     */
    public void releaseInput(PlayerBoard playerBoard) {
        if (playerBoard == null)
            throw new ParametersNotValidException();
        Map<ResourceType, Integer> resources = new HashMap<>();
        for (Resource resource : currentInput)
            resources.merge(resource.getType(), 1, Integer::sum);
        playerBoard.addResourcesToWaitingRoom(resources);
    }

    /**
     * Adds to the player's strongbox the resources in the active productions output
     *
     * @param playerBoard the player's board
     */
    public void releaseOutput(PlayerBoard playerBoard) {
        if (playerBoard == null)
            throw new ParametersNotValidException();
        for (Resource resource : currentOutput) {
            resource.addResourceFromProduction(playerBoard);
        }
    }

    //PRIVATE METHODS

    /**
     * Updates the input list every time a Production is selected or unselected
     */
    private void updateCurrentInput() {
        currentInput.clear();
        for (Production production : productions) {
            if (production.isSelectedByHandler()) {
                currentInput.addAll(production.getInput());
            }
        }

        notifyObservers();
    }

    /**
     * Updates the output list every time a Production is selected or unselected
     */
    private void updateCurrentOutput() {
        currentOutput.clear();
        for (Production production : productions) {
            if (production.isSelectedByHandler()) {
                currentOutput.addAll(production.getOutput());
            }
        }

        notifyObservers();
    }

    //GETTERS

    /**
     * Getter
     *
     * @return productions
     */
    public List<Production> getProductions() {
        return productions;
    }

    /**
     * Getter that returns only production that are currently selected
     *
     * @return a list of Productions that are currently selected in the player's ProductionHandler
     */
    public List<Production> getSelectedProductions() {
        List<Production> selectedProductions = new ArrayList<>();
        for (Production production : productions) {
            if (production.isSelectedByHandler())
                selectedProductions.add(production);
        }
        return selectedProductions;
    }

    /**
     * Getter
     *
     * @return currentInput
     */
    public List<Resource> getCurrentInput() {
        return currentInput;
    }

    /**
     * Getter
     *
     * @return currentOutput
     */
    public List<Resource> getCurrentOutput() {
        return currentOutput;
    }

    /**
     * Getter for the amount of resources of a certain type needed in current input
     *
     * @param resource the resource to get the debt of
     * @return the debt amount for the given resource
     */
    public int getDebt(Resource resource) {
        int sum = 0;
        for (Resource debt : currentInput) {
            if (debt.equals(resource)) {
                sum++;
            }
        }
        return sum;
    }

    // PERSISTENCE METHODS

    /**
     * Restores the handler's active productions
     *
     * @param activeProductions an array flagging the productions that were active
     */
    public void restoreProductions(boolean[] activeProductions) {
        for (int i = 0; i < activeProductions.length; i++) {
            if (activeProductions[i])
                this.productions.get(i).select();
        }
    }

    /**
     * Restores the handler's current input
     *
     * @param inputTypes      an array of the input's resource types
     * @param inputQuantities an array of the input's resource quantities
     */
    public void restoreCurrentInput(ResourceType[] inputTypes, int[] inputQuantities) {
        currentInput.clear();
        for (int i = 0; i < inputTypes.length; i++) {
            for (int j = 0; j < inputQuantities[i]; j++) {
                this.currentInput.add(inputTypes[i].toResource());
            }
        }
    }

    /**
     * Restores the handler's current output
     *
     * @param outputTypes      an array of the output's resource types
     * @param outputQuantities an array of the output's resource quantities
     */
    public void restoreCurrentOutput(ResourceType[] outputTypes, int[] outputQuantities) {
        currentOutput.clear();
        for (int i = 0; i < outputTypes.length; i++) {
            for (int j = 0; j < outputQuantities[i]; j++) {
                this.currentOutput.add(outputTypes[i].toResource());
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
