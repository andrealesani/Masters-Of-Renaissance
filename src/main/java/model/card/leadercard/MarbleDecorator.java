package model.card.leadercard;

import model.CardColor;
import model.PlayerBoard;
import model.card.DevelopmentCard;
import model.resource.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 * This LeaderCard lets the user convert a WhiteOrb to a specified Resource when he picks it from the Market
 */
public class MarbleDecorator extends LeaderCardDecorator {
    private final Resource resource;
    private final CardColor requiredColor;
    private final int requiredQuantity;

    /**
     * Constructor
     *
     * @param leaderCard - needed to implement the decorator Design Pattern
     * @param resource - Resource that the WhiteOrb can be transformed into by this card
     * @param requiredColor - CardColor of the DevelopmentCards required to activate this card
     * @param requiredQuantity - number of DevelopmentCards of the specified CardColor required to activate this card
     */
    public MarbleDecorator(LeaderCard leaderCard, Resource resource, CardColor requiredColor, int requiredQuantity) {
        super(leaderCard);
        this.resource = resource;
        this.requiredColor = requiredColor;
        this.requiredQuantity = requiredQuantity;
    }

    /**
     * Adds the specified marble conversion to the specified PlayerBoard
     *
     * @param playerBoard - specifies to which PlayerBoard the marble conversion has to be added
     */
    private void activateMarbleEffect(PlayerBoard playerBoard) {
        playerBoard.addMarbleConversion(resource);
    }

    /**
     * Calls the specific method for this LeaderCard, activateMarbleEffect()
     *
     * @param playerBoard - specifies to which PlayerBoard the discount has to be added
     */
    @Override
    public void doAction(PlayerBoard playerBoard) {
        activateMarbleEffect(playerBoard);
    }

    /**
     * Checks if the player has enough DevelopmentCards of the required ColorType
     *
     * @param playerBoard - specifies which PlayerBoard to check
     * @return - returns true if the requirements are met, false otherwise
     */
    @Override
    public boolean areRequirementsMet(PlayerBoard playerBoard) {
        if (playerBoard.getNumOfCards(requiredColor) >= requiredQuantity)
            return true;
        return false;
    }
}
