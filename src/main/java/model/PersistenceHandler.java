package model;

import model.card.DevelopmentCard;
import model.card.leadercard.*;
import model.resource.Resource;
import model.resource.ResourceType;
import model.storage.UnlimitedStorage;
import model.storage.Warehouse;
import network.beans.SlotBean;
import server.GameController;

import java.util.*;
import java.util.stream.Collectors;

import static model.resource.ResourceType.*;

public class PersistenceHandler {

    private transient final GameController controller;

    private Map<CardColor, List<List<DevelopmentCard>>> cards;

    private int[][][] cardTable;

    private String[] playersTurnOrder;

    private String currentPlayer;

    private String[] username;

    private int lastTriggeredTile;

    private TurnPhase turnPhase;

    private boolean weReInTheEndGameNow;

    private String winner;

    private int winnerVp;

    private ResourceType[][] marketBoard;

    private ResourceType slideMarble;

    private PopeTileState[][] popeTileStates;

    private int[] whiteMarbleNum;

    private int[] faith;

    private ResourceType[][] marbleConversions;

    private ResourceType[][] discountType;

    private int[][] discountQuantity;

    private SlotBean[][] cardSlots;

    private int[][] leaderCards;

    private int[][] leaderDepotCardsWarehouse;

    private int[][] leaderDepotCardsLeaderCard;

    private int[][] vpFaithTiles;

    private int[][] vpFaithValues;

    private int[][] productions;

    private boolean[][] activeProductions;

    private int idProduction;

    private List<Resource> inputv;

    private List<Resource> outputv;

    private boolean selectedByHandler;

    private ResourceType[][] prodHandlerInputResources;

    private int[][] prodHandlerInputQuantities;

    private ResourceType[][] prodHandlerOutputResources;

    private int[][] prodHandlerOutputQuantities;

    private int sizeBasicDepot;

    private ResourceType storedResourceInBasicDepot;

    private int amountInBasicDepot;

    private int[] basicDepotNum;

    private ResourceType[][] depotType;

    private int[][] depotQuantity;

    private int[][] depotSize;

    private ResourceType[][] strongboxTypes;

    private int[][] strongboxQuantities;

    private ResourceType[][] waitingRoomTypes;

    private int[][] waitingRoomQuantities;

    // CONSTRUCTORS

    private PersistenceHandler(GameController controller) {
        this.controller = controller;
    }

    // PRIVATE METHODS

    private void saveGameStatus(Game game) {
        playersTurnOrder = new String[game.getPlayersTurnOrder().size()];
        for (int i = 0; i < game.getPlayersTurnOrder().size(); i++)
            playersTurnOrder[i] = game.getPlayersTurnOrder().get(i).getUsername();
        currentPlayer = game.getCurrentPlayer().getUsername();
        lastTriggeredTile = game.getLastTriggeredTile();
        turnPhase = game.getTurnPhase();
        weReInTheEndGameNow = game.isEndGame();
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
        /*cardTable = new int[3][game.getCardTable().getCards().entrySet().size()][];
        cards = game.getCardTable().getCards();

        for (int i = 0; i < cardTable.length; i++) {
            for (int j = 0; j < 3; j++) {
                for (Map.Entry<CardColor, List<List<DevelopmentCard>>> color : cards.entrySet()) {
                    if (color.getValue().get(i).size() == 0)
                        cardTable[i][j] = -1;
                    else
                        cardTable[i][j] = color.getValue().get(i).get(j).getId();
                    j++;
                }
            }
        }*/
    }

