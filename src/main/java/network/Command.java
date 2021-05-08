package network;

import Exceptions.*;
import model.CardColor;
import model.ResourceType;
import model.UserCommandsInterface;
import model.resource.Resource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a single command sent by the client to the server
 */
public class Command {
    /**
     * The command's parameters, used in calling the Game's methods
     */
    private final Map parameters;
    /**
     * The type of user command that is being attempted
     */
    private final UserCommandsType commandType;

    //CONSTRUCTORS

    public Command(UserCommandsType commandType, Map parameters) {
        if (commandType == null)
            throw new ParametersNotValidException();
        this.commandType = commandType;

        if (parameters == null) {
            this.parameters = new HashMap<String, String>();
        } else {
            this.parameters = parameters;
        }
    }

    //PUBLIC METHODS

    /**
     * Executes the stored command
     *
     * @param game the Model's Game class
     * @return a String detailing the error if the command fails, null otherwise
     */
    public String runCommand(UserCommandsInterface game) {
        Method commandMethod = null;
        try {
            commandMethod = Command.class.getDeclaredMethod(commandType.toString(), UserCommandsInterface.class);
        } catch (SecurityException | NoSuchMethodException ex) {
            return "Command not valid.";
        }

        try {
            commandMethod.invoke(this, game);
        } catch (Exception ex) {
            if (ex.getMessage() == null) {
                return "Unspecified error when executing command.";
            }
            return ex.getMessage();
        }

        return null;
    }

