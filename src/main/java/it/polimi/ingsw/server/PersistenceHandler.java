package it.polimi.ingsw.server;

import it.polimi.ingsw.Exceptions.ParametersNotValidException;
import it.polimi.ingsw.StaticMethods;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.leadercard.LeaderCard;
import it.polimi.ingsw.model.lorenzo.Lorenzo;
import it.polimi.ingsw.model.lorenzo.tokens.ActionToken;
import it.polimi.ingsw.model.lorenzo.tokens.LorenzoTokenType;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.model.storage.UnlimitedStorage;
import it.polimi.ingsw.model.storage.Warehouse;
import it.polimi.ingsw.network.beans.SlotBean;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.polimi.ingsw.model.resource.ResourceType.*;

/**
 * This class is used by the GameController class to save its game's state on disk after every user command,
 * and by the lobby to restore said games at the start of the server if the server was interrupted during their course.
 */
public class PersistenceHandler {
    /**
     * The game's id, used to differentiate between different save files
     */
    private int id;
    /**
     * The game's card table
     */
    private Map<CardColor, List<List<DevelopmentCard>>> cardTable;
    /**
     * The players' usernames arranged by turn order
     */
    private String[] playersTurnOrder;
    /**
     * The current player's username
     */
    private String currentPlayer;
    /**
     * The last triggered pope's favor tile
     */
    private int lastTriggeredTile;
    /**
     * The current turn phase
     */
    private TurnPhase turnPhase;
    /**
     * Whether or not the game is in its last turn
     */
    private boolean isLastTurn;
    /**
     * The market board
     */
    private ResourceType[][] marketBoard;
    /**
     * The marble on the market's slide
     */
    private ResourceType slideMarble;
    /**
     * The pope's favor tiles' state for each player
     */
    private PopeTileState[][] popeTileStates;
    /**
     * The number of unconverted white marbles / bonus resources for each player
     */
    private int[] resourcesToConvert;
    /**
     * The faith score for each player
     */
    private int[] faith;
    /**
     * Whether or not each player has completed the leader selection
     */
    private boolean[] firstTurnTaken;
    /**
     * The card slots' card's id for each player
     */
    private SlotBean[][] cardSlots;
    /**
     * The leader cards' id for each player
     */
    private int[][] leaderCards;
    /**
     * Which leader cards are active for each player
     */
    private boolean[][] activeLeaderCards;
    /**
     * Which productions are active for each player
     */
    private boolean[][] activeProductions;
    /**
     * Which resources are in production handler input for each player
     */
    private ResourceType[][] prodHandlerInputResources;
    /**
     * Which amounts of resources are in production handler input for each player
     */
    private int[][] prodHandlerInputQuantities;
    /**
     * Which resources are in production handler output for each player
     */
    private ResourceType[][] prodHandlerOutputResources;
    /**
     * Which amounts of resources are in production handler output for each player
     */
    private int[][] prodHandlerOutputQuantities;
    /**
     * Which resources are contained in each depot for each player
     */
    private ResourceType[][] depotType;
    /**
     * Which amounts of resources are contained in each depot for each player
     */
    private int[][] depotQuantity;
    /**
     * Which resources are contained in the strongbox for each player
     */
    private ResourceType[][] strongboxTypes;
    /**
     * Which amounts of resources are contained in the strongbox for each player
     */
    private int[][] strongboxQuantities;
    /**
     * Which resources are contained in the waiting room for each player
     */
    private ResourceType[][] waitingRoomTypes;
    /**
     * Which amounts of resources are contained in the waiting room for each player
     */
    private int[][] waitingRoomQuantities;
    /**
     * The game AI's faith score
     */
    private int lorenzoFaith;
    /**
     * The game AI's discarded tokens
     */
    private LorenzoTokenType[] lorenzoUsedTokens;

    // PUBLIC METHODS

    /**
     * Saves the current game state inside the class' attributes, and then on a file bearing the id as part of its name
     *
     * @param game the Game to be saved
     */
    public void saveGame(Game game) {
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

        if (id == 0)
            id = StaticMethods.findFirstFreePersistenceId();

        StaticMethods.saveGameOnDisk(this);
    }

    /**
     * Retrieves the game file bearing the class' same id from the file system, and restores it
     *
     * @return the restored Game
     */
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

    /**
     * Saves the game's status, meaning most of the Game's class' contents
     *
     * @param game the Game class
     */
    private void saveGameStatus(Game game) {
        playersTurnOrder = new String[game.getPlayersUsernamesTurnOrder().size()];
        for (int i = 0; i < game.getPlayersUsernamesTurnOrder().size(); i++)
            playersTurnOrder[i] = game.getPlayersUsernamesTurnOrder().get(i);
        currentPlayer = game.getCurrentPlayer().getUsername();
        turnPhase = game.getTurnPhase();
        lastTriggeredTile = game.getLastTriggeredTile();
        isLastTurn = game.isLastTurn();
    }

    /**
     * Saves the market
     *
     * @param game the Game class
     */
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

