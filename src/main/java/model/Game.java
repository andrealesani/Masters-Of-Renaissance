package model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import model.card.leadercard.*;
import model.lorenzo.Lorenzo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model.Game master class
 */
public class Game {
    private final Market market;
    private final CardTable cardTable;
    private final List<LeaderCard> leaderCards;
    private final List<PlayerBoard> playersTurnOrder;
    private PlayerBoard currentPlayer;
    private final Lorenzo lorenzo;
    private int lastTriggeredTile;

    /**
     * Constructor
     */
    public Game(List<String> nicknames) {
        market = new Market();
        cardTable = new CardTable();
        leaderCards = new ArrayList<>();
        playersTurnOrder = new ArrayList<>();
        lorenzo = new Lorenzo();
        lastTriggeredTile = 0;


        for (String nickname : nicknames) {
            List<PopeFavorTile> popeFavorTiles = new ArrayList<>();
            popeFavorTiles.add(new PopeFavorTile(2, 8, 4));
            popeFavorTiles.add(new PopeFavorTile(3, 16, 5));
            popeFavorTiles.add(new PopeFavorTile(4, 24, 6));
            playersTurnOrder.add(new PlayerBoard(this, nickname, 3, popeFavorTiles));
        }
        assignInkwell();
        currentPlayer = playersTurnOrder.get(0);
    }

    /**
     * Testing Constructor
     */
    public Game() {
        market = new Market();
        cardTable = new CardTable();
        leaderCards = new ArrayList<>();
        playersTurnOrder = new ArrayList<>();
        lorenzo = new Lorenzo();
        initializeLeaderCards();
    }

    /**
     * !! ATM The method does NOT control the validity of the values read from the JSON files, like quantities <0 or typos in enums !!
     * This method creates the instances of all the LeaderCards before the game starts
     */
    private void initializeLeaderCards() {

        Gson gson = new Gson();
        JsonReader reader = null;

        // DEPOT LEADER CARDS
        try {
            reader = new JsonReader(new FileReader("./src/main/java/persistence/cards/leadercards/DepotLeaderCards.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Type DepotDecArray = new TypeToken<ArrayList<DepotDecorator>>() {
        }.getType();
        ArrayList<DepotDecorator> depotLeaderCards = gson.fromJson(reader, DepotDecArray);
        leaderCards.addAll(depotLeaderCards);

        // DISCOUNT LEADER CARDS
        try {
            reader = new JsonReader(new FileReader("./src/main/java/persistence/cards/leadercards/DiscountLeaderCards.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Type DiscountDecArray = new TypeToken<ArrayList<DiscountDecorator>>() {
        }.getType();
        ArrayList<DiscountDecorator> discountLeaderCards = gson.fromJson(reader, DiscountDecArray);
        leaderCards.addAll(discountLeaderCards);

        // MARBLE LEADER CARDS
        try {
            reader = new JsonReader(new FileReader("./src/main/java/persistence/cards/leadercards/MarbleLeaderCards.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Type MarbleDecArray = new TypeToken<ArrayList<MarbleDecorator>>() {
        }.getType();
        ArrayList<MarbleDecorator> marbleLeaderCards = gson.fromJson(reader, MarbleDecArray);
        leaderCards.addAll(marbleLeaderCards);

        // PRODUCTION LEADER CARDS
        try {
            reader = new JsonReader(new FileReader("./src/main/java/persistence/cards/leadercards/ProductionLeaderCards.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Type ProductionDecArray = new TypeToken<ArrayList<ProductionDecorator>>() {
        }.getType();
        ArrayList<ProductionDecorator> productionLeaderCards = gson.fromJson(reader, ProductionDecArray);
        leaderCards.addAll(productionLeaderCards);
    }

    /**
     * This method assumes that playersTurnOrder list has been filled already. It randomly chooses the first player
     * by putting it first in playersTurnOrder list
     */
    private void assignInkwell() {
        int num = (int) (Math.random() * playersTurnOrder.size());
        for (int i = 0; i < num; i++) {
            playersTurnOrder.add(0, playersTurnOrder.get(playersTurnOrder.size() - 1));
        }
    }

    /**
     * This method shuffles the LeaderCards before distributing them to the players
     */
    private void shuffleLeaderCards() {
        Collections.shuffle(leaderCards);
    }

    /**
     * This method is called when a player's move makes all other players increase their faith
     * (for example when discarding resources)
     *
     * @param quantity number of faith points to add
     */
    private void faithIncreaseAll(int quantity) {
        for (PlayerBoard playerBoard : playersTurnOrder) {
            if (!playerBoard.equals(getCurrentPlayer())) {
                playerBoard.increaseFaith(quantity);
            }
        }
    }



    /**
     * Getter
     *
     * @return leaderCards list
     */
    public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    /**
     * Getter
     *
     * @return current active player
     */
    public PlayerBoard getCurrentPlayer() {
        return currentPlayer;
    }

    //shufflare le leadercards
    //dividere il mazzo delle leader cards in 4 mazzetti (da dichiarare negli attributi e aggiungere nell'UML)
    public void chooseLeaderCard(int i) {
        //TODO
    }

    //se solo mode attivare Lorenzo
    //controllare se il gioco è finito (sia solo mode sia multiplayer)
    //se il gioco non è finito e partita è multiplayer cambiare currentPlayer
    public void endCurrentTurn() {
        //if SOLO MODE
        //...
        //else if MULTIPLAYER
        int leftoverResources = currentPlayer.leftInWaitingRoom();
        int newTriggeredTile = 0;

        if (leftoverResources != 0) {
            faithIncreaseAll(leftoverResources);
            currentPlayer.clearWaitingRoom();
            for (PlayerBoard player : playersTurnOrder) {
                if (player.getNewTriggeredTile(lastTriggeredTile) > newTriggeredTile) {
                    newTriggeredTile = player.getNewTriggeredTile(lastTriggeredTile);
                }
            }
        } else {
            newTriggeredTile = currentPlayer.getNewTriggeredTile(lastTriggeredTile);
        }

        if (newTriggeredTile > lastTriggeredTile) {
            for (PlayerBoard player : playersTurnOrder) {
                player.theVaticanReport(newTriggeredTile, lastTriggeredTile);
            }
            lastTriggeredTile = newTriggeredTile;
        }
    }
}
