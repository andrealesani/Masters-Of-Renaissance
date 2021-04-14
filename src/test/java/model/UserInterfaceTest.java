package model;

import Exceptions.NotEnoughResourceException;
import Exceptions.SlotNotValidException;
import Exceptions.WrongTurnPhaseException;
import model.card.DevelopmentCard;
import model.card.leadercard.LeaderCard;
import org.junit.jupiter.api.Test;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserInterfaceTest {

    // GIGI SECTION
    @Test
    void chooseLeaderCard() throws WrongTurnPhaseException {
        // Game creation
        List<String> nicknames = new ArrayList<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);
        // During first turn players must choose which LeaderCards to keep

        PlayerBoard currentPlayer = game.getCurrentPlayer();
        List<LeaderCard> listaLeaderCards = currentPlayer.getLeaderCards();
        List<LeaderCard> memoryList = new ArrayList(listaLeaderCards);

        game.chooseLeaderCard(1);
        game.chooseLeaderCard(3);
        game.endTurn();

        assertEquals(2, currentPlayer.getLeaderCards().size());
        
        assertEquals(memoryList.get(0), currentPlayer.getLeaderCards().get(0));
        assertEquals(memoryList.get(2), currentPlayer.getLeaderCards().get(1));

    }

    @Test
    void playLeaderCard() {

    }

    @Test
    void discardLeaderCard() throws WrongTurnPhaseException {
        // Game creation
        List<String> nicknames = new ArrayList<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);
        // During first turn players must choose which LeaderCards to keep
        for (PlayerBoard player : game.getPlayers()) {
            game.chooseLeaderCard(1);
            game.chooseLeaderCard(2);
            game.endTurn();
        }


    }

    @Test
    void selectFromMarket() {

    }

    // TOM SECTION
    @Test
    void sendResourceToDepot() {

    }

    @Test
    void chooseMarbleConversion() {

    }

    @Test
    void takeResourceFromWarehouseCard() {

    }

    @Test
    void takeResourceFromStrongboxCard() {

    }

    @Test
    void takeResourceFromWarehouseProduction() {

    }

    @Test
    void takeResourceFromStrongboxProduction() {

    }

    // ANDRE SECTION
    @Test
    void buyDevelopmentCard() throws SlotNotValidException, NotEnoughResourceException, WrongTurnPhaseException {
        // Game creation
        List<String> nicknames = new ArrayList<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);
        // During first turn players must choose which LeaderCards to keep
        for (PlayerBoard player : game.getPlayers()) {
            game.chooseLeaderCard(1);
            game.chooseLeaderCard(2);
            game.endTurn();
        }

        // TEST
        game.buyDevelopmentCard(CardColor.GREEN, 1, 1);
    }

    @Test
    void selectProduction() {

    }

    @Test
    void resetProductionChoice() {

    }

    @Test
    void confirmProductionChoice() {

    }

    @Test
    void chooseJollyInput() {

    }

    @Test
    void chooseJollyOutput() {

    }

    // END TURN
    @Test
    void endTurn() {

    }
}