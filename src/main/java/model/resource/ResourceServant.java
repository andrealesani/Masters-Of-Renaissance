package model.resource;

import model.PlayerBoard;
import model.ResourceType;

public class ResourceServant extends Resource {

    public ResourceServant() {
        super(ResourceType.SERVANT);
    }

    @Override
    public void addResourceFromMarket(PlayerBoard playerBoard) {
        playerBoard.addResourceToWarehouse(ResourceType.SERVANT, 1);
    }

    @Override
    public void addResourceFromProduction(PlayerBoard playerBoard) {
        playerBoard.addResourceToWarehouse(ResourceType.SERVANT, 1);
    }
}
