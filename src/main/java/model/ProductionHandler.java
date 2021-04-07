package model;

import Exceptions.ResourceNotPresentException;
import model.resource.Resource;
import model.resource.ResourceUnknown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static model.ResourceType.UNKNOWN;

/**
 * This class was made to simplify the interaction between PlayerBoard and Production classes during a single turn.
 * It holds the information about every Production the player decides to activate during his turn so that all the
 * Productions can be activated at the same time at the end of the turn
 */
public class ProductionHandler {

    private final Game game;
    private final List<Production> productions;
    private final List<Resource> currentInput;
    private final List<Resource> currentOutput;

    public ProductionHandler(Game game) {
        this.game = game;
        productions = new ArrayList<>();
        currentInput = new ArrayList<>();
        currentOutput = new ArrayList<>();
    }

    /**
     * ONLY FOR TESTING
     */
    public ProductionHandler() {
        game = null;
        productions = new ArrayList<>();
        currentInput = new ArrayList<>();
        currentOutput = new ArrayList<>();
    }

    private void updateCurrentInput() {
        currentInput.clear();
        for (Production production : productions) {
            if (production.isSelectedByHandler()) {
                for (Resource resource : production.getInput()) {
                    currentInput.add(resource);
                }
            }
        }
    }

    private void updateCurrentOutput() {
        currentOutput.clear();
        for (Production production : productions) {
            if (production.isSelectedByHandler()) {
                for (Resource resource : production.getOutput()) {
                    currentOutput.add(resource);
                }
            }
        }
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
     * Adds the specified Production to the player's ProductionHandler
     *
     * @param production specifies the Production to be added
     */
    public void addProduction(Production production) {
        productions.add(production);
    }

    /**
     * Removes the specified Production from the player's ProductionHandler
     *
     * @param production specifies the Production to be removed
     */
    public void removeProduction(Production production) {
        productions.remove(production);
    }

    /**
     * Removes the first ResourceUnknown it finds in currentInput and replaces it with the specified Resource.
     * Atm if it doesn't find any ResourceUnknown it does nothing.
     *
     * @param resource specifies the Resource that is going to replace the ResourceUnknown
     */
    public void chooseJollyInput(Resource resource, ResourceUnknown resourceUnknown) {
        if (currentInput.remove(resourceUnknown))
            currentInput.add(resource);
        // else throw exception "no UnknownResource present in currentInput"
    }

    /**
     * Removes the first ResourceUnknown it finds in currentOutput and replaces it with the specified Resource.
     * Atm if it doesn't find any ResourceUnknown it does nothing.
     *
     * @param resource specifies the Resource that is going to replace the ResourceUnknown
     */
    public void chooseJollyOutput(Resource resource, ResourceUnknown resourceUnknown) {
        if (currentOutput.remove(resourceUnknown))
            currentOutput.add(resource);
        // else throw exception "no UnknownResource present in currentOutput"
    }

    /**
     * Marks the specified Production as selected and updates currentInput and currentOutput lists.
     * At the end of the turn, only selected Productions will be activated
     *
     * @param i indicates the position of the element in the List
     */
    public void selectProduction(int i) {
        productions.get(i).select();

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
     * Activates the selected Productions. In order to do this, the method removes all the input Resources and adds all
     * the output Resources specified in the selected Productions to the specified PlayerBoard
     *
     * @param playerBoard indicates which player is activating the Productions
     */
    public void confirmProductionChoice(PlayerBoard playerBoard) {
        //TODO
    }

    /**
     * Removes the specified Resource from the currentInput list without actually removing it from the Warehouse.
     * In this way, if the player realizes that he doesn't have enough Resources to activate all the Productions he
     * selected during his turn, he can reset his choices and make them again
     *
     * @param resource specifies the Resource to be removed from the inputList
     * @param quantity specifies the quantity of the Resource to be removed
     * @throws ResourceNotPresentException is thrown when the player tries to remove a Resource that is not present in the inputList
     */
    public void takeResourceFromWarehouse(Resource resource, int quantity) throws ResourceNotPresentException {
        for (int i = 0; i < quantity; i++) {
            if (!currentInput.remove(resource))
                throw new ResourceNotPresentException();
        }
    }

    public void takeResourceFromStrongbox(Resource resource, int quantity) {
        //TODO
    }
}
