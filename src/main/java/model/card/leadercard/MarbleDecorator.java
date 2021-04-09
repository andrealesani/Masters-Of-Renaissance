package model.card.leadercard;

import model.CardColor;
import model.PlayerBoard;
import model.ResourceType;

import java.util.ArrayList;

/**
 * This LeaderCard lets the user convert a WhiteOrb to a specified Resource when he picks it from the Market
 */
public class MarbleDecorator extends LeaderCardDecorator {
    private final ResourceType resourceType;
    private final ArrayList<CardColor> requiredColors;
    private final int[] requiredQuantities;

    /**
     * Constructor
     *
     * @param leaderCard       needed to implement the decorator Design Pattern
     * @param resourceType     Resource that the WhiteOrb can be transformed into by this card
     * @param requiredColors    CardColor of the DevelopmentCards required to activate this card
     * @param requiredQuantities number of DevelopmentCards of the specified CardColor required to activate this card
     */
    public MarbleDecorator(LeaderCard leaderCard, ResourceType resourceType, ArrayList<CardColor> requiredColors, int[] requiredQuantities) {
        super(leaderCard);
        this.resourceType = resourceType;
        this.requiredColors = requiredColors;
        this.requiredQuantities = requiredQuantities;
    }

    /**
     * Adds the specified marble conversion to the specified PlayerBoard
     *
     * @param playerBoard specifies to which PlayerBoard the marble conversion has to be added
     */
    private void activateMarbleEffect(PlayerBoard playerBoard) {
        playerBoard.addMarbleConversion(resourceType);
    }

    /**
     * Calls the specific method for this LeaderCard, activateMarbleEffect()
     *
     * @param playerBoard specifies to which PlayerBoard the discount has to be added
     */
    @Override
    public void doAction(PlayerBoard playerBoard) {
        activateMarbleEffect(playerBoard);
    }

    /**
     * Checks if the player has enough DevelopmentCards of the required ColorType
     *
     * @param playerBoard specifies which PlayerBoard to check
     * @return returns true if the requirements are met, false otherwise
     */
    @Override
    public boolean areRequirementsMet(PlayerBoard playerBoard) {
        for (int i = 0; i < requiredColors.size(); i++) {
            if (playerBoard.getNumOfCards(requiredColors.get(i)) < requiredQuantities[i])
                return false;
        }
        return true;
    }
}
