package model;

import model.card.DevelopmentCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerBoardTest {

    @Test
    void addResourceToWarehouse() {
    }

    @Test
    void addWhiteMarble() {
    }

    @Test
    void sendResourceToDepot() {
    }

    @Test
    void chooseMarbleConversion() {
    }

    @Test
    void addDevelopmentCard() {
        PlayerBoard playerBoard = new PlayerBoard();
        playerBoard.addDevelopmentCard(1, new DevelopmentCard(10, 1, CardColor.GREEN, null, null, null, null, null, null));
    }

    @Test
    void addResourceToStrongbox() {
    }

    @Test
    void addLeaderCard() {
    }

    @Test
    void playLeaderCard() {
    }

    @Test
    void addMarbleConversion() {
    }

    @Test
    void addProduction() {
    }

    @Test
    void addDiscount() {
    }

    @Test
    void discardLeaderCard() {
    }

    @Test
    void faithIncrease() {
    }

    @Test
    void theVaticanReport() {
    }

    @Test
    void getNumOfResource() {
    }

    @Test
    void getNumOfCards() {
    }

    @Test
    void testGetNumOfCards() {
    }

    @Test
    void leftInWaitingRoom() {
    }

    @Test
    void isGameEnding() {
    }
}