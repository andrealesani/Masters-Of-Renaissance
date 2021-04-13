package model.resource;

import model.PlayerBoard;
import model.ResourceType;

public class ResourceWhite extends Resource {
    private final ResourceType type;

    public ResourceWhite() {
        this.type = ResourceType.WHITEORB;
    }

    public ResourceType getType(){
        return type;
    }

    @Override
    public void addResourceFromMarket(PlayerBoard playerBoard) {
        playerBoard.addWhiteMarble();
    }

    @Override
    public void addResourceFromProduction(PlayerBoard playerBoard) {
        //does nothing
    }
}