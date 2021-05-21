package model.card.leadercard;

import model.CardColor;
import model.Color;
import model.PlayerBoard;
import model.resource.ResourceType;

/**
 * This LeaderCard awards the user a conversion for white marbles to a specified Resource type (when they are picked from the Market) upon activation
 */
public class MarbleLeaderCard extends LeaderCard {
    /**
     * This attribute stores the resource type to which the white marble can be converted
     */
    private final ResourceType resourceType;
    /**
     * This array stores the development card colors required for activation
     */
    private final CardColor[] requiredColors;
    /**
     * This array stores the amounts required for each card color
     */
    private final int[] requiredQuantities;

    //CONSTRUCTORS

    /**
     * Constructor
     *
     * @param resourceType       Resource that the WhiteOrb can be transformed into by this card
     * @param requiredColors     CardColor of the DevelopmentCards required to activate this card
     * @param requiredQuantities number of DevelopmentCards of the specified CardColor required to activate this card
     */
    public MarbleLeaderCard(int victoryPoints, ResourceType resourceType, CardColor[] requiredColors, int[] requiredQuantities) {
        super(victoryPoints);
        this.resourceType = resourceType;
        this.requiredColors = requiredColors;
        this.requiredQuantities = requiredQuantities;
    }

    //PUBLIC METHODS

    /**
     * Calls the specific method for this LeaderCard, activateMarbleEffect()
     *
     * @param playerBoard specifies to which PlayerBoard the discount has to be added
     */
    @Override
    public void doAction(PlayerBoard playerBoard) {
        activate();
        activateMarbleEffect(playerBoard);
    }

    /**
     * Checks if the player has enough DevelopmentCards of the required ColorTypes
     *
     * @param playerBoard specifies which PlayerBoard to check
     * @return returns true if the requirements are met, false otherwise
     */
    @Override
    public boolean areRequirementsMet(PlayerBoard playerBoard) {
        for (int i = 0; i < requiredColors.length; i++) {
            if (playerBoard.getNumOfCards(requiredColors[i]) < requiredQuantities[i])
                return false;
        }
        return true;
    }

    //PRIVATE METHODS

    /**
     * Adds the specified marble conversion to the specified PlayerBoard
     *
     * @param playerBoard specifies to which PlayerBoard the marble conversion has to be added
     */
    private void activateMarbleEffect(PlayerBoard playerBoard) {
        playerBoard.addMarbleConversion(resourceType);
    }

    //GETTERS

    /**
     * Getter
     *
     * @return the ResourceType of the Resource to which the white marble can be converted
     */
    public ResourceType getResourceType() {
        return resourceType;
    }

    /**
     * Getter
     *
     * @return an array with the colors of the DevelopmentCards that the player needs to own in order to activate this LeaderCard
     */
    public CardColor[] getRequiredColors() {
        return requiredColors;
    }

    /**
     * Getter
     *
     * @return an array with the number of DevelopmentCards for each color that the player needs to own in order to activate this LeaderCard
     */
    public int[] getRequiredQuantities() {
        return requiredQuantities;
    }

    @Override
    public String toString() {
        String content = "";
        content += Color.HEADER + "Marble Leader Card:" + Color.RESET +
                super.toString();

        content += "\n Required cards: ";
        for (int i = 0; i < requiredColors.length; i++) {
            if (requiredQuantities[i] > 0)
                content += " " + requiredColors[i].iconPrint() + " x " + requiredQuantities[i] + "  ";
        }

        content += "\n Conversion: ";
        content += ResourceType.WHITEORB.iconPrint() + " -> " + resourceType.iconPrint();

        return content;
    }
}
