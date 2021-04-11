package model.lorenzo;

import model.PopeFavorTile;
import model.PopeTileState;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LorenzoTest {
    /**
     * This method tests the retrieval of Lorenzo's active deck
     */
    @Test
    void getActiveDeck() {

    }

    @Test
    void takeTurn() {
    }

    /**
     * This method tests the increasing of Lorenzo's faith
     */
    @Test
    void increaseFaith() {
        ArtificialIntelligence lorenzo = new Lorenzo(null, null);

        lorenzo.increaseFaith(13);
        lorenzo.increaseFaith(13);

        assertEquals(26, lorenzo.getFaith());
    }

    @Test
    void getNewTriggeredTile() {
        List<PopeFavorTile> tileList = new ArrayList<>();
        PopeFavorTile tile1 = new PopeFavorTile(0, 10, 0);
        tileList.add(tile1);
        ArtificialIntelligence lorenzo = new Lorenzo(null, tileList);

        assertEquals(0, lorenzo.getNewTriggeredTile(0));

        lorenzo.increaseFaith(5);

        assertEquals(0, lorenzo.getNewTriggeredTile(0));

        lorenzo.increaseFaith(5);

        assertEquals(1, lorenzo.getNewTriggeredTile(0));
    }

    @Test
    void shuffleDeck() {
    }
}