package model.resource;

import model.PlayerBoard;
import model.ResourceType;

public class ResourceShield extends Resource {

    public ResourceShield() {
        super(ResourceType.SHIELD);
    }

    @Override
    public void addResourceFromMarket(PlayerBoard playerBoard) {
        playerBoard.addResourceToWarehouse(ResourceType.SHIELD, 1);
    }

    @Override
    public void addResourceFromProduction(PlayerBoard playerBoard) {
        playerBoard.addResourceToStrongbox(ResourceType.SHIELD, 1);
    }
}
