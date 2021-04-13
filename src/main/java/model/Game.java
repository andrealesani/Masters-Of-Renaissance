package model;

import Exceptions.*;
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
public class Game implements UserInterface {
    private final Market market;
    private final CardTable cardTable;
    private final List<LeaderCard> leaderCards;
    private final List<PlayerBoard> playersTurnOrder;
    private PlayerBoard currentPlayer;
    private ArtificialIntelligence lorenzo;
    private int lastTriggeredTile;
    private final int finalFaith;
    private boolean weReInTheEndGameNow;
    private TurnPhase turnPhase;

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
        turnPhase = TurnPhase.LEADERCHOICE;
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
        turnPhase = TurnPhase.LEADERCHOICE;
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
    //Per ogni playerboard in currentplayers chiami addLeaderCard() per e gli dai in ingresso il proprio mazzetto (lista)
    public void distributeLeaderCards() {

        shuffleLeaderCards();

        int i, j;
        int numCards = 0;

        for (PlayerBoard playerBoard : playersTurnOrder) {
            for (i = 0; i <= 3; i++) {
                playerBoard.addLeaderCard(leaderCards.get(0));
                leaderCards.remove(0);
            }
        }
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

        //If in solo game: check Lorenzo
        if (lorenzo != null) {
            newTriggeredTile = lorenzo.getNewTriggeredTile(lastTriggeredTile);
        }

        //Check all players
        for (PlayerBoard player : playersTurnOrder) {
            if (player.getNewTriggeredTile(lastTriggeredTile) > newTriggeredTile) {
                newTriggeredTile = player.getNewTriggeredTile(lastTriggeredTile);
            }
        }

        //If necessity for vatican report, call it
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

        currentPlayer = playersTurnOrder.get((currentIndex+1)%playersTurnOrder.size());


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
            for (int i = 0; i < playersTurnOrder.size(); i++) {
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

    //LeaderCards handling actions

    /**
     * Allows the player to choose which leader cards to keep (after choosing two the rest are discarded)
     *
     * @param number the number of the leaderCard to choose
     */
    @Override
    public void chooseLeaderCard(int number) throws WrongTurnPhaseException {
        if (turnPhase!=TurnPhase.LEADERCHOICE) {
            throw new WrongTurnPhaseException();
        }
        currentPlayer.chooseLeaderCard(number);
    }

    /**
     * Allows the player to activate the leader card corresponding to the given number
     *
     * @param number the number of the leaderCard to activate
     */
    @Override
    public void playLeaderCard(int number) throws RequirementsNotMetException, WrongTurnPhaseException {
        if (turnPhase==TurnPhase.LEADERCHOICE) {
            throw new WrongTurnPhaseException();
        }
        //TODO leadercard does not exist
        currentPlayer.playLeaderCard(number);
    }

    /**
     * Allows the player to discard the leader card corresponding to the given number
     *
     * @param number the number of the leaderCard to discard
     */
    @Override
    public void discardLeaderCard(int number) throws WrongTurnPhaseException {
        if (turnPhase==TurnPhase.LEADERCHOICE) {
            throw new WrongTurnPhaseException();
        }
        //TODO leadercard does not exist
        currentPlayer.discardLeaderCard(number);
    }

    //Market selection actions

    /**
     * Allows the player to select a row or column from the market and take its resources
     *
     * @param marketScope distinguishes between selecting a row or column
     * @param numScope    the index of the selected row or column
     */
    @Override
    public void selectFromMarket(MarketScope marketScope, int numScope) throws WrongTurnPhaseException {
        if (turnPhase!=TurnPhase.ACTIONSELECTION) {
            throw new WrongTurnPhaseException();
        }
        //TODO marketscope null, numScope invalido, currentPlayer null
        market.selectResources(marketScope, numScope, currentPlayer);
        turnPhase = TurnPhase.MARKETDISTRIBUTION;
    }

    /**
     * Allows the player to send a resource obtained from the market to a specific depot
     *
     * @param depotNumber the number of the depot to which to send the resource
     * @param resource    the resource to send to the depot
     * @param quantity    the amount of resource to send
     */
    @Override
    public void sendResourceToDepot(int depotNumber, Resource resource, int quantity) throws DepotNotPresentException, NotEnoughResourceException, BlockedResourceException, NotEnoughSpaceException, WrongResourceTypeException, WrongTurnPhaseException {
        if (turnPhase!=TurnPhase.MARKETDISTRIBUTION) {
            throw new WrongTurnPhaseException();
        }
        //TODO negative quantities, null type
        currentPlayer.sendResourceToDepot(depotNumber, resource.getType(), quantity);
    }

    /**
     * Allows the player to choose to convert a white marble resource into one from their conversions list
     *
     * @param resource the resource into which to convert the white marble
     * @param quantity the amount of resource to convert
     */
    @Override
    public void chooseMarbleConversion(Resource resource, int quantity) throws ConversionNotAvailableException, NotEnoughResourceException, WrongTurnPhaseException {
        if (turnPhase!=TurnPhase.MARKETDISTRIBUTION) {
            throw new WrongTurnPhaseException();
        }
        //TODO negative quantities
        currentPlayer.chooseMarbleConversion(resource.getType(), quantity);
    }

    //DevelopmentCard purchasing actions

    /**
     * Allows the player to buy a development card from the cardTable
     *
     * @param cardColor the color of the card to buy
     * @param row       the card table row from which to buy the card
     * @param slot      the car slot in which to put the card
     */
    @Override
    public void buyDevelopmentCard(CardColor cardColor, int row, int slot) throws SlotNotValidException, NotEnoughResourceException, WrongTurnPhaseException {
        if (turnPhase!=TurnPhase.ACTIONSELECTION) {
            throw new WrongTurnPhaseException();
        }
        //TODO cardcolor null, invalid row
        cardTable.buyTopCard(cardColor, row, currentPlayer, slot);
        turnPhase = TurnPhase.CARDPAYMENT;
    }

    /**
     * Allows the player to pay the development card cost by taking resources from the given depot in the warehouse
     *
     * @param depotNumber the number of the depot from which to take the resource
     * @param resource    the resource to take
     * @param quantity    the amount of resource to take (and of cost to pay)
     */
    @Override
    public void takeResourceFromWarehouseCard(int depotNumber, Resource resource, int quantity) throws NotEnoughResourceException, DepotNotPresentException, WrongTurnPhaseException {
        if (turnPhase!=TurnPhase.CARDPAYMENT) {
            throw new WrongTurnPhaseException();
        }
        //TODO resource null, negative quantity, no more debt
        currentPlayer.takeResourceFromWarehouseCard(depotNumber, resource.getType(), quantity);
    }

    /**
     * Allows the player to pay the development card cost by taking resources from the strongbox
     *
     * @param resource the resource to take
     * @param quantity the amount of resource to take (and of cost to pay)
     */
    @Override
    public void takeResourceFromStrongboxCard(Resource resource, int quantity) throws NotEnoughResourceException, WrongTurnPhaseException {
        if (turnPhase!=TurnPhase.CARDPAYMENT) {
            throw new WrongTurnPhaseException();
        }
        //TODO resource null, negative quantity, no more debt
        currentPlayer.takeResourceFromStrongboxCard(resource.getType(), quantity);
    }

    //Production selection actions

    /**
     * Allows the player to select a production for activation
     *
     * @param number the number of the production
     */
    @Override
    public void selectProduction(int number) throws WrongTurnPhaseException {
        if (turnPhase!=TurnPhase.ACTIONSELECTION) {
            throw new WrongTurnPhaseException();
        }
        //TODO production does not exist
        currentPlayer.selectProduction(number);
    }

    /**
     * Allows the player to reset the selected productions
     */
    @Override
    public void resetProductionChoice() throws WrongTurnPhaseException {
        if (turnPhase!=TurnPhase.ACTIONSELECTION) {
            throw new WrongTurnPhaseException();
        }
        currentPlayer.resetProductionChoice();
    }

    /**
     * Allows the player to confirm the selected production for activation
     */
    @Override
    public void confirmProductionChoice() throws NotEnoughResourceException, UnknownResourceException, WrongTurnPhaseException {
        if (turnPhase!=TurnPhase.ACTIONSELECTION) {
            throw new WrongTurnPhaseException();
        }
        currentPlayer.confirmProductionChoice();
        turnPhase = TurnPhase.PRODUCTIONPAYMENT;
    }

    /**
     * Allows the player to choose into which resource to turn a jolly in the production's input
     *
     * @param resource the resource into which to turn the jolly
     */
    @Override
    public void chooseJollyInput(Resource resource) throws WrongTurnPhaseException {
        if (turnPhase!=TurnPhase.ACTIONSELECTION) {
            throw new WrongTurnPhaseException();
        }
        //TODO invalid resource types and null, coherence in jolly/unknownResource naming convention, no jollies
        currentPlayer.chooseJollyInput(resource);
    }

    /**
     * Allows the player to choose into which resource to turn a jolly in the production's output
     *
     * @param resource the resource into which to turn the jolly
     */
    @Override
    public void chooseJollyOutput(Resource resource) throws WrongTurnPhaseException {
        if (turnPhase!=TurnPhase.ACTIONSELECTION) {
            throw new WrongTurnPhaseException();
        }
        //TODO invalid resource types and null, coherence in jolly/unknownResource naming convention, no jollies
        currentPlayer.chooseJollyOutput (resource);
    }

    /**
     * Allows the player to pay the production cost by taking resources from the given depot in the warehouse
     *
     * @param depotNumber the number of the depot from which to take the resource
     * @param resource    the resource to take
     * @param quantity    the amount of resource to take (and of cost to pay)
     */
    @Override
    public void takeResourceFromWarehouseProduction(int depotNumber, Resource resource, int quantity) throws NotEnoughResourceException, DepotNotPresentException, WrongTurnPhaseException {
        if (turnPhase!=TurnPhase.PRODUCTIONPAYMENT) {
            throw new WrongTurnPhaseException();
        }
        //TODO resource null, negative quantity, no more debt
        currentPlayer.takeResourceFromWarehouseProduction(depotNumber, resource, quantity);
    }

    @Override
    public void takeResourceFromStrongboxProduction(Resource resource, int quantity) throws NotEnoughResourceException, WrongTurnPhaseException {
        if (turnPhase!=TurnPhase.PRODUCTIONPAYMENT) {
            throw new WrongTurnPhaseException();
        }
        //TODO resource null, negative quantity, no more debt
        currentPlayer.takeResourceFromStrongboxCard(resource.getType(), quantity);
    }

    @Override
    public void endTurn() throws WrongTurnPhaseException {
        if (turnPhase == TurnPhase.ACTIONSELECTION) {
            throw new WrongTurnPhaseException();
        }

        //Depending on which action the player took, makes different end-of-turn checks
        if (turnPhase == TurnPhase.MARKETDISTRIBUTION) {

            checkDiscarded();
            turnPhase = TurnPhase.ACTIONSELECTION;

        } else if (turnPhase == TurnPhase.CARDPAYMENT) {

            if (currentPlayer.leftInWaitingRoom()>0) {
                throw new WrongTurnPhaseException();
            }
            currentPlayer.clearWaitingRoom();
            turnPhase = TurnPhase.ACTIONSELECTION;

        } else if (turnPhase == turnPhase.PRODUCTIONPAYMENT) {

            if (!currentPlayer.isProductionInputEmpty()) {
                throw new WrongTurnPhaseException();
            }
            currentPlayer.resetProductionChoice();
            turnPhase = TurnPhase.ACTIONSELECTION;

        } else if (turnPhase == TurnPhase.LEADERCHOICE) {

            if (currentPlayer.getActiveLeaderCards()!=2) {
                throw new WrongTurnPhaseException();
            }
            currentPlayer.finishLeaderCardSelection();
            if (playersTurnOrder.indexOf(currentPlayer) >= playersTurnOrder.size()-1) {
                turnPhase = TurnPhase.ACTIONSELECTION;
            }

        }

        //If in solo mode, Lorenzo takes His action
        if (lorenzo != null) {
            lorenzo.takeTurn();
        }

        //Activates a vatican report if necessary
        checkVaticanReport();

        //Switches current player to the next one
        switchPlayer();

        //Checks if the current turn is the last one, or if the final phase of the game has been triggered
        if (weReInTheEndGameNow) {
            if (currentPlayer == playersTurnOrder.get(0)) {
                endTheGame();
            }
        } else if (isGameEnding()) {
            weReInTheEndGameNow = true;
        }
    }
}