    //PRIVATE METHODS

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param game the Model's Game class
     */
    private void chooseBonusResourceType(UserCommandsInterface game) throws NotEnoughResourceException, WrongTurnPhaseException {

        Resource resource = extractResource(parameters.get("resource"));
        int quantity = extractInt(parameters.get("quantity"));
        game.chooseBonusResourceType(resource, quantity);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param game the Model's Game class
     */
    private void chooseLeaderCard(UserCommandsInterface game) throws WrongTurnPhaseException {
        int number = extractInt(parameters.get("number"));
        game.chooseLeaderCard(number);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param game the Model's Game class
     */
    private void playLeaderCard(UserCommandsInterface game) throws LeaderRequirementsNotMetException, WrongTurnPhaseException {
        int number = extractInt(parameters.get("number"));
        game.playLeaderCard(number);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param game the Model's Game class
     */
    private void discardLeaderCard(UserCommandsInterface game) throws LeaderIsActiveException, WrongTurnPhaseException {
        int number = extractInt(parameters.get("number"));
        game.discardLeaderCard(number);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param game the Model's Game class
     */
    private void selectMarketRow(UserCommandsInterface game) throws WrongTurnPhaseException {
        int number = extractInt(parameters.get("number"));
        game.selectMarketRow(number);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param game the Model's Game class
     */
    private void selectMarketColumn(UserCommandsInterface game) throws WrongTurnPhaseException {
        int number = extractInt(parameters.get("number"));
        game.selectMarketColumn(number);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param game the Model's Game class
     */
    private void sendResourceToDepot(UserCommandsInterface game) throws DepotNotPresentException, NotEnoughSpaceException, NotEnoughResourceException, WrongResourceInsertionException, WrongTurnPhaseException, BlockedResourceException {
        int number = extractInt(parameters.get("number"));
        Resource resource = extractResource(parameters.get("resource"));
        int quantity = extractInt(parameters.get("quantity"));
        game.sendResourceToDepot(number, resource, quantity);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param game the Model's Game class
     */
    private void chooseMarbleConversion(UserCommandsInterface game) throws NotEnoughResourceException, ConversionNotAvailableException, WrongTurnPhaseException {
        Resource resource = extractResource(parameters.get("resource"));
        int quantity = extractInt(parameters.get("quantity"));
        game.chooseMarbleConversion(resource, quantity);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param game the Model's Game class
     */
    private void swapDepotContent(UserCommandsInterface game) throws DepotNotPresentException, SwapNotValidException, WrongTurnPhaseException {
        int[] depots = extractIntArray(parameters.get("depots"));
        game.swapDepotContent(depots[0], depots[1]);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param game the Model's Game class
     */
    private void moveDepotContent(UserCommandsInterface game) throws DepotNotPresentException, NotEnoughSpaceException, NotEnoughResourceException, WrongTurnPhaseException, WrongResourceInsertionException, BlockedResourceException {
        int[] depots = extractIntArray(parameters.get("depots"));
        Resource resource = extractResource(parameters.get("resource"));
        int quantity = extractInt(parameters.get("quantity"));
        game.moveDepotContent(depots[0], depots[1], resource, quantity);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param game the Model's Game class
     */
    private void takeDevelopmentCard(UserCommandsInterface game) throws SlotNotValidException, NotEnoughResourceException, EmptyDeckException, WrongTurnPhaseException {
        CardColor color = extractColor(parameters.get("color"));
        int level = extractInt(parameters.get("level"));
        int number = extractInt(parameters.get("number"));
        game.takeDevelopmentCard(color, level, number);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param game the Model's Game class
     */
    private void selectProduction(UserCommandsInterface game) throws ProductionNotPresentException, WrongTurnPhaseException {
        int number = extractInt(parameters.get("number"));
        game.selectProduction(number);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param game the Model's Game class
     */
    private void resetProductionChoice(UserCommandsInterface game) throws WrongTurnPhaseException {
        game.resetProductionChoice();
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param game the Model's Game class
     */
    private void confirmProductionChoice(UserCommandsInterface game) throws NotEnoughResourceException, UnknownResourceException, WrongTurnPhaseException {
        game.confirmProductionChoice();
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param game the Model's Game class
     */
    private void chooseJollyInput(UserCommandsInterface game) throws ResourceNotPresentException, WrongTurnPhaseException {
        Resource resource = extractResource(parameters.get("resource"));
        game.chooseJollyInput(resource);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param game the Model's Game class
     */
    private void chooseJollyOutput(UserCommandsInterface game) throws ResourceNotPresentException, WrongTurnPhaseException {
        Resource resource = extractResource(parameters.get("resource"));
        game.chooseJollyOutput(resource);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param game the Model's Game class
     */
    private void payFromWarehouse(UserCommandsInterface game) throws DepotNotPresentException, NotEnoughResourceException, WrongTurnPhaseException {
        int number = extractInt(parameters.get("number"));
        Resource resource = extractResource(parameters.get("resource"));
        int quantity = extractInt(parameters.get("quantity"));
        game.payFromWarehouse(number, resource, quantity);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param game the Model's Game class
     */
    private void payFromStrongbox(UserCommandsInterface game) throws NotEnoughResourceException, WrongTurnPhaseException {
        Resource resource = extractResource(parameters.get("resource"));
        int quantity = extractInt(parameters.get("quantity"));
        game.payFromStrongbox(resource, quantity);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param game the Model's Game class
     */
    private void endTurn(UserCommandsInterface game) throws WrongTurnPhaseException {
        game.endTurn();
    }

    //EXTRACTORS

    /**
     * Converts an Integer received as an Object into an int
     *
     * @param obj the object to be converted
     * @return the resulting int
     */
    private int extractInt(Object obj) {
        if (!(obj instanceof Integer))
            throw new ParametersNotValidException();
        return (Integer) obj;
    }

    /**
     * Converts a ResourceType received as an object into a Resource
     *
     * @param obj the object to be converted
     * @return the resulting Resource
     */
    private Resource extractResource(Object obj) {
        if (!(obj instanceof ResourceType))
            throw new ParametersNotValidException();
        return ((ResourceType) obj).toResource();
    }

    /**
     * Converts a CardColor received as an object into a CardColor
     *
     * @param obj the object to be converted
     * @return the resulting CardColor
     */
    private CardColor extractColor(Object obj) {
        if (!(obj instanceof CardColor))
            throw new ParametersNotValidException();
        return (CardColor) obj;
    }

    /**
     * Converts an ArrayList of Integers received as an Object into an array of int
     *
     * @param obj the object to be converted
     * @return the resulting int[]
     */
    private int[] extractIntArray(Object obj) {
        if (!(obj instanceof int[]))
            throw new ParametersNotValidException();
        return (int[]) obj;
    }

    //GETTERS

    /**
     * Getter
     *
     * @return a Map containing the command's parameters as values, and their String names as keys
     */
    public Map getParameters() {
        return parameters;
    }

    /**
     * Getter
     *
     * @return the type of the command
     */
    public UserCommandsType getCommandType() {
        return commandType;
    }
}
