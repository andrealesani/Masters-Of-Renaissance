package model;

import Exceptions.*;
import model.resource.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static model.ResourceType.*;

/**
 * This class was made to simplify the interaction between PlayerBoard and Production classes during a single turn.
 * It holds the information about every Production the player can activate during his turn so that the player can
 * select all the Productions he wants to activate and then all the selected Productions can be activated at the same
 * time at the end of the turn
 */
public class ProductionHandler {
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
    }

    /**
     * Removes the specified Production from the player's ProductionHandler
     *
     * @param production specifies the Production to be removed
     */
    public void removeProduction(Production production) throws ProductionIsSelectedException, ProductionNotPresentException {
        if (production == null)
            throw new ParametersNotValidException();
        if (!productions.contains(production))
            throw new ProductionNotPresentException();
        if (production.isSelectedByHandler())
            throw new ProductionIsSelectedException();
        productions.remove(production);
    }

    /**
     * Removes the first ResourceUnknown it finds in currentInput and replaces it with the specified Resource.
     * Throws RuntimeException if it doesn't find any ResourceUnknown.
     *
     * @param resource specifies the Resource that is going to replace the ResourceUnknown
     * @throws ResourceNotPresentException if the productions' input does not contain any more jollies
     */
    public void chooseJollyInput(Resource resource) throws ResourceNotPresentException {
        if (currentInput.remove(new ResourceUnknown()))
            currentInput.add(resource);
        else throw new ResourceNotPresentException();
    }

    /**
     * Removes the first ResourceUnknown it finds in currentOutput and replaces it with the specified Resource.
     * Throws RuntimeException if it doesn't find any ResourceUnknown.
     *
     * @param resource specifies the Resource that is going to replace the ResourceUnknown
     * @throws ResourceNotPresentException if the productions' input does not contain any more jollies
     */
    public void chooseJollyOutput(Resource resource) throws ResourceNotPresentException {
        if (currentOutput.remove(new ResourceUnknown()))
            currentOutput.add(resource);
        else throw new ResourceNotPresentException();
    }

    /**
     * Marks the specified Production as selected and updates currentInput and currentOutput lists.
     * At the end of the turn, only selected Productions will be activated
     *
     * @param productionNumber indicates the number of the required production
     */
    public void selectProduction(int productionNumber) throws ProductionNotPresentException {
        if (productionNumber < 1)
            throw new ParametersNotValidException();
        if (productions.size() < productionNumber)
            throw new ProductionNotPresentException();
        productions.get(productionNumber - 1).select();

        updateCurrentInput();
        updateCurrentOutput();
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
    }

    /**
     * Returns whether or not the player has enough resources to activate the currently selected productions
     *
     * @param playerBoard specifies which PlayerBoard to control in order to ensure that it has enough Resources
     * @return true if the player has enough resources (considering the entirety of his storage) to activate all the Productions he selected
     * @throws UnknownResourceException if there are still UnknownResources in input or output lists (the exception message will specify if they're in input or output)
     */
    public boolean arePlayerResourcesEnough(PlayerBoard playerBoard) throws UnknownResourceException {
        int numCoin = 0, numFaith = 0, numServant = 0, numShield = 0, numStone = 0;

        if (getCurrentInput().contains(new ResourceUnknown()))
            throw new UnknownResourceException("input");
        if (getCurrentOutput().contains(new ResourceUnknown()))
            throw new UnknownResourceException("output");

        //Turns the input into a map, and makes a set of the different resource types it contains
        List<ResourceType> input = getCurrentInput().stream().map(Resource::getType).collect(Collectors.toList());
        Map<ResourceType, Long> inputQuantities =
                input.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        Set<ResourceType> inputCostMap = inputQuantities.keySet();

        //Controls that for each ResourceType in the map the player has enough Resources to pay up the cost
        for (ResourceType resourceType : inputCostMap) {
            int resourceCost = Math.toIntExact(inputQuantities.get(resourceType));
            if (playerBoard.getNumOfResource(resourceType) < resourceCost)
                return false;
        }

        return true;
    }

    /**
     * Adds to the player's waiting room the resources in the active productions input
     *
     * @param playerBoard the player's board
     */
    public void releaseInput(PlayerBoard playerBoard) {
        if(playerBoard == null)
            throw new ParametersNotValidException();
        for (Resource resource : currentInput) {
            playerBoard.addResourceToWaitingRoom(resource.getType(), 1);
        }
    }

    /**
     * Adds to the player's strongbox the resources in the active productions output
     *
     * @param playerBoard the player's board
     */
    public void releaseOutput(PlayerBoard playerBoard) {
        if(playerBoard == null)
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

    public int getDebt(Resource resource) {
        int sum = 0;
        for (Resource debt : currentInput) {
            if (debt.equals(resource)) {
                sum++;
            }
        }
        return sum;
    }
}
