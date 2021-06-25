package model;

import Exceptions.ParametersNotValidException;
import com.google.gson.Gson;
import model.card.DevelopmentCard;
import model.card.leadercard.*;
import model.lorenzo.ArtificialIntelligence;
import model.lorenzo.Lorenzo;
import model.lorenzo.tokens.ActionToken;
import model.lorenzo.tokens.LorenzoTokenType;
import model.resource.Resource;
import model.resource.ResourceType;
import model.storage.UnlimitedStorage;
import model.storage.Warehouse;
import network.StaticMethods;
import network.beans.SlotBean;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static model.resource.ResourceType.*;

public class PersistenceHandler {

    int id;

    private Map<CardColor, List<List<DevelopmentCard>>> cardTable;

    private String[] playersTurnOrder;

    private String currentPlayer;

    private String[] username;

    private int lastTriggeredTile;

    private TurnPhase turnPhase;

    private boolean isLastTurn;

    private String winner;

    private int winnerVp;

    private ResourceType[][] marketBoard;

    private ResourceType slideMarble;

    private PopeTileState[][] popeTileStates;

    private int[] whiteMarbleNum;

    private int[] faith;

    private boolean[] firstTurnTaken;

    private SlotBean[][] cardSlots;

    private int[][] leaderCards;

    private boolean[][] activeLeaderCards;

    private boolean[][] activeProductions;

    private ResourceType[][] prodHandlerInputResources;

    private int[][] prodHandlerInputQuantities;

    private ResourceType[][] prodHandlerOutputResources;

    private int[][] prodHandlerOutputQuantities;

    private int[] basicDepotNum;

    private ResourceType[][] depotType;

    private int[][] depotQuantity;

    private int[][] depotSize;

    private ResourceType[][] strongboxTypes;

    private int[][] strongboxQuantities;

    private ResourceType[][] waitingRoomTypes;

    private int[][] waitingRoomQuantities;

    private int lorenzoFaith;

    private LorenzoTokenType[] lorenzoUsedTokens;

    // PUBLIC METHODS

    public void saveGame(Game game) {
        Gson gson = new Gson();
        if (game == null)
            throw new ParametersNotValidException();

        saveGameStatus(game);
        saveMarket(game);
        saveCardTable(game);
        savePlayerBoards(game);
        saveProductionHandlers(game);
        saveWarehouses(game);
        saveStrongboxes(game);
        saveWaitingRooms(game);
        saveLorenzo(game);

        try {
            if (id == 0)
                id = StaticMethods.findFirstFreePersistenceId();
            PrintWriter writer = new PrintWriter("src/main/resources/savedGames/game" + id + ".json", StandardCharsets.UTF_8);
            writer.print(gson.toJson(this));
            writer.close();
        } catch (IOException e) {
            System.err.println("Warning: couldn't save game to file.");
        }
    }

    public Game restoreGame() {

        Game game = restoreGameStatus();
        restoreMarket(game);
        restoreCardTable(game);
        restorePlayerBoards(game);
        restoreProductionHandlers(game);
        restoreWarehouses(game);
        restoreStrongboxes(game);
        restoreWaitingRooms(game);
        restoreLorenzo(game);
        restoreEndTurn(game);

        return game;
    }

    // PRIVATE SAVING METHODS

    private void saveGameStatus(Game game) {
        playersTurnOrder = new String[game.getPlayersBoardsTurnOrder().size()];
        for (int i = 0; i < game.getPlayersBoardsTurnOrder().size(); i++)
            playersTurnOrder[i] = game.getPlayersBoardsTurnOrder().get(i).getUsername();
        currentPlayer = game.getCurrentPlayer().getUsername();
        turnPhase = game.getTurnPhase();
        lastTriggeredTile = game.getLastTriggeredTile();
        isLastTurn = game.isLastTurn();
        winner = game.getWinner();
        winnerVp = game.getWinnerVp();
    }

