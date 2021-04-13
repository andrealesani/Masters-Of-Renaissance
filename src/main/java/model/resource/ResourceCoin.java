package model.resource;

import model.PlayerBoard;
import model.ResourceType;

public class ResourceCoin extends Resource {


    public ResourceCoin() {
        super(ResourceType.COIN);
    }

    public void test () {
        System.out.println(getType());
    }
    @Override
    public void addResourceFromMarket(PlayerBoard playerBoard) {
        playerBoard.addResourceToWarehouse(getType(), 1);
    }

    @Override
    public void addResourceFromProduction(PlayerBoard playerBoard) {
        playerBoard.addResourceToWarehouse(ResourceType.COIN, 1);
    }


}
