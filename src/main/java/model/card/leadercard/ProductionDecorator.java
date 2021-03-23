package model.card.leadercard;

import model.card.DevelopmentCard;
import model.Production;

import java.util.HashMap;
import java.util.Map;

public class ProductionDecorator extends LeaderCardDecorator {
    private Production production;
    private Map<DevelopmentCard, Integer> requiredCards = new HashMap<>();
    private Map<DevelopmentCard, Integer> requiredLevels = new HashMap<>();

    public ProductionDecorator(LeaderCard leaderCard) {
        super(leaderCard);
    }

    private void addProduction(){
        //TODO
        System.out.println("I am Production LeaderCard");
    }

    @Override
    public void doAction(){
        addProduction();
    }
}
