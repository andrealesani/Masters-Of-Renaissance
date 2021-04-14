package model.resource;

import model.PlayerBoard;
import model.ResourceType;

public class ResourceWhite extends Resource {

    public ResourceWhite() {
        super(ResourceType.WHITEORB);
    }

    @Override
    public void addResourceFromMarket(PlayerBoard playerBoard) {
        playerBoard.addWhiteMarble(1);
    }

    @Override
    public void addResourceFromProduction(PlayerBoard playerBoard) {
        //does nothing
    }
}