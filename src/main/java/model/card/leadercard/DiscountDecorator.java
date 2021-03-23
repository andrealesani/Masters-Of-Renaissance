package model.card.leadercard;

import model.card.DevelopmentCard;
import model.Resource;

import java.util.HashMap;
import java.util.Map;

public class DiscountDecorator extends LeaderCardDecorator {
    private Map<Resource, Integer> discount = new HashMap<>();
    private Map<DevelopmentCard, Integer> requiredCards = new HashMap<>();

    public DiscountDecorator(LeaderCard leaderCard) {
        super(leaderCard);
    }

    private void applyDiscount(){
        //TODO
    }

    @Override
    public void doAction(){ /* this method should either be boolean or throw an exception */
        applyDiscount();
    }
}
