package Model;

import java.util.HashMap;
import java.util.Map;

public class DiscountDecorator extends LeaderCardDecorator{
    private Map<Resource, Integer> discount = new HashMap<>();
    private Map<DevelopmentCard, Integer> requiredCards = new HashMap<>();

    private void applyDiscount(){
        //TODO
    }

    @Override
    public void doAction(){ /* this method should either be boolean or throw an exception */
        applyDiscount();
    }
}
