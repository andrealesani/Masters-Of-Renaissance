package model;

import model.resource.Resource;

/**
 * This interface indicates that the implementing class can be used as an interface for the player actions called by the controller.
 * It has methods for doing everything a player can do in the game.
 */
public interface UserInterface {
    //LeaderCards handling actions

    /**
     * Allows the player to choose which leader cards to keep (after choosing two the rest are discarded)
     *
     * @param number the number of the leaderCard to choose
     */
    void chooseLeaderCard(int number);

    /**
     * Allows the player to activate the leader card corresponding to the given number
     *
     * @param number the number of the leaderCard to activate
     */
    void playLeaderCard(int number);

    /**
     * Allows the player to discard the leader card corresponding to the given number
     *
     * @param number the number of the leaderCard to discard
     */
    void discardLeaderCard(int number);

    //Market selection actions

    /**
     * Allows the player to select a row or column from the market and take its resources
     * @param marketScope distinguishes between selecting a row or column
     * @param numScope the index of the selected row or column
     */
    void selectFromMarket(MarketScope marketScope, int numScope);

    /**
     * Allows the player to send a resource obtained from the market to a specific depot
     * @param depotNumber the number of the depot to which to send the resource
     * @param resource the resource to send to the depot
     * @param quantity the amount of resource to send
     */
    void sendResourceToDepot(int depotNumber, Resource resource, int quantity);

    /**
     * Allows the player to choose to convert a white marble resource into one from their conversions list
     * @param resource the resource into which to convert the white marble
     * @param quantity the amount of resource to convert
     */
    void chooseMarbleConversion(Resource resource, int quantity);

    //DevelopmentCard purchasing actions

    /**
     * Allows the player to buy a development card from the cardTable
     *
     * @param cardColor the color of the card to buy
     * @param row the card table row from which to buy the card
     * @param slot the car slot in which to put the card
     */
    void buyDevelopmentCard(CardColor cardColor, int row, int slot);

    /**
     * Allows the player to pay the development card cost by taking resources from the given depot in the warehouse
     * @param depotNumber the number of the depot from which to take the resource
     * @param resource the resource to take
     * @param quantity the amount of resource to take (and of cost to pay)
     */
    void takeResourceFromWarehouseCard(int depotNumber, Resource resource, int quantity);

    /**
     * Allows the player to pay the development card cost by taking resources from the strongbox

     * @param resource the resource to take
     * @param quantity the amount of resource to take (and of cost to pay)
     */
    void takeResourceFromStrongboxCard(Resource resource, int quantity);

    //Production selection actions

    /**
     * Allows the player to select a production for activation
     * @param number the number of the production
     */
    void selectProduction (int number);

    /**
     * Allows the player to reset the selected productions
     */
    void resetProductionChoice ();

    /**
     * Allows the player to confirm the selected production for activation
     */
    void confirmProductions ();

    /**
     * Allows the player to choose into which resource to turn a jolly in the production's input
     * @param resource the resource into which to turn the jolly
     */
    void chooseJollyInput(Resource resource);

    /**
     * Allows the player to choose into which resource to turn a jolly in the production's output
     * @param resource the resource into which to turn the jolly
     */
    void chooseJollyOutput(Resource resource);

    /**
     * Allows the player to pay the production cost by taking resources from the given depot in the warehouse
     * @param depotNumber the number of the depot from which to take the resource
     * @param resource the resource to take
     * @param quantity the amount of resource to take (and of cost to pay)
     */
    void takeResourceFromWarehouseProduction(int depotNumber, Resource resource, int quantity);

    /**
     * Allows the player to pay the production cost by taking resources from the strongbox

     * @param resource the resource to take
     * @param quantity the amount of resource to take (and of cost to pay)
     */
    void takeResourceFromStrongboxProduction(Resource resource, int quantity);

    //End turn actions

    /**
     * Allows the player to end their current turn
     */
    void endTurn();
}
