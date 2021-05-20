package model.card.leadercard;

import model.Color;
import model.PlayerBoard;
import model.ResourceType;
import model.resource.Resource;
import model.resource.ResourceShield;
import model.storage.LeaderDepot;
import model.storage.ResourceDepot;

import java.util.HashMap;
import java.util.Map;

/**
 * This LeaderCard awards the user with a new Leader Depot in his warehouse upon activation
 */
public class DepotLeaderCard extends LeaderCard {
    /**
     * This attribute stores the card's required resource for activation
     */
    private final ResourceType requiredResource;
    /**
     * This attribute stores the card's required quantity for the resource
     */
    private final int requiredQuantity;
    /**
     * This attribute stores the resource type that can be stored by the card's depot
     */
    private final ResourceType storableResource;
    /**
     * This attribute stores the maximum capacity of the card's depot
     */
    private final int storableQuantity;

    //CONSTRUCTORS

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

    //PUBLIC METHODS

    /**
     * Calls the specific method for this LeaderCard, activateSpecialDepot()
     *
     * @param playerBoard specifies to which PlayerBoard the discount has to be added
     */
    @Override
    public void doAction(PlayerBoard playerBoard) { /* this method should either be boolean or throw an exception */
        activate();
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

    //PRIVATE METHODS

    /**
     * Adds a SpecialDepot with the parameters specified by the card to the specified PlayerBoard
     *
     * @param playerBoard specifies to which PlayerBoard the SpecialDepot has to be added
     */
    private void activateSpecialDepot(PlayerBoard playerBoard) {
        playerBoard.addNewDepot(new LeaderDepot(storableQuantity, storableResource));
    }

    //GETTERS

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

    public String gigi(){
        String content = "";
        content += "\n\u001B[32;1mDepotLeaderCard:\u001B[0m";
        content += super.toString();
        content += "\n\n required resources: ";


        return content;
    }

    @Override
    public String toString() {
        String content = "";
        content += Color.HEADER + "Depot Leader Card:" + Color.RESET +
                super.toString();

        content += "\n Required resources: ";
        content += " " + requiredResource.iconPrint() + " x " + requiredQuantity + "  ";

        content += "\n Depot: ";
        content += "[";

        for (int j = 0; j < storableQuantity; j++) {
                content += Color.RESOURCE_STD + "□" + Color.RESET;
        }

        content += "(" + storableResource.iconPrint() + ")";
        content += "]";

        return content;
    }
}


