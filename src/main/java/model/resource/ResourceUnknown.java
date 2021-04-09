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
        //does nothing
    }

    @Override
    public void addResourceFromProduction(PlayerBoard playerBoard) {
        //does nothing
    }
}
