package model.card.leadercard;

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
    private Map<DevelopmentCard, Integer> requiredCards = new HashMap<>();

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
        //TODO
        return false;
    }
}
