package it.polimi.ingsw.model.lorenzo;

import it.polimi.ingsw.model.CardTable;
import it.polimi.ingsw.model.PopeFavorTile;
import it.polimi.ingsw.model.lorenzo.ArtificialIntelligence;
import it.polimi.ingsw.model.lorenzo.Lorenzo;
import it.polimi.ingsw.model.lorenzo.tokens.ActionToken;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LorenzoTest {
    /**
     * This method tests lorenzo's turn
     */
    @Test
    void takeTurn() {
        Lorenzo lorenzo = new Lorenzo(new CardTable(0), new ArrayList<>());

        assertEquals(6, lorenzo.getActiveDeck().size());

        lorenzo.takeTurn();


    }

    /**
     * This method tests the increasing of Lorenzo's faith
     */
    @Test
    void increaseFaith() {
        ArtificialIntelligence lorenzo = new Lorenzo(new CardTable(0), new ArrayList<>());

        lorenzo.addFaith(13);
        lorenzo.addFaith(13);

        assertEquals(26, lorenzo.getFaith());
    }

    /**
     * This method tests lorenzo's ability to detect the necessity for a new vatican report based on his faith score
     */
    @Test
    void getNewTriggeredTile() {
        List<PopeFavorTile> tileList = new ArrayList<>();
        PopeFavorTile tile1 = new PopeFavorTile(0, 10, 1);
        tileList.add(tile1);
        ArtificialIntelligence lorenzo = new Lorenzo(new CardTable(0), tileList);

        assertEquals(0, lorenzo.getNewTriggeredTile(0));

        lorenzo.addFaith(5);

        assertEquals(0, lorenzo.getNewTriggeredTile(0));

        lorenzo.addFaith(5);

        assertEquals(1, lorenzo.getNewTriggeredTile(0));
    }

    /**
     * This method tests the restoration and shuffling of the active deck
     */
    @Test
    void shuffleDeck() {
        Lorenzo lorenzo = new Lorenzo(new CardTable(0), new ArrayList<>());
        int numOfTokens = lorenzo.getActiveDeck().size();
        List<ActionToken> firstList = new ArrayList<>(lorenzo.getActiveDeck());

        //lollo takes 3 turns
        lorenzo.takeTurn();
        lorenzo.takeTurn();
        lorenzo.takeTurn();

        //shuffle the deck
        lorenzo.shuffleDeck();

        assertEquals(numOfTokens, lorenzo.getActiveDeck().size());

        List<ActionToken> secondList = lorenzo.getActiveDeck();

        lorenzo.takeTurn();
        lorenzo.takeTurn();

        lorenzo.shuffleDeck();

        assertEquals(numOfTokens, lorenzo.getActiveDeck().size());


        boolean same = true;
        for (int i = 0; i < firstList.size(); i++) {
            if (firstList.get(i) != secondList.get(i)) {
                same = false;
            }
        }
        if (same) {
            fail();
        }
    }
}