package it.polimi.ingsw.model;

import it.polimi.ingsw.model.PopeFavorTile;
import it.polimi.ingsw.model.PopeTileState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PopeFavorTileTest {

    /**
     * This method tests the triggering of an inactive tile
     */
    @Test
    void isTriggeredTrue() {
        PopeFavorTile tile = new PopeFavorTile(0, 15,5);

        assertEquals (PopeTileState.INACTIVE, tile.getState());

        assertTrue (tile.isTriggered(15));
        assertTrue (tile.isTriggered(600));
    }

    /**
     * This method tests the failure to trigger an inactive tile because of lack of faith in the one true god
     */
    @Test
    void isTriggeredFalseNotEnoughFaith() {
        PopeFavorTile tile = new PopeFavorTile(0, 15,5);

        assertFalse (tile.isTriggered(14));
    }

    /**
     * This method tests the failure to trigger an tile because it has already been activated
     */
    @Test
    void isTriggeredFalseAlreadyActive() {
        PopeFavorTile tile = new PopeFavorTile(0, 15,5);

        tile.checkActivation(11);

        assertFalse (tile.isTriggered(15));
    }

    /**
     * This method tests the failure to trigger an tile because it has already been discarded
     */
    @Test
    void isTriggeredFalseAlreadyDiscarded() {
        PopeFavorTile tile = new PopeFavorTile(0, 15,5);

        tile.checkActivation(10);

        assertFalse (tile.isTriggered(15));
    }

    /**
     * This method tests the activation of the tile
     */
    @Test
    void checkActivationActivate() {
        PopeFavorTile tile = new PopeFavorTile(0, 15,5);

        tile.checkActivation(11);

        assertEquals (PopeTileState.ACTIVE, tile.getState());
    }

    /**
     * This method tests the discarding of the tile
     */
    @Test
    void checkActivationDiscard() {
        PopeFavorTile tile = new PopeFavorTile(0, 15,5);

        tile.checkActivation(10);

        assertEquals (PopeTileState.DISCARDED, tile.getState());
    }

    /**
     * This method tests the attempt to discard a tile that has already been activated
     */
    @Test
    void checkActivationAlreadyActive() {
        PopeFavorTile tile = new PopeFavorTile(0, 15,5);

        tile.checkActivation(11);
        tile.checkActivation(10);

        assertEquals (PopeTileState.ACTIVE, tile.getState());
    }

    /**
     * This method tests the attempt to activate a tile that has already been discarded
     */
    @Test
    void checkActivationAlreadyDiscarded() {
        PopeFavorTile tile = new PopeFavorTile(0, 15,5);

        tile.checkActivation(10);
        tile.checkActivation(11);

        assertEquals (PopeTileState.DISCARDED, tile.getState());
    }

    /**
     * This method tests the victory points of an inactive tile
     */
    @Test
    void getVictoryPointsInactive() {
        PopeFavorTile tile = new PopeFavorTile(5, 15,5);

        assertEquals (0, tile.getActiveVictoryPoints());
    }

    /**
     * This method tests the victory points of an active tile
     */
    @Test
    void getVictoryPointsActive() {
        PopeFavorTile tile = new PopeFavorTile(5, 15,5);

        tile.checkActivation(11);

        assertEquals (5, tile.getActiveVictoryPoints());
    }

    /**
     * This method tests the victory points of a discarded tile
     */
    @Test
    void getVictoryPointsDiscarded() {
        PopeFavorTile tile = new PopeFavorTile(5, 15,5);

        tile.checkActivation(10);

        assertEquals (0, tile.getActiveVictoryPoints());
    }
}