    private void savePlayerBoards(Game game) {
        int numOfPlayers = game.getPlayersTurnOrder().size();
        username = new String[numOfPlayers];
        popeTileStates = new PopeTileState[numOfPlayers][];
        whiteMarbleNum = new int[numOfPlayers];
        faith = new int[numOfPlayers];
        marbleConversions = new ResourceType[numOfPlayers][];
        discountType = new ResourceType[numOfPlayers][];
        discountQuantity = new int[numOfPlayers][];
        cardSlots = new SlotBean[numOfPlayers][];
        vpFaithTiles = new int[numOfPlayers][];
        leaderCards = new int[numOfPlayers][];

        for (int i = 0; i < game.getPlayersTurnOrder().size(); i++) {

            // USERNAME

            username[i] = game.getPlayersTurnOrder().get(i).getUsername();

            // FAITH

            faith[i] = game.getPlayersTurnOrder().get(i).getFaith();

            // WHITE MARBLES

            whiteMarbleNum[i] = game.getPlayersTurnOrder().get(i).getWhiteMarbles();

            // MARBLE CONVERSIONS

            int j = 0;
            marbleConversions[i] = new ResourceType[game.getPlayersTurnOrder().get(i).getMarbleConversions().size()];
            for (ResourceType resourceType : game.getPlayersTurnOrder().get(i).getMarbleConversions()) {
                marbleConversions[i][j] = resourceType;
                j++;
            }

            // DISCOUNTS

            j = 0;
            discountType[i] = new ResourceType[]{COIN, SERVANT, SHIELD, STONE};
            discountQuantity[i] = new int[discountType[i].length];
            for (Map.Entry<ResourceType, Integer> entry : game.getPlayersTurnOrder().get(i).getDiscounts().entrySet()) {
                discountType[i][j] = entry.getKey();
                discountQuantity[i][j] = entry.getValue();
                j++;
            }

            // CARD SLOTS

            cardSlots[i] = new SlotBean[game.getPlayersTurnOrder().get(i).getCardSlots().size()];
            for (j = 0; j < 3; j++) {
                cardSlots[i][j] = new SlotBean();
                cardSlots[i][j].setDevelopmentCardsFromPB(game.getPlayersTurnOrder().get(i), j + 1);
            }

            // LEADER CARDS

            j = 0;
            leaderCards[i] = new int[game.getPlayersTurnOrder().get(i).getLeaderCards().size()];
            for (LeaderCard leaderCard : game.getPlayersTurnOrder().get(i).getLeaderCards()) {
                leaderCards[i][j++] = leaderCard.getId();
            }

            j = 0;
            for (Map.Entry<Integer, Integer> entry : game.getPlayersTurnOrder().get(i).getLeaderDepotCards().entrySet()) {
                leaderDepotCardsWarehouse[i][j] = entry.getKey();
                leaderDepotCardsLeaderCard[i][j] = entry.getValue();
                j++;
            }

            // VICTORY POINTS

            int[] faithTiles = game.getPlayersTurnOrder().get(i).getVpFaithTiles();
            vpFaithTiles[i] = new int[faithTiles.length];
            for (j = 0; j < vpFaithTiles[i].length; j++)
                vpFaithTiles[i][j] = faithTiles[j];

            int[] faithValues = game.getPlayersTurnOrder().get(i).getVpFaithValues();
            vpFaithValues[i] = new int[faithValues.length];
            for (j = 0; j < vpFaithValues[i].length; j++)
                vpFaithValues[i][j] = faithValues[j];

            // POPE FAVOR TILES

            List<PopeFavorTile> favorTiles = game.getPlayersTurnOrder().get(i).getPopeFavorTiles();
            popeTileStates[i] = new PopeTileState[favorTiles.size()];
            for (j = 0; j < popeTileStates[i].length; j++)
                popeTileStates[i][j] = favorTiles.get(i).getState();

        }
    }

