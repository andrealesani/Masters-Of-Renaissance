package model.resource;

import model.PlayerBoard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceFaithTest {

    @Test
    void equals() {
        assertTrue(new ResourceFaith().equals(new ResourceFaith()));
        assertFalse(new ResourceFaith().equals(new ResourceServant()));
    }

    @Test
    void addResourceFromMarket() {
        PlayerBoard playerBoard = new PlayerBoard(null, null, 3, 100, 100, null, null, null);
        ResourceFaith faith = new ResourceFaith();

        faith.addResourceFromMarket(playerBoard);
        assertEquals(1, playerBoard.getFaith());

        faith.addResourceFromMarket(playerBoard);
        assertEquals(2, playerBoard.getFaith());
    }

    @Test
    void addResourceFromProduction() {
        PlayerBoard playerBoard = new PlayerBoard(null, null, 3, 100, 100, null, null, null);
        ResourceFaith faith = new ResourceFaith();

        faith.addResourceFromMarket(playerBoard);
        assertEquals(1, playerBoard.getFaith());

        faith.addResourceFromMarket(playerBoard);
        assertEquals(2, playerBoard.getFaith());
    }

}