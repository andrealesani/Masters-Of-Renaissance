package model.card.leadercard;

import model.PlayerBoard;
import model.ResourceType;
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
    private final ResourceType resourceType;
    private final int resourceQuantity;
    private final ResourceDepot depot = new LeaderDepot();

    public DepotDecorator(LeaderCard leaderCard, ResourceType resourceType, int resourceQuantity) {
        super(leaderCard);
        this.resourceType = resourceType;
        this.resourceQuantity = resourceQuantity;
    }

    private void activateSpecialDepot(PlayerBoard playerBoard){
        //TO TEST
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