    private void saveMarket(Game game) {
        Market market = game.getMarket();
        marketBoard = new ResourceType[market.getBoard().length][market.getBoard()[0].length];
        for (int i = 0; i < market.getBoard().length; i++) {
            for (int j = 0; j < market.getBoard()[0].length; j++) {
                marketBoard[i][j] = market.getBoard()[i][j].getType();
            }
        }
        slideMarble = market.getSlideOrb().getType();
    }

    private void saveCardTable(Game game) {
        cardTable = game.getCardTable().getCards();
    }

    private void savePlayerBoards(Game game) {
        int numOfPlayers = game.getPlayersBoardsTurnOrder().size();
        username = new String[numOfPlayers];
        firstTurnTaken = new boolean[numOfPlayers];
        popeTileStates = new PopeTileState[numOfPlayers][];
        whiteMarbleNum = new int[numOfPlayers];
        faith = new int[numOfPlayers];
        cardSlots = new SlotBean[numOfPlayers][];
        leaderCards = new int[numOfPlayers][];
        activeLeaderCards = new boolean[numOfPlayers][];

        for (int i = 0; i < game.getPlayersBoardsTurnOrder().size(); i++) {

            PlayerBoard player = game.getPlayersBoardsTurnOrder().get(i);

            // USERNAME

            username[i] = player.getUsername();

            // FAITH

            faith[i] = player.getFaith();

            //HAS TAKEN FIRST TURN

            firstTurnTaken[i] = player.isFirstTurnTaken();

            // WHITE MARBLES

            whiteMarbleNum[i] = player.getWhiteMarbles();

            // CARD SLOTS
            int j;
            cardSlots[i] = new SlotBean[player.getCardSlots().size()];
            for (j = 0; j < 3; j++) {
                cardSlots[i][j] = new SlotBean();
                cardSlots[i][j].setDevelopmentCardsFromPB(player, j + 1);
            }

            // LEADER CARDS

            j = 0;
            leaderCards[i] = new int[player.getLeaderCards().size()];
            activeLeaderCards[i] = new boolean[player.getLeaderCards().size()];
            for (LeaderCard leaderCard : player.getLeaderCards()) {
                leaderCards[i][j] = leaderCard.getId();
                activeLeaderCards[i][j] = leaderCard.isActive();
                j++;
            }

            // POPE'S FAVOR TILES

            List<PopeFavorTile> favorTiles = player.getPopeFavorTiles();
            popeTileStates[i] = new PopeTileState[favorTiles.size()];
            for (j = 0; j < popeTileStates[i].length; j++) {
                popeTileStates[i][j] = favorTiles.get(j).getState();
            }
        }
    }

