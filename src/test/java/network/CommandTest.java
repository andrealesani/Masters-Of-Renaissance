package network;

import Exceptions.*;
import com.google.gson.Gson;
import model.CardColor;
import model.Game;
import model.PlayerBoard;
import model.ResourceType;
import model.card.leadercard.LeaderCard;
import model.resource.Resource;
import model.resource.ResourceCoin;
import model.resource.ResourceShield;
import model.resource.ResourceStone;
import model.storage.LeaderDepot;
import model.storage.Warehouse;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    @Test
    void serialize() {
        Gson gson = new Gson();

        Map parameters = new HashMap();
        parameters.put("Resource","COIN");
        parameters.put("Quantity", 2);
        Command command = new Command(UserCommandsType.chooseBonusResourceType, parameters);

        String jsonCommand = gson.toJson(command);
        System.out.println(jsonCommand);

        String expected = "{\"parameters\":{\"Resource\":\"COIN\",\"Quantity\":2},\"commandType\":\"chooseBonusResourceType\"}";
        assertTrue(jsonCommand.equals(expected));

        Command rebuiltCommand = (Command) gson.fromJson(jsonCommand, Command.class);

        assertTrue(rebuiltCommand.getCommandType()==UserCommandsType.chooseBonusResourceType);

        Map rebuiltParameters = rebuiltCommand.getParameters();

        ResourceType resource = ResourceType.valueOf((String) rebuiltParameters.get("Resource"));
        assertTrue(resource == ResourceType.COIN);

        Double quantity = (Double) rebuiltParameters.get("Quantity");
        assertTrue(quantity.intValue() == 2);
    }

    @Test
    void runCommandIntegerParameter() {
        // Game creation
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);
        // FIRST TURN: players must choose which LeaderCards to keep

        PlayerBoard currentPlayer = game.getCurrentPlayer();
        List<LeaderCard> listLeaderCards = currentPlayer.getLeaderCards();
        List<LeaderCard> memoryList = new ArrayList(listLeaderCards);

        Map parameters1 = new HashMap();
        parameters1.put("number", 1);
        Command command1 = new Command(UserCommandsType.chooseLeaderCard, parameters1);
        String result1 = command1.runCommand(game);
        assertEquals(result1, null);

        Map parameters2 = new HashMap();
        parameters2.put("number", 3);
        Command command2 = new Command(UserCommandsType.chooseLeaderCard, parameters2);
        String result2 = command2.runCommand(game);
        assertEquals(result2, null);

        Command command3 = new Command(UserCommandsType.endTurn, null);
        String result3 = command3.runCommand(game);
        assertEquals(result3, null);

        assertEquals(2, currentPlayer.getLeaderCards().size());

        assertEquals(memoryList.get(0), currentPlayer.getLeaderCards().get(0));
        assertEquals(memoryList.get(2), currentPlayer.getLeaderCards().get(1));
    }

    @Test
    void runCommandResourceParameter() throws WrongTurnPhaseException {
        // Game creation
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);
        // During first turn players must choose which LeaderCards to keep
        for (PlayerBoard player : game.getPlayersTurnOrder()) {
            game.chooseLeaderCard(1);
            game.chooseLeaderCard(2);
            //Avoids having to deal with bonus first-turn resource choice
            player.clearWaitingRoom();
            game.endTurn();
        }

        //Adds manually a leader depot and some resources to waiting room
        PlayerBoard player = game.getCurrentPlayer();
        player.addNewDepot(new LeaderDepot(2, ResourceType.SHIELD));
        player.addResourceToWaitingRoom(ResourceType.SHIELD, 3);
        player.addResourceToWaitingRoom(ResourceType.COIN, 1);
        player.addResourceToWaitingRoom(ResourceType.STONE, 1);

        //to change game phase to MARKETDISTRIBUTION
        game.selectMarketRow(1);

        //Test the actual method
        Map parameters1 = new HashMap();
        parameters1.put("number", 1);
        parameters1.put("resource", ResourceType.COIN);
        parameters1.put("quantity", 1);
        Command command1 = new Command(UserCommandsType.sendResourceToDepot, parameters1);
        String result1 = command1.runCommand(game);
        assertEquals(result1, null);

        Map parameters2 = new HashMap();
        parameters2.put("number", 2);
        parameters2.put("resource", ResourceType.SHIELD);
        parameters2.put("quantity", 2);
        Command command2 = new Command(UserCommandsType.sendResourceToDepot, parameters2);
        String result2 = command2.runCommand(game);
        assertEquals(result2, null);

        Map parameters3 = new HashMap();
        parameters3.put("number", 3);
        parameters3.put("resource", ResourceType.STONE);
        parameters3.put("quantity", 1);
        Command command3 = new Command(UserCommandsType.sendResourceToDepot, parameters3);
        String result3 = command3.runCommand(game);
        assertEquals(result3, null);

        Map parameters4 = new HashMap();
        parameters4.put("number", 4);
        parameters4.put("resource", ResourceType.SHIELD);
        parameters4.put("quantity", 1);
        Command command4 = new Command(UserCommandsType.sendResourceToDepot, parameters4);
        String result4 = command4.runCommand(game);
        assertEquals(result4, null);

        Command command5 = new Command(UserCommandsType.endTurn, null);
        String result5 = command3.runCommand(game);
        assertEquals(result5, null);

        //Verify resources got to the correct depots
        Warehouse warehouse = player.getWarehouse();
        assertEquals(1, warehouse.getDepot(1).getNumOfResource(ResourceType.COIN));
        assertEquals(2, warehouse.getDepot(2).getNumOfResource(ResourceType.SHIELD));
        assertEquals(1, warehouse.getDepot(3).getNumOfResource(ResourceType.STONE));
        assertEquals(1, warehouse.getDepot(4).getNumOfResource(ResourceType.SHIELD));
    }

    @Test
    void runCommandCardColorParameter() throws WrongTurnPhaseException {
        // Game creation
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);
        // FIRST TURN: players must choose which LeaderCards to keep
        for (PlayerBoard player : game.getPlayersTurnOrder()) {
            game.chooseLeaderCard(1);
            game.chooseLeaderCard(2);
            //Avoids having to deal with bonus first-turn resource choice
            player.clearWaitingRoom();
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
        // NB This test only checks that the card is taken, it doesn't check the payment phase

        assertEquals(4, game.getCardTable().getGreenCards().get(0).size());

        // SECOND TURN: first player chooses to buy a DevelopmentCard
        Map parameters1 = new HashMap();
        parameters1.put("color", CardColor.GREEN);
        parameters1.put("level", 1);
        parameters1.put("number", 1);
        Command command1 = new Command(UserCommandsType.takeDevelopmentCard, parameters1);
        String result1 = command1.runCommand(game);
        assertEquals(result1, null);

        assertEquals(3, game.getCardTable().getGreenCards().get(0).size());
        assertEquals(1, game.getCurrentPlayer().getCardSlots().get(0).size());
    }

    @Test
    void runCommandIntArrayParameter() throws WrongTurnPhaseException, DepotNotPresentException, NotEnoughSpaceException, WrongResourceInsertionException, BlockedResourceException {
        // Game creation
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Andre");
        nicknames.add("Tom");
        nicknames.add("Gigi");
        Game game = new Game(nicknames);
        // During first turn players must choose which LeaderCards to keep
        for (PlayerBoard player : game.getPlayersTurnOrder()) {
            game.chooseLeaderCard(1);
            game.chooseLeaderCard(2);
            //Avoids having to deal with bonus first-turn resource choice
            player.clearWaitingRoom();
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
        Map parameters1 = new HashMap();
        int[] depots1 = {2, 3};
        parameters1.put("depots", depots1);
        Command command1 = new Command(UserCommandsType.swapDepotContent, parameters1);
        String result1 = command1.runCommand(game);
        assertEquals(result1, null);

        Map parameters2 = new HashMap();
        int[] depots2 = {1, 2};
        parameters2.put("depots", depots2);
        Command command2 = new Command(UserCommandsType.swapDepotContent, parameters2);
        String result2 = command2.runCommand(game);
        assertEquals(result2, null);

        Map parameters3 = new HashMap();
        int[] depots3 = {2, 4};
        parameters3.put("depots", depots3);
        Command command3 = new Command(UserCommandsType.swapDepotContent, parameters3);
        String result3 = command3.runCommand(game);

        //Verify resources got to the correct depots
        assertEquals(1, warehouse.getDepot(1).getNumOfResource(ResourceType.STONE));
        assertEquals(1, warehouse.getDepot(2).getNumOfResource(ResourceType.SHIELD));
        assertEquals(2, warehouse.getDepot(3).getNumOfResource(ResourceType.COIN));
        assertEquals(1, warehouse.getDepot(4).getNumOfResource(ResourceType.SHIELD));
    }
}