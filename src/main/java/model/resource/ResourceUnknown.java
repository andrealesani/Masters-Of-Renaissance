package model.resource;

import model.PlayerBoard;
import model.ResourceType;

public class ResourceUnknown extends Resource {
    private final ResourceType type;

    public ResourceUnknown() {
        this.type = ResourceType.UNKNOWN;
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
