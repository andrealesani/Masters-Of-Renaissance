package model.resource;

import model.PlayerBoard;
import model.ResourceType;

public class ResourceShield extends Resource {
    private final ResourceType type;

    public ResourceShield() {
        this.type = ResourceType.SHIELD;
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
