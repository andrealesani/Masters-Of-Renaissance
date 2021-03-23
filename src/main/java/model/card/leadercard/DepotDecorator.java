package model.card.leadercard;

import model.Resource;

import java.util.HashMap;
import java.util.Map;

public class DepotDecorator extends LeaderCardDecorator {
    private Map<Resource, Integer> maxResource = new HashMap<>();
    private Map<Resource, Integer> numResource = new HashMap<>();
    private Map<Resource, Integer> requiredResources = new HashMap<>();

    private void activateSpecialDepot(){
        //TODO
    }

    @Override
    public void doAction(){ /* this method should either be boolean or throw an exception */
        activateSpecialDepot();
    }
}