    private void saveProductionHandlers(Game game) {
        int numOfPlayers = game.getPlayersBoardsTurnOrder().size();
        activeProductions = new boolean[numOfPlayers][];
        prodHandlerInputResources = new ResourceType[numOfPlayers][];
        prodHandlerInputQuantities = new int[numOfPlayers][];
        prodHandlerOutputResources = new ResourceType[numOfPlayers][];
        prodHandlerOutputQuantities = new int[numOfPlayers][];

        for (int i = 0; i < game.getPlayersBoardsTurnOrder().size(); i++) {
            ProductionHandler productionHandler = game.getPlayersBoardsTurnOrder().get(i).getProductionHandler();
            activeProductions[i] = new boolean[productionHandler.getProductions().size()];
            for (int j = 0; j < activeProductions[i].length; j++) {
                activeProductions[i][j] = productionHandler.getProductions().get(j).isSelectedByHandler();
            }

            prodHandlerInputResources[i] = new ResourceType[]{COIN, SERVANT, SHIELD, STONE, JOLLY};
            prodHandlerInputQuantities[i] = new int[prodHandlerInputResources[i].length];

            for (ResourceType resourceType : productionHandler.getCurrentInput().stream().map(Resource::getType).collect(Collectors.toList()))
                if (resourceType == COIN)
                    prodHandlerInputQuantities[i][0]++;
                else if (resourceType == SERVANT)
                    prodHandlerInputQuantities[i][1]++;
                else if (resourceType == SHIELD)
                    prodHandlerInputQuantities[i][2]++;
                else if (resourceType == STONE)
                    prodHandlerInputQuantities[i][3]++;
                else if (resourceType == JOLLY)
                    prodHandlerInputQuantities[i][4]++;
                else
                    System.out.println("Warning: found unsupported ResourceType inside ProductionHandler during save: " + resourceType);

            prodHandlerOutputResources[i] = new ResourceType[]{COIN, SERVANT, SHIELD, STONE, FAITH, JOLLY};
            prodHandlerOutputQuantities[i] = new int[prodHandlerOutputResources[i].length];

            for (ResourceType resourceType : productionHandler.getCurrentOutput().stream().map(Resource::getType).collect(Collectors.toList()))
                if (resourceType == COIN)
                    prodHandlerOutputQuantities[i][0]++;
                else if (resourceType == SERVANT)
                    prodHandlerOutputQuantities[i][1]++;
                else if (resourceType == SHIELD)
                    prodHandlerOutputQuantities[i][2]++;
                else if (resourceType == STONE)
                    prodHandlerOutputQuantities[i][3]++;
                else if (resourceType == FAITH)
                    prodHandlerOutputQuantities[i][4]++;
                else if (resourceType == JOLLY)
                    prodHandlerOutputQuantities[i][5]++;
                else
                    System.out.println("Warning: found unsupported ResourceType inside ProductionHandler during save: " + resourceType);
        }
    }

    private void saveWarehouses(Game game) {
        int numOfPlayers = game.getPlayersBoardsTurnOrder().size();
        basicDepotNum = new int[numOfPlayers];
        depotType = new ResourceType[numOfPlayers][];
        depotQuantity = new int[numOfPlayers][];
        depotSize = new int[numOfPlayers][];
        for (int i = 0; i < game.getPlayersBoardsTurnOrder().size(); i++) {
            basicDepotNum[i] = game.getPlayersBoardsTurnOrder().get(i).getWarehouse().getNumOfDepots();
            depotType[i] = new ResourceType[game.getPlayersBoardsTurnOrder().get(i).getWarehouse().getNumOfDepots()];
            depotQuantity[i] = new int[depotType[i].length];
            depotSize[i] = new int[depotType[i].length];

            for (int j = 0; j < game.getPlayersBoardsTurnOrder().get(i).getWarehouse().getNumOfDepots(); j++) {
                if (game.getPlayersBoardsTurnOrder().get(i).getWarehouse().getDepot(j + 1).getStoredResources().size() > 0)
                    depotType[i][j] = game.getPlayersBoardsTurnOrder().get(i).getWarehouse().getDepot(j + 1).getStoredResources().get(0);
                depotQuantity[i][j] = game.getPlayersBoardsTurnOrder().get(i).getWarehouse().getDepot(j + 1).getNumOfResource(depotType[i][j]);
                depotSize[i][j] = game.getPlayersBoardsTurnOrder().get(i).getWarehouse().getDepot(j + 1).getSize();
            }
        }
    }

    private void saveStrongboxes(Game game) {
        int numOfPlayers = game.getPlayersBoardsTurnOrder().size();
        strongboxTypes = new ResourceType[numOfPlayers][];
        strongboxQuantities = new int[numOfPlayers][];
        for (int i = 0; i < game.getPlayersBoardsTurnOrder().size(); i++) {
            strongboxTypes[i] = new ResourceType[]{COIN, SERVANT, SHIELD, STONE};
            strongboxQuantities[i] = new int[strongboxTypes[i].length];
            for (ResourceType resourceType : game.getPlayersBoardsTurnOrder().get(i).getStrongbox().getStoredResources()) {
                if (resourceType == COIN)
                    strongboxQuantities[i][0]++;
                else if (resourceType == SERVANT)
                    strongboxQuantities[i][1]++;
                else if (resourceType == SHIELD)
                    strongboxQuantities[i][2]++;
                else if (resourceType == STONE)
                    strongboxQuantities[i][3]++;
                else
                    System.out.println("Warning: found unsupported ResourceType inside Strongbox during save: " + resourceType);
            }
        }
    }

