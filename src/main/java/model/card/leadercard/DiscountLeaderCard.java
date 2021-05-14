package model.card.leadercard;

import model.CardColor;
import model.PlayerBoard;
import model.ResourceType;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This LeaderCard awards the user with a discount when buying DevelopmentCards upon activation
 */
public class DiscountLeaderCard extends LeaderCard {
    /**
     * This attribute stores the type of resource cost that the discount applies to
     */
    private final ResourceType discountType;
    /**
     * This attribute stores the amount by which the resource gets discounted
     */
    private final int discount;
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

    //PUBLIC METHODS

    /**
     * Calls the specific method for this LeaderCard, applyDiscount()
     *
     * @param playerBoard specifies to which PlayerBoard the discount has to be added
     */
    @Override
    public void doAction(PlayerBoard playerBoard) { /* this method should either be boolean or throw an exception */
        activate();
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

    //PRIVATE METHODS

    /**
     * Adds the specified discount to the specified PlayerBoard
     *
     * @param playerBoard specifies which player has activated the discount
     */
    private void applyDiscount(PlayerBoard playerBoard) {
        playerBoard.addDiscount(discountType, discount);
    }

    //GETTERS

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

    @Override
    public String toString() {
        return "\n\u001B[32;1mDiscountLeaderCard:\u001B[0m" +
                "\n discountType: " + discountType +
                "\n discount: " + discount +
                "\n requiredColors: " + Arrays.toString(requiredColors) +
                "\n requiredQuantities: " + Arrays.toString(requiredQuantities) +
                '\n';
    }
}
