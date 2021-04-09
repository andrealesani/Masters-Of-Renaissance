package model.resource;

import model.PlayerBoard;
import model.ResourceType;

public class ResourceFaith extends Resource {
    private final ResourceType type;

    public ResourceFaith() {
        type = ResourceType.FAITH;
    }

    @Override
    public void addResourceFromMarket(PlayerBoard playerBoard) {
        playerBoard.faithIncrease();
    }

    @Override
    public void addResourceFromProduction(PlayerBoard playerBoard) {
        playerBoard.faithIncrease();
    }
}
