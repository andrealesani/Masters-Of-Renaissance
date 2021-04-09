package model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import model.card.Card;
import model.card.leadercard.*;
import model.lorenzo.Lorenzo;
import model.resource.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
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

    /**
     * Constructor
     */
    public Game(List<String> nicknames) {
        market = new Market();
        cardTable = new CardTable();
        leaderCards = new ArrayList<>();
        playersTurnOrder = new ArrayList<>();
        lorenzo = new Lorenzo();

        for (String nickname : nicknames) {
            playersTurnOrder.add(new PlayerBoard(this, nickname));
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
     * @param i number of faith points to add
     */
    private void faithIncreaseAll(int i) {
        for (PlayerBoard playerBoard : playersTurnOrder) {
            if (!playerBoard.equals(getCurrentPlayer())) {
                for (int j = 0; j < i; j++) {
                    playerBoard.faithIncrease();
                }
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

    public void checkPopeFavor() {
        //TODO
    }

    //se solo mode attivare Lorenzo
    //controllare se il gioco è finito (sia solo mode sia multiplayer)
    //se il gioco non è finito e partita è multiplayer cambiare currentPlayer
    public void endCurrentTurn() {
        //TODO
    }
}