    /**
     * Saves the card table
     *
     * @param game the Game class
     */
    private void saveCardTable(Game game) {
        cardTable = game.getCardTable().getCards();
    }

    /**
     * Saves the player boards
     *
     * @param game the Game class
     */
    private void savePlayerBoards(Game game) {
        int numOfPlayers = game.getPlayersBoardsTurnOrder().size();
        firstTurnTaken = new boolean[numOfPlayers];
        popeTileStates = new PopeTileState[numOfPlayers][];
        resourcesToConvert = new int[numOfPlayers];
        faith = new int[numOfPlayers];
        cardSlots = new SlotBean[numOfPlayers][];
        leaderCards = new int[numOfPlayers][];
        activeLeaderCards = new boolean[numOfPlayers][];

        for (int i = 0; i < game.getPlayersBoardsTurnOrder().size(); i++) {

            PlayerBoard player = game.getPlayersBoardsTurnOrder().get(i);

            // FAITH

            faith[i] = player.getFaith();

            //HAS TAKEN FIRST TURN

            firstTurnTaken[i] = player.isFirstTurnTaken();

            // RESOURCES TO CONVERT

            resourcesToConvert[i] = player.getResourcesToConvert();

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

    /**
     * Saves the production handlers
     *
     * @param game the Game class
     */
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

    /**
     * Saves the warehouses
     *
     * @param game the Game class
     */
    private void saveWarehouses(Game game) {
        int numOfPlayers = game.getPlayersBoardsTurnOrder().size();
        depotType = new ResourceType[numOfPlayers][];
        depotQuantity = new int[numOfPlayers][];
        for (int i = 0; i < game.getPlayersBoardsTurnOrder().size(); i++) {
            depotType[i] = new ResourceType[game.getPlayersBoardsTurnOrder().get(i).getWarehouse().getNumOfDepots()];
            depotQuantity[i] = new int[depotType[i].length];

            for (int j = 0; j < game.getPlayersBoardsTurnOrder().get(i).getWarehouse().getNumOfDepots(); j++) {
                if (game.getPlayersBoardsTurnOrder().get(i).getWarehouse().getDepot(j + 1).getStoredResources().size() > 0)
                    depotType[i][j] = game.getPlayersBoardsTurnOrder().get(i).getWarehouse().getDepot(j + 1).getStoredResources().get(0);
                depotQuantity[i][j] = game.getPlayersBoardsTurnOrder().get(i).getWarehouse().getDepot(j + 1).getNumOfResource(depotType[i][j]);
            }
        }
    }

    /**
     * Saves the strongboxes
     *
     * @param game the Game class
     */
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

    /**
     * Saves the waiting rooms
     *
     * @param game the Game class
     */
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

    /**
     * Saves the game's AI
     *
     * @param game the Game class
     */
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

    /**
     * Restores the game's status
     *
     * @return the restored Game class
     */
    private Game restoreGameStatus() {
        Game game = new Game(Arrays.stream(playersTurnOrder).collect(Collectors.toSet()));
        game.restorePlayerTurnOrder(playersTurnOrder);
        game.restoreLastTriggeredTile(lastTriggeredTile);
        game.restoreIsLastTurn(isLastTurn);

        return game;
    }

    /**
     * Restores the marker
     *
     * @param game the restored Game class
     */
    private void restoreMarket(Game game) {
        game.getMarket().restoreBoard(marketBoard);
        game.getMarket().restoreSlideMarble(slideMarble);
    }

    /**
     * Restores the card table
     *
     * @param game the restored Game class
     */
    private void restoreCardTable(Game game) {
        game.getCardTable().restoreCards(cardTable);
    }

    /**
     * Restores the player boards
     *
     * @param game the restored Game class
     */
    private void restorePlayerBoards(Game game) {
        for (PlayerBoard player : game.getPlayersBoardsTurnOrder()) {
            int i;
            for (i = 0; i < playersTurnOrder.length; i++)
                if (playersTurnOrder[i].equals(player.getUsername()))
                    break;

            if (i == playersTurnOrder.length)
                System.err.println("Was not able to find player " + player.getUsername() + " in turn order when restoring.");

            // FAITH
            player.restoreFaith(faith[i]);

            // RESOURCES TO CONVERT
            player.restoreResourcesToConvert(resourcesToConvert[i]);

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

    /**
     * Restores the production handlers
     *
     * @param game the restored Game class
     */
    private void restoreProductionHandlers(Game game) {
        for (PlayerBoard player : game.getPlayersBoardsTurnOrder()) {
            int i;
            for (i = 0; i < playersTurnOrder.length; i++)
                if (playersTurnOrder[i].equals(player.getUsername()))
                    break;

            if (i == playersTurnOrder.length)
                System.err.println("Was not able to find player " + player.getUsername() + " in turn order when restoring.");


            ProductionHandler productionHandler = player.getProductionHandler();

            productionHandler.restoreProductions(activeProductions[i]);
            productionHandler.restoreCurrentInput(prodHandlerInputResources[i], prodHandlerInputQuantities[i]);
            productionHandler.restoreCurrentOutput(prodHandlerOutputResources[i], prodHandlerOutputQuantities[i]);
        }
    }

    /**
     * Restores the warehouses
     *
     * @param game the restored Game class
     */
    private void restoreWarehouses(Game game) {
        for (PlayerBoard player : game.getPlayersBoardsTurnOrder()) {
            int i;
            for (i = 0; i < playersTurnOrder.length; i++)
                if (playersTurnOrder[i].equals(player.getUsername()))
                    break;

            if (i == playersTurnOrder.length)
                System.err.println("Was not able to find player " + player.getUsername() + " in turn order when restoring.");

            Warehouse warehouse = player.getWarehouse();

            warehouse.restoreDepots(depotType[i], depotQuantity[i]);
        }
    }

    /**
     * Restores the strongboxes
     *
     * @param game the restored Game class
     */
    private void restoreStrongboxes(Game game) {
        for (PlayerBoard player : game.getPlayersBoardsTurnOrder()) {
            int i;
            for (i = 0; i < playersTurnOrder.length; i++)
                if (playersTurnOrder[i].equals(player.getUsername()))
                    break;

            if (i == playersTurnOrder.length)
                System.err.println("Was not able to find player " + player.getUsername() + " in turn order when restoring.");

            UnlimitedStorage strongbox = player.getStrongbox();

            strongbox.restoreContent(strongboxTypes[i], strongboxQuantities[i]);
        }
    }

    /**
     * Restores the waiting rooms
     *
     * @param game the restored Game class
     */
    private void restoreWaitingRooms(Game game) {
        for (PlayerBoard player : game.getPlayersBoardsTurnOrder()) {
            int i;
            for (i = 0; i < playersTurnOrder.length; i++)
                if (playersTurnOrder[i].equals(player.getUsername()))
                    break;

            if (i == playersTurnOrder.length)
                System.err.println("Was not able to find player " + player.getUsername() + " in turn order when restoring.");

            UnlimitedStorage waitingRoom = player.getWaitingRoom();

            waitingRoom.restoreContent(waitingRoomTypes[i], waitingRoomQuantities[i]);
        }
    }

    /**
     * Restores the game's AI
     *
     * @param game the restored Game class
     */
    private void restoreLorenzo(Game game) {
        Lorenzo lorenzo = (Lorenzo) game.getLorenzo();
        if (lorenzo != null) {
            lorenzo.restoreFaith(lorenzoFaith);
            lorenzo.restoreTokens(lorenzoUsedTokens);
        }
    }

    /**
     * Carries out any necessary clean-up operation for the last turn that got saved
     *
     * @param game the restored Game class
     */
    private void restoreEndTurn(Game game) {
        if (game.getPlayer(currentPlayer) != null)
            game.getPlayer(currentPlayer).restoreEndTurn(turnPhase, game);
    }

    // PRINTING METHODS

    /**
     * Returns a String representation of the class' contents
     *
     * @return the class' String representation
     */
    @Override
    public String toString() {
        return "PersistenceHandler{" +
                ",\n cards=" + cardTable +
                ",\n playersTurnOrder=" + Arrays.toString(playersTurnOrder) +
                ",\n currentPlayer='" + currentPlayer + '\'' +
                ",\n lastTriggeredTile=" + lastTriggeredTile +
                ",\n turnPhase=" + turnPhase +
                ",\n isLastTurn=" + isLastTurn +
                ",\n marketBoard=" + Arrays.toString(marketBoard) +
                ",\n slideMarble=" + slideMarble +
                ",\n popeTileStates=" + Arrays.toString(popeTileStates) +
                ",\n resourcesToConvert=" + Arrays.toString(resourcesToConvert) +
                ",\n faith=" + Arrays.toString(faith) +
                ",\n cardSlots=" + Arrays.toString(cardSlots) +
                ",\n leaderCards=" + Arrays.toString(leaderCards) +
                ",\n activeProductions=" + Arrays.toString(activeProductions) +
                ",\n prodHandlerInputResources=" + Arrays.toString(prodHandlerInputResources) +
                ",\n prodHandlerInputQuantities=" + Arrays.toString(prodHandlerInputQuantities) +
                ",\n prodHandlerOutputResources=" + Arrays.toString(prodHandlerOutputResources) +
                ",\n prodHandlerOutputQuantities=" + Arrays.toString(prodHandlerOutputQuantities) +
                ",\n depotType=" + Arrays.toString(depotType) +
                ",\n depotQuantity=" + Arrays.toString(depotQuantity) +
                ",\n strongboxTypes=" + Arrays.toString(strongboxTypes) +
                ",\n strongboxQuantities=" + Arrays.toString(strongboxQuantities) +
                ",\n waitingRoomTypes=" + Arrays.toString(waitingRoomTypes) +
                ",\n waitingRoomQuantities=" + Arrays.toString(waitingRoomQuantities) +
                '}';
    }

    // GETTERS

    /**
     * Returns the handler's id, used in naming the corresponding file
     *
     * @return the persistence handler's id
     */
    public int getId() {
        return id;
    }
}
