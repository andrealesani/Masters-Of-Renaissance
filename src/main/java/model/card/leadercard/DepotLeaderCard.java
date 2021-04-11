package model.card.leadercard;

import model.PlayerBoard;
import model.ResourceType;
import model.resource.Resource;
import model.resource.ResourceShield;
import model.storage.LeaderDepot;
import model.storage.ResourceDepot;

import java.util.HashMap;
import java.util.Map;

/**
 * This LeaderCard lets the user have a new Special Depot in his warehouse
 */
public class DepotLeaderCard extends LeaderCard {
    private final ResourceType requiredResource;
    private final int requiredQuantity;
    private final ResourceType storableResource;
    private final int storableQuantity;

    /**
     * Constructor
     *
     * @param requiredResource ResourceType of the Resources required to activate this card
     * @param requiredQuantity number of Resources of the specified ResourceType required to activate this card
     */
    public DepotLeaderCard(int victoryPoints, ResourceType requiredResource, int requiredQuantity, ResourceType storableResource, int storableQuantity) {
        super(victoryPoints);
        this.requiredResource = requiredResource;
        this.requiredQuantity = requiredQuantity;
        this.storableResource = storableResource;
        this.storableQuantity = storableQuantity;
    }

    /**
     * Adds a SpecialDepot with the parameters specified by the card to the specified PlayerBoard
     *
     * @param playerBoard specifies to which PlayerBoard the SpecialDepot has to be added
     */
    private void activateSpecialDepot(PlayerBoard playerBoard) {
        playerBoard.getWarehouse().addNewDepot(new LeaderDepot(storableQuantity, storableResource));
    }

    /**
     * Calls the specific method for this LeaderCard, activateSpecialDepot()
     *
     * @param playerBoard specifies to which PlayerBoard the discount has to be added
     */
    @Override
    public void doAction(PlayerBoard playerBoard) { /* this method should either be boolean or throw an exception */
        activateSpecialDepot(playerBoard);
    }

    /**
     * Checks if the player has enough Resources of the required type
     *
     * @param playerBoard specifies which PlayerBoard to check
     * @return returns true if the requirements are met, false otherwise
     */
    @Override
    public boolean areRequirementsMet(PlayerBoard playerBoard) {
        if (playerBoard.getNumOfResource(requiredResource) >= requiredQuantity)
            return true;
        return false;
    }

    /**
     * Getter TESTING ONLY
     *
     * @return the Resource required to activate the card
     */
    public ResourceType getRequiredResource() {
        return requiredResource;
    }

    /**
     * Getter TESTING ONLY
     *
     * @return the quantity of the specified Resource required to activate the card
     */
    public int getRequiredQuantity() {
        return requiredQuantity;
    }

    /**
     * Getter TESTING ONLY
     *
     * @return the Resource of the depot
     */
    public ResourceType getStorableResource() {
        return storableResource;
    }

    /**
     * Getter TESTING ONLY
     *
     * @return the quantity of the specified Resource of the depot
     */
    public int getStorableQuantity() {
        return storableQuantity;
    }
}
