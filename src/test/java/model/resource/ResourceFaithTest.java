package model.resource;

import model.PlayerBoard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceFaithTest {

    @Test
    void addResourceFromMarket() {
        PlayerBoard playerBoard = new PlayerBoard(null, null, 3, null);
        ResourceFaith faith = new ResourceFaith();

        faith.addResourceFromMarket(playerBoard);
        assertEquals(1, playerBoard.getFaith());

        faith.addResourceFromMarket(playerBoard);
        assertEquals(2, playerBoard.getFaith());
    }

    @Test
    void addResourceFromProduction() {
        PlayerBoard playerBoard = new PlayerBoard(null, null, 3, null);
        ResourceFaith faith = new ResourceFaith();

        faith.addResourceFromMarket(playerBoard);
        assertEquals(1, playerBoard.getFaith());

        faith.addResourceFromMarket(playerBoard);
        assertEquals(2, playerBoard.getFaith());
    }

}