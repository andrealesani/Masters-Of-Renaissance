package model.resource;

import model.PlayerBoard;
import model.ResourceType;

public class ResourceCoin extends Resource {
    private final ResourceType type;

    public ResourceCoin() {
        this.type = ResourceType.COIN;
    }

    @Override
    public void addResourceFromMarket(PlayerBoard playerBoard) {
        playerBoard.addResourceToWarehouse(type, 1);
    }

    @Override
    public void addResourceFromProduction(PlayerBoard playerBoard) {
        //TODO
    }


}
