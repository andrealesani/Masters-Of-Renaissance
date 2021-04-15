package model;

<<<<<<< Updated upstream
import Exceptions.*;
import model.card.DevelopmentCard;
=======
import Exceptions.NotEnoughResourceException;
import Exceptions.SlotNotValidException;
import Exceptions.WrongTurnPhaseException;
>>>>>>> Stashed changes
import model.card.leadercard.LeaderCard;
import model.resource.ResourceCoin;
import model.resource.ResourceShield;
import model.resource.ResourceStone;
import model.storage.LeaderDepot;
import model.storage.Warehouse;
import org.junit.jupiter.api.Test;
<<<<<<< Updated upstream
import java.beans.Transient;
=======

>>>>>>> Stashed changes
import java.util.ArrayList;
import java.util.List;

import static model.UtilsForModel.typeToResource;
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
        // FIRST TURN: players must choose which LeaderCards to keep

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
<<<<<<< Updated upstream

        PlayerBoard currentPlayer = game.getCurrentPlayer();
        List<LeaderCard> listaLeaderCards = currentPlayer.getLeaderCards();

        for (PlayerBoard player : game.getPlayers()) {
=======
        // FIRST TURN: players must choose which LeaderCards to keep
        for (PlayerBoard player : game.getPlayersTurnOrder()) {
>>>>>>> Stashed changes
            game.chooseLeaderCard(1);
            game.chooseLeaderCard(2);
            game.endTurn();
        }

        List<LeaderCard> memoryList = new ArrayList(listaLeaderCards);
        int preFaith = currentPlayer.getFaith();
        game.discardLeaderCard(1);

        assertEquals(memoryList.size()-1, currentPlayer.getLeaderCards().size());
        assertEquals(memoryList.get(0), currentPlayer.getLeaderCards().get(0));
        assertEquals(preFaith+1, currentPlayer.getFaith());

    }

    @Test
    void selectFromMarket() {

    }

    // TOM SECTION
    @Test
    void sendResourceToDepotCorrect() throws WrongTurnPhaseException, NotEnoughSpaceException, WrongResourceTypeException, BlockedResourceException, NotEnoughResourceException, DepotNotPresentException {
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

        //Adds manually a leader depot and some resources to waiting room
        PlayerBoard player = game.getCurrentPlayer();
        player.addNewDepot(new LeaderDepot(2, ResourceType.SHIELD));
        player.addResourceToWarehouse(ResourceType.SHIELD, 3);
        player.addResourceToWarehouse(ResourceType.COIN, 1);
        player.addResourceToWarehouse(ResourceType.STONE, 1);

        //to change game phase to MARKETDISTRIBUTION
        game.selectFromMarket(MarketScope.ROW, 1);

        //Test the actual method
        game.sendResourceToDepot(1, new ResourceCoin(), 1);
        game.sendResourceToDepot(2, new ResourceShield(), 2);
        game.sendResourceToDepot(3, new ResourceStone(), 1);
        game.sendResourceToDepot(4, new ResourceShield(), 1);

        //Verify resources got to the correct depots
        Warehouse warehouse = player.getWarehouse();
        assertEquals(1, warehouse.getDepot(1).getNumOfResource(ResourceType.COIN));
        assertEquals(2, warehouse.getDepot(2).getNumOfResource(ResourceType.SHIELD));
        assertEquals(1, warehouse.getDepot(3).getNumOfResource(ResourceType.STONE));
        assertEquals(1, warehouse.getDepot(4).getNumOfResource(ResourceType.SHIELD));
    }

    @Test
    void chooseMarbleConversion() throws WrongTurnPhaseException, ConversionNotAvailableException, NotEnoughResourceException, WrongResourceTypeException, BlockedResourceException, NotEnoughSpaceException, DepotNotPresentException {
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

        //Manually add conversions and white marbles
        PlayerBoard player = game.getCurrentPlayer();
        player.addMarbleConversion(ResourceType.SHIELD);
        player.addMarbleConversion(ResourceType.COIN);
        player.addMarbleConversion(ResourceType.STONE);
        player.addWhiteMarble(6);

        //To change game phase to MARKETDISTRIBUTION
        game.selectFromMarket(MarketScope.ROW, 1);

        //Actually tests the method
        game.chooseMarbleConversion(new ResourceShield(), 2);
        game.chooseMarbleConversion(new ResourceStone(), 3);
        game.chooseMarbleConversion(new ResourceCoin(), 1);

        //verifies resources are present in waiting room
        assertTrue(player.leftInWaitingRoom() >= 6);

        game.sendResourceToDepot(1, new ResourceCoin(), 1);
        game.sendResourceToDepot(2, new ResourceShield(), 2);
        game.sendResourceToDepot(3, new ResourceStone(), 3);

        //Verify resources got to the correct depots
        Warehouse warehouse = player.getWarehouse();
        assertEquals(1, warehouse.getDepot(1).getNumOfResource(ResourceType.COIN));
        assertEquals(2, warehouse.getDepot(2).getNumOfResource(ResourceType.SHIELD));
        assertEquals(3, warehouse.getDepot(3).getNumOfResource(ResourceType.STONE));

        //verifies quantity of resources in waiting room
        assertTrue(player.leftInWaitingRoom() <=4);
    }

    @Test
    void swapDepotContent() {

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
        // FIRST TURN: players must choose which LeaderCards to keep
        for (PlayerBoard player : game.getPlayersTurnOrder()) {
            game.chooseLeaderCard(1);
            game.chooseLeaderCard(2);
            game.endTurn();
        }

        // TEST
        // NB This test only checks that the card is taken, it doesn't check the payment phase (that is checked in the next test)
        assertEquals(4, game.getCardTable().getGreenCards().get(2).size());
        game.buyDevelopmentCard(CardColor.GREEN, 1, 1);
        assertEquals(3, game.getCardTable().getGreenCards().get(2).size());
        assertEquals(1, game.getCurrentPlayer().getCardSlots().get(0).size());
    }

    @Test
    void selectProduction() throws WrongTurnPhaseException, SlotNotValidException, NotEnoughResourceException {
        // Game creation
        List<String> nicknames = new ArrayList<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);
        // FIRST TURN: players must choose which LeaderCards to keep
        for (PlayerBoard player : game.getPlayersTurnOrder()) {
            game.chooseLeaderCard(1);
            game.chooseLeaderCard(2);
            game.endTurn();
        }
        // We gonna cheat and add some Resources to all players so that they can buy cards without waiting 100 turns
        for (PlayerBoard player : game.getPlayersTurnOrder()) {
            player.addResourceToStrongbox(ResourceType.COIN, 100);
            player.addResourceToStrongbox(ResourceType.SERVANT, 100);
            player.addResourceToStrongbox(ResourceType.SHIELD, 100);
            player.addResourceToStrongbox(ResourceType.STONE, 100);
        }
        // SECOND TURN: every player can only do one move out of 3 possible moves
        for (PlayerBoard player : game.getPlayersTurnOrder()) {
            game.buyDevelopmentCard(CardColor.GREEN, 1, 1);
            for (ResourceType resourceType: game.getCurrentPlayer().getCardSlots().get(0).get(0).getCost()) {
                game.takeResourceFromStrongboxCard(typeToResource(resourceType), 1);
            }
            game.endTurn();
        }

        // TEST
        game.selectProduction(0);
        assertEquals(1, game.getCurrentPlayer().getProductionHandler().getProductions().size());
        assertEquals(1, game.getCurrentPlayer().getProductionHandler().getSelectedProductions().size());

    }

    @Test
    void resetProductionChoice() throws WrongTurnPhaseException, SlotNotValidException, NotEnoughResourceException {
        // Game creation
        List<String> nicknames = new ArrayList<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);
        // FIRST TURN: players must choose which LeaderCards to keep
        for (PlayerBoard player : game.getPlayersTurnOrder()) {
            game.chooseLeaderCard(1);
            game.chooseLeaderCard(2);
            game.endTurn();
        }
        // We gonna cheat and add some Resources to all players so that they can buy cards without waiting 100 turns
        for (PlayerBoard player : game.getPlayersTurnOrder()) {
            player.addResourceToStrongbox(ResourceType.COIN, 100);
            player.addResourceToStrongbox(ResourceType.SERVANT, 100);
            player.addResourceToStrongbox(ResourceType.SHIELD, 100);
            player.addResourceToStrongbox(ResourceType.STONE, 100);
        }
        // SECOND TURN: every player can only do one move out of 3 possible moves
        for (PlayerBoard player : game.getPlayersTurnOrder()) {
            game.buyDevelopmentCard(CardColor.GREEN, 1, 1);
            for (ResourceType resourceType: game.getCurrentPlayer().getCardSlots().get(0).get(0).getCost()) {
                game.takeResourceFromStrongboxCard(typeToResource(resourceType), 1);
            }
            game.endTurn();
        }

        // The player now selects stupid Productions he doesn't actually want to activate
        game.selectProduction(0);
        assertEquals(1, game.getCurrentPlayer().getProductionHandler().getProductions().size());
        assertEquals(1, game.getCurrentPlayer().getProductionHandler().getSelectedProductions().size());

        // TEST
        game.resetProductionChoice();
        assertEquals(1, game.getCurrentPlayer().getProductionHandler().getProductions().size());
        assertEquals(0, game.getCurrentPlayer().getProductionHandler().getSelectedProductions().size());
        // The should now be able to do whatever he wants during the turn, he could even chose to buy a DevelopmentCard
        // coz he realized he's dumb and doesn't really want to activate that stupid Production
        game.buyDevelopmentCard(CardColor.YELLOW, 1, 2);

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
    void endTurnBasic() throws WrongTurnPhaseException {
        // Game creation
        List<String> nicknames = new ArrayList<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);
        List<PlayerBoard> players = game.getPlayers();
        // During first turn players must choose which LeaderCards to keep
        for (int i = 0; i<players.size(); i++) {
            assertEquals(players.get(i).getUsername(), game.getCurrentPlayer().getUsername());
            game.chooseLeaderCard(1);
            game.chooseLeaderCard(2);
            game.endTurn();
        }

        //Pass the turn just taking from market and discarding all resources
        for (int i = 0; i<players.size(); i++) {
            assertEquals(players.get(i).getUsername(), game.getCurrentPlayer().getUsername());
            game.selectFromMarket(MarketScope.ROW, 1);
            game.endTurn();
        }
    }
}