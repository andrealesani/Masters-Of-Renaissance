package model.card.leadercard;

import model.PlayerBoard;
import model.resource.Resource;
import model.storage.LeaderDepot;
import model.storage.ResourceDepot;

import java.util.HashMap;
import java.util.Map;

/**
 * Decorator that adds the Depot power to a generic LeaderCard
 */

//Nel costruttore bisogna specificare le requiredResources
public class DepotDecorator extends LeaderCardDecorator {
    private Map<Resource, Integer> requiredResources = new HashMap<>();
    private ResourceDepot depot = new LeaderDepot();

    public DepotDecorator(LeaderCard leaderCard) {
        super(leaderCard);
    }

    private void activateSpecialDepot(PlayerBoard playerBoard){
        //TODO
        System.out.println("I am Depot LeaderCard");

        playerBoard.getWarehouse().addNewDepot(depot);
    }

    @Override
    public void doAction(PlayerBoard playerBoard){ /* this method should either be boolean or throw an exception */
        activateSpecialDepot(playerBoard);
    }

    @Override
    public boolean areRequirementsMet(PlayerBoard playerBoard){
        //TODO
        return false;
    }
}
