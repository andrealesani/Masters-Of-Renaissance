package model;

import Exceptions.*;
import Exceptions.NotEnoughResourceException;
import Exceptions.SlotNotValidException;
import Exceptions.WrongTurnPhaseException;
import model.card.leadercard.LeaderCard;
import model.resource.*;
import model.storage.LeaderDepot;
import model.storage.UnlimitedStorage;
import model.storage.Warehouse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        PlayerBoard currentPlayer = game.getCurrentPlayer();
        List<LeaderCard> listaLeaderCards = currentPlayer.getLeaderCards();

        // FIRST TURN: players must choose which LeaderCards to keep
        for (PlayerBoard player : game.getPlayersTurnOrder()) {
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
    void selectFromMarket() throws WrongTurnPhaseException {

        List<String> nicknames = new ArrayList<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);


        for (PlayerBoard player : game.getPlayersTurnOrder()) {
            game.chooseLeaderCard(1);
            game.chooseLeaderCard(2);
            game.endTurn();
        }

        PlayerBoard currentPlayer = game.getCurrentPlayer();
        UnlimitedStorage waitingRoom = currentPlayer.getWaitingRoom();

        Market market = game.getMarket();
        Resource[][] board = market.getBoard();
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
        game.selectFromMarket(MarketScope.ROW, 2);

        assertEquals(numShields,waitingRoom.getNumOfResource(ResourceType.SHIELD));
        assertEquals(numCoins,waitingRoom.getNumOfResource(ResourceType.COIN));
        assertEquals(numStones,waitingRoom.getNumOfResource(ResourceType.STONE));
        assertEquals(numServants,waitingRoom.getNumOfResource(ResourceType.SERVANT));
        assertEquals(numFaithes, currentPlayer.getFaith());

    }

    // TOM SECTION
    @Test
    void sendResourceToDepotCorrect() throws WrongTurnPhaseException, NotEnoughSpaceException, WrongResourceInsertionException, BlockedResourceException, NotEnoughResourceException, DepotNotPresentException {
        // Game creation
        List<String> nicknames = new ArrayList<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);
        // During first turn players must choose which LeaderCards to keep
        for (PlayerBoard player : game.getPlayersTurnOrder()) {
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
    void chooseMarbleConversion() throws WrongTurnPhaseException, ConversionNotAvailableException, NotEnoughResourceException, WrongResourceInsertionException, BlockedResourceException, NotEnoughSpaceException, DepotNotPresentException {
        // Game creation
        List<String> nicknames = new ArrayList<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);
        // During first turn players must choose which LeaderCards to keep
        for (PlayerBoard player : game.getPlayersTurnOrder()) {
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
        assertTrue(player.getLeftInWaitingRoom() <=4);
    }

    @Test
    void swapDepotContent() throws WrongTurnPhaseException, BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException, DepotNotPresentException, SwapNotValidException, ParametersNotValidException {
        // Game creation
            List<String> nicknames = new ArrayList<>();
            nicknames.add("Andre");
            nicknames.add("Tom");
            nicknames.add("Gigi");
            Game game = new Game(nicknames);
            // During first turn players must choose which LeaderCards to keep
            for (PlayerBoard player : game.getPlayersTurnOrder()) {
                game.chooseLeaderCard(1);
                game.chooseLeaderCard(2);
                game.endTurn();
            }

        //Adds manually resources to the depots
        PlayerBoard player = game.getCurrentPlayer();
        player.addNewDepot(new LeaderDepot(2, ResourceType.SHIELD));
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

    @Test
    void takeResourceFromWarehouseCard() throws WrongTurnPhaseException, BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException, DepotNotPresentException, SlotNotValidException, NotEnoughResourceException {
        // Game creation
        List<String> nicknames = new ArrayList<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);
        // During first turn players must choose which LeaderCards to keep
        for (PlayerBoard player : game.getPlayersTurnOrder()) {
            game.chooseLeaderCard(1);
            game.chooseLeaderCard(2);
            game.endTurn();
        }

        //Manually adds large depots to the player to ensure they can pay the cost
        PlayerBoard player = game.getCurrentPlayer();
        player.addNewDepot(new LeaderDepot(20, ResourceType.SERVANT));
        player.addNewDepot(new LeaderDepot(20, ResourceType.SHIELD));
        player.addNewDepot(new LeaderDepot(20, ResourceType.STONE));
        player.addNewDepot(new LeaderDepot(20, ResourceType.COIN));

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
        List<ResourceType> cost = game.getCardTable().getGreenCards().get(2).get(0).getCost();

        //Buys the card
        game.buyDevelopmentCard(CardColor.GREEN, 1, 1);

        //Actually tests the method
        for (ResourceType resource : cost) {
            System.out.println("Processing cost: " + resource);
            for (int i=1; i<8; i++) {
                try {
                    game.takeResourceFromWarehouseCard(i, UtilsForModel.typeToResource(resource), 1);
                    System.out.println("Taking resource from depot: " + i);
                    inStock.put(resource, inStock.get(resource)-1);
                    break;
                } catch (NotEnoughResourceException | DepotNotPresentException ex) {
                    //DO NOTHING
                }
            }
        }

        game.endTurn();

        //verifies quantities left
        assertEquals (inStock.get(ResourceType.SHIELD), warehouse.getNumOfResource(ResourceType.SHIELD));
        assertEquals (inStock.get(ResourceType.COIN), warehouse.getNumOfResource(ResourceType.COIN));
        assertEquals (inStock.get(ResourceType.SERVANT), warehouse.getNumOfResource(ResourceType.SERVANT));
        assertEquals (inStock.get(ResourceType.STONE), warehouse.getNumOfResource(ResourceType.STONE));

        //Visual verification
        System.out.println(warehouse.getDepot(1).getNumOfResource(ResourceType.SHIELD));
        System.out.println(warehouse.getDepot(2).getNumOfResource(ResourceType.COIN));
        System.out.println(warehouse.getDepot(3).getNumOfResource(ResourceType.STONE));
        System.out.println(warehouse.getDepot(4).getNumOfResource(ResourceType.SERVANT));
        System.out.println(warehouse.getDepot(5).getNumOfResource(ResourceType.SHIELD));
        System.out.println(warehouse.getDepot(6).getNumOfResource(ResourceType.STONE));
        System.out.println(warehouse.getDepot(7).getNumOfResource(ResourceType.COIN));

    }

    @Test
    void takeResourceFromStrongboxCard() throws WrongTurnPhaseException, SlotNotValidException, NotEnoughResourceException {
        // Game creation
        List<String> nicknames = new ArrayList<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);
        // During first turn players must choose which LeaderCards to keep
        for (PlayerBoard player : game.getPlayersTurnOrder()) {
            game.chooseLeaderCard(1);
            game.chooseLeaderCard(2);
            game.endTurn();
        }

        //Manually adds resources to player's strongbox
        PlayerBoard player = game.getCurrentPlayer();
        UnlimitedStorage strongbox = player.getStrongbox();
        strongbox.addResource(ResourceType.SHIELD, 20);
        strongbox.addResource(ResourceType.STONE, 20);
        strongbox.addResource(ResourceType.SERVANT, 20);
        strongbox.addResource(ResourceType.COIN, 20);


        //Quantifies the resources the player has before the purchase
        Map<ResourceType, Integer> inStock = new HashMap<>();
        inStock.put(ResourceType.STONE, 20);
        inStock.put(ResourceType.COIN, 20);
        inStock.put(ResourceType.SHIELD, 20);
        inStock.put(ResourceType.SERVANT, 20);

        //Saves the cost of the card
        List<ResourceType> cost = game.getCardTable().getGreenCards().get(2).get(0).getCost();

        //Buys the card
        game.buyDevelopmentCard(CardColor.GREEN, 1, 1);

        //Actually tests the method
        for (ResourceType resource : cost) {
            System.out.println("Processing cost: " + resource);
            game.takeResourceFromStrongboxCard(UtilsForModel.typeToResource(resource), 1);
            inStock.put(resource, inStock.get(resource)-1);
        }

        game.endTurn();

        //verifies quantities left
        assertEquals (inStock.get(ResourceType.SHIELD), strongbox.getNumOfResource(ResourceType.SHIELD));
        assertEquals (inStock.get(ResourceType.COIN), strongbox.getNumOfResource(ResourceType.COIN));
        assertEquals (inStock.get(ResourceType.SERVANT), strongbox.getNumOfResource(ResourceType.SERVANT));
        assertEquals (inStock.get(ResourceType.STONE), strongbox.getNumOfResource(ResourceType.STONE));

        //Visual verification
        System.out.println(strongbox.getNumOfResource(ResourceType.SHIELD));
        System.out.println(strongbox.getNumOfResource(ResourceType.COIN));
        System.out.println(strongbox.getNumOfResource(ResourceType.STONE));
        System.out.println(strongbox.getNumOfResource(ResourceType.SERVANT));
    }

    @Test
    void takeResourceFromWarehouseProduction() throws WrongTurnPhaseException, NotEnoughResourceException, BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException, DepotNotPresentException, UnknownResourceException {
        // Game creation
        List<String> nicknames = new ArrayList<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);
        // During first turn players must choose which LeaderCards to keep
        for (PlayerBoard player : game.getPlayersTurnOrder()) {
            game.chooseLeaderCard(1);
            game.chooseLeaderCard(2);
            game.endTurn();
        }

        //Adds manually resources to the depots
        PlayerBoard player = game.getCurrentPlayer();
        Warehouse warehouse = player.getWarehouse();
        warehouse.addToDepot(1, ResourceType.SHIELD, 1);
        warehouse.addToDepot(2, ResourceType.COIN, 2);
        warehouse.addToDepot(3, ResourceType.STONE, 3);

        //Loads two productions in current player's board
        List<Resource> input = new ArrayList<>();
        input.add(new ResourceShield());
        input.add(new ResourceStone());
        List<Resource> output = new ArrayList<>();
        output.add(new ResourceFaith());
        output.add(new ResourceFaith());
        output.add(new ResourceServant());
        player.addProduction(new Production(input, output));

        //Activates the two productions
        game.selectProduction(1);
        game.selectProduction(2);

        //Select jollies of base production
        game.chooseJollyInput(new ResourceCoin());
        game.chooseJollyInput(new ResourceStone());
        game.chooseJollyOutput(new ResourceServant());

        //Confirm choice
        game.confirmProductionChoice();

        //Actually tests the method
        game.takeResourceFromWarehouseProduction(1, new ResourceShield(), 1);
        game.takeResourceFromWarehouseProduction(2, new ResourceCoin(), 1);
        game.takeResourceFromWarehouseProduction(3, new ResourceStone(), 2);

        game.endTurn();

        //Verifies quantities left in warehouse
        assertEquals (0, warehouse.getDepot(1).getNumOfResource(ResourceType.SHIELD));
        assertEquals (1, warehouse.getDepot(2).getNumOfResource(ResourceType.COIN));
        assertEquals (1, warehouse.getDepot(3).getNumOfResource(ResourceType.STONE));

        //Verifies quantities earned in strongbox and faith score
        UnlimitedStorage strongbox = player.getStrongbox();
        assertEquals (2, strongbox.getNumOfResource(ResourceType.SERVANT));
        assertEquals (0, strongbox.getNumOfResource(ResourceType.SHIELD));
        assertEquals (0, strongbox.getNumOfResource(ResourceType.COIN));
        assertEquals (0, strongbox.getNumOfResource(ResourceType.STONE));
        assertEquals (2, player.getFaith());

        //Some more checks just to be safe
        strongbox.addResource(ResourceType.COIN, 3);
        assertEquals (0, player.getNumOfResource(ResourceType.SHIELD));
        assertEquals (4, player.getNumOfResource(ResourceType.COIN));
        assertEquals (1, player.getNumOfResource(ResourceType.STONE));
        assertEquals (2, player.getNumOfResource(ResourceType.SERVANT));
    }

    @Test
    void takeResourceFromStrongboxProduction() throws WrongTurnPhaseException, NotEnoughResourceException, DepotNotPresentException, UnknownResourceException, BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException {
// Game creation
        List<String> nicknames = new ArrayList<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);
        // During first turn players must choose which LeaderCards to keep
        for (PlayerBoard player : game.getPlayersTurnOrder()) {
            game.chooseLeaderCard(1);
            game.chooseLeaderCard(2);
            game.endTurn();
        }

        //Adds manually resources to the strongbox
        PlayerBoard player = game.getCurrentPlayer();
        UnlimitedStorage strongbox = player.getStrongbox();
        strongbox.addResource(ResourceType.SHIELD, 20);
        strongbox.addResource(ResourceType.STONE, 20);
        strongbox.addResource(ResourceType.SERVANT, 20);
        strongbox.addResource(ResourceType.COIN, 20);

        //Loads two productions in current player's board
        List<Resource> input1 = new ArrayList<>();
        input1.add(new ResourceCoin());
        input1.add(new ResourceCoin());
        List<Resource> output1 = new ArrayList<>();
        output1.add(new ResourceFaith());
        output1.add(new ResourceUnknown());
        player.addProduction(new Production(input1, output1));

        List<Resource> input2 = new ArrayList<>();
        input2.add(new ResourceShield());
        List<Resource> output2 = new ArrayList<>();
        output2.add(new ResourceStone());
        output2.add(new ResourceStone());
        player.addProduction(new Production(input2, output2));

        //Activates the two productions
        game.selectProduction(2);
        game.selectProduction(3);

        //Select jollies
        game.chooseJollyOutput(new ResourceServant());

        //Confirm choice
        game.confirmProductionChoice();

        //Actually tests the method
        game.takeResourceFromStrongboxProduction(new ResourceShield(), 5);
        game.takeResourceFromStrongboxProduction(new ResourceCoin(), 18);

        game.endTurn();

        //Verifies quantities left
        assertEquals (22, strongbox.getNumOfResource(ResourceType.STONE));
        assertEquals (21, strongbox.getNumOfResource(ResourceType.SERVANT));
        assertEquals (18, strongbox.getNumOfResource(ResourceType.COIN));
        assertEquals (19, strongbox.getNumOfResource(ResourceType.SHIELD));
        assertEquals ( 1, player.getFaith());

        //Some more checks just to be safe
        player.getWarehouse().addToDepot(2, ResourceType.SHIELD, 2);
        player.getWarehouse().addToDepot(3, ResourceType.SERVANT, 3);
        assertEquals (21, player.getNumOfResource(ResourceType.SHIELD));
        assertEquals (18, player.getNumOfResource(ResourceType.COIN));
        assertEquals (22, player.getNumOfResource(ResourceType.STONE));
        assertEquals (24, player.getNumOfResource(ResourceType.SERVANT));
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
        // We gonna cheat and add some Resources to all players so that they can buy cards without waiting 100 turns
        for (PlayerBoard player : game.getPlayersTurnOrder()) {
            player.addResourceToStrongbox(ResourceType.COIN, 100);
            player.addResourceToStrongbox(ResourceType.SERVANT, 100);
            player.addResourceToStrongbox(ResourceType.SHIELD, 100);
            player.addResourceToStrongbox(ResourceType.STONE, 100);
        }

        // TEST STARTS HERE
        // NB This test only checks that the card is taken, it doesn't check the payment phase (that is checked in the next test)

        assertEquals(4, game.getCardTable().getGreenCards().get(2).size());
        // SECOND TURN: every player can only do one move out of 3 possible moves
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
        // SECOND TURN: every player can only do one move out of 3 possible moves and he chooses to BUY A DEVELOPMENT CARD
        for (PlayerBoard player : game.getPlayersTurnOrder()) {
            game.buyDevelopmentCard(CardColor.GREEN, 1, 1);
            for (ResourceType resourceType: game.getCurrentPlayer().getCardSlots().get(0).get(0).getCost()) {
                game.takeResourceFromStrongboxCard(typeToResource(resourceType), 1);
            }
            game.endTurn();
        }

        // TEST STARTS HERE
        game.selectProduction(2);
        assertEquals(2, game.getCurrentPlayer().getProductionHandler().getProductions().size());
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
        game.selectProduction(2);
        assertEquals(2, game.getCurrentPlayer().getProductionHandler().getProductions().size());
        assertEquals(1, game.getCurrentPlayer().getProductionHandler().getSelectedProductions().size());

        // TEST STARTS HERE
        // The player realizes how deficient his decision making is and wants to go bacc
        game.resetProductionChoice();
        assertEquals(2, game.getCurrentPlayer().getProductionHandler().getProductions().size());
        assertEquals(0, game.getCurrentPlayer().getProductionHandler().getSelectedProductions().size());
        // The player should now be able to do whatever he wants during the turn, he could even chose to buy a DevelopmentCard
        // coz he has realized he's dumb and doesn't really want to activate that stupid Production
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
        List<PlayerBoard> players = game.getPlayersTurnOrder();
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

    @Test
    void endTurnDiscardResources() throws WrongTurnPhaseException {
        // Game creation
        List<String> nicknames = new ArrayList<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);
        List<PlayerBoard> players = game.getPlayersTurnOrder();
        // During first turn players must choose which LeaderCards to keep
        for (int i = 0; i<players.size(); i++) {
            assertEquals(players.get(i).getUsername(), game.getCurrentPlayer().getUsername());
            game.chooseLeaderCard(1);
            game.chooseLeaderCard(2);
            game.endTurn();
        }

        //First player selects from market and discards all obtained resources
        game.selectFromMarket(MarketScope.ROW, 1);
        int discarded = players.get(0).getLeftInWaitingRoom();
        game.endTurn();

        //Checks that other players' faith has been increased by amount of discarded resources
        assertEquals(discarded, players.get(1).getFaith());
        assertEquals(discarded, players.get(2).getFaith());
    }

    @Test
    void endTurn () {

    }
}