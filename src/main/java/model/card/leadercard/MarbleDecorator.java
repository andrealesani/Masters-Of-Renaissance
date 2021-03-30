package model.card.leadercard;

import model.PlayerBoard;
import model.card.DevelopmentCard;
import model.resource.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 * Decorator that adds the Marble power to a generic LeaderCard
 */
public class MarbleDecorator extends LeaderCardDecorator {
    private Resource resource;
    private Map<DevelopmentCard, Integer> requiredCards = new HashMap<>();

    public MarbleDecorator(LeaderCard leaderCard) {
        super(leaderCard);
    }

    private void activateMarbleEffect(PlayerBoard playerBoard){
        //TODO
        System.out.println("I am Marble LeaderCard");
    }

    @Override
    public void doAction(PlayerBoard playerBoard){
        activateMarbleEffect(playerBoard);
    }

    @Override
    public boolean areRequirementsMet(PlayerBoard playerBoard){
        //TODO
        return false;
    }
}
