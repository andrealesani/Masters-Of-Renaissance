package model;

import Exceptions.ResourceNotPresentException;
import Exceptions.UnknownResourceException;
import model.resource.*;

import java.util.ArrayList;
import java.util.List;

import static model.ResourceType.*;

/**
 * This class was made to simplify the interaction between PlayerBoard and Production classes during a single turn.
 * It holds the information about every Production the player decides to activate during his turn so that all the
 * Productions can be activated at the same time at the end of the turn
 */
public class ProductionHandler {

    private final List<Production> productions;
    private final List<Resource> currentInput;
    private final List<Resource> currentOutput;

    /**
     * Constructor
     */
    public ProductionHandler() {
        productions = new ArrayList<>();
        currentInput = new ArrayList<>();
        currentOutput = new ArrayList<>();
    }

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

    /**
     * Getter
     *
     * @return productions
     */
    public List<Production> getProductions() {
        return productions;
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
        if(production.isSelectedByHandler())
            throw new RuntimeException("Can't remove a production when it's selected");
        productions.remove(production);
    }

    /**
     * Removes the first ResourceUnknown it finds in currentInput and replaces it with the specified Resource.
     * Throws RuntimeException if it doesn't find any ResourceUnknown.
     *
     * @param resource specifies the Resource that is going to replace the ResourceUnknown
     */
    public void chooseJollyInput(Resource resource) {
        if (currentInput.remove(new ResourceUnknown()))
            currentInput.add(resource);
        else throw new RuntimeException("no UnknownResource present in currentInput");
    }

    /**
     * Removes the first ResourceUnknown it finds in currentOutput and replaces it with the specified Resource.
     * Throws RuntimeException if it doesn't find any ResourceUnknown.
     *
     * @param resource specifies the Resource that is going to replace the ResourceUnknown
     */
    public void chooseJollyOutput(Resource resource) {
        if (currentOutput.remove(new ResourceUnknown()))
            currentOutput.add(resource);
        else throw new RuntimeException("no UnknownResource present in currentOutput");
    }

    /**
     * Marks the specified Production as selected and updates currentInput and currentOutput lists.
     * At the end of the turn, only selected Productions will be activated
     *
     * @param i indicates the position of the Production in the List
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
     * @param playerBoard specifies which PlayerBoard to control in order to ensure that it has enough Resources
     * @return true if the player has enough resources (considering the entirety of his storage) to activate all the Productions he selected
     * @throws UnknownResourceException if there are still UnknownResources in input or output lists (the exception message will specify if they're in input or output)
     */
    public boolean resourcesAreEnough(PlayerBoard playerBoard) throws UnknownResourceException {
        int numCoin = 0, numFaith = 0, numServant = 0, numShield = 0, numStone = 0;

        //TODO una versione che non usi gli if, magari prendendo il contenuto di currentInput e trasformandolo in una mappa come in playerboard.buyDevelopmentCard
        // In più perchè usare getCurrentInput quando currentInput è un attributo della classe??
        for (Resource resource : getCurrentInput()) {
            if (resource.equals(new ResourceUnknown()))
                throw new UnknownResourceException("input");
            else if (resource.equals(new ResourceCoin()))
                numCoin++;
            else if (resource.equals(new ResourceFaith()))
                numFaith++;
            else if (resource.equals(new ResourceServant()))
                numServant++;
            else if (resource.equals(new ResourceShield()))
                numShield++;
            else if (resource.equals(new ResourceStone()))
                numStone++;
        }
        for (Resource resource : getCurrentOutput()) {
            if (resource.equals(new ResourceUnknown()))
                throw new UnknownResourceException("output");
        }

        //TODO come sopra
        if (playerBoard.getNumOfResource(COIN) < numCoin)
            return false;
        if (playerBoard.getNumOfResource(FAITH) < numFaith)
            return false;
        if (playerBoard.getNumOfResource(SERVANT) < numServant)
            return false;
        if (playerBoard.getNumOfResource(SHIELD) < numShield)
            return false;
        if (playerBoard.getNumOfResource(STONE) < numStone)
            return false;

        return true;
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

    /**
     * Removes a Resource from currentInput list when that Resource has been taken from the player's stashes (the method is called from the PlayerBoard).
     * If currentInput is empty this means that all input Resources have been paid.
     * In this case, the method will call releaseOutput() and give all output Resources to the player
     *
     * @param playerBoard specifies the PlayerBoard that has spent the resources so that the ProductionHandler can give it the output Resources if the input debt is extinguished
     * @param resource    specifies the Resource to be removed from currentInput
     * @param quantity    specifies the quantity of the Resource to be removed
     * @throws ResourceNotPresentException is thrown when the player tries to remove a Resource that is not present in the inputList
     */
    public void takeResource(PlayerBoard playerBoard, Resource resource, int quantity) throws ResourceNotPresentException {
        for (int i = 0; i < quantity; i++) {
            if (!currentInput.remove(resource))
                throw new ResourceNotPresentException();
        }
        if (currentInput.isEmpty())
            releaseOutput(playerBoard);
    }

    /**
     * Puts all the Resources due in currentOutput in the player's strongbox
     */
    private void releaseOutput(PlayerBoard playerBoard) {
        for (Resource resource : currentOutput) {
            playerBoard.addResourceToStrongbox(resource.getType(), 1);
        }
    }
}
