package model.resource;

import model.PlayerBoard;
import model.ResourceType;

public class ResourceUnknown extends Resource {
    private final ResourceType type;

    public ResourceUnknown() {
        type = ResourceType.UNKNOWN;
    }

    public ResourceType getType(){
        return type;
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
