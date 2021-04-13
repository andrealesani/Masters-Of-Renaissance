package model.resource;

import model.PlayerBoard;
import model.ResourceType;

public class ResourceCoin extends Resource {
    private final ResourceType type;

    public ResourceCoin() {
        type = ResourceType.COIN;
    }

    public ResourceType getType(){
        return type;
    }

    @Override
    public void addResourceFromMarket(PlayerBoard playerBoard) {
        playerBoard.addResourceToWarehouse(type, 1);
    }

    @Override
    public void addResourceFromProduction(PlayerBoard playerBoard) {
        playerBoard.addResourceToWarehouse(type, 1);
    }


}
