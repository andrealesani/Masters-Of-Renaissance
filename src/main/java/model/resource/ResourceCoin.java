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
        //TODO
    }

    @Override
    public void addResourceFromProduction(PlayerBoard playerBoard) {
        //TODO
    }


}
