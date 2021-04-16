package model;

import Exceptions.*;
import model.resource.Resource;

//TODO add exceptions to javadoc

/**
 * This interface indicates that the implementing class can be used as an interface for the player actions called by the controller.
 * It has methods for doing everything a player can do in the game.
 */
public interface UserInterface {
    //PUBLIC METHODS

    //First turn actions

    /**
     * Allows the player to choose which resource to get (if any) as part of the first turn's bonus resources
     *
     * @param resource the type of bonus resource to get
     * @param quantity the amount of resource to get
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     */
    void chooseBonusResourceType(Resource resource, int quantity) throws NotEnoughResourceException, WrongTurnPhaseException;

    /**
     * Allows the player to choose which leader cards to keep (after choosing two the rest are discarded)
     *
     * @param pos the number of the leaderCard to choose (STARTS FROM 1)
     */
    void chooseLeaderCard(int pos) throws WrongTurnPhaseException;

    //LeaderCards handling actions

    /**
     * Allows the player to activate the leader card corresponding to the given number
     *
     * @param number the number of the leaderCard to activate
     */
    void playLeaderCard(int number) throws LeaderRequirementsNotMetException, WrongTurnPhaseException;

    /**
     * Allows the player to discard the leader card corresponding to the given number
     *
     * @param number the number of the leaderCard to discard
     */
    void discardLeaderCard(int number) throws WrongTurnPhaseException;

    //Market selection actions

    /**
     * Allows the player to select a row from the market and take its resources
     *
     * @param numScope    the index of the selected row
     */
    void selectMarketRow(int numScope) throws WrongTurnPhaseException;

    /**
     * Allows the player to select a column from the market and take its resources
     *
     * @param numScope    the index of the selected column
     */
    void selectMarketColumn(int numScope) throws WrongTurnPhaseException;

    /**
     * Allows the player to send a resource obtained from the market to a specific depot
     *
     * @param depotNumber the number of the depot to which to send the resource
     * @param resource    the resource to send to the depot
     * @param quantity    the amount of resource to send
     */
    void sendResourceToDepot(int depotNumber, Resource resource, int quantity) throws DepotNotPresentException, NotEnoughResourceException, BlockedResourceException, NotEnoughSpaceException, WrongResourceInsertionException, WrongTurnPhaseException;

    /**
     * Allows the player to choose to convert a white marble resource into one from their conversions list
     *
     * @param resource the resource into which to convert the white marble
     * @param quantity the amount of resource to convert
     */
    void chooseMarbleConversion(Resource resource, int quantity) throws ConversionNotAvailableException, NotEnoughResourceException, WrongTurnPhaseException;

    /**
     * Allows the user to swap the contents of two warehouse depots
     *
     * @param depotNumber1 the number of the first depot to swap
     * @param depotNumber2 the number of the second depot to swap
     */
    void swapDepotContent(int depotNumber1, int depotNumber2) throws SwapNotValidException, ParametersNotValidException, DepotNotPresentException, WrongTurnPhaseException;

    /**
     * Allows user to send some of the contents of a depot to a different one
     *
     * @param depotNumberTake the number of the depot from which to take the resources
     * @param depotNumberGive the number of the depot to which to move the resources
     * @param resource the resource to move between the two depots
     * @param quantity the quantity of the resource to move
     */
    void moveDepotContent(int depotNumberTake, int depotNumberGive, Resource resource, int quantity) throws WrongTurnPhaseException, DepotNotPresentException, WrongResourceInsertionException, BlockedResourceException, NotEnoughSpaceException, NotEnoughResourceException;

    //DevelopmentCard purchasing actions

    /**
     * Allows the player to buy a development card from the cardTable
     *
     * @param cardColor the color of the card to buy
     * @param row       the card table row from which to buy the card
     * @param slot      the car slot in which to put the card
     */
    void takeDevelopmentCard(CardColor cardColor, int row, int slot) throws SlotNotValidException, NotEnoughResourceException, WrongTurnPhaseException, EmptyDeckException;

    //Production selection actions

    /**
     * Allows the player to select a production for activation
     * STARTS FROM 0
     *
     * @param number the number of the production
     */
    void selectProduction(int number) throws WrongTurnPhaseException;

    /**
     * Allows the player to reset the selected productions
     */
    void resetProductionChoice() throws WrongTurnPhaseException;

    /**
     * Allows the player to confirm the selected production for activation
     */
    void confirmProductionChoice() throws NotEnoughResourceException, UnknownResourceException, WrongTurnPhaseException;

    /**
     * Allows the player to choose into which resource to turn a jolly in the production's input
     *
     * @param resource the resource into which to turn the jolly
     */
    void chooseJollyInput(Resource resource) throws WrongTurnPhaseException;

    /**
     * Allows the player to choose into which resource to turn a jolly in the production's output
     *
     * @param resource the resource into which to turn the jolly
     */
    void chooseJollyOutput(Resource resource) throws WrongTurnPhaseException;

    //Debt payment actions

    /**
     * Allows the player to pay the development card cost by taking resources from the given depot in the warehouse
     *
     * @param depotNumber the number of the depot from which to take the resource
     * @param resource    the resource to take
     * @param quantity    the amount of resource to take (and of cost to pay)
     */
    void payFromWarehouse(int depotNumber, Resource resource, int quantity) throws NotEnoughResourceException, DepotNotPresentException, WrongTurnPhaseException;

    /**
     * Allows the player to pay the development card cost by taking resources from the strongbox
     *
     * @param resource the resource to take
     * @param quantity the amount of resource to take (and of cost to pay)
     */
    void payFromStrongbox(Resource resource, int quantity) throws NotEnoughResourceException, WrongTurnPhaseException;

    //End turn actions

    /**
     * Allows the player to end their current turn
     */
    void endTurn() throws WrongTurnPhaseException;
}
