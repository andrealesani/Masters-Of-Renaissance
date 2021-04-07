package model.resource;

import model.PlayerBoard;
import model.ResourceType;

public class ResourceServant extends Resource {
    private final ResourceType type;

    public ResourceServant() {
        this.type = ResourceType.SERVANT;
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
