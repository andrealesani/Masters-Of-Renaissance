package network;

import Exceptions.*;
import model.card.CardColor;
import model.resource.ResourceType;
import model.UserCommandsInterface;
import model.resource.Resource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class represents a single command sent by the client to the server
 */
public class Command {
    /**
     * The command's parameters, used in calling the Game's methods
     */
    private Map<String, Object> parameters;
    /**
     * The type of user command that is being attempted
     */
    private UserCommandsType commandType;

    //CONSTRUCTORS

    /**
     * Constructor
     *
     * @param commandType the type of the stored command
     * @param parameters the parameters of the stored command
     */
    public Command(UserCommandsType commandType, Map<String, Object> parameters) {
        if (commandType == null)
            throw new ParametersNotValidException();
        this.commandType = commandType;

        this.parameters = Objects.requireNonNullElseGet(parameters, HashMap::new);
    }

    /**
     * Constructor used for deserialization by the Gson class
     */
    public Command() {
        //Do nothing
    }

    //PUBLIC METHODS

    /**
     * Executes the stored command
     *
     * @param game the Model's Game class
     * @return a String detailing the error if the command fails, null otherwise
     */
    public String runCommand(UserCommandsInterface game) {
        Method commandMethod;
        try {
            commandMethod = Command.class.getDeclaredMethod(commandType.toString(), UserCommandsInterface.class);
        } catch (SecurityException | NoSuchMethodException ex) {
            return "Command not valid.";
        }

        try {
            commandMethod.invoke(this, game);
        } catch (InvocationTargetException ex) {
            Exception methodEx = (Exception) ex.getTargetException();
            return methodEx.getMessage();
        } catch (Exception ex) {
            return "Unspecified error when executing command.";
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
    private void chooseLeaderCard(UserCommandsInterface game) throws WrongTurnPhaseException, LeaderNotPresentException {
        int number = extractInt(parameters.get("number"));
        game.chooseLeaderCard(number);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param game the Model's Game class
     */
    private void playLeaderCard(UserCommandsInterface game) throws LeaderRequirementsNotMetException, WrongTurnPhaseException, LeaderNotPresentException, CardAlreadyActiveException {
        int number = extractInt(parameters.get("number"));
        game.playLeaderCard(number);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param game the Model's Game class
     */
    private void discardLeaderCard(UserCommandsInterface game) throws LeaderIsActiveException, WrongTurnPhaseException, LeaderNotPresentException {
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
    private void confirmProductionChoice(UserCommandsInterface game) throws NotEnoughResourceException, UndefinedJollyException, WrongTurnPhaseException {
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
        if (obj instanceof Integer)
            return (Integer) obj;

        if (obj instanceof Double)
            return ((Double) obj).intValue();

        throw new ParametersNotValidException();
    }

    /**
     * Converts a ResourceType received as an object into a Resource
     *
     * @param obj the object to be converted
     * @return the resulting Resource
     */
    private Resource extractResource(Object obj) {
        if (obj instanceof ResourceType)
            return ((ResourceType) obj).toResource();

        if (obj instanceof String)
            return (ResourceType.valueOf((String) obj)).toResource();

        throw new ParametersNotValidException();
    }

    /**
     * Converts a CardColor received as an object into a CardColor
     *
     * @param obj the object to be converted
     * @return the resulting CardColor
     */
    private CardColor extractColor(Object obj) {
        if (obj instanceof CardColor)
            return (CardColor) obj;

        if (obj instanceof String)
            return CardColor.valueOf((String) obj);

        throw new ParametersNotValidException();
    }

    /**
     * Converts an ArrayList of Integers received as an Object into an array of int
     *
     * @param obj the object to be converted
     * @return the resulting int[]
     */
    private int[] extractIntArray(Object obj) {
        if (obj instanceof int[])
            return (int[]) obj;

        if (obj instanceof ArrayList) {
            try {
                return ((ArrayList<Double>) obj).stream().mapToInt(Double::intValue).toArray();
            } catch (Exception ex) {}
        }

        throw new ParametersNotValidException();

    }

    //GETTERS

    /**
     * Getter
     *
     * @return a Map containing the command's parameters as values, and their String names as keys
     */
    public Map<String, Object> getParameters() {
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