    private void saveWaitingRooms(Game game) {
        int numOfPlayers = game.getPlayersBoardsTurnOrder().size();
        waitingRoomTypes = new ResourceType[numOfPlayers][];
        waitingRoomQuantities = new int[numOfPlayers][];
        for (int i = 0; i < game.getPlayersBoardsTurnOrder().size(); i++) {
            waitingRoomTypes[i] = new ResourceType[]{COIN, SERVANT, SHIELD, STONE};
            waitingRoomQuantities[i] = new int[waitingRoomTypes[i].length];
            for (ResourceType resourceType : game.getPlayersBoardsTurnOrder().get(i).getStrongbox().getStoredResources()) {
                if (resourceType == COIN)
                    waitingRoomQuantities[i][0]++;
                else if (resourceType == SERVANT)
                    waitingRoomQuantities[i][1]++;
                else if (resourceType == SHIELD)
                    waitingRoomQuantities[i][2]++;
                else if (resourceType == STONE)
                    waitingRoomQuantities[i][3]++;
                else
                    System.out.println("Warning: found unsupported ResourceType inside Waiting Room during save: " + resourceType);
            }
        }
    }

    private void saveLorenzo(Game game) {
        Lorenzo lorenzo = (Lorenzo) game.getLorenzo();

        if (lorenzo != null) {

            lorenzoFaith = lorenzo.getFaith();

            List<ActionToken> usedTokens = lorenzo.getUsedDeck();
            lorenzoUsedTokens = new LorenzoTokenType[usedTokens.size()];
            for (int i = 0; i < lorenzoUsedTokens.length; i++)
                lorenzoUsedTokens[i] = usedTokens.get(i).getType();
        }
    }

    // PRIVATE RESTORING METHODS

    private Game restoreGameStatus() {
        Game game = new Game(Arrays.stream(playersTurnOrder).collect(Collectors.toSet()));
        game.restorePlayerTurnOrder(playersTurnOrder);
        game.restoreLastTriggeredTile(lastTriggeredTile);
        game.restoreTurnPhase(turnPhase);
        game.restoreIsLastTurn(isLastTurn);
        game.restoreWinner(winner);
        game.restoreWinnerVp(winnerVp);

        return game;
    }

    private void restoreMarket(Game game) {
        game.getMarket().restoreBoard(marketBoard);
        game.getMarket().restoreSlideMarble(slideMarble);
    }

    private void restoreCardTable(Game game) {
        game.getCardTable().restoreCards(cardTable);
    }

    private void restorePlayerBoards(Game game) {
        for (int i = 0; i < game.getPlayersBoardsTurnOrder().size(); i++) {
            PlayerBoard player = game.getPlayersBoardsTurnOrder().get(i);

            // FAITH
            player.restoreFaith(faith[i]);

            // WHITE MARBLES
            player.restoreWhiteMarbleNum(whiteMarbleNum[i]);

            // CARD SLOTS
            player.restoreCardSlots(cardSlots[i], game);

            // LEADER CARDS
            player.restoreLeaderCards(leaderCards[i], activeLeaderCards[i], firstTurnTaken[i], game);

            // POPE FAVOR TILES
            player.restorePopeTileState(popeTileStates[i]);

            // FIRST TURN TAKEN
            player.restoreFirstTurnTaken(firstTurnTaken[i]);
        }
    }

    private void restoreProductionHandlers(Game game) {
        for (int i = 0; i < game.getPlayersBoardsTurnOrder().size(); i++) {
            ProductionHandler productionHandler = game.getPlayersBoardsTurnOrder().get(i).getProductionHandler();
            productionHandler.restoreProductions(activeProductions[i]);
            productionHandler.restoreCurrentInput(prodHandlerInputResources[i], prodHandlerInputQuantities[i]);
            productionHandler.restoreCurrentOutput(prodHandlerOutputResources[i], prodHandlerOutputQuantities[i]);
        }
    }

