package model.card.leadercard;

import model.CardColor;
import model.PlayerBoard;
import model.ResourceType;

import java.util.ArrayList;

/**
 * This LeaderCard grants to the user a discount when buying DevelopmentCards
 */
public class DiscountLeaderCard extends LeaderCard {
    private final ResourceType discountType;
    private final int discount;
    private final CardColor[] requiredColors;
    private final int[] requiredQuantities;

    /**
     * Constructor
     *
     * @param discountType       Resource that gets discounted by this card
     * @param discount           number of Resources that get discounted to the player
     * @param requiredColors     CardColor of the DevelopmentCards required to activate this card
     * @param requiredQuantities number of DevelopmentCards of the specified CardColor required to activate this card
     */
    public DiscountLeaderCard(int victoryPoints, ResourceType discountType, int discount, CardColor[] requiredColors, int[] requiredQuantities) {
        super(victoryPoints);
        this.discountType = discountType;
        this.discount = discount;
        this.requiredColors = requiredColors;
        this.requiredQuantities = requiredQuantities;
    }

    /**
     * Adds the specified discount to the specified PlayerBoard
     *
     * @param playerBoard specifies which player has activated the discount
     */
    private void applyDiscount(PlayerBoard playerBoard) {
        playerBoard.addDiscount(discountType, discount);
    }

    /**
     * Calls the specific method for this LeaderCard, applyDiscount()
     *
     * @param playerBoard specifies to which PlayerBoard the discount has to be added
     */
    @Override
    public void doAction(PlayerBoard playerBoard) { /* this method should either be boolean or throw an exception */
        applyDiscount(playerBoard);
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

    /**
     * Getter TESTING ONLY
     *
     * @return the type of the resource that gets discounted by activating this leader card
     */
    public ResourceType getDiscountType() {
        return discountType;
    }

    /**
     * Getter TESTING ONLY
     *
     * @return the number of resources discounted when this leader card gets activated
     */
    public int getDiscount() {
        return discount;
    }

    /**
     * Getter TESTING ONLY
     *
     * @return the color of the development cards required to activate this leader card
     */
    public CardColor[] getRequiredColors() {
        return requiredColors;
    }

    /**
     * Getter TESTING ONLY
     *
     * @return the number of development cards of the specified color required to activate this leader card
     */
    public int[] getRequiredQuantities() {
        return requiredQuantities;
    }
}
