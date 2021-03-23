package model.card.leadercard;

import model.card.DevelopmentCard;
import model.resource.Resource;

import java.util.HashMap;
import java.util.Map;

public class MarbleDecorator extends LeaderCardDecorator {
    private Resource resource;
    private Map<DevelopmentCard, Integer> requiredCards = new HashMap<>();

    public MarbleDecorator(LeaderCard leaderCard) {
        super(leaderCard);
    }

    private void activateMarbleEffect(){
        //TODO
        System.out.println("Skusi sto postando cringe");
    }

    @Override
    public void doAction(){
        activateMarbleEffect();
    }
}
