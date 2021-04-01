package model.card.leadercard;

import model.CardColor;
import model.PlayerBoard;
import model.ResourceType;
import model.card.DevelopmentCard;
import model.resource.Resource;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Decorator that adds the Discount power to a generic LeaderCard
 */
public class DiscountDecorator extends LeaderCardDecorator {
    private Resource resource;
    private int discount;
    private CardColor requiredColor;
    private int requiredQuantity;

    public DiscountDecorator(LeaderCard leaderCard) {
        super(leaderCard);
    }

    private void applyDiscount(PlayerBoard playerBoard){
        //TO TEST
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
