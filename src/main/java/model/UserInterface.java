package model;

import model.resource.Resource;

/**
 * This interface indicates that the implementing class can be used as an interface for the player actions called by the controller.
 * It has methods for doing everything a player can do in the game.
 */
public interface UserInterface {
    //LeaderCards handling actions

    /**
     * Allows the user to choose which leader cards to keep (after choosing two the rest are discarded
     *
     * @param number the number of the leaderCard to choose
     */
    public void chooseLeaderCard(int number);

    public void playLeaderCard(int number);

    public void discardLeaderCard(int number);

    //Market selection actions

    public void selectFromMarket(MarketScope marketScope, int numScope);

    public void sendResourceToDepot(int depotNumber, Resource resource, int quantity);

    public void chooseMarbleConversion(Resource resource, int quantity);

    //DevelopmentCard purchasing actions

    public void buyDevelopmentCard(CardColor cardColor, int level, int slot);

    public void takeResourceFromWarehouseCard(int depotNumber, Resource resource, int quantity);

    public void takeResourceFromStrongboxCard(Resource resource, int quantity);

    //Production selection actions

    public void selectProduction (int number);

    public void resetProductionChoice ();

    public void chooseJollyInput(Resource resource);

    public void chooseJollyOutput(Resource resource);

    public void takeResourceFromWarehouseProduction(int depotNumber, Resource resource, int quantity);

    public void takeResourceFromStrongboxProduction(Resource resource, int quantity);

    //End turn actions

    public void endTurn();
}
