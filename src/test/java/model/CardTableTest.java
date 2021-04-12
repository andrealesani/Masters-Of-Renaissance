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
    void getGreenCards() {
        CardTable cardTable = new CardTable();
        System.out.println(cardTable.getGreenCards().get(0).get(0).getLevel());
        System.out.println(cardTable.getGreenCards().get(1).get(0).getLevel());
        System.out.println(cardTable.getGreenCards().get(2).get(0).getLevel());
        System.out.println(cardTable.getBlueCards().get(0).get(0).getLevel());
        System.out.println(cardTable.getBlueCards().get(1).get(0).getLevel());
        System.out.println(cardTable.getBlueCards().get(2).get(0).getLevel());
        System.out.println(cardTable.getYellowCards().get(0).get(0).getLevel());
        System.out.println(cardTable.getYellowCards().get(1).get(0).getLevel());
        System.out.println(cardTable.getYellowCards().get(2).get(0).getLevel());
        System.out.println(cardTable.getPurpleCards().get(0).get(0).getLevel());
        System.out.println(cardTable.getPurpleCards().get(1).get(0).getLevel());
        System.out.println(cardTable.getPurpleCards().get(2).get(0).getLevel());
    }

    @Test
    void discardTop() throws EmptyDeckException {
        CardTable cardtable = new CardTable();
        cardtable.discardTop(CardColor.YELLOW);
    }
}