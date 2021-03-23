package model.card.leadercard;

import model.card.DevelopmentCard;
import model.Production;

import java.util.HashMap;
import java.util.Map;

public class ProductionDecorator extends LeaderCardDecorator {
    private Production production;
    private Map<DevelopmentCard, Integer> requiredCards = new HashMap<>();
    private Map<DevelopmentCard, Integer> requiredLevels = new HashMap<>();

    private void addProduction(){
        //TODO
    }

    @Override
    public void doAction(){
        addProduction();
    }
}
