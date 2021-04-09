package model.resource;

import model.PlayerBoard;
import model.ResourceType;

public class ResourceWhite extends Resource {
    private final ResourceType type;

    public ResourceWhite() {
        this.type = ResourceType.WHITEORB;
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