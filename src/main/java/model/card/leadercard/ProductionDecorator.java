package model.card.leadercard;

import model.PlayerBoard;
import model.card.DevelopmentCard;
import model.Production;

import java.util.HashMap;
import java.util.Map;

/**
 * Decorator that adds the Production power to a generic LeaderCard.
 */
public class ProductionDecorator extends LeaderCardDecorator {
    private Production production;
    private Map<DevelopmentCard, Integer> requiredCards = new HashMap<>();
    private Map<DevelopmentCard, Integer> requiredLevels = new HashMap<>();

    public ProductionDecorator(LeaderCard leaderCard) {
        super(leaderCard);

    }

    private void addProduction(PlayerBoard playerBoard){
        //TO TEST
        playerBoard.addProduction(production);
    }

    @Override
    public void doAction(PlayerBoard playerBoard){
        addProduction(playerBoard);
    }

    @Override
    public boolean areRequirementsMet(PlayerBoard playerBoard){
        return false;
    }
}
