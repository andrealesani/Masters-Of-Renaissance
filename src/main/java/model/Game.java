package model;

import Exceptions.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import model.card.leadercard.*;
import model.lorenzo.ArtificialIntelligence;
import model.lorenzo.Lorenzo;
import model.resource.Resource;
import model.resource.ResourceUnknown;

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
    /**
     * This attribute stores the game's market
     */
    private final Market market;
    /**
     * This attribute stores the game's card table
     */
    private final CardTable cardTable;
    /**
     * This attribute stores the game's leader cards before they are distributed to players
     */
    private final List<LeaderCard> leaderCards;
    /**
     * This attribute stores the player's boards in the same order as the turn order
     */
    private final List<PlayerBoard> playersTurnOrder;
    /**
     * This attribute stores the player who is currently taking their turn
     */
    private PlayerBoard currentPlayer;
    /**
     * This attribute stores the artificial intelligence for solo games
     */
    private ArtificialIntelligence lorenzo;
    /**
     * This stores the last pope's favor tile to have been triggered as part of a vatican report
     */
    private int lastTriggeredTile;
    /**
     * This attribute the faith score value that triggers the end of the game
     */
    private final int finalFaith;
    /**
     * This attribute stores the flag that determines if the ending phase of the game has been triggered
     */
    private boolean weReInTheEndGameNow;
    /**
     * This attribute stores the number of leader cards each player is given at the beginning of the game
     */
    private final int initialLeaderCardNumber;
    /**
     * This attribute stores the number of leader cards each player must keep at the beginning of the game
     */
    private final int finalLeaderCardNumber;
    /**
     * This attribute stores the phase of the turn in which the current player is in
     */
    private TurnPhase turnPhase;

    //CONSTRUCTORS

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

        //TODO make popefavortiles, leaderCardNumbers, vpfaithtiles, vpfaithvalues, numofdepots, baseProduction, devCardMax and finalfaith initialized in a JSON
        finalFaith = 24;
        initialLeaderCardNumber = 4;
        finalLeaderCardNumber = 2;
        int devCardMax = 7;
        int numOfDepots = 3;
        int[] vpFaithTiles = {3, 6, 9, 12, 15, 18, 21, 24};
        int[] vpFaithValues = {1, 2, 4, 6, 9, 12, 16, 20};
        Resource jolly = new ResourceUnknown();
        List<Resource> baseProdInput = new ArrayList<>();
        baseProdInput.add(jolly);
        baseProdInput.add(jolly);
        List<Resource> baseProdOutput = new ArrayList<>();
        baseProdOutput.add(jolly);
        Production baseProduction = new Production(baseProdInput, baseProdOutput);

        for (String nickname : nicknames) {
            List<PopeFavorTile> popeFavorTiles = new ArrayList<>();
            popeFavorTiles.add(new PopeFavorTile(2, 8, 4));
            popeFavorTiles.add(new PopeFavorTile(3, 16, 5));
            popeFavorTiles.add(new PopeFavorTile(4, 24, 6));

            if (nicknames.size() == 1) {
                lorenzo = new Lorenzo(cardTable, popeFavorTiles);
            }

            playersTurnOrder.add(new PlayerBoard(this, nickname, numOfDepots, finalFaith, devCardMax, vpFaithTiles, vpFaithValues, popeFavorTiles, baseProduction));
        }

        //TODO give leadercards to player in constructor
        distributeLeaderCards();
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
        initialLeaderCardNumber = 4;
        finalLeaderCardNumber = 2;
        turnPhase = TurnPhase.LEADERCHOICE;
        initializeLeaderCards();
        distributeLeaderCards();
        lastTriggeredTile = 0;
        weReInTheEndGameNow = false;
    }

    //PLAYER ACTIONS

    //LeaderCards handling actions

    /**
     * Allows the player to choose which leader cards to keep (after choosing leaderCardsNumber cards, the rest are discarded)
     *
     * @param pos the number of the leaderCard to choose  (STARTS FROM 1)
     * @throws WrongTurnPhaseException if the player attempts this action when they are not allowed to
     */
    @Override
    public void chooseLeaderCard(int pos) throws WrongTurnPhaseException {
        if (turnPhase != TurnPhase.LEADERCHOICE) {
            throw new WrongTurnPhaseException();
        }
        //TODO leadercard does not exist exception
        currentPlayer.chooseLeaderCard(pos);
    }

    /**
     * Allows the player to activate the leader card corresponding to the given number
     *
     * @param number the number of the leaderCard to activate
     * @throws WrongTurnPhaseException if the player attempts this action when they are not allowed to
     */
    @Override
    public void playLeaderCard(int number) throws RequirementsNotMetException, WrongTurnPhaseException {
        if (turnPhase == TurnPhase.LEADERCHOICE) {
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
        if (turnPhase == TurnPhase.LEADERCHOICE) {
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
        if (turnPhase != TurnPhase.ACTIONSELECTION) {
            throw new WrongTurnPhaseException();
        }
        //TODO marketscope null, numScope invalido, currentPlayer null
        market.selectResources(marketScope, numScope, currentPlayer);
        currentPlayer.resetProductionChoice();
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
        if (turnPhase != TurnPhase.MARKETDISTRIBUTION) {
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
        if (turnPhase != TurnPhase.MARKETDISTRIBUTION) {
            throw new WrongTurnPhaseException();
        }
        //TODO negative quantities
        currentPlayer.chooseMarbleConversion(resource.getType(), quantity);
    }

    /**
     * Allows the user to swap the contents of two warehouse depots
     *
     * @param depotNumber1 the number of the first depot to swap
     * @param depotNumber2 the number of the second depot to swap
     */
    public void swapDepotContent(int depotNumber1, int depotNumber2) throws SwapNotValidException, ParametersNotValidException, DepotNotPresentException, WrongTurnPhaseException {
        if (turnPhase == TurnPhase.LEADERCHOICE) {
            throw new WrongTurnPhaseException();
        }
        currentPlayer.swapDepotContent(depotNumber1, depotNumber2);
    }

    //DevelopmentCard purchasing actions

    /**
     * Allows the player to buy a development card from the cardTable
     *
     * @param cardColor the color of the card to buy
     * @param level     the level of the card to be bought
     * @param slot      the car slot in which to put the card
     */
    @Override
    public void buyDevelopmentCard(CardColor cardColor, int level, int slot) throws SlotNotValidException, NotEnoughResourceException, WrongTurnPhaseException {
        if (turnPhase != TurnPhase.ACTIONSELECTION) {
            throw new WrongTurnPhaseException();
        }
        //TODO cardcolor null, invalid row
        cardTable.buyTopCard(cardColor, level, currentPlayer, slot);
        currentPlayer.resetProductionChoice();
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
        if (turnPhase != TurnPhase.CARDPAYMENT) {
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
        if (turnPhase != TurnPhase.CARDPAYMENT) {
            throw new WrongTurnPhaseException();
        }
        //TODO resource null, negative quantity, no more debt
        currentPlayer.takeResourceFromStrongboxCard(resource.getType(), quantity);
    }

    //Production selection actions

    /**
     * Allows the player to select a production for activation
     * STARTS FROM 0
     *
     * @param number the number of the production
     */
    @Override
    public void selectProduction(int number) throws WrongTurnPhaseException {
        if (turnPhase != TurnPhase.ACTIONSELECTION) {
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
        if (turnPhase != TurnPhase.ACTIONSELECTION) {
            throw new WrongTurnPhaseException();
        }
        currentPlayer.resetProductionChoice();
    }

    /**
     * Allows the player to confirm the selected production for activation
     */
    @Override
    public void confirmProductionChoice() throws NotEnoughResourceException, UnknownResourceException, WrongTurnPhaseException {
        if (turnPhase != TurnPhase.ACTIONSELECTION) {
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
        if (turnPhase != TurnPhase.ACTIONSELECTION) {
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
        if (turnPhase != TurnPhase.ACTIONSELECTION) {
            throw new WrongTurnPhaseException();
        }
        //TODO invalid resource types and null, coherence in jolly/unknownResource naming convention, no jollies
        currentPlayer.chooseJollyOutput(resource);
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
        if (turnPhase != TurnPhase.PRODUCTIONPAYMENT) {
            throw new WrongTurnPhaseException();
        }
        //TODO resource null, negative quantity, no more debt
        currentPlayer.takeResourceFromWarehouseProduction(depotNumber, resource, quantity);
    }

    @Override
    public void takeResourceFromStrongboxProduction(Resource resource, int quantity) throws NotEnoughResourceException, WrongTurnPhaseException {
        if (turnPhase != TurnPhase.PRODUCTIONPAYMENT) {
            throw new WrongTurnPhaseException();
        }
        //TODO resource null, negative quantity, no more debt
        currentPlayer.takeResourceFromStrongboxProduction(resource, quantity);
    }

    @Override
    public void endTurn() throws WrongTurnPhaseException {
        if (turnPhase == TurnPhase.ACTIONSELECTION) {
            throw new WrongTurnPhaseException();
        }

        //Depending on which action the player took, makes different end-of-turn checks
        if (turnPhase == TurnPhase.MARKETDISTRIBUTION) {

            checkDiscarded();
            currentPlayer.resetProductionChoice();
            turnPhase = TurnPhase.ACTIONSELECTION;

        } else if (turnPhase == TurnPhase.CARDPAYMENT) {

            if (currentPlayer.leftInWaitingRoom() > 0) {
                throw new WrongTurnPhaseException();
            }
            currentPlayer.clearWaitingRoom();
            turnPhase = TurnPhase.ACTIONSELECTION;

        } else if (turnPhase == TurnPhase.PRODUCTIONPAYMENT) {

            if (!currentPlayer.isProductionInputEmpty()) {
                throw new WrongTurnPhaseException();
            }
            turnPhase = TurnPhase.ACTIONSELECTION;

        } else if (turnPhase == TurnPhase.LEADERCHOICE) {
            //TODO EXTRA RESOURCES AND FAITH AT FIRST TURN
            if (currentPlayer.getActiveLeaderCards() != finalLeaderCardNumber) {
                throw new WrongTurnPhaseException();
            }
            currentPlayer.finishLeaderCardSelection();
            if (playersTurnOrder.indexOf(currentPlayer) >= playersTurnOrder.size() - 1) {
                turnPhase = TurnPhase.ACTIONSELECTION;
            }

        }

        if (turnPhase == TurnPhase.ACTIONSELECTION) {
            //If in solo mode, Lorenzo takes His action
            if (lorenzo != null) {
                lorenzo.takeTurn();
            }

            //Activates a vatican report if necessary
            checkVaticanReport();
        }

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

    //PRIVATE METHODS

    /**
     * Creates the leader cards for the game by reading them from a JSON file
     */
    private void initializeLeaderCards() {
        //TODO controllare valori in input dal JSON (typo nelle enum, valori <0, etc)
        Gson gson = new Gson();
        JsonReader reader = null;

        // depot leader cards
        try {
            reader = new JsonReader(new FileReader("./src/main/java/persistence/cards/leadercards/DepotLeaderCards.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Type DepotDecArray = new TypeToken<ArrayList<DepotLeaderCard>>() {
        }.getType();
        ArrayList<DepotLeaderCard> depotLeaderCards = gson.fromJson(reader, DepotDecArray);
        leaderCards.addAll(depotLeaderCards);

        // discount leader cards
        try {
            reader = new JsonReader(new FileReader("./src/main/java/persistence/cards/leadercards/DiscountLeaderCards.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Type DiscountDecArray = new TypeToken<ArrayList<DiscountLeaderCard>>() {
        }.getType();
        ArrayList<DiscountLeaderCard> discountLeaderCards = gson.fromJson(reader, DiscountDecArray);
        leaderCards.addAll(discountLeaderCards);

        // marble leader cards
        try {
            reader = new JsonReader(new FileReader("./src/main/java/persistence/cards/leadercards/MarbleLeaderCards.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Type MarbleDecArray = new TypeToken<ArrayList<MarbleLeaderCard>>() {
        }.getType();
        ArrayList<MarbleLeaderCard> marbleLeaderCards = gson.fromJson(reader, MarbleDecArray);
        leaderCards.addAll(marbleLeaderCards);

        // production leader cards
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
     * Shuffles the leader cards, then splits them into decks of size initialLeaderCardNumber and gives them to each player
     */
    private void distributeLeaderCards() {

        Collections.shuffle(leaderCards);

        int j = 0;

        for (PlayerBoard playerBoard : playersTurnOrder) {
            for (int i = 0; i <= initialLeaderCardNumber; i++) {
                playerBoard.addLeaderCard(leaderCards.get(j));
                j++;
            }
        }
    }

    /**
     * Checks number of discarded resources at the end of a player's turn, after they have used the market action
     */
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

    /**
     * Checks the players' faith score to see if a vatican report is triggered, and if so it carries it out
     */
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

    /**
     * Returns whether or not the conditions are met to start the final phase of the game
     *
     * @return true if the final phase has been triggered
     */
    private boolean isGameEnding() {
        if (lorenzo != null) {
            if (!cardTable.checkAllColorsAvailable() || lorenzo.getFaith() >= finalFaith) {
                return true;
            }
        }
        for (PlayerBoard player : playersTurnOrder) {
            if (player.isEndGameTime()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Switches the current player to the next in the turn order
     */
    private void switchPlayer() {

        int currentIndex = playersTurnOrder.indexOf(currentPlayer);

        currentPlayer = playersTurnOrder.get((currentIndex + 1) % playersTurnOrder.size());


    }

    /**
     * Determines the winner and their score, then prints endgame messages
     */
    private void endTheGame() {
        //TODO this method probably needs to be implemented in some other way tied to the lobby
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

    /**
     * Assumes that playersTurnOrder list has been filled already.
     * Shuffles playersTurnOrder, first in list is first player
     */
    private void assignInkwell() {
        Collections.shuffle(playersTurnOrder);
    }


    //GETTERS (mostly for debug purposes)

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

    /**
     * Getter
     *
     * @return CardTable
     */
    CardTable getCardTable() {
        return cardTable;
    }

    /**
     * Getter
     *
     * @return player turn order
     */
    public List<PlayerBoard> getPlayersTurnOrder() {
        return playersTurnOrder;
    }
}