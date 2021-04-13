package model;

import Exceptions.NotEnoughResourceException;
import Exceptions.SlotNotValidException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import model.card.leadercard.*;
import model.lorenzo.ArtificialIntelligence;
import model.lorenzo.Lorenzo;
import model.resource.Resource;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class models a single game of Masters of the Renaissance
 */
public class Game implements UserInterface{
    private final Market market;
    private final CardTable cardTable;
    private final List<LeaderCard> leaderCards;
    private final List<PlayerBoard> playersTurnOrder;
    private PlayerBoard currentPlayer;
    private ArtificialIntelligence lorenzo;
    private int lastTriggeredTile;
    private final int finalFaith;
    private boolean weReInTheEndGameNow;

    /**
     * Constructor
     */
    public Game(List<String> nicknames) {
        market = new Market();
        cardTable = new CardTable();
        leaderCards = new ArrayList<>();
        playersTurnOrder = new ArrayList<>();
        lorenzo = null;
        lastTriggeredTile = 0;
        weReInTheEndGameNow = false;
        initializeLeaderCards();

        //TODO make popefavortiles, vpfaithtiles, vpfaithvalues, numofdepots, devCardMax and finalfaith initialized in a JSON
        finalFaith = 24;
        int devCardMax = 7;
        int numOfDepots = 3;
        int[] vpFaithTiles = {3, 6, 9, 12, 15, 18, 21, 24};
        int[] vpFaithValues = {1, 2, 4, 6, 9, 12, 16, 20};
        for (String nickname : nicknames) {
            List<PopeFavorTile> popeFavorTiles = new ArrayList<>();
            popeFavorTiles.add(new PopeFavorTile(2, 8, 4));
            popeFavorTiles.add(new PopeFavorTile(3, 16, 5));
            popeFavorTiles.add(new PopeFavorTile(4, 24, 6));

            if (nicknames.size() == 1) {
                lorenzo = new Lorenzo(cardTable, popeFavorTiles);
            }

            playersTurnOrder.add(new PlayerBoard(this, nickname, numOfDepots, finalFaith, devCardMax, vpFaithTiles, vpFaithValues, popeFavorTiles));
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
        lorenzo = null;
        finalFaith = 24;
        initializeLeaderCards();
        lastTriggeredTile = 0;
        weReInTheEndGameNow = false;
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
        Type DepotDecArray = new TypeToken<ArrayList<DepotLeaderCard>>() {
        }.getType();
        ArrayList<DepotLeaderCard> depotLeaderCards = gson.fromJson(reader, DepotDecArray);
        leaderCards.addAll(depotLeaderCards);

        // DISCOUNT LEADER CARDS
        try {
            reader = new JsonReader(new FileReader("./src/main/java/persistence/cards/leadercards/DiscountLeaderCards.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Type DiscountDecArray = new TypeToken<ArrayList<DiscountLeaderCard>>() {
        }.getType();
        ArrayList<DiscountLeaderCard> discountLeaderCards = gson.fromJson(reader, DiscountDecArray);
        leaderCards.addAll(discountLeaderCards);

        // MARBLE LEADER CARDS
        try {
            reader = new JsonReader(new FileReader("./src/main/java/persistence/cards/leadercards/MarbleLeaderCards.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Type MarbleDecArray = new TypeToken<ArrayList<MarbleLeaderCard>>() {
        }.getType();
        ArrayList<MarbleLeaderCard> marbleLeaderCards = gson.fromJson(reader, MarbleDecArray);
        leaderCards.addAll(marbleLeaderCards);

        // PRODUCTION LEADER CARDS
        try {
            reader = new JsonReader(new FileReader("./src/main/java/persistence/cards/leadercards/ProductionLeaderCards.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Type ProductionDecArray = new TypeToken<ArrayList<ProductionLeaderCard>>() {
        }.getType();
        ArrayList<ProductionLeaderCard> productionLeaderCards = gson.fromJson(reader, ProductionDecArray);
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
    private void increaseFaithAll(int quantity) {
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

    //shufflare le leadercards (shuffleLeaderCards())
    //dividere il mazzo delle leader cards in un mazzetto di 4 carte per ogni giocatore
    //Per ogni playerboard in currentplayers chiami addLeaderCards() per e gli dai in ingresso il proprio mazzetto (lista)
    public void distributeLeaderCards(int i) {
        //TODO
    }

    private void checkDiscarded() {
        int numDiscardedResources = currentPlayer.leftInWaitingRoom();

        if (numDiscardedResources > 0) {

            if (lorenzo != null) {

                lorenzo.increaseFaith(numDiscardedResources);

            } else {

                String currentPlayerName = currentPlayer.getUsername();
                for (PlayerBoard playerBoard : playersTurnOrder) {
                    if (!playerBoard.getUsername().equals(currentPlayerName)) {
                        playerBoard.increaseFaith(numDiscardedResources);
                    }
                }

            }
            currentPlayer.clearWaitingRoom();

        }
    }

    private void checkVaticanReport() {
        int newTriggeredTile = 0;

        if (lorenzo != null) {
            lorenzo.takeTurn();
            newTriggeredTile = lorenzo.getNewTriggeredTile(lastTriggeredTile);
        }

        for (PlayerBoard player : playersTurnOrder) {
            if (player.getNewTriggeredTile(lastTriggeredTile) > newTriggeredTile) {
                newTriggeredTile = player.getNewTriggeredTile(lastTriggeredTile);
            }
        }

        if (newTriggeredTile > lastTriggeredTile) {
            for (PlayerBoard player : playersTurnOrder) {
                player.theVaticanReport(newTriggeredTile, lastTriggeredTile);
            }
            lastTriggeredTile = newTriggeredTile;
        }
    }

    private boolean isGameEnding() {
        if (lorenzo != null) {
            if (!cardTable.checkAllColorsAvailable() || lorenzo.getFaith() >= finalFaith) {
                return true;
            }
        }
        for (PlayerBoard player : playersTurnOrder) {
            if (player.isGameEnding()) {
                return true;
            }
        }

        return false;
    }

    private void switchPlayer() {

        int currentIndex = playersTurnOrder.indexOf(currentPlayer);
        if (currentIndex < playersTurnOrder.size() - 1) {
            currentPlayer = playersTurnOrder.get(currentIndex + 1);
        } else {
            currentPlayer = playersTurnOrder.get(0);
        }

    }

    private void endTheGame() {
        if (lorenzo != null) {
            if (!cardTable.checkAllColorsAvailable() || lorenzo.getFaith() >= finalFaith) {
                System.out.println("FATALITY: Lorenzo wins!");
            } else {
                System.out.println("FATALITY: " + currentPlayer.getUsername() + " wins with " + currentPlayer.calculateVictoryPoints() + " victory points!");
            }
        } else {
            //TODO maybe change with stream implementation?
            int winner = 0;
            int maxVictoryPoints = 0;
            for (int i = 0; i<playersTurnOrder.size(); i++) {
                int playerPoints = playersTurnOrder.get(i).calculateVictoryPoints();
                if (playerPoints > maxVictoryPoints) {
                    winner = i;
                    maxVictoryPoints = playerPoints;
                }
            }
            System.out.println("FATALITY: " + playersTurnOrder.get(winner).getUsername() + " wins with " + playersTurnOrder.get(winner).calculateVictoryPoints() + " victory points!");
        }
    }

    // HIC SUNT ACTIONEM GIOCATORIBUS

    @Override
    public void chooseLeaderCard(int number) {
        //TODO
    }

    @Override
    public void playLeaderCard(int number) {

    }

    @Override
    public void discardLeaderCard(int number) {

    }

    @Override
    public void selectFromMarket(MarketScope marketScope, int numScope) {

    }

    @Override
    public void sendResourceToDepot(int depotNumber, Resource resource, int quantity) {

    }

    @Override
    public void chooseMarbleConversion(Resource resource, int quantity) {

    }

    @Override
    public void buyDevelopmentCard(CardColor color, int level, int slot) throws SlotNotValidException, NotEnoughResourceException {
        cardTable.buyTopCard(color, level, currentPlayer, slot);
    }

    @Override
    public void takeResourceFromWarehouseCard(int depotNumber, Resource resource, int quantity) {

    }

    @Override
    public void takeResourceFromStrongboxCard(Resource resource, int quantity) {

    }

    @Override
    public void selectProduction(int number) {

    }

    @Override
    public void resetProductionChoice() {

    }

    @Override
    public void confirmProductions() {

    }

    @Override
    public void chooseJollyInput(Resource resource) {

    }

    @Override
    public void chooseJollyOutput(Resource resource) {

    }

    @Override
    public void takeResourceFromWarehouseProduction(int depotNumber, Resource resource, int quantity) {

    }

    @Override
    public void takeResourceFromStrongboxProduction(Resource resource, int quantity) {

    }

    @Override
    public void endTurn() {
        checkDiscarded();

        checkVaticanReport();

        if (lorenzo==null) {
            switchPlayer();
        }

        if (weReInTheEndGameNow) {
            if (currentPlayer == playersTurnOrder.get(0)) {
                endTheGame();
            }
        } else if (isGameEnding()) {
            weReInTheEndGameNow = true;
        }
    }
}