    private void saveProductionHandlers(Game game) {
        int numOfPlayers = game.getPlayersTurnOrder().size();
        productions = new int[numOfPlayers][];
        activeProductions = new boolean[numOfPlayers][];
        prodHandlerInputResources = new ResourceType[numOfPlayers][];
        prodHandlerInputQuantities = new int[numOfPlayers][];
        prodHandlerOutputResources = new ResourceType[numOfPlayers][];
        prodHandlerOutputQuantities = new int[numOfPlayers][];

        for (int i = 0; i < game.getPlayersTurnOrder().size(); i++) {
            ProductionHandler productionHandler = game.getPlayersTurnOrder().get(i).getProductionHandler();
            productions[i] = new int[productionHandler.getProductions().size()];
            activeProductions[i] = new boolean[productions.length];
            for (int j = 0; j < productions.length; j++) {
                productions[i][j] = productionHandler.getProductions().get(j).getId();
                if (productionHandler.getProductions().get(j).isSelectedByHandler())
                    activeProductions[i][j] = true;
                else
                    activeProductions[i][j] = false;
            }

            prodHandlerInputResources[i] = new ResourceType[]{COIN, SERVANT, SHIELD, STONE, JOLLY};
            prodHandlerInputQuantities[i] = new int[prodHandlerInputResources.length];

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
            prodHandlerOutputQuantities[i] = new int[prodHandlerOutputResources.length];

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
        int numOfPlayers = game.getPlayersTurnOrder().size();
        basicDepotNum = new int[numOfPlayers];
        depotType = new ResourceType[numOfPlayers][];
        depotQuantity = new int[numOfPlayers][];
        depotSize = new int[numOfPlayers][];
        for (int i = 0; i < game.getPlayersTurnOrder().size(); i++) {
            basicDepotNum[i] = game.getPlayersTurnOrder().get(i).getWarehouse().getNumOfDepots();
            depotType[i] = new ResourceType[game.getPlayersTurnOrder().get(i).getWarehouse().getNumOfDepots()];
            depotQuantity[i] = new int[depotType.length];
            depotSize[i] = new int[depotType.length];

            for (int j = 0; j < game.getPlayersTurnOrder().get(i).getWarehouse().getNumOfDepots(); j++) {
                if (game.getPlayersTurnOrder().get(i).getWarehouse().getDepot(j + 1).getStoredResources().size() > 0)
                    depotType[i][j] = game.getPlayersTurnOrder().get(i).getWarehouse().getDepot(j + 1).getStoredResources().get(0);
                depotQuantity[i][j] = game.getPlayersTurnOrder().get(i).getWarehouse().getDepot(j + 1).getNumOfResource(depotType[i][j]);
                depotSize[i][j] = game.getPlayersTurnOrder().get(i).getWarehouse().getDepot(j + 1).getSize();
            }
        }
    }

    private void saveStrongboxes(Game game) {
        int numOfPlayers = game.getPlayersTurnOrder().size();
        strongboxTypes = new ResourceType[numOfPlayers][];
        strongboxQuantities = new int[numOfPlayers][];
        for (int i = 0; i < game.getPlayersTurnOrder().size(); i++) {
            strongboxTypes[i] = new ResourceType[]{COIN, SERVANT, SHIELD, STONE};
            strongboxQuantities[i] = new int[strongboxTypes[i].length];
            for (ResourceType resourceType : game.getPlayersTurnOrder().get(i).getStrongbox().getStoredResources()) {
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
        int numOfPlayers = game.getPlayersTurnOrder().size();
        waitingRoomTypes = new ResourceType[numOfPlayers][];
        waitingRoomQuantities = new int[numOfPlayers][];
        for (int i = 0; i < game.getPlayersTurnOrder().size(); i++) {
            waitingRoomTypes[i] = new ResourceType[]{COIN, SERVANT, SHIELD, STONE, JOLLY};
            waitingRoomQuantities[i] = new int[waitingRoomTypes[i].length];
            for (ResourceType resourceType : game.getPlayersTurnOrder().get(i).getStrongbox().getStoredResources()) {
                if (resourceType == COIN)
                    waitingRoomQuantities[i][0]++;
                else if (resourceType == SERVANT)
                    waitingRoomQuantities[i][1]++;
                else if (resourceType == SHIELD)
                    waitingRoomQuantities[i][2]++;
                else if (resourceType == STONE)
                    waitingRoomQuantities[i][3]++;
                else if (resourceType == JOLLY)
                    waitingRoomQuantities[i][4]++;
                else
                    System.out.println("Warning: found unsupported ResourceType inside Strongbox during save: " + resourceType);
            }
        }
    }

    private Game restoreGameStatus() {
        Set<String> usernames = Arrays.stream(playersTurnOrder).collect(Collectors.toSet());
        Game game = new Game(usernames);
        game.restoreCurrentPlayer(currentPlayer);
        game.restoreLastTriggeredTile(lastTriggeredTile);
        game.restoreTurnPhase(turnPhase);
        game.restoreIsLastTurn(weReInTheEndGameNow);
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
        for (int i = 0; i < game.getPlayersTurnOrder().size(); i++) {
            PlayerBoard player = game.getPlayersTurnOrder().get(i);

            // FAITH
            player.restoreFaith(faith[i]);

            // WHITE MARBLES
            player.restoreMarbleConversions(marbleConversions[i]);

            // MARBLE CONVERSIONS
            player.restoreMarbleConversions(marbleConversions[i]);

            // DISCOUNTS
            player.restoreDiscounts(discountType[i], discountQuantity[i]);

            // CARD SLOTS
            player.restoreCardSlots(cardSlots[i]);

            // LEADER CARDS
            player.restoreLeaderCards(leaderCards[i], leaderDepotCardsWarehouse[i], leaderDepotCardsLeaderCard[i]);

            // VICTORY POINTS
            player.restoreTilesVictoryPoints(vpFaithTiles[i], vpFaithValues[i]);

            // POPE FAVOR TILES
            player.restorePopeTileState(popeTileStates[i]);

        }
    }

    private void restoreProductionHandlers(Game game) {
        for (int i = 0; i < game.getPlayersTurnOrder().size(); i++) {
            ProductionHandler productionHandler = game.getPlayersTurnOrder().get(i).getProductionHandler();
            productionHandler.restoreProductions(productions[i]);
            productionHandler.restoreCurrentInput(prodHandlerInputResources[i], prodHandlerInputQuantities[i]);
            productionHandler.restoreCurrentOutput(prodHandlerOutputResources[i], prodHandlerOutputQuantities[i]);
        }
    }

    private void restoreWarehouses(Game game) {
        for (int i = 0; i < game.getPlayersTurnOrder().size(); i++) {
            Warehouse warehouse = game.getPlayersTurnOrder().get(i).getWarehouse();
            warehouse.restoreDepots(depotType[i], depotQuantity[i]);
        }
    }

    private void restoreStrongboxes(Game game) {
        for (int i = 0; i < game.getPlayersTurnOrder().size(); i++) {
            UnlimitedStorage strongbox = game.getPlayersTurnOrder().get(i).getStrongbox();
            strongbox.restoreContent(strongboxTypes[i], strongboxQuantities[i]);
        }
    }

    private void restoreWaitingRooms(Game game) {
        for (int i = 0; i < game.getPlayersTurnOrder().size(); i++) {
            UnlimitedStorage waitingRoom = game.getPlayersTurnOrder().get(i).getWaitingRoom();
            waitingRoom.restoreContent(strongboxTypes[i], strongboxQuantities[i]);
        }
    }

    // PUBLIC METHODS

    public void saveGame(Game game) {
        if (game == null)
            throw new RuntimeException("PersistenceHandler received a null pointer");

        saveGameStatus(game);
        saveMarket(game);
        saveCardTable(game);
        savePlayerBoards(game);
        saveProductionHandlers(game);
        saveWarehouses(game);
        saveStrongboxes(game);
        saveWaitingRooms(game);
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

        return game;
    }
}
