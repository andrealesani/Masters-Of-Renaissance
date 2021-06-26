package model;

import Exceptions.*;
import model.card.CardColor;
import model.resource.Resource;


/**
 * This interface indicates that the implementing class can be used as an interface for the player actions called by the controller.
 * It has methods for doing everything a player can do in the game.
 */
public interface UserCommandsInterface {
    //PUBLIC METHODS

    //First turn actions

    /**
     * Allows the player to choose which resource to get (if any) as part of the first turn's bonus resources
     *
     * @param resource the type of bonus resource to get
     * @param quantity the amount of resource to get
     * @throws NotEnoughResourceException  if the player does not have sufficient bonus resources left to convert
     * @throws WrongTurnPhaseException     if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     */
    void chooseBonusResourceType(Resource resource, int quantity) throws NotEnoughResourceException, WrongTurnPhaseException;

    /**
     * Allows the player to choose which leader cards to keep (after choosing two the rest are discarded)
     *
     * @param pos the number of the leaderCard to choose (STARTS FROM 1)
     * @throws LeaderNotPresentException   if the number selected does not correspond to a leader card
     * @throws WrongTurnPhaseException     if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     */
    void chooseLeaderCard(int pos) throws WrongTurnPhaseException, LeaderNotPresentException;

    //LeaderCards handling actions

    /**
     * Allows the player to activate the leader card corresponding to the given number
     *
     * @param number the number of the leaderCard to activate
     * @throws LeaderRequirementsNotMetException if the player does not meet the requirements for activating the leader card
     * @throws LeaderNotPresentException         if the number selected does not correspond to a leader card
     * @throws CardAlreadyActiveException        if the selected card is already active
     * @throws WrongTurnPhaseException           if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException       if the given parameters are not admissible for the game's rules
     */
    void playLeaderCard(int number) throws LeaderRequirementsNotMetException, WrongTurnPhaseException, LeaderNotPresentException, CardAlreadyActiveException;

    /**
     * Allows the player to discard the leader card corresponding to the given number
     *
     * @param number the number of the leaderCard to discard
     * @throws LeaderIsActiveException     if the player attempts to discard a leader card they have previously activated
     * @throws LeaderNotPresentException   if the number selected does not correspond to a leader card
     * @throws WrongTurnPhaseException     if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     */
    void discardLeaderCard(int number) throws WrongTurnPhaseException, LeaderIsActiveException, LeaderNotPresentException;

    //Market selection actions

    /**
     * Allows the player to select a row from the market and take its resources
     *
     * @param numScope the index of the selected row
     * @throws WrongTurnPhaseException     if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     */
    void selectMarketRow(int numScope) throws WrongTurnPhaseException;

    /**
     * Allows the player to select a column from the market and take its resources
     *
     * @param numScope the index of the selected column
     * @throws WrongTurnPhaseException     if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     */
    void selectMarketColumn(int numScope) throws WrongTurnPhaseException;

    /**
     * Allows the player to send a resource obtained from the market to a specific depot
     *
     * @param depotNumber the number of the depot to which to send the resource
     * @param resource    the resource to send to the depot
     * @param quantity    the amount of resource to send
     * @throws DepotNotPresentException        if there is no depot corresponding to the given number
     * @throws NotEnoughResourceException      if there is not enough of the selected resource in waiting room
     * @throws BlockedResourceException        if the resource cannot be inserted in the target depot because it is blocked by a different one
     * @throws NotEnoughSpaceException         if the target depot does not have enough space to contain the given amount of resource
     * @throws WrongResourceInsertionException if the target depot cannot hold the given type of resource
     * @throws WrongTurnPhaseException         if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException     if the given parameters are not admissible for the game's rules
     */
    void sendResourceToDepot(int depotNumber, Resource resource, int quantity) throws DepotNotPresentException, NotEnoughResourceException, BlockedResourceException, NotEnoughSpaceException, WrongResourceInsertionException, WrongTurnPhaseException;

    /**
     * Allows the player to choose to convert a white marble resource into one from their conversions list
     *
     * @param resource the resource into which to convert the white marble
     * @param quantity the amount of resource to convert
     * @throws ConversionNotAvailableException if the player does not have access to conversion of white marbles to the given resource
     * @throws NotEnoughResourceException      if the player does not have enough white marbles to convert
     * @throws WrongTurnPhaseException         if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException     if the given parameters are not admissible for the game's rules
     */
    void chooseMarbleConversion(Resource resource, int quantity) throws ConversionNotAvailableException, NotEnoughResourceException, WrongTurnPhaseException;

    /**
     * Allows the user to swap the contents of two warehouse depots
     *
     * @param depotNumber1 the number of the first depot to swap
     * @param depotNumber2 the number of the second depot to swap
     * @throws SwapNotValidException       if the content of one or both of the depots cannot be transferred to the other
     * @throws DepotNotPresentException    if there is no depot corresponding to one of the given numbers
     * @throws WrongTurnPhaseException     if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     */
    void swapDepotContent(int depotNumber1, int depotNumber2) throws SwapNotValidException, DepotNotPresentException, WrongTurnPhaseException;

