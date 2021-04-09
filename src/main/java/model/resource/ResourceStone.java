package model.resource;

import model.PlayerBoard;
import model.ResourceType;

public class ResourceStone extends Resource {
    private final ResourceType type;

    public ResourceStone() {
        this.type = ResourceType.STONE;
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