    private void restoreWarehouses(Game game) {
        for (int i = 0; i < game.getPlayersBoardsTurnOrder().size(); i++) {
            Warehouse warehouse = game.getPlayersBoardsTurnOrder().get(i).getWarehouse();
            warehouse.restoreDepots(depotType[i], depotQuantity[i]);
        }
    }

    private void restoreStrongboxes(Game game) {
        for (int i = 0; i < game.getPlayersBoardsTurnOrder().size(); i++) {
            UnlimitedStorage strongbox = game.getPlayersBoardsTurnOrder().get(i).getStrongbox();
            strongbox.restoreContent(strongboxTypes[i], strongboxQuantities[i]);
        }
    }

    private void restoreWaitingRooms(Game game) {
        for (int i = 0; i < game.getPlayersBoardsTurnOrder().size(); i++) {
            UnlimitedStorage waitingRoom = game.getPlayersBoardsTurnOrder().get(i).getWaitingRoom();
            waitingRoom.restoreContent(waitingRoomTypes[i], waitingRoomQuantities[i]);
        }
    }

    private void restoreLorenzo(Game game) {
        Lorenzo lorenzo = (Lorenzo) game.getLorenzo();
        if (lorenzo != null) {
            lorenzo.restoreFaith(lorenzoFaith);
            lorenzo.restoreTokens(lorenzoUsedTokens);
        }
    }

    private void restoreEndTurn(Game game) {
        if (game.getPlayer(currentPlayer) != null)
            game.getPlayer(currentPlayer).restoreEndTurn(turnPhase, game);
    }

    // PRINTING METHODS

    @Override
    public String toString() {
        return "PersistenceHandler{" +
                ",\n cards=" + cardTable +
                ",\n playersTurnOrder=" + Arrays.toString(playersTurnOrder) +
                ",\n currentPlayer='" + currentPlayer + '\'' +
                ",\n username=" + Arrays.toString(username) +
                ",\n lastTriggeredTile=" + lastTriggeredTile +
                ",\n turnPhase=" + turnPhase +
                ",\n isLastTurn=" + isLastTurn +
                ",\n winner='" + winner + '\'' +
                ",\n winnerVp=" + winnerVp +
                ",\n marketBoard=" + Arrays.toString(marketBoard) +
                ",\n slideMarble=" + slideMarble +
                ",\n popeTileStates=" + Arrays.toString(popeTileStates) +
                ",\n whiteMarbleNum=" + Arrays.toString(whiteMarbleNum) +
                ",\n faith=" + Arrays.toString(faith) +
                ",\n cardSlots=" + Arrays.toString(cardSlots) +
                ",\n leaderCards=" + Arrays.toString(leaderCards) +
                ",\n activeProductions=" + Arrays.toString(activeProductions) +
                ",\n prodHandlerInputResources=" + Arrays.toString(prodHandlerInputResources) +
                ",\n prodHandlerInputQuantities=" + Arrays.toString(prodHandlerInputQuantities) +
                ",\n prodHandlerOutputResources=" + Arrays.toString(prodHandlerOutputResources) +
                ",\n prodHandlerOutputQuantities=" + Arrays.toString(prodHandlerOutputQuantities) +
                ",\n basicDepotNum=" + Arrays.toString(basicDepotNum) +
                ",\n depotType=" + Arrays.toString(depotType) +
                ",\n depotQuantity=" + Arrays.toString(depotQuantity) +
                ",\n depotSize=" + Arrays.toString(depotSize) +
                ",\n strongboxTypes=" + Arrays.toString(strongboxTypes) +
                ",\n strongboxQuantities=" + Arrays.toString(strongboxQuantities) +
                ",\n waitingRoomTypes=" + Arrays.toString(waitingRoomTypes) +
                ",\n waitingRoomQuantities=" + Arrays.toString(waitingRoomQuantities) +
                '}';
    }

    // GETTERS

    public int getId() {
        return id;
    }
}
