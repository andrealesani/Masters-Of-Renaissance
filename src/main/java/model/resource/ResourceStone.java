package model.resource;

import model.PlayerBoard;
import model.ResourceType;

public class ResourceStone extends Resource {

    public ResourceStone() {
        super(ResourceType.STONE);
    }

    @Override
    public void addResourceFromMarket(PlayerBoard playerBoard) {
        playerBoard.addResourceToWarehouse(ResourceType.STONE, 1);
    }

    @Override
    public void addResourceFromProduction(PlayerBoard playerBoard) {
        playerBoard.addResourceToWarehouse(ResourceType.STONE, 1);
    }
}
