package it.polimi.ingsw.model;

import it.polimi.ingsw.model.resource.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResourceTypeTest {

    /**
     * Tests the conversion of ResourceTypes to the corresponding Resources
     */
    @Test
    void toResource() {
        ResourceType stoneType = ResourceType.STONE;
        Resource stone = stoneType.toResource();
        assertEquals(new ResourceStone(), stone);

        ResourceType shieldType = ResourceType.SHIELD;
        Resource shield = shieldType.toResource();
        assertEquals(new ResourceShield(), shield);

        ResourceType servantType = ResourceType.SERVANT;
        Resource servant = servantType.toResource();
        assertEquals(new ResourceServant(), servant);

        ResourceType coinType = ResourceType.COIN;
        Resource coin = coinType.toResource();
        assertEquals(new ResourceCoin(), coin);

        ResourceType faithType = ResourceType.FAITH;
        Resource faith = faithType.toResource();
        assertEquals(new ResourceFaith(), faith);

        ResourceType jollyType = ResourceType.JOLLY;
        Resource jolly = jollyType.toResource();
        assertEquals(new ResourceJolly(), jolly);

        ResourceType whiteType = ResourceType.WHITEORB;
        Resource white = whiteType.toResource();
        assertEquals(new ResourceWhite(), white);
    }
}