    /**
     * Allows user to send some of the contents of a depot to a different one
     *
     * @param providingDepotNumber the number of the depot from which to take the resources
     * @param receivingDepotNumber the number of the depot to which to move the resources
     * @param resource             the resource to move between the two depots
     * @param quantity             the quantity of the resource to move
     * @throws DepotNotPresentException        if there is no depot corresponding to one of the given numbers
     * @throws NotEnoughResourceException      if there is not enough of the selected resource in the providing depot
     * @throws BlockedResourceException        if the resource cannot be inserted in the receiving depot because it is blocked by a different one
     * @throws NotEnoughSpaceException         if the receiving depot does not have enough space to contain the given amount of resource
     * @throws WrongResourceInsertionException if the receiving depot cannot hold the given type of resource
     * @throws WrongTurnPhaseException         if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException     if the given parameters are not admissible for the game's rules
     */
    void moveDepotContent(int providingDepotNumber, int receivingDepotNumber, Resource resource, int quantity) throws WrongTurnPhaseException, DepotNotPresentException, WrongResourceInsertionException, BlockedResourceException, NotEnoughSpaceException, NotEnoughResourceException;

    //DevelopmentCard purchasing actions

    /**
     * Allows the player to buy a development card from the cardTable
     *
     * @param cardColor the color of the card to buy
     * @param level     the level of the card to buy
     * @param slot      the card slot in which to put the card
     * @throws SlotNotValidException       if the selected card cannot be inserted in the given slot
     * @throws NotEnoughResourceException  if the player does not have enough resources to buy the card
     * @throws EmptyDeckException          if there are no cards left of the given color and level
     * @throws WrongTurnPhaseException     if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     */
    void takeDevelopmentCard(CardColor cardColor, int level, int slot) throws SlotNotValidException, NotEnoughResourceException, WrongTurnPhaseException, EmptyDeckException;

    //Production selection actions

    /**
     * Allows the player to select a production for activation (STARTS FROM 1)
     *
     * @param number the number of the production
     * @throws ProductionNotPresentException if there is no production that corresponds to the given number
     * @throws WrongTurnPhaseException       if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException   if the given parameters are not admissible for the game's rules
     */
    void selectProduction(int number) throws WrongTurnPhaseException, ProductionNotPresentException;

    /**
     * Allows the player to reset the selected productions
     *
     * @throws WrongTurnPhaseException if the player attempts this action when they are not allowed to
     */
    void resetProductionChoice() throws WrongTurnPhaseException;

    //TODO aggiungere quantità ai metodi chooseJolly

    /**
     * Allows the player to choose into which resource to turn a jolly in the production's input
     *
     * @param resource the resource into which to turn the jolly
     * @throws WrongTurnPhaseException     if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     * @throws ResourceNotPresentException if the productions' input does not contain any more jollies
     */
    void chooseJollyInput(Resource resource) throws WrongTurnPhaseException, ResourceNotPresentException;

    /**
     * Allows the player to choose into which resource to turn a jolly in the production's output
     *
     * @param resource the resource into which to turn the jolly
     * @throws WrongTurnPhaseException     if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     * @throws ResourceNotPresentException if the productions' input does not contain any more jollies
     */
    void chooseJollyOutput(Resource resource) throws WrongTurnPhaseException, ResourceNotPresentException;

    /**
     * Allows the player to confirm the selected production for activation
     *
     * @throws NotEnoughResourceException  if the player does not have enough resources to activate the selected productions
     * @throws UndefinedJollyException    if the player still has to choose which resources some jollies in the productions' input or output will become
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     * @throws ResourceNotPresentException if the productions' input does not contain any more jollies
     */
    void confirmProductionChoice() throws NotEnoughResourceException, UndefinedJollyException, WrongTurnPhaseException;

    //Debt payment actions

    /**
     * Allows the player to pay the development card cost by taking resources from the given depot in the warehouse
     *
     * @param depotNumber the number of the depot from which to take the resource
     * @param resource    the resource to take
     * @param quantity    the amount of resource to take (and of cost to pay)
     * @throws NotEnoughResourceException  if the selected depot does not contain enough of the given resource
     * @throws DepotNotPresentException    if there is no depot corresponding to the given number
     * @throws WrongTurnPhaseException     if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     */
    void payFromWarehouse(int depotNumber, Resource resource, int quantity) throws NotEnoughResourceException, DepotNotPresentException, WrongTurnPhaseException;

    /**
     * Allows the player to pay the development card cost by taking resources from the strongbox
     *
     * @param resource the resource to take
     * @param quantity the amount of resource to take (and of cost to pay)
     * @throws NotEnoughResourceException  if the strongbox does not contain enough of the given resource
     * @throws WrongTurnPhaseException     if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     */
    void payFromStrongbox(Resource resource, int quantity) throws NotEnoughResourceException, WrongTurnPhaseException;

    //End turn actions

    /**
     * Allows the player to end their current turn
     *
     * @throws WrongTurnPhaseException if the player attempts this action when they are not allowed to
     */
    void endTurn() throws WrongTurnPhaseException;
}
