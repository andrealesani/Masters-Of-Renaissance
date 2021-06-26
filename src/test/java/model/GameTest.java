package model;

import Exceptions.*;
import model.card.DevelopmentCard;
import model.card.leadercard.*;
import model.lorenzo.Lorenzo;
import model.lorenzo.tokens.ActionToken;
import model.resource.*;
import model.storage.UnlimitedStorage;
import model.storage.Warehouse;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    // Verifies that card IDs are unique consistently between both DevelopmentCards and LeaderCards
    @Test
    void newIds() {
        Game game = new Game();
        for (Map.Entry<CardColor, List<List<DevelopmentCard>>> entry : game.getCardTable().getCards().entrySet()) {
            for (List<DevelopmentCard> deck : entry.getValue()) {
                for (DevelopmentCard card : deck) {
                    for (LeaderCard lc : game.getLeaderCards()) {
                        assertNotEquals(card.getId(), lc.getId());
                    }
                }
            }
        }
    }

    //END OF THE GAME TESTS

    @Test
    void lorenzoWinsFromFaith() throws WrongTurnPhaseException, LeaderNotPresentException {
        // Game creation
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Gigi");
        Game game = new Game(nicknames);
        PlayerBoard player = game.getPlayersBoardsTurnOrder().get(0);

        //Verify Lorenzo lives
        Lorenzo lollo = (Lorenzo) game.getLorenzo();
        List<ActionToken> activeTokens = lollo.getActiveDeck();
        List<ActionToken> usedTokens = lollo.getUsedDeck();
        assertTrue(lollo != null);

        //Player takes first turn
        assertEquals(player.getUsername(), game.getCurrentPlayer().getUsername());
        game.chooseLeaderCard(1);
        game.chooseLeaderCard(2);
        game.endTurn();

        //Lorenzo should not take first turn
        assertEquals(6, activeTokens.size());
        assertEquals(0, usedTokens.size());
        assertEquals(0, lollo.getFaith());

        //Player takes second turn
        game.selectMarketRow(1);
        int baseFaith = player.getLeftInWaitingRoom();
        //Lil cheat to make Lollo win faster
        lollo.addFaith(24);

        //Game should end
        game.endTurn();
    }

    @Test
    void lorenzoWinsFromNoMoreCards() throws WrongTurnPhaseException, LeaderNotPresentException {
        // Game creation
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Gigi");
        Game game = new Game(nicknames);
        PlayerBoard player = game.getPlayersBoardsTurnOrder().get(0);

        //Verify Lorenzo lives
        Lorenzo lollo = (Lorenzo) game.getLorenzo();
        List<ActionToken> activeTokens = lollo.getActiveDeck();
        List<ActionToken> usedTokens = lollo.getUsedDeck();
        assertTrue(lollo != null);

        //Player takes first turn
        assertEquals(player.getUsername(), game.getCurrentPlayer().getUsername());
        game.chooseLeaderCard(1);
        game.chooseLeaderCard(2);
        game.endTurn();

        //Lorenzo should not take first turn
        assertEquals(6, activeTokens.size());
        assertEquals(0, usedTokens.size());
        assertEquals(0, lollo.getFaith());

        //Player takes second turn
        game.selectMarketRow(1);
        int baseFaith = player.getLeftInWaitingRoom();
        //Lil cheat to force the game to end by removing all green cards from the table
        game.getCardTable().getGreenCards().get(2).clear();
        game.getCardTable().getGreenCards().get(1).clear();
        game.getCardTable().getGreenCards().get(0).clear();

        //Game should end
        game.endTurn();
    }

    @Test
    void playerWinsFrom7Cards() throws WrongTurnPhaseException, SlotNotValidException, NotEnoughResourceException, EmptyDeckException, LeaderNotPresentException {
        // Game creation
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);

        for (PlayerBoard player : game.getPlayersBoardsTurnOrder()) {
            Map<ResourceType, Integer> resources = new HashMap<>();
            resources.put(ResourceType.COIN, 100);
            resources.put(ResourceType.SERVANT, 100);
            resources.put(ResourceType.SHIELD, 100);
            resources.put(ResourceType.STONE, 100);
            player.addResourcesToStrongbox(resources);
        }
        // We gonna cheat again and add 6 cards to a player so that the next turn he will buy his 7th and end the game
        game.getCurrentPlayer().getCardSlots().get(1).add(new DevelopmentCard(5, 1, CardColor.GREEN, null, null, null, null, null, null));
        game.getCurrentPlayer().getCardSlots().get(1).add(new DevelopmentCard(5, 2, CardColor.GREEN, null, null, null, null, null, null));
        game.getCurrentPlayer().getCardSlots().get(1).add(new DevelopmentCard(5, 3, CardColor.GREEN, null, null, null, null, null, null));
        game.getCurrentPlayer().getCardSlots().get(2).add(new DevelopmentCard(5, 1, CardColor.GREEN, null, null, null, null, null, null));
        game.getCurrentPlayer().getCardSlots().get(2).add(new DevelopmentCard(5, 2, CardColor.GREEN, null, null, null, null, null, null));
        game.getCurrentPlayer().getCardSlots().get(2).add(new DevelopmentCard(5, 3, CardColor.GREEN, null, null, null, null, null, null));

        for (PlayerBoard player : game.getPlayersBoardsTurnOrder()) {
            game.chooseLeaderCard(1);
            game.chooseLeaderCard(2);
            player.clearWaitingRoom();
            game.endTurn();

            game.takeDevelopmentCard(CardColor.GREEN, 1, 1);
            for (ResourceType resourceType : game.getCurrentPlayer().getCardSlots().get(0).get(0).getCost()) {
                game.payFromStrongbox(resourceType.toResource(), 1);
            }

            // Game should end after the last player's turn
            game.endTurn();
        }
    }

    @Test
    void playerWinsFromFaith() throws WrongTurnPhaseException, SlotNotValidException, NotEnoughResourceException, EmptyDeckException, LeaderNotPresentException {
        // Game creation
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);

        for (PlayerBoard player : game.getPlayersBoardsTurnOrder()) {
            Map<ResourceType, Integer> resources = new HashMap<>();
            resources.put(ResourceType.COIN, 100);
            resources.put(ResourceType.SERVANT, 100);
            resources.put(ResourceType.SHIELD, 100);
            resources.put(ResourceType.STONE, 100);
            player.addResourcesToStrongbox(resources);
        }
        //We're gonna do what's called a 'pro-gamer-move' and add a bunch of faith to the first player
        game.getCurrentPlayer().addFaith(25);

        // SECOND TURN: every player chooses to buy a DevelopmentCard and pays for it
        for (PlayerBoard player : game.getPlayersBoardsTurnOrder()) {
            game.chooseLeaderCard(1);
            game.chooseLeaderCard(2);
            player.clearWaitingRoom();
            game.endTurn();

            game.takeDevelopmentCard(CardColor.GREEN, 1, 1);
            for (ResourceType resourceType : game.getCurrentPlayer().getCardSlots().get(0).get(0).getCost()) {
                game.payFromStrongbox(resourceType.toResource(), 1);
            }

            // Game should end after the last player's turn
            game.endTurn();
        }
    }

    //Checks the automatic payment of debts when ending one's turn
    @Test
    public void automaticPaymentTest() throws LeaderNotPresentException, WrongTurnPhaseException, DepotNotPresentException, NotEnoughSpaceException, WrongResourceInsertionException, BlockedResourceException, ProductionNotPresentException, NotEnoughResourceException, UndefinedJollyException {
        // Game creation
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);
        // FIRST TURN: player must choose which LeaderCards to keep
        game.chooseLeaderCard(1);
        game.chooseLeaderCard(2);
        game.endTurn();

        //Adds manually resources to warehouse
        PlayerBoard player = game.getCurrentPlayer();
        Warehouse warehouse = player.getWarehouse();
        warehouse.addToDepot(1, ResourceType.SHIELD, 1);
        warehouse.addToDepot(2, ResourceType.COIN, 2);
        warehouse.addToDepot(3, ResourceType.STONE, 3);

        //Adds manually resources to the strongbox
        UnlimitedStorage strongbox = player.getStrongbox();
        Map<ResourceType, Integer> resources = new HashMap<>();
        resources.put(ResourceType.SHIELD, 20);
        resources.put(ResourceType.STONE, 20);
        resources.put(ResourceType.SERVANT, 20);
        resources.put(ResourceType.COIN, 20);
        strongbox.addResources(resources);

        //Loads a production in the current player's board
        List<Resource> input1 = new ArrayList<>();
        input1.add(new ResourceShield());
        input1.add(new ResourceShield());
        input1.add(new ResourceStone());
        List<Resource> output1 = new ArrayList<>();
        output1.add(new ResourceFaith());
        player.addProduction(new Production(-1, input1, output1));


        //Activates the production
        game.selectProduction(2);

        //Confirm choice
        game.confirmProductionChoice();

        //Actually tests the method
        game.endTurn();

        //Verifies quantities left
        assertEquals(0, warehouse.getDepot(1).getNumOfResource(ResourceType.SHIELD));
        assertEquals(2, warehouse.getDepot(2).getNumOfResource(ResourceType.COIN));
        assertEquals(2, warehouse.getDepot(3).getNumOfResource(ResourceType.STONE));
        assertEquals(20, strongbox.getNumOfResource(ResourceType.STONE));
        assertEquals(20, strongbox.getNumOfResource(ResourceType.SERVANT));
        assertEquals(20, strongbox.getNumOfResource(ResourceType.COIN));
        assertEquals(19, strongbox.getNumOfResource(ResourceType.SHIELD));
        assertEquals(1, player.getFaith());
    }

    //CONNECTION METHODS

    @Test
    public void setConnectedStatusRandomPlayer() {
        // Game creation
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);

        //Disconnecting the second player bypassing the game's checks
        PlayerBoard player = game.getPlayersBoardsTurnOrder().get(1);
        player.setDisconnectedStatus();

        //Testing the method
        game.setConnectedStatus(player.getUsername());
        game.updateReconnectedPlayer(player.getUsername());

        assertTrue(game.isPlayerConnected(player.getUsername()));
    }

    @Test
    public void setDisconnectedStatus() {
        // Game creation
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);

        //Disconnecting first player traditionally
        PlayerBoard player = game.getCurrentPlayer();
        game.setDisconnectedStatus(player.getUsername());

        //Check the player is disconnected and the second player is now the current player
        assertFalse(player.isConnected());
        assertEquals(game.getPlayersUsernamesTurnOrder().get(1), game.getCurrentPlayer().getUsername());

        //Disconnecting all other players
        for (String username : game.getPlayersUsernamesTurnOrder())
            game.setDisconnectedStatus(username);

        //Checking all players disconnected and no current player
        for (PlayerBoard playerBoard : game.getPlayersBoardsTurnOrder())
            assertFalse(playerBoard.isConnected());
        assertNull(game.getCurrentPlayer());
    }

    @Test
    public void setConnectedStatusNoCurrentPlayer() {
        // Game creation
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);

        //Disconnecting the players
        for (String username : game.getPlayersUsernamesTurnOrder())
            game.setDisconnectedStatus(username);

        //Reconnecting a player
        game.setConnectedStatus("Tom");
        game.updateReconnectedPlayer("Tom");

        //Verifying only they are connected
        for (PlayerBoard playerBoard : game.getPlayersBoardsTurnOrder())
            if (playerBoard.getUsername().equals("Tom"))
                assertTrue(playerBoard.isConnected());
            else
                assertFalse(playerBoard.isConnected());

        //Verifying they are the new current player
        assertEquals("Tom", game.getCurrentPlayer().getUsername());

    }
}