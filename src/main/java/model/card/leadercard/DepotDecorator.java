package model.card.leadercard;

import model.PlayerBoard;
import model.ResourceType;
import model.resource.Resource;
import model.storage.LeaderDepot;
import model.storage.ResourceDepot;

import java.util.HashMap;
import java.util.Map;

/**
 * This LeaderCard lets the user have a new Special Depot in his warehouse
 */
public class DepotDecorator extends LeaderCardDecorator {
    private final ResourceType requiredResource;
    private final int requiredQuantity;
    private final ResourceDepot depot = new LeaderDepot();

    /**
     * Constructor
     *
     * @param leaderCard - needed to implement the decorator Design Pattern
     * @param requiredResource - ResourceType of the Resources required to activate this card
     * @param requiredQuantity - number of Resources of the specified ResourceType required to activate this card
     */
    public DepotDecorator(LeaderCard leaderCard, ResourceType requiredResource, int requiredQuantity) {
        super(leaderCard);
        this.requiredResource = requiredResource;
        this.requiredQuantity = requiredQuantity;
    }

    /**
     * Adds the a SpecialDepot to the specified PlayerBoard
     *
     * @param playerBoard - specifies to which PlayerBoard the SpecialDepot has to be added
     */
    private void activateSpecialDepot(PlayerBoard playerBoard) {
        playerBoard.getWarehouse().addNewDepot(depot);
    }

    /**
     * Calls the specific method for this LeaderCard, activateSpecialDepot()
     *
     * @param playerBoard - specifies to which PlayerBoard the discount has to be added
     */
    @Override
    public void doAction(PlayerBoard playerBoard) { /* this method should either be boolean or throw an exception */
        activateSpecialDepot(playerBoard);
    }

    /**
     * Checks if the player has enough Resources of the required type
     *
     * @param playerBoard - specifies which PlayerBoard to check
     * @return - returns true if the requirements are met, false otherwise
     */
    @Override
    public boolean areRequirementsMet(PlayerBoard playerBoard) {
        if(playerBoard.getNumOfResource(requiredResource) >= requiredQuantity)
            return true;
        return false;
    }
}
