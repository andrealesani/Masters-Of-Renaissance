package model;

import Exceptions.NotEnoughResourceException;
import Exceptions.SlotNotValidException;
import Exceptions.WrongTurnPhaseException;
import model.card.DevelopmentCard;
import org.junit.jupiter.api.Test;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserInterfaceTest {

    // GIGI SECTION
    @Test
    void chooseLeaderCard() {

    }

    @Test
    void playLeaderCard() {

    }

    @Test
    void discardLeaderCard() {

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
        assertTrue(game.getCurrentPlayer().getLeaderCards().size() == 0);
        game.chooseLeaderCard(1);
        game.endTurn();

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