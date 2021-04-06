package model.card.leadercard;

import model.CardColor;
import model.PlayerBoard;
import model.resource.Resource;

/**
 * This LeaderCard grants to the user a discount when buying DevelopmentCards
 */
public class DiscountDecorator extends LeaderCardDecorator {
    private final Resource resource;
    private final int discount;
    private final CardColor requiredColor;
    private final int requiredQuantity;

    /**
     * Constructor
     *
     * @param leaderCard       needed to implement the decorator Design Pattern
     * @param resource         Resource that gets discounted by this card
     * @param discount         number of Resources that get discounted to the player
     * @param requiredColor    CardColor of the DevelopmentCards required to activate this card
     * @param requiredQuantity number of DevelopmentCards of the specified CardColor required to activate this card
     */
    public DiscountDecorator(LeaderCard leaderCard, Resource resource, int discount, CardColor requiredColor, int requiredQuantity) {
        super(leaderCard);
        this.resource = resource;
        this.discount = discount;
        this.requiredColor = requiredColor;
        this.requiredQuantity = requiredQuantity;
    }

    /**
     * Adds the specified discount to the specified PlayerBoard
     *
     * @param playerBoard specifies which player has activated the discount
     */
    private void applyDiscount(PlayerBoard playerBoard) {
        playerBoard.addDiscount(resource, discount);
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
     * Checks if the player has enough DevelopmentCards of the required ColorType
     *
     * @param playerBoard specifies which PlayerBoard to check
     * @return returns true if the requirements are met, false otherwise
     */
    @Override
    public boolean areRequirementsMet(PlayerBoard playerBoard) {
        if (playerBoard.getNumOfCards(requiredColor) >= requiredQuantity)
            return true;
        return false;
    }
}
