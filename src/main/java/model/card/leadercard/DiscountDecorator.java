package model.card.leadercard;

import model.card.DevelopmentCard;
import model.resource.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 * Decorator that adds the Discount power to a generic LeaderCard
 */
public class DiscountDecorator extends LeaderCardDecorator {
    private Map<Resource, Integer> discount = new HashMap<>();
    private Map<DevelopmentCard, Integer> requiredCards = new HashMap<>();

    public DiscountDecorator(LeaderCard leaderCard) {
        super(leaderCard);
    }

    private void applyDiscount(){
        //TODO
        System.out.println("I am Discount LeaderCard");
    }

    @Override
    public void doAction(){ /* this method should either be boolean or throw an exception */
        applyDiscount();
    }

    @Override
    public boolean areRequirementsMet(){
        //TODO
        return false;
    }
}
