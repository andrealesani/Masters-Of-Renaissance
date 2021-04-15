package model.lorenzo;

import model.CardColor;
import model.CardTable;
import model.PopeFavorTile;
import model.lorenzo.tokens.ActionToken;
import model.lorenzo.tokens.DoubleFaithToken;
import model.lorenzo.tokens.RemoveCardsToken;
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
        Lorenzo lorenzo = new Lorenzo(new CardTable(), null);

        assertEquals(6, lorenzo.getActiveDeck().size());

        lorenzo.takeTurn();


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

    /**
     * This method tests lorenzo's ability to detect the necessity for a new vatican report based on his faith score
     */
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

    /**
     * This method tests the restoration and shuffling of the active deck
     */
    @Test
    void shuffleDeck() {
        Lorenzo lorenzo = new Lorenzo(new CardTable(), null);
        int numOfTokens = lorenzo.getActiveDeck().size();
        List<ActionToken> firstList = new ArrayList<>(lorenzo.getActiveDeck());

        //lollo takes 3 turns
        lorenzo.takeTurn();
        lorenzo.takeTurn();
        lorenzo.takeTurn();

        //shuffle the deck
        lorenzo.shuffleDeck();

        assertEquals (numOfTokens, lorenzo.getActiveDeck().size());

        List<ActionToken> secondList = lorenzo.getActiveDeck();

        lorenzo.takeTurn();
        lorenzo.takeTurn();

        lorenzo.shuffleDeck();

        assertEquals (numOfTokens, lorenzo.getActiveDeck().size());


        boolean same = true;
        for (int i=0; i<firstList.size(); i++) {
            if (firstList.get(i)!=secondList.get(i)) {
                same = false;
            }
        }
        if (same) {
            fail();
        }
    }
}