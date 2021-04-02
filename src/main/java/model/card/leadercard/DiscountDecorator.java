package model.card.leadercard;

import model.CardColor;
import model.PlayerBoard;
import model.resource.Resource;

/**
 * Decorator that adds the Discount power to a generic LeaderCard
 */
public class DiscountDecorator extends LeaderCardDecorator {
    private Resource resource;
    private int discount;
    private final CardColor requiredColor;
    private final int requiredQuantity;

    public DiscountDecorator(LeaderCard leaderCard, CardColor requiredColor, int requiredQuantity) {
        super(leaderCard);
        this.requiredColor = requiredColor;
        this. requiredQuantity = requiredQuantity;
    }

    private void applyDiscount(PlayerBoard playerBoard){
        playerBoard.addDiscount(resource, discount);
    }

    @Override
    public void doAction(PlayerBoard playerBoard){ /* this method should either be boolean or throw an exception */
        applyDiscount(playerBoard);
    }

    @Override
    public boolean areRequirementsMet(PlayerBoard playerBoard){
        //TO TEST
        if(playerBoard.getNumOfCards(requiredColor) >= requiredQuantity)
            return true;
        return false;
    }
}
