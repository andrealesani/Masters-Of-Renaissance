package it.polimi.ingsw.model;

import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.leadercard.*;
import it.polimi.ingsw.model.resource.*;
import it.polimi.ingsw.model.lorenzo.Lorenzo;
import it.polimi.ingsw.model.lorenzo.tokens.ActionToken;
import it.polimi.ingsw.model.lorenzo.tokens.DoubleFaithToken;
import it.polimi.ingsw.model.lorenzo.tokens.RemoveCardsToken;
import it.polimi.ingsw.model.storage.LeaderDepot;
import it.polimi.ingsw.model.storage.UnlimitedStorage;
import it.polimi.ingsw.model.storage.Warehouse;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class UserCommandsInterfaceTest {

    /**
     * Tests the homonymous UserCommandsInterface method, simulating an in-game player action
     */
    @Test
    void chooseBonusResourceType() throws LeaderNotPresentException, WrongTurnPhaseException, NotEnoughResourceException {
        // Game creation
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        nicknames.add("GeeGee");
        Game game = new Game(nicknames);

        //First player's turn
        PlayerBoard currentPlayer = game.getCurrentPlayer();
        List<LeaderCard> listLeaderCards = currentPlayer.getLeaderCards();
        List<LeaderCard> memoryList = new ArrayList(listLeaderCards);
        game.chooseLeaderCard(1);
        game.chooseLeaderCard(3);
        game.endTurn();
        game.selectMarketRow(1);
        game.endTurn();

        //Second player's turn
        currentPlayer = game.getCurrentPlayer();
        assertEquals(1, currentPlayer.getResourcesToConvert());
        assertEquals(0, currentPlayer.getWaitingRoom().getStoredResources().size());

        game.chooseBonusResourceType(new ResourceCoin(), 1);
        assertEquals(0, currentPlayer.getResourcesToConvert());
        assertEquals(1, currentPlayer.getWaitingRoom().getStoredResources().size());

        assertEquals(1, currentPlayer.getWaitingRoom().getNumOfResource(ResourceType.COIN));
        assertEquals(0, currentPlayer.getWaitingRoom().getNumOfResource(ResourceType.SHIELD));
        assertEquals(0, currentPlayer.getWaitingRoom().getNumOfResource(ResourceType.SERVANT));
        assertEquals(0, currentPlayer.getWaitingRoom().getNumOfResource(ResourceType.STONE));
    }

    /**
     * Tests the homonymous UserCommandsInterface method, simulating an in-game player action
     */
    @Test
    void chooseLeaderCard() throws WrongTurnPhaseException, LeaderNotPresentException {
        // Game creation
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);

        PlayerBoard currentPlayer = game.getCurrentPlayer();
        List<LeaderCard> listLeaderCards = currentPlayer.getLeaderCards();
        List<LeaderCard> memoryList = new ArrayList(listLeaderCards);

        game.chooseLeaderCard(1);
        game.chooseLeaderCard(3);
        game.endTurn();

        assertEquals(2, currentPlayer.getLeaderCards().size());

        assertEquals(memoryList.get(0), currentPlayer.getLeaderCards().get(0));
        assertEquals(memoryList.get(2), currentPlayer.getLeaderCards().get(1));
    }

    /**
     * Tests the homonymous UserCommandsInterface method, simulating an in-game player action
     */
    @Test
    void playLeaderCard() throws WrongTurnPhaseException, LeaderNotPresentException, CardIsActiveException {
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

        LeaderCard LeaderCard1 = game.getCurrentPlayer().getLeaderCards().get(0);

        //PRE-TEST

        int NumOFDepots = 0;

        if (LeaderCard1 instanceof DepotLeaderCard) {
            NumOFDepots = game.getCurrentPlayer().getWarehouse().getNumOfDepots();
        } else if (LeaderCard1 instanceof DiscountLeaderCard) {
            assertTrue(game.getCurrentPlayer().getDiscounts().isEmpty());
        } else if (LeaderCard1 instanceof MarbleLeaderCard) {
            assertTrue(game.getCurrentPlayer().getMarbleConversions().isEmpty());
            assertEquals(0, game.getCurrentPlayer().getMarbleConversions().size());
        } else if (LeaderCard1 instanceof ProductionLeaderCard) {
            assertEquals(1, game.getCurrentPlayer().getProductionHandler().getProductions().size());
        }

        //TEST

        LeaderCard1.doAction(game.getCurrentPlayer());

        //DEPOT

        if (LeaderCard1 instanceof DepotLeaderCard)
            assertEquals(NumOFDepots + 1, game.getCurrentPlayer().getWarehouse().getNumOfDepots());

            //DISCOUNT

        else if (LeaderCard1 instanceof DiscountLeaderCard)
            assertFalse(game.getCurrentPlayer().getDiscounts().isEmpty());

            //white
            //marbleconversion.get(0) , COIN

        else if (LeaderCard1 instanceof MarbleLeaderCard) {
            assertFalse(game.getCurrentPlayer().getMarbleConversions().isEmpty());
            assertEquals(1, game.getCurrentPlayer().getMarbleConversions().size());
            assertNotSame(ResourceType.JOLLY, game.getCurrentPlayer().getMarbleConversions().get(0));
            assertNotSame(ResourceType.FAITH, game.getCurrentPlayer().getMarbleConversions().get(0));

        }

        //production
        //game.getCurrentPlayer.getProductionHandler.getProductions.size(),  2

        else if (LeaderCard1 instanceof ProductionLeaderCard)
            assertEquals(2, game.getCurrentPlayer().getProductionHandler().getProductions().size());

    }

    /**
     * Tests the homonymous UserCommandsInterface method, simulating an in-game player action
     */
    @Test
    void discardLeaderCard() throws WrongTurnPhaseException, CardIsActiveException, LeaderNotPresentException {
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

        List<LeaderCard> memoryList = new ArrayList<>(game.getCurrentPlayer().getLeaderCards());
        int preFaith = game.getCurrentPlayer().getFaith();
        game.discardLeaderCard(1);

        assertEquals(memoryList.size() - 1, game.getCurrentPlayer().getLeaderCards().size());
        assertEquals(memoryList.get(1), game.getCurrentPlayer().getLeaderCards().get(0));
        assertEquals(preFaith + 1, game.getCurrentPlayer().getFaith());

    }

    /**
     * Tests the homonymous UserCommandsInterface method, simulating an in-game player action
     */
    @Test
    void selectMarketRow() throws WrongTurnPhaseException, LeaderNotPresentException {

        Set<String> nicknames = new HashSet<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);


        // FIRST TURN: player must choose which LeaderCards to keep
        game.chooseLeaderCard(1);
        game.chooseLeaderCard(2);
        game.endTurn();

        Resource[][] board = game.getMarket().getBoard();
        int numCoins = 0, numShields = 0, numServants = 0, numStones = 0, numFaithes = 0, numWhites = 0;

        for (int j = 0; j < 4; j++) {
            if (board[2][j] instanceof ResourceCoin)
                numCoins++;
            else if (board[2][j] instanceof ResourceFaith)
                numFaithes++;
            else if (board[2][j] instanceof ResourceServant)
                numServants++;
            else if (board[2][j] instanceof ResourceShield)
                numShields++;
            else if (board[2][j] instanceof ResourceStone)
                numStones++;
            else if (board[2][j] instanceof ResourceWhite)
                numWhites++;
        }

        //TEST
        game.selectMarketRow(3);

        assertEquals(numShields, game.getCurrentPlayer().getWaitingRoom().getNumOfResource(ResourceType.SHIELD));
        assertEquals(numCoins, game.getCurrentPlayer().getWaitingRoom().getNumOfResource(ResourceType.COIN));
        assertEquals(numStones, game.getCurrentPlayer().getWaitingRoom().getNumOfResource(ResourceType.STONE));
        assertEquals(numServants, game.getCurrentPlayer().getWaitingRoom().getNumOfResource(ResourceType.SERVANT));
        assertEquals(numFaithes, game.getCurrentPlayer().getFaith());

    }

    /**
     * Tests the homonymous UserCommandsInterface method, simulating an in-game player action
     */
    @Test
    void selectMarketColumn() throws WrongTurnPhaseException, LeaderNotPresentException {

        Set<String> nicknames = new HashSet<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);


        // FIRST TURN: player must choose which LeaderCards to keep
        game.chooseLeaderCard(1);
        game.chooseLeaderCard(2);
        game.endTurn();

        Resource[][] board = game.getMarket().getBoard();
        int numCoins = 0, numShields = 0, numServants = 0, numStones = 0, numFaithes = 0, numWhites = 0;

        for (int i = 0; i < 3; i++) {
            if (board[i][2] instanceof ResourceCoin)
                numCoins++;
            else if (board[i][2] instanceof ResourceFaith)
                numFaithes++;
            else if (board[i][2] instanceof ResourceServant)
                numServants++;
            else if (board[i][2] instanceof ResourceShield)
                numShields++;
            else if (board[i][2] instanceof ResourceStone)
                numStones++;
            else if (board[i][2] instanceof ResourceWhite)
                numWhites++;
        }

        //TEST
        game.selectMarketColumn(3);

        assertEquals(numShields, game.getCurrentPlayer().getWaitingRoom().getNumOfResource(ResourceType.SHIELD));
        assertEquals(numCoins, game.getCurrentPlayer().getWaitingRoom().getNumOfResource(ResourceType.COIN));
        assertEquals(numStones, game.getCurrentPlayer().getWaitingRoom().getNumOfResource(ResourceType.STONE));
        assertEquals(numServants, game.getCurrentPlayer().getWaitingRoom().getNumOfResource(ResourceType.SERVANT));
        assertEquals(numFaithes, game.getCurrentPlayer().getFaith());

    }

    /**
     * Tests the homonymous UserCommandsInterface method, simulating an in-game player action
     */
    @Test
    void sendResourceToDepot() throws WrongTurnPhaseException, NotEnoughSpaceException, WrongResourceInsertionException, BlockedResourceException, NotEnoughResourceException, DepotNotPresentException, LeaderNotPresentException {
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

        //Adds manually a leader depot and some resources to waiting room
        PlayerBoard player = game.getCurrentPlayer();
        player.addNewDepot(new LeaderDepot(2, ResourceType.SHIELD, 0));
        Map<ResourceType, Integer> resources = new HashMap<>();
        resources.put(ResourceType.SHIELD, 3);
        resources.put(ResourceType.COIN, 1);
        resources.put(ResourceType.STONE, 1);
        player.addResourcesToWaitingRoom(resources);

        //to change game phase to MARKETDISTRIBUTION
        game.selectMarketRow(1);

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

    /**
     * Tests the homonymous UserCommandsInterface method, simulating an in-game player action
     */
    @Test
    void chooseMarbleConversion() throws WrongTurnPhaseException, ConversionNotAvailableException, NotEnoughResourceException, WrongResourceInsertionException, BlockedResourceException, NotEnoughSpaceException, DepotNotPresentException, LeaderNotPresentException {
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

        //Manually add conversions and white marbles
        PlayerBoard player = game.getCurrentPlayer();
        player.addMarbleConversion(ResourceType.SHIELD);
        player.addMarbleConversion(ResourceType.COIN);
        player.addMarbleConversion(ResourceType.STONE);
        player.addWhiteMarble(6);

        //To change game phase to MARKETDISTRIBUTION
        game.selectMarketRow(1);

        //Actually tests the method
        game.chooseMarbleConversion(new ResourceShield(), 2);
        game.chooseMarbleConversion(new ResourceStone(), 3);
        game.chooseMarbleConversion(new ResourceCoin(), 1);

        //verifies resources are present in waiting room
        assertTrue(player.getLeftInWaitingRoom() >= 6);

        game.sendResourceToDepot(1, new ResourceCoin(), 1);
        game.sendResourceToDepot(2, new ResourceShield(), 2);
        game.sendResourceToDepot(3, new ResourceStone(), 3);

        //Verify resources got to the correct depots
        Warehouse warehouse = player.getWarehouse();
        assertEquals(1, warehouse.getDepot(1).getNumOfResource(ResourceType.COIN));
        assertEquals(2, warehouse.getDepot(2).getNumOfResource(ResourceType.SHIELD));
        assertEquals(3, warehouse.getDepot(3).getNumOfResource(ResourceType.STONE));

        //verifies quantity of resources in waiting room
        assertTrue(player.getLeftInWaitingRoom() <= 4);
    }

    /**
     * Tests the homonymous UserCommandsInterface method, simulating an in-game player action
     */
    @Test
    void swapDepotContent() throws WrongTurnPhaseException, BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException, DepotNotPresentException, SwapNotValidException, ParametersNotValidException, LeaderNotPresentException {
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

        //Adds manually resources to the depots
        PlayerBoard player = game.getCurrentPlayer();
        player.addNewDepot(new LeaderDepot(2, ResourceType.SHIELD, 0));
        Warehouse warehouse = player.getWarehouse();
        warehouse.addToDepot(1, ResourceType.SHIELD, 1);
        warehouse.addToDepot(2, ResourceType.COIN, 2);
        warehouse.addToDepot(3, ResourceType.STONE, 1);
        warehouse.addToDepot(4, ResourceType.SHIELD, 1);


        //actually tests the method
        game.swapDepotContent(2, 3);
        game.swapDepotContent(1, 2);
        game.swapDepotContent(2, 4);

        //Verify resources got to the correct depots
        assertEquals(1, warehouse.getDepot(1).getNumOfResource(ResourceType.STONE));
        assertEquals(1, warehouse.getDepot(2).getNumOfResource(ResourceType.SHIELD));
        assertEquals(2, warehouse.getDepot(3).getNumOfResource(ResourceType.COIN));
        assertEquals(1, warehouse.getDepot(4).getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * Tests the homonymous UserCommandsInterface method, simulating an in-game player action
     */
    @Test
    void moveDepotContent() throws WrongTurnPhaseException, BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException, DepotNotPresentException, SwapNotValidException, ParametersNotValidException, LeaderNotPresentException, NotEnoughResourceException {
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

        //Adds manually resources to the depots
        PlayerBoard player = game.getCurrentPlayer();
        player.addNewDepot(new LeaderDepot(2, ResourceType.SHIELD, 0));
        Warehouse warehouse = player.getWarehouse();
        warehouse.addToDepot(1, ResourceType.SHIELD, 1);
        warehouse.addToDepot(2, ResourceType.COIN, 1);
        warehouse.addToDepot(4, ResourceType.SHIELD, 1);


        //actually tests the method
        game.moveDepotContent(1, 4, new ResourceShield(), 1);
        game.moveDepotContent(2, 1, new ResourceCoin(), 1);

        //Verify resources got to the correct depots
        assertEquals(1, warehouse.getDepot(1).getNumOfResource(ResourceType.COIN));
        assertEquals(0, warehouse.getDepot(2).getStoredResources().size());
        assertEquals(0, warehouse.getDepot(3).getStoredResources().size());
        assertEquals(2, warehouse.getDepot(4).getNumOfResource(ResourceType.SHIELD));
    }

    /**
     * Tests the homonymous UserCommandsInterface method, simulating an in-game player action
     */
    @Test
    void takeDevelopmentCard() throws SlotNotValidException, NotEnoughResourceException, WrongTurnPhaseException, EmptyDeckException, LeaderNotPresentException {
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
        // We gonna cheat and add some Resources to all players so that they can buy cards without waiting 100 turns
        for (PlayerBoard player : game.getPlayersBoardsTurnOrder()) {
            Map<ResourceType, Integer> resources = new HashMap<>();
            resources.put(ResourceType.COIN, 100);
            resources.put(ResourceType.SERVANT, 100);
            resources.put(ResourceType.SHIELD, 100);
            resources.put(ResourceType.STONE, 100);
            player.addResourcesToStrongbox(resources);
        }

        // TEST STARTS HERE
        // This test only checks that the card is taken, it doesn't check the payment phase (that is checked in another test)

        assertEquals(4, game.getCardTable().getGreenCards().get(0).size());
        // SECOND TURN: first player chooses to buy a DevelopmentCard
        game.takeDevelopmentCard(CardColor.GREEN, 1, 1);
        assertEquals(3, game.getCardTable().getGreenCards().get(0).size());
        assertEquals(1, game.getCurrentPlayer().getCardSlots().get(0).size());
    }

    /**
     * Tests the homonymous UserCommandsInterface method, simulating an in-game player action
     */
    @Test
    void selectProduction() throws WrongTurnPhaseException, SlotNotValidException, NotEnoughResourceException, EmptyDeckException, ProductionNotPresentException, LeaderNotPresentException {
        // Game creation
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);

        // We gonna cheat and add some Resources to all players so that they can buy cards without waiting 100 turns
        for (PlayerBoard player : game.getPlayersBoardsTurnOrder()) {
            Map<ResourceType, Integer> resources = new HashMap<>();
            resources.put(ResourceType.COIN, 100);
            resources.put(ResourceType.SERVANT, 100);
            resources.put(ResourceType.SHIELD, 100);
            resources.put(ResourceType.STONE, 100);
            player.addResourcesToStrongbox(resources);
        }


        for (PlayerBoard player : game.getPlayersBoardsTurnOrder()) {
            game.chooseLeaderCard(1);
            game.chooseLeaderCard(2);
            player.clearWaitingRoom();
            game.endTurn();

            game.takeDevelopmentCard(CardColor.GREEN, 1, 1);
            for (ResourceType resourceType : game.getCurrentPlayer().getCardSlots().get(0).get(0).getCost()) {
                game.payFromStrongbox(resourceType.toResource(), 1);
            }
            game.endTurn();
        }


        // TEST STARTS HERE
        game.selectProduction(2);
        assertEquals(2, game.getCurrentPlayer().getProductionHandler().getProductions().size());
        assertEquals(1, game.getCurrentPlayer().getProductionHandler().getSelectedProductions().size());

    }

    /**
     * Tests the homonymous UserCommandsInterface method, simulating an in-game player action
     */
    @Test
    void resetProductionChoice() throws WrongTurnPhaseException, SlotNotValidException, NotEnoughResourceException, EmptyDeckException, ProductionNotPresentException, LeaderNotPresentException {
        // Game creation
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);

        // We gonna cheat and add some Resources to all players so that they can buy cards without waiting 100 turns
        for (PlayerBoard player : game.getPlayersBoardsTurnOrder()) {
            Map<ResourceType, Integer> resources = new HashMap<>();
            resources.put(ResourceType.COIN, 100);
            resources.put(ResourceType.SERVANT, 100);
            resources.put(ResourceType.SHIELD, 100);
            resources.put(ResourceType.STONE, 100);
            player.addResourcesToStrongbox(resources);
        }


        for (PlayerBoard player : game.getPlayersBoardsTurnOrder()) {
            game.chooseLeaderCard(1);
            game.chooseLeaderCard(2);
            player.clearWaitingRoom();
            game.endTurn();

            game.takeDevelopmentCard(CardColor.GREEN, 1, 1);
            for (ResourceType resourceType : game.getCurrentPlayer().getCardSlots().get(0).get(0).getCost()) {
                game.payFromStrongbox(resourceType.toResource(), 1);
            }
            game.endTurn();
        }

        // The first player now selects stupid Productions he doesn't actually want to activate
        game.selectProduction(1);
        game.selectProduction(2);
        assertEquals(2, game.getCurrentPlayer().getProductionHandler().getProductions().size());
        assertEquals(2, game.getCurrentPlayer().getProductionHandler().getSelectedProductions().size());

        // TEST STARTS HERE
        // The first player realizes how deficient his decision making is and wants to go bacc
        game.resetProductionChoice();
        assertEquals(2, game.getCurrentPlayer().getProductionHandler().getProductions().size());
        assertEquals(0, game.getCurrentPlayer().getProductionHandler().getSelectedProductions().size());
        // The first player should now be able to do whatever he wants during the turn, he could even chose to buy a DevelopmentCard
        // coz he has realized he's dumb and doesn't really want to activate that stupid Production
        game.takeDevelopmentCard(CardColor.YELLOW, 1, 2);

    }

    /**
     * Tests the homonymous UserCommandsInterface method, simulating an in-game player action
     */
    @Test
    void confirmProductionChoice() throws WrongTurnPhaseException, SlotNotValidException, NotEnoughResourceException, UndefinedJollyException, EmptyDeckException, ProductionNotPresentException, LeaderNotPresentException {
        // Game creation
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);

        // We gonna cheat and add some Resources to all players so that they can buy cards without waiting 100 turns
        for (PlayerBoard player : game.getPlayersBoardsTurnOrder()) {
            Map<ResourceType, Integer> resources = new HashMap<>();
            resources.put(ResourceType.COIN, 100);
            resources.put(ResourceType.SERVANT, 100);
            resources.put(ResourceType.SHIELD, 100);
            resources.put(ResourceType.STONE, 100);
            player.addResourcesToStrongbox(resources);
        }


        for (PlayerBoard player : game.getPlayersBoardsTurnOrder()) {
            game.chooseLeaderCard(1);
            game.chooseLeaderCard(2);
            player.clearWaitingRoom();
            game.endTurn();

            game.takeDevelopmentCard(CardColor.GREEN, 1, 1);
            for (ResourceType resourceType : game.getCurrentPlayer().getCardSlots().get(0).get(0).getCost()) {
                game.payFromStrongbox(resourceType.toResource(), 1);
            }
            game.endTurn();
        }

        // The first player now selects 1 Production
        game.selectProduction(2);
        assertEquals(2, game.getCurrentPlayer().getProductionHandler().getProductions().size());
        assertEquals(1, game.getCurrentPlayer().getProductionHandler().getSelectedProductions().size());

        // The first player realizes the Production he selected represents a very good deal and decides to activate it
        game.confirmProductionChoice();

    }

    /**
     * Tests the homonymous UserCommandsInterface method, simulating an in-game player action
     */
    @Test
    void chooseJollyInputAndOutput() throws WrongTurnPhaseException, SlotNotValidException, NotEnoughResourceException, UndefinedJollyException, ProductionNotPresentException, NotEnoughResourceException, LeaderNotPresentException {
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
        // We gonna cheat and add some Resources to all players so that they can buy cards without waiting 100 turns
        for (PlayerBoard player : game.getPlayersBoardsTurnOrder()) {
            Map<ResourceType, Integer> resources = new HashMap<>();
            resources.put(ResourceType.COIN, 100);
            resources.put(ResourceType.SERVANT, 100);
            resources.put(ResourceType.SHIELD, 100);
            resources.put(ResourceType.STONE, 100);
            player.addResourcesToStrongbox(resources);
        }
        // First player decides to activate the basic Production (WHICH INCLUDES JOLLY RESOURCES) so he has to choose Jolly for both input and output
        game.selectProduction(1);
        assertThrows(UndefinedJollyException.class, game::confirmProductionChoice);
        game.chooseJollyInput(new ResourceStone());
        assertThrows(UndefinedJollyException.class, game::confirmProductionChoice);
        game.chooseJollyInput(new ResourceStone());
        assertThrows(UndefinedJollyException.class, game::confirmProductionChoice);
        game.chooseJollyOutput(new ResourceServant());
        game.confirmProductionChoice();
    }

    /**
     * Tests the homonymous UserCommandsInterface method (when applied to paying for development cards), simulating an in-game player action
     */
    @Test
    void payFromWarehouseCard() throws WrongTurnPhaseException, BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException, DepotNotPresentException, SlotNotValidException, NotEnoughResourceException, EmptyDeckException, LeaderNotPresentException {
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

        //Manually adds large depots to the player to ensure they can pay the cost
        PlayerBoard player = game.getCurrentPlayer();
        player.addNewDepot(new LeaderDepot(20, ResourceType.SERVANT, 0));
        player.addNewDepot(new LeaderDepot(20, ResourceType.SHIELD, 0));
        player.addNewDepot(new LeaderDepot(20, ResourceType.STONE, 0));
        player.addNewDepot(new LeaderDepot(20, ResourceType.COIN, 0));

        //Adds manually resources to the depots
        Warehouse warehouse = player.getWarehouse();
        warehouse.addToDepot(1, ResourceType.SHIELD, 1);
        warehouse.addToDepot(2, ResourceType.COIN, 2);
        warehouse.addToDepot(3, ResourceType.STONE, 3);
        warehouse.addToDepot(4, ResourceType.SERVANT, 20);
        warehouse.addToDepot(5, ResourceType.SHIELD, 20);
        warehouse.addToDepot(6, ResourceType.STONE, 20);
        warehouse.addToDepot(7, ResourceType.COIN, 20);

        //Quantifies the resources the player has before the purchase
        Map<ResourceType, Integer> inStock = new HashMap<>();
        inStock.put(ResourceType.STONE, 23);
        inStock.put(ResourceType.COIN, 22);
        inStock.put(ResourceType.SHIELD, 21);
        inStock.put(ResourceType.SERVANT, 20);

        //Saves the cost of the card
        List<ResourceType> cost = game.getCardTable().getGreenCards().get(0).get(0).getCost();

        //Buys the card
        game.takeDevelopmentCard(CardColor.GREEN, 1, 1);

        //Actually tests the method
        for (ResourceType resource : cost) {
            for (int i = 1; i < 8; i++) {
                try {
                    game.payFromWarehouse(i, resource.toResource(), 1);
                    inStock.put(resource, inStock.get(resource) - 1);
                    break;
                } catch (NotEnoughResourceException | DepotNotPresentException ex) {
                    //DO NOTHING
                }
            }
        }

        game.endTurn();

        //verifies quantities left
        assertEquals(inStock.get(ResourceType.SHIELD), warehouse.getNumOfResource(ResourceType.SHIELD));
        assertEquals(inStock.get(ResourceType.COIN), warehouse.getNumOfResource(ResourceType.COIN));
        assertEquals(inStock.get(ResourceType.SERVANT), warehouse.getNumOfResource(ResourceType.SERVANT));
        assertEquals(inStock.get(ResourceType.STONE), warehouse.getNumOfResource(ResourceType.STONE));

    }

    /**
     * Tests the homonymous UserCommandsInterface method (when applied to paying for development cards), simulating an in-game player action
     */
    @Test
    void payFromStrongboxCard() throws WrongTurnPhaseException, SlotNotValidException, NotEnoughResourceException, EmptyDeckException, LeaderNotPresentException {
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

        //Manually adds resources to player's strongbox
        PlayerBoard player = game.getCurrentPlayer();
        UnlimitedStorage strongbox = player.getStrongbox();
        Map<ResourceType, Integer> resources = new HashMap<>();
        resources.put(ResourceType.SHIELD, 20);
        resources.put(ResourceType.STONE, 20);
        resources.put(ResourceType.SERVANT, 20);
        resources.put(ResourceType.COIN, 20);
        strongbox.addResources(resources);


        //Quantifies the resources the player has before the purchase
        Map<ResourceType, Integer> inStock = new HashMap<>();
        inStock.put(ResourceType.STONE, 20);
        inStock.put(ResourceType.COIN, 20);
        inStock.put(ResourceType.SHIELD, 20);
        inStock.put(ResourceType.SERVANT, 20);

        //Saves the cost of the card
        List<ResourceType> cost = game.getCardTable().getGreenCards().get(0).get(0).getCost();

        //Buys the card
        game.takeDevelopmentCard(CardColor.GREEN, 1, 1);

        //Actually tests the method
        for (ResourceType resource : cost) {
            game.payFromStrongbox(resource.toResource(), 1);
            inStock.put(resource, inStock.get(resource) - 1);
        }

        game.endTurn();

        //verifies quantities left
        assertEquals(inStock.get(ResourceType.SHIELD), strongbox.getNumOfResource(ResourceType.SHIELD));
        assertEquals(inStock.get(ResourceType.COIN), strongbox.getNumOfResource(ResourceType.COIN));
        assertEquals(inStock.get(ResourceType.SERVANT), strongbox.getNumOfResource(ResourceType.SERVANT));
        assertEquals(inStock.get(ResourceType.STONE), strongbox.getNumOfResource(ResourceType.STONE));
    }

    /**
     * Tests the homonymous UserCommandsInterface method (when applied to paying for productions), simulating an in-game player action
     */
    @Test
    void payFromWarehouseProduction() throws WrongTurnPhaseException, NotEnoughResourceException, BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException, DepotNotPresentException, UndefinedJollyException, ProductionNotPresentException, NotEnoughResourceException, LeaderNotPresentException {
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

        //Manually adds large depots to the player to ensure they can pay the cost
        PlayerBoard player = game.getCurrentPlayer();
        player.addNewDepot(new LeaderDepot(20, ResourceType.SERVANT, 0));
        player.addNewDepot(new LeaderDepot(20, ResourceType.SHIELD, 0));
        player.addNewDepot(new LeaderDepot(20, ResourceType.STONE, 0));
        player.addNewDepot(new LeaderDepot(20, ResourceType.COIN, 0));

        //Adds manually resources to the depots
        Warehouse warehouse = player.getWarehouse();
        warehouse.addToDepot(1, ResourceType.SHIELD, 1);
        warehouse.addToDepot(2, ResourceType.COIN, 2);
        warehouse.addToDepot(3, ResourceType.STONE, 3);
        warehouse.addToDepot(4, ResourceType.SERVANT, 20);
        warehouse.addToDepot(5, ResourceType.SHIELD, 20);
        warehouse.addToDepot(6, ResourceType.STONE, 20);
        warehouse.addToDepot(7, ResourceType.COIN, 20);

        //Quantifies the resources the player has before the purchase
        Map<ResourceType, Integer> inStock = new HashMap<>();
        inStock.put(ResourceType.STONE, 23);
        inStock.put(ResourceType.COIN, 22);
        inStock.put(ResourceType.SHIELD, 21);
        inStock.put(ResourceType.SERVANT, 20);

        //Loads two productions in current player's board
        List<Resource> input = new ArrayList<>();
        input.add(new ResourceShield());
        input.add(new ResourceStone());
        List<Resource> output = new ArrayList<>();
        output.add(new ResourceFaith());
        output.add(new ResourceFaith());
        output.add(new ResourceServant());
        player.addProduction(new Production(-1, input, output));

        //Activates the two productions
        game.selectProduction(1);
        game.selectProduction(2);

        //Select jollies of base production
        game.chooseJollyInput(new ResourceCoin());
        game.chooseJollyInput(new ResourceStone());
        game.chooseJollyOutput(new ResourceServant());

        //Confirm choice
        game.confirmProductionChoice();

        //Saves the cost of the productions
        List<Resource> cost = new ArrayList<>();
        cost.addAll(input);
        cost.add(new ResourceCoin());
        cost.add(new ResourceStone());

        //Actually tests the method
        for (Resource resource : cost) {
            for (int i = 1; i < 8; i++) {
                try {
                    game.payFromWarehouse(i, resource, 1);
                    inStock.put(resource.getType(), inStock.get(resource.getType()) - 1);
                    break;
                } catch (NotEnoughResourceException | DepotNotPresentException ex) {
                    //DO NOTHING
                }
            }
        }

        game.endTurn();

        //verifies quantities left
        assertEquals(inStock.get(ResourceType.SHIELD), warehouse.getNumOfResource(ResourceType.SHIELD));
        assertEquals(inStock.get(ResourceType.COIN), warehouse.getNumOfResource(ResourceType.COIN));
        assertEquals(inStock.get(ResourceType.SERVANT), warehouse.getNumOfResource(ResourceType.SERVANT));
        assertEquals(inStock.get(ResourceType.STONE), warehouse.getNumOfResource(ResourceType.STONE));

    }

    /**
     * Tests the homonymous UserCommandsInterface method (when applied to paying for productions), simulating an in-game player action
     */
    @Test
    void payFromStrongboxProduction() throws WrongTurnPhaseException, NotEnoughResourceException, DepotNotPresentException, UndefinedJollyException, BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException, ProductionNotPresentException, NotEnoughResourceException, LeaderNotPresentException {
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

        //Adds manually resources to the strongbox
        PlayerBoard player = game.getCurrentPlayer();
        UnlimitedStorage strongbox = player.getStrongbox();
        Map<ResourceType, Integer> resources = new HashMap<>();
        resources.put(ResourceType.SHIELD, 20);
        resources.put(ResourceType.STONE, 20);
        resources.put(ResourceType.SERVANT, 20);
        resources.put(ResourceType.COIN, 20);
        strongbox.addResources(resources);

        //Loads two productions in current player's board
        List<Resource> input1 = new ArrayList<>();
        input1.add(new ResourceCoin());
        input1.add(new ResourceCoin());
        List<Resource> output1 = new ArrayList<>();
        output1.add(new ResourceFaith());
        output1.add(new ResourceJolly());
        player.addProduction(new Production(-1, input1, output1));

        List<Resource> input2 = new ArrayList<>();
        input2.add(new ResourceShield());
        List<Resource> output2 = new ArrayList<>();
        output2.add(new ResourceStone());
        output2.add(new ResourceStone());
        player.addProduction(new Production(-1, input2, output2));

        //Activates the two productions
        game.selectProduction(2);
        game.selectProduction(3);

        //Select jollies
        game.chooseJollyOutput(new ResourceServant());

        //Confirm choice
        game.confirmProductionChoice();

        //Actually tests the method
        game.payFromStrongbox(new ResourceShield(), 5);
        game.payFromStrongbox(new ResourceCoin(), 18);

        game.endTurn();

        //Verifies quantities left
        assertEquals(22, strongbox.getNumOfResource(ResourceType.STONE));
        assertEquals(21, strongbox.getNumOfResource(ResourceType.SERVANT));
        assertEquals(18, strongbox.getNumOfResource(ResourceType.COIN));
        assertEquals(19, strongbox.getNumOfResource(ResourceType.SHIELD));
        assertEquals(1, player.getFaith());

        //Some more checks just to be safe
        player.getWarehouse().addToDepot(2, ResourceType.SHIELD, 2);
        player.getWarehouse().addToDepot(3, ResourceType.SERVANT, 3);
        assertEquals(21, player.getNumOfResource(ResourceType.SHIELD));
        assertEquals(18, player.getNumOfResource(ResourceType.COIN));
        assertEquals(22, player.getNumOfResource(ResourceType.STONE));
        assertEquals(24, player.getNumOfResource(ResourceType.SERVANT));
    }


    // END TURN

    /**
     * Tests the homonymous UserCommandsInterface method, simulating an in-game player action.
     * Specifically, tests ending the turn for the first turn for all players (both the leader cards selection and the following action)
     */
    @Test
    void endTurnBasic() throws WrongTurnPhaseException, LeaderNotPresentException {
        // Game creation
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);
        List<PlayerBoard> players = game.getPlayersBoardsTurnOrder();
        // During first turn players must choose which LeaderCards to keep
        for (int i = 0; i < players.size(); i++) {
            assertEquals(players.get(i).getUsername(), game.getCurrentPlayer().getUsername());
            //Avoids having to deal with bonus first-turn resource choice
            players.get(i).clearWaitingRoom();
            game.chooseLeaderCard(1);
            game.chooseLeaderCard(2);
            game.endTurn();

            assertEquals(players.get(i).getUsername(), game.getCurrentPlayer().getUsername());
            game.selectMarketRow(1);
            game.endTurn();
        }
    }

    /**
     * Tests the homonymous UserCommandsInterface method, simulating an in-game player action.
     * Specifically, tests ending the turn thereby discarding resources taken from market
     */
    @Test
    void endTurnDiscardResources() throws WrongTurnPhaseException, ProductionNotPresentException, LeaderNotPresentException {
        // Game creation
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);
        List<PlayerBoard> players = game.getPlayersBoardsTurnOrder();
        // During first turn players must choose which LeaderCards to keep

        assertEquals(players.get(0).getUsername(), game.getCurrentPlayer().getUsername());
        //Avoids having to deal with bonus first-turn resource choice
        players.get(0).clearWaitingRoom();
        game.chooseLeaderCard(1);
        game.chooseLeaderCard(2);
        game.endTurn();


        //First player selects a production but doesn't activate it
        game.selectProduction(1);

        //First player selects from market and discards all obtained resources
        game.selectMarketRow(1);
        int discarded = players.get(0).getLeftInWaitingRoom();
        game.endTurn();

        //Checks that other players' faith has been increased by amount of discarded resources
        assertEquals(discarded, players.get(1).getFaith());
        assertEquals(discarded + 1, players.get(2).getFaith());

        //Checks that productions have been reset
        assertTrue(players.get(0).isProductionInputEmpty());
    }

    /**
     * Tests the homonymous UserCommandsInterface method, simulating an in-game player action.
     * Specifically, ends the turn in a solo game thereby causing lorenzo to take an action
     */
    @Test
    void endTurnSoloGame() throws WrongTurnPhaseException, LeaderNotPresentException {
        // Game creation
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Gigi");
        Game game = new Game(nicknames);
        PlayerBoard player = game.getPlayersBoardsTurnOrder().get(0);

        //Verify Lorenzo lives
        Lorenzo lollo = (Lorenzo) game.getLorenzo();
        List<ActionToken> activeTokens = lollo.getActiveDeck();
        List<ActionToken> usedTokens = lollo.getUsedDeck();
        assertNotNull(lollo);

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
        game.endTurn();

        //Verification
        if (usedTokens.isEmpty()) {
            assertEquals(6, activeTokens.size());
            assertEquals(baseFaith + 1, lollo.getFaith());
        } else {
            assertEquals(5, activeTokens.size());
            assertEquals(1, usedTokens.size());

            if (usedTokens.get(0) instanceof DoubleFaithToken) {
                assertEquals(baseFaith + 2, lollo.getFaith());
            } else if (usedTokens.get(0) instanceof RemoveCardsToken) {
                if (((RemoveCardsToken) usedTokens.get(0)).getColor() == CardColor.GREEN) {
                    assertEquals(2, game.getCardTable().getGreenCards().get(0).size());
                } else if (((RemoveCardsToken) usedTokens.get(0)).getColor() == CardColor.BLUE) {
                    assertEquals(2, game.getCardTable().getBlueCards().get(0).size());
                } else if (((RemoveCardsToken) usedTokens.get(0)).getColor() == CardColor.YELLOW) {
                    assertEquals(2, game.getCardTable().getYellowCards().get(0).size());
                } else {
                    assertEquals(2, game.getCardTable().getPurpleCards().get(0).size());
                }
            }
        }

        //Check that action tokens do not accumulate in following turns
        for (int i = 0; i < 3; i++) {
            game.selectMarketRow(1);
            game.endTurn();
            assertEquals(6, activeTokens.size() + usedTokens.size());
        }
    }

    /**
     * Tests the homonymous UserCommandsInterface method, simulating an in-game player action.
     * Specifically, tests ending the turn whilst causing a vatican report
     */
    @Test
    void endTurnVaticanReport() throws WrongTurnPhaseException, LeaderNotPresentException {
        // Game creation
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);
        List<PlayerBoard> players = game.getPlayersBoardsTurnOrder();

        //adds faith to players
        players.get(0).addFaith(9);
        players.get(1).addFaith(5);
        players.get(2).addFaith(1);

        //selects from market with player one, but then empties waiting room to not cause increases in the others' faith
        game.chooseLeaderCard(1);
        game.chooseLeaderCard(2);
        game.endTurn();

        game.selectMarketRow(1);
        players.get(0).clearWaitingRoom();
        game.endTurn();

        //checks popetile status for the three players
        assertEquals(PopeTileState.ACTIVE, players.get(0).getPopeFavorTiles().get(0).getState());
        assertEquals(PopeTileState.ACTIVE, players.get(1).getPopeFavorTiles().get(0).getState());
        assertEquals(PopeTileState.DISCARDED, players.get(2).getPopeFavorTiles().get(0).getState());
    }
}