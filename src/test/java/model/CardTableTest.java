package model;

import Exceptions.EmptyDeckException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTableTest {
    @Test
    void constructor(){
        CardTable cardTable = new CardTable();
    }

    @Test
    void getYellowCards() {
        CardTable cardTable = new CardTable();
        System.out.println(cardTable.getYellowCards().get(0).get(0).getLevel());
    }

    @Test
    void discardTop() throws EmptyDeckException {
        CardTable cardtable = new CardTable();
        cardtable.discardTop(CardColor.YELLOW);
    }
}