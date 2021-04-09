package model.card.leadercard;

import model.CardColor;
import model.PlayerBoard;
import model.ResourceType;
import model.resource.Resource;

/**
 * This LeaderCard grants to the user a discount when buying DevelopmentCards
 */
public class DiscountDecorator extends LeaderCardDecorator {
    private final ResourceType resourceType;
    private final int discount;
    private final CardColor requiredColor;
    private final int requiredQuantity;

    /**
     * Constructor
     *
     * @param leaderCard       needed to implement the decorator Design Pattern
     * @param resourceType     Resource that gets discounted by this card
     * @param discount         number of Resources that get discounted to the player
     * @param requiredColor    CardColor of the DevelopmentCards required to activate this card
     * @param requiredQuantity number of DevelopmentCards of the specified CardColor required to activate this card
     */
    public DiscountDecorator(LeaderCard leaderCard, ResourceType resourceType, int discount, CardColor requiredColor, int requiredQuantity) {
        super(leaderCard);
        this.resourceType = resourceType;
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
        playerBoard.addDiscount(resourceType, discount);
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

    /**
     * Getter TESTING ONLY
     *
     * @return the type of the resource that gets discounted by activating this leader card
     */
    public ResourceType getResourceType() {
        return resourceType;
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
    public CardColor getRequiredColor() {
        return requiredColor;
    }

    /**
     * Getter TESTING ONLY
     *
     * @return the number of development cards of the specified color required to activate this leader card
     */
    public int getRequiredQuantity() {
        return requiredQuantity;
    }
}
