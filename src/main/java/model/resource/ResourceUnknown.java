package model.resource;

import model.PlayerBoard;
import model.ResourceType;

public class ResourceUnknown extends Resource {

    public ResourceUnknown() {
        super(ResourceType.UNKNOWN);
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
