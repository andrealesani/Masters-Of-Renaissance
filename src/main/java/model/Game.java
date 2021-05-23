package model;

import Exceptions.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.card.leadercard.*;
import model.lorenzo.ArtificialIntelligence;
import model.lorenzo.Lorenzo;
import model.resource.Resource;
import server.GameController;
import network.beans.*;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static model.TurnPhase.*;

/**
 * This class models a single game of Masters of the Renaissance
 */
public class Game implements UserCommandsInterface, Observable {
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
     * This attribute stores the player board of the player who is currently taking their turn.
     * Is set to null if all players are not connected
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
    /**
     * This attribute is set when the endTheGame() method finds the winner
     */
    private String winner;
    /**
     * This attribute is set when the endTheGame() method has calculated the winner's victory points
     */
    private int winnerVp = -1;

    //CONSTRUCTORS

    /**
     * Constructor
     */
    public Game(Set<String> nicknames) {
        market = new Market();
        leaderCards = new ArrayList<>();
        playersTurnOrder = new ArrayList<>();
        lorenzo = null;
        lastTriggeredTile = 0;
        weReInTheEndGameNow = false;
        setTurnPhase(LEADERCHOICE);
        initializeLeaderCards();
        cardTable = new CardTable(leaderCards.size()); // WARNING THIS METHOD HAS TO BE CALLED AFTER LEADER CARDS INITIALIZATION

        //TODO make these attributes initialized in a JSON (these ones have to be in Game)
        finalFaith = 24;
        initialLeaderCardNumber = 4;
        finalLeaderCardNumber = 2;

        for (String nickname : nicknames) {
            //TODO make popefavor tiles initialized in json?
            List<PopeFavorTile> popeFavorTiles = new ArrayList<>();
            popeFavorTiles.add(new PopeFavorTile(2, 8, 4));
            popeFavorTiles.add(new PopeFavorTile(3, 16, 5));
            popeFavorTiles.add(new PopeFavorTile(4, 24, 6));

            if (nicknames.size() == 1) {
                lorenzo = new Lorenzo(cardTable, popeFavorTiles);
            }

            playersTurnOrder.add(new PlayerBoard(this, nickname, finalFaith, popeFavorTiles));
        }

        //TODO give leadercards to player in constructor?
        distributeLeaderCards();

        assignInkwell();

        //TODO initialize in a JSON
        int[] firstTurnBonusResources = {0, 1, 1, 2};
        int[] firstTurnBonusFaith = {0, 0, 1, 1};
        for (int i = 0; i < playersTurnOrder.size(); i++) {
            playersTurnOrder.get(i).addWhiteNoCheck(firstTurnBonusResources[i]);
            playersTurnOrder.get(i).addFaith(firstTurnBonusFaith[i]);
        }

        currentPlayer = playersTurnOrder.get(0);
    }

    /**
     * Testing Constructor
     */
    public Game() {

        market = new Market();
        leaderCards = new ArrayList<>();
        playersTurnOrder = new ArrayList<>();
        lorenzo = null;
        finalFaith = 24;
        initialLeaderCardNumber = 4;
        finalLeaderCardNumber = 2;
        setTurnPhase(LEADERCHOICE);
        initializeLeaderCards();
        cardTable = new CardTable(leaderCards.size());  // WARNING THIS METHOD HAS TO BE CALLED AFTER LEADER CARDS INITIALIZATION
        distributeLeaderCards();
        lastTriggeredTile = 0;
        weReInTheEndGameNow = false;
    }

    //PLAYER ACTIONS

    //First turn actions

    /**
     * Allows the player to choose which resource to get (if any) as part of the first turn's bonus resources
     *
     * @param resource the type of bonus resource to get
     * @param quantity the amount of resource to get
     * @throws NotEnoughResourceException  if the player does not have sufficient bonus resources left to convert
     * @throws WrongTurnPhaseException     if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     */
    public void chooseBonusResourceType(Resource resource, int quantity) throws NotEnoughResourceException, WrongTurnPhaseException {
        if (turnPhase != LEADERCHOICE) {
            throw new WrongTurnPhaseException();
        }
        if (currentPlayer == null)
            throw new ParametersNotValidException();
        currentPlayer.chooseStartingResource(resource.getType(), quantity);
    }

    /**
     * Allows the player to choose which leader cards to keep (after choosing two the rest are discarded)
     *
     * @param pos the number of the leaderCard to choose (STARTS FROM 1)
     * @throws WrongTurnPhaseException     if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     */
    @Override
    public void chooseLeaderCard(int pos) throws WrongTurnPhaseException, LeaderNotPresentException {
        if (turnPhase != LEADERCHOICE) {
            throw new WrongTurnPhaseException();
        }
        //TODO leadercard does not exist exception
        currentPlayer.chooseLeaderCard(pos);
    }

    //LeaderCards handling actions

    /**
     * Allows the player to activate the leader card corresponding to the given number
     *
     * @param number the number of the leaderCard to activate
     * @throws LeaderRequirementsNotMetException if the player does not meet the requirements for activating the leader card
     * @throws WrongTurnPhaseException           if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException       if the given parameters are not admissible for the game's rules
     * @throws LeaderNotPresentException         if the number selected does not correspond to a leader card
     */
    @Override
    public void playLeaderCard(int number) throws LeaderRequirementsNotMetException, WrongTurnPhaseException, LeaderNotPresentException {
        if (turnPhase == LEADERCHOICE) {
            throw new WrongTurnPhaseException();
        }
        //TODO leadercard does not exist
        currentPlayer.playLeaderCard(number);
    }

    /**
     * Allows the player to discard the leader card corresponding to the given number
     *
     * @param number the number of the leaderCard to discard
     * @throws LeaderIsActiveException     if the player attempts to discard a leader card they have previously activated
     * @throws WrongTurnPhaseException     if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     * @throws LeaderNotPresentException   if the number selected does not correspond to a leader card
     */
    @Override
    public void discardLeaderCard(int number) throws WrongTurnPhaseException, LeaderIsActiveException, LeaderNotPresentException {
        if (turnPhase == LEADERCHOICE) {
            throw new WrongTurnPhaseException();
        }

        currentPlayer.discardLeaderCard(number);
    }

    //Market selection actions

    /**
     * Allows the player to select a row from the market and take its resources
     *
     * @param numScope the index of the selected row
     * @throws WrongTurnPhaseException     if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     */
    @Override
    public void selectMarketRow(int numScope) throws WrongTurnPhaseException {
        if (turnPhase != TurnPhase.ACTIONSELECTION) {
            throw new WrongTurnPhaseException();
        }
        //TODO num scope invalido, currentPlayer null
        market.selectRow(numScope, currentPlayer);
        currentPlayer.resetProductionChoice();
        setTurnPhase(MARKETDISTRIBUTION);
    }

    /**
     * Allows the player to select a column from the market and take its resources
     *
     * @param numScope the index of the selected column+
     * @throws WrongTurnPhaseException     if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     */
    @Override
    public void selectMarketColumn(int numScope) throws WrongTurnPhaseException {
        if (turnPhase != TurnPhase.ACTIONSELECTION) {
            throw new WrongTurnPhaseException();
        }
        //TODO numScope invalido, currentPlayer null
        market.selectColumn(numScope, currentPlayer);
        currentPlayer.resetProductionChoice();
        setTurnPhase(MARKETDISTRIBUTION);
    }

    /**
     * Allows the player to send a resource obtained from the market to a specific depot
     *
     * @param depotNumber the number of the depot to which to send the resource
     * @param resource    the resource to send to the depot
     * @param quantity    the amount of resource to send
     * @throws DepotNotPresentException        if there is no depot corresponding to the given number
     * @throws NotEnoughResourceException      if there is not enough of the selected resource in waiting room
     * @throws BlockedResourceException        if the resource cannot be inserted in the target depot because it is blocked by a different one
     * @throws NotEnoughSpaceException         if the target depot does not have enough space to contain the given amount of resource
     * @throws WrongResourceInsertionException if the target depot cannot hold the given type of resource
     * @throws WrongTurnPhaseException         if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException     if the given parameters are not admissible for the game's rules
     */
    @Override
    public void sendResourceToDepot(int depotNumber, Resource resource, int quantity) throws DepotNotPresentException, NotEnoughResourceException, BlockedResourceException, NotEnoughSpaceException, WrongResourceInsertionException, WrongTurnPhaseException {
        if (turnPhase != TurnPhase.MARKETDISTRIBUTION && turnPhase != LEADERCHOICE) {
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
     * @throws ConversionNotAvailableException if the player does not have access to conversion of white marbles to the given resource
     * @throws NotEnoughResourceException      if the player does not have enough white marbles to convert
     * @throws WrongTurnPhaseException         if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException     if the given parameters are not admissible for the game's rules
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
     * @throws SwapNotValidException       if the content of one or both of the depots cannot be transferred to the other
     * @throws DepotNotPresentException    if there is no depot corresponding to one of the given numbers
     * @throws WrongTurnPhaseException     if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     */
    @Override
    public void swapDepotContent(int depotNumber1, int depotNumber2) throws SwapNotValidException, DepotNotPresentException, WrongTurnPhaseException {
        if (turnPhase == LEADERCHOICE) {
            throw new WrongTurnPhaseException();
        }
        currentPlayer.swapDepotContent(depotNumber1, depotNumber2);
    }

    /**
     * Allows user to send some of the contents of a depot to a different one
     *
     * @param providingDepotNumber the number of the depot from which to take the resources
     * @param receivingDepotNumber the number of the depot to which to move the resources
     * @param resource             the resource to move between the two depots
     * @param quantity             the quantity of the resource to move
     * @throws DepotNotPresentException        if there is no depot corresponding to one of the given numbers
     * @throws NotEnoughResourceException      if there is not enough of the selected resource in the providing depot
     * @throws BlockedResourceException        if the resource cannot be inserted in the receiving depot because it is blocked by a different one
     * @throws NotEnoughSpaceException         if the receiving depot does not have enough space to contain the given amount of resource
     * @throws WrongResourceInsertionException if the receiving depot cannot hold the given type of resource
     * @throws WrongTurnPhaseException         if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException     if the given parameters are not admissible for the game's rules
     */
    @Override
    public void moveDepotContent(int providingDepotNumber, int receivingDepotNumber, Resource resource, int quantity) throws WrongTurnPhaseException, DepotNotPresentException, WrongResourceInsertionException, BlockedResourceException, NotEnoughSpaceException, NotEnoughResourceException {
        if (turnPhase == LEADERCHOICE) {
            throw new WrongTurnPhaseException();
        }
        currentPlayer.moveDepotContent(providingDepotNumber, receivingDepotNumber, resource.getType(), quantity);
    }

    //DevelopmentCard purchasing actions

    /**
     * Allows the player to buy a development card from the cardTable
     *
     * @param cardColor the color of the card to buy
     * @param level     the level of the card to buy
     * @param slot      the card slot in which to put the card
     * @throws SlotNotValidException       if the selected card cannot be inserted in the given slot
     * @throws NotEnoughResourceException  if the player does not have enough resources to buy the card
     * @throws EmptyDeckException          if there are no cards left of the given color and level
     * @throws WrongTurnPhaseException     if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     */
    @Override
    public void takeDevelopmentCard(CardColor cardColor, int level, int slot) throws SlotNotValidException, NotEnoughResourceException, WrongTurnPhaseException, EmptyDeckException {
        if (turnPhase != TurnPhase.ACTIONSELECTION) {
            throw new WrongTurnPhaseException();
        }
        //TODO cardcolor null, invalid row
        cardTable.buyTopCard(cardColor, level, currentPlayer, slot);
        currentPlayer.resetProductionChoice();
        setTurnPhase(CARDPAYMENT);
        notifyObservers();
    }

    //Production selection actions

    /**
     * Allows the player to select a production for activation (STARTS FROM 1)
     *
     * @param number the number of the production
     * @throws ProductionNotPresentException if there is no production that corresponds to the given number
     * @throws WrongTurnPhaseException       if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException   if the given parameters are not admissible for the game's rules
     */
    @Override
    public void selectProduction(int number) throws WrongTurnPhaseException, ProductionNotPresentException {
        if (turnPhase != TurnPhase.ACTIONSELECTION) {
            throw new WrongTurnPhaseException();
        }
        //TODO production does not exist
        currentPlayer.selectProduction(number);
    }

    /**
     * Allows the player to reset the selected productions
     *
     * @throws WrongTurnPhaseException if the player attempts this action when they are not allowed to
     */
    @Override
    public void resetProductionChoice() throws WrongTurnPhaseException {
        if (turnPhase != TurnPhase.ACTIONSELECTION) {
            throw new WrongTurnPhaseException();
        }
        currentPlayer.resetProductionChoice();
    }

    /**
     * Allows the player to choose into which resource to turn a jolly in the production's input
     *
     * @param resource the resource into which to turn the jolly
     * @throws WrongTurnPhaseException     if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     * @throws ResourceNotPresentException if the productions' input does not contain any more jollies
     */
    @Override
    public void chooseJollyInput(Resource resource) throws WrongTurnPhaseException, ResourceNotPresentException {
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
     * @throws WrongTurnPhaseException     if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     * @throws ResourceNotPresentException if the productions' input does not contain any more jollies
     */
    @Override
    public void chooseJollyOutput(Resource resource) throws WrongTurnPhaseException, ResourceNotPresentException {
        if (turnPhase != TurnPhase.ACTIONSELECTION) {
            throw new WrongTurnPhaseException();
        }
        //TODO invalid resource types and null, coherence in jolly/unknownResource naming convention, no jollies
        currentPlayer.chooseJollyOutput(resource);
    }

    /**
     * Allows the player to confirm the selected production for activation
     *
     * @throws NotEnoughResourceException  if the player does not have enough resources to activate the selected productions
     * @throws UnknownResourceException    if the player still has to choose which resources some jollies in the productions' input or output will become
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     */
    @Override
    public void confirmProductionChoice() throws NotEnoughResourceException, UnknownResourceException, WrongTurnPhaseException {
        if (turnPhase != TurnPhase.ACTIONSELECTION || currentPlayer.isProductionInputEmpty()) {
            throw new WrongTurnPhaseException();
        }
        currentPlayer.confirmProductionChoice();
        setTurnPhase(PRODUCTIONPAYMENT);
        notifyObservers();
    }

    //Debt payment actions

    /**
     * Allows the player to pay the development card cost by taking resources from the given depot in the warehouse
     *
     * @param depotNumber the number of the depot from which to take the resource
     * @param resource    the resource to take
     * @param quantity    the amount of resource to take (and of cost to pay)
     * @throws NotEnoughResourceException  if the selected depot does not contain enough of the given resource
     * @throws DepotNotPresentException    if there is no depot corresponding to the given number
     * @throws WrongTurnPhaseException     if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     */
    @Override
    public void payFromWarehouse(int depotNumber, Resource resource, int quantity) throws NotEnoughResourceException, DepotNotPresentException, WrongTurnPhaseException {
        if (turnPhase != TurnPhase.CARDPAYMENT && turnPhase != TurnPhase.PRODUCTIONPAYMENT) {
            throw new WrongTurnPhaseException();
        }
        //TODO resource null, negative quantity, no more debt
        currentPlayer.takeResourceFromWarehouse(depotNumber, resource.getType(), quantity);
    }

    /**
     * Allows the player to pay the development card cost by taking resources from the strongbox
     *
     * @param resource the resource to take
     * @param quantity the amount of resource to take (and of cost to pay)
     * @throws NotEnoughResourceException  if the strongbox does not contain enough of the given resource
     * @throws WrongTurnPhaseException     if the player attempts this action when they are not allowed to
     * @throws ParametersNotValidException if the given parameters are not admissible for the game's rules
     */
    @Override
    public void payFromStrongbox(Resource resource, int quantity) throws NotEnoughResourceException, WrongTurnPhaseException {
        if (turnPhase != TurnPhase.CARDPAYMENT && turnPhase != TurnPhase.PRODUCTIONPAYMENT) {
            throw new WrongTurnPhaseException();
        }
        //TODO resource null, negative quantity, no more debt
        currentPlayer.takeResourceFromStrongbox(resource.getType(), quantity);
    }

    //End turn actions

    /**
     * Allows the player to end their current turn
     *
     * @throws WrongTurnPhaseException if the player attempts this action when they are not allowed to
     */
    @Override
    public void endTurn() throws WrongTurnPhaseException {
        if (turnPhase == LEADERCHOICE) {

            if (currentPlayer.getActiveLeaderCards() != finalLeaderCardNumber || currentPlayer.getLeftInWaitingRoom() > 0) {
                throw new WrongTurnPhaseException();
            }

            currentPlayer.finishLeaderCardSelection();
            currentPlayer.setFirstTurnTaken();
            turnPhase = ACTIONSELECTION;
            notifyObservers();
            return;
        }

        //Does all necessary end turn checks and actions and updates based on the action taken by the player
        endTurnChecks();

        //Checks if the final phase of the game should be triggered
        if (isGameEnding() && !weReInTheEndGameNow) {
            weReInTheEndGameNow = true;
            notifyObservers(); // It needs to be put here because if the currentPlayer is the last one of the list switchPlayer() won't be called and observers wouldn't be notified
        }

        //Checks if the game is in its final phase and the next turn is the first player's, and if so ends the game
        if (weReInTheEndGameNow && currentPlayer == playersTurnOrder.get(playersTurnOrder.size() - 1)) {
            endTheGame();
        }

        //Switches current player to the next one
        switchPlayer();
    }

    //PUBLIC METHODS

    /**
     * Sets the given player's connection status to connected.
     * If all players were disconnected, sets the given player as current player
     *
     * @param username the player's username
     */
    public void setConnectedStatus(String username) {
        for (PlayerBoard player : playersTurnOrder) {
            if (player.getUsername().equals(username)) {

                player.setConnectedStatus();
                System.out.println("Player updated after reconnection");

                if (currentPlayer == null) {
                    chooseTurnStartingPhase(player);
                    currentPlayer = player;
                    notifyObservers();
                }

                return;
            }
        }
        throw new InvalidParameterException();
    }

    /**
     * Sets the given player's connection status to disconnected.
     * If the player is the current player changes the current player
     *
     * @param username the player's username
     */
    public void setDisconnectedStatus(String username) {
        for (PlayerBoard player : playersTurnOrder) {
            if (player.getUsername().equals(username)) {

                if (currentPlayer == null) {
                    throw new ParametersNotValidException();
                } else {
                    player.setDisconnectedStatus();

                    if (currentPlayer == player) {
                        try {
                            endTurn();
                        } catch (WrongTurnPhaseException ex) {
                            currentPlayer.resetProductionChoice();
                            switchPlayer();
                        }
                    }
                }

                return;
            }
        }
        throw new InvalidParameterException();
    }

    /**
     * Returns whether or not the given player is flagged as connected
     *
     * @param username the player's username
     * @return true if the player is flagged as connected
     */
    public boolean isConnected(String username) {
        for (PlayerBoard player : playersTurnOrder) {
            if (player.getUsername().equals(username)) {
                return player.isConnected();
            }
        }

        throw new InvalidParameterException();
    }

    /**
     * This method is called by the Controller right after creating the Game. It creates the beans so that they
     * can notify the clients when something in the model changes
     *
     * @param controller that the beans will have to interact with
     */
    public void createBeans(GameController controller) {
        addObserver(new GameBean(controller));
        getCardTable().addObserver(new CardTableBean(controller));
        getMarket().addObserver(new MarketBean(controller));
        for (PlayerBoard playerBoard : getPlayersTurnOrder()) {
            playerBoard.addObserver(new PlayerBoardBean(controller));
            playerBoard.getStrongbox().addObserver(new StrongboxBean(controller, playerBoard.getUsername()));
            playerBoard.getWaitingRoom().addObserver(new WaitingRoomBean(controller, playerBoard.getUsername()));
            playerBoard.getWarehouse().addObserver(new WarehouseBean(controller, playerBoard.getUsername(), playerBoard.getWarehouse().getNumOfDepots()));
        }

        if (getPlayersTurnOrder().size() == 1) {
            ((Lorenzo) getLorenzo()).addObserver(new LorenzoBean(controller));
        }
    }

    public void updateSinglePlayer(String username) {
        for (Observer observer : cardTable.getObservers())
            observer.updateSinglePlayer(username);
        for (Observer observer : getObservers())
            observer.updateSinglePlayer(username);
        if (lorenzo != null)
            for (Observer observer : ((Lorenzo) lorenzo).getObservers())
                observer.updateSinglePlayer(username);
        for (Observer observer : market.getObservers())
            observer.updateSinglePlayer(username);
        for (PlayerBoard playerBoard : getPlayersTurnOrder()) {
            for (Observer observer : playerBoard.getObservers())
                observer.updateSinglePlayer(username);
            for (Observer observer : playerBoard.getStrongbox().getObservers())
                observer.updateSinglePlayer(username);
            for (Observer observer : playerBoard.getWaitingRoom().getObservers())
                observer.updateSinglePlayer(username);
            for (Observer observer : playerBoard.getWarehouse().getObservers())
                observer.updateSinglePlayer(username);
        }
    }

    //PRIVATE METHODS

    /**
     * Creates the leader cards for the game by reading them from a JSON file
     */
    private void initializeLeaderCards() {
        //TODO controllare valori in input dal JSON (typo nelle enum, valori <0, etc)
        Gson gson = new Gson();
        Reader reader;
        Type LeaderCardArray = new TypeToken<ArrayList<LeaderCard>>() {
        }.getType();

        // depot leader cards
        reader = new InputStreamReader(this.getClass().getResourceAsStream("/json/cards/leadercards/DepotLeaderCards.json"), StandardCharsets.UTF_8);
        Type DepotDecArray = new TypeToken<ArrayList<DepotLeaderCard>>() {
        }.getType();
        ArrayList<DepotLeaderCard> depotLeaderCards = gson.fromJson(reader, DepotDecArray);
        leaderCards.addAll(depotLeaderCards);

        // discount leader cards
        reader = new InputStreamReader(this.getClass().getResourceAsStream("/json/cards/leadercards/DiscountLeaderCards.json"), StandardCharsets.UTF_8);
        Type DiscountDecArray = new TypeToken<ArrayList<DiscountLeaderCard>>() {
        }.getType();
        ArrayList<DiscountLeaderCard> discountLeaderCards = gson.fromJson(reader, DiscountDecArray);
        leaderCards.addAll(discountLeaderCards);

        // marble leader cards
        reader = new InputStreamReader(this.getClass().getResourceAsStream("/json/cards/leadercards/MarbleLeaderCards.json"), StandardCharsets.UTF_8);
        Type MarbleDecArray = new TypeToken<ArrayList<MarbleLeaderCard>>() {
        }.getType();
        ArrayList<MarbleLeaderCard> marbleLeaderCards = gson.fromJson(reader, MarbleDecArray);
        leaderCards.addAll(marbleLeaderCards);

        // production leader cards
        reader = new InputStreamReader(this.getClass().getResourceAsStream("/json/cards/leadercards/ProductionLeaderCards.json"), StandardCharsets.UTF_8);
        Type ProductionDecArray = new TypeToken<ArrayList<ProductionLeaderCard>>() {
        }.getType();
        ArrayList<ProductionLeaderCard> productionLeaderCards = gson.fromJson(reader, ProductionDecArray);
        leaderCards.addAll(productionLeaderCards);

        int i = 1; // i++ prima passa i e poi lo incrementa => se voglio che id parta da 1 devo settare i a 1
        for (LeaderCard leaderCard : leaderCards) {
            leaderCard.setId(i++);
        }
    }

    /**
     * Shuffles the leader cards, then splits them into decks of size initialLeaderCardNumber and gives them to each player
     */
    private void distributeLeaderCards() {

        Collections.shuffle(leaderCards);

        int j = 0;

        for (PlayerBoard playerBoard : playersTurnOrder) {
            List<LeaderCard> leaderCards = new ArrayList<>();
            for (int i = 0; i < initialLeaderCardNumber; i++) {
                leaderCards.add(this.leaderCards.get(j));
                j++;
            }
            playerBoard.addLeaderCards(leaderCards);
        }
    }

    /**
     * Checks number of discarded resources at the end of a player's turn, after they have used the market action.
     * If there are more than zero, increases faith for all other players accordingly
     */
    private void checkDiscarded() {
        int numDiscardedResources = currentPlayer.getLeftInWaitingRoom();

        if (numDiscardedResources > 0) {

            if (lorenzo != null) {
                lorenzo.addFaith(numDiscardedResources);
            } else {
                String currentPlayerName = currentPlayer.getUsername();
                for (PlayerBoard playerBoard : playersTurnOrder) {
                    if (!playerBoard.getUsername().equals(currentPlayerName)) {
                        playerBoard.addFaith(numDiscardedResources);
                    }
                }
            }
            currentPlayer.clearWaitingRoom();

        }
    }

    /**
     * Checks that the player can end their turn based on the action taken and makes end-of-turn tidying up
     */
    private void endTurnChecks() throws WrongTurnPhaseException {
        if (turnPhase == TurnPhase.ACTIONSELECTION) {

            throw new WrongTurnPhaseException();

        } else if (turnPhase == LEADERCHOICE) {

            if (currentPlayer.getActiveLeaderCards() != finalLeaderCardNumber || currentPlayer.getLeftInWaitingRoom() > 0) {
                throw new WrongTurnPhaseException();
            }

            currentPlayer.finishLeaderCardSelection();
            currentPlayer.setFirstTurnTaken();
            return;

        } else if (turnPhase == MARKETDISTRIBUTION) {

            checkDiscarded();

        } else if (turnPhase == CARDPAYMENT) {

            currentPlayer.automaticPayment();

        } else if (turnPhase == PRODUCTIONPAYMENT) {

            currentPlayer.automaticPayment();
            currentPlayer.releaseProductionOutput();

        }

        //If in solo mode, Lorenzo takes His action
        if (lorenzo != null) {
            lorenzo.takeTurn();
        }

        //Activates a vatican report if necessary
        checkVaticanReport();
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
     * Returns whether or not the conditions are met to start the final phase of the game. In order to do that, the
     * method asks each PlayerBoard if it has one of the requirements to call the end of the game by calling isEndGameTime().
     * If the game is in solo mode, this method checks if Lorenzo has any of the requirements that make him win the game
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
     * Switches the current player to the next connected one in the turn order.
     * If there are no players connected, sets current player to null
     */
    private void switchPlayer() {
        int size = playersTurnOrder.size();

        int currentIndex = playersTurnOrder.indexOf(currentPlayer);

        PlayerBoard nextPlayer;
        for (int nextIndex = (currentIndex + 1) % size; nextIndex != currentIndex; nextIndex = (nextIndex + 1) % size) {
            nextPlayer = playersTurnOrder.get(nextIndex);
            if (nextPlayer.isConnected()) {

                chooseTurnStartingPhase(nextPlayer);
                currentPlayer = nextPlayer;
                notifyObservers();
                return;
            }
        }

        if (!currentPlayer.isConnected()) {
            System.out.println("All players have disconnected, next player to reconnect will become current player.");
            currentPlayer = null;
        } else {
            chooseTurnStartingPhase(currentPlayer);
        }

        notifyObservers();
    }

    /**
     * Selects the starting phase for the next player's turn
     *
     * @param nextPlayer the next player in turn order
     */
    private void chooseTurnStartingPhase(PlayerBoard nextPlayer) {
        if (nextPlayer.isFirstTurnTaken()) {
            turnPhase = ACTIONSELECTION;
        } else {
            turnPhase = LEADERCHOICE;
        }
    }

    /**
     * Determines the winner and their score, then prints endgame messages
     */
    private void endTheGame() {
        //TODO this method probably needs to be implemented in some other way tied to the lobby
        if (lorenzo != null) {
            if (!cardTable.checkAllColorsAvailable() || lorenzo.getFaith() >= finalFaith) {
                System.out.println("FATALITY: Lorenzo wins!");
                winner = "Lorenzo";
                winnerVp = lorenzo.getFaith();
            } else {
                System.out.println("FATALITY: " + currentPlayer.getUsername() + " wins with " + currentPlayer.calculateVictoryPoints() + " victory points!");
                winner = currentPlayer.getUsername();
                winnerVp = currentPlayer.calculateVictoryPoints();
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
            this.winner = playersTurnOrder.get(winner).getUsername();
            this.winnerVp = playersTurnOrder.get(winner).calculateVictoryPoints();
        }
        notifyObservers();
    }

    /**
     * Assumes that playersTurnOrder list has been filled already.
     * Shuffles playersTurnOrder, first in list is first player
     */
    private void assignInkwell() {
        Collections.shuffle(playersTurnOrder);
    }

    /**
     * Changes the turn phase to the given one
     *
     * @param turnPhase the new turn phase
     */
    private void setTurnPhase(TurnPhase turnPhase) {
        this.turnPhase = turnPhase;
        notifyObservers();
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
     * @return market
     */
    public Market getMarket() {
        return market;
    }

    /**
     * Getter
     *
     * @return CardTable
     */
    public CardTable getCardTable() {
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

    /**
     * Getter
     *
     * @return lorenzo
     */
    public ArtificialIntelligence getLorenzo() {
        return lorenzo;
    }

    /**
     * Getter
     *
     * @return the turn phase
     */
    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    /**
     * Getter
     *
     * @return the winner is the game has ended. Returns null if the game is still running
     */
    public String getWinner() {
        return winner;
    }

    /**
     * Getter
     *
     * @return the winner's victory points if the game has ended. Returns -1 if the game is still running
     */
    public int getWinnerVp() {
        return winnerVp;
    }

    public boolean isEndGame() {
        return weReInTheEndGameNow;
    }

    // OBSERVABLE ATTRIBUTES AND METHODS

    /**
     * List of observers that need to get updated when the object state changes
     */
    private final List<Observer> observers = new ArrayList<>();

    /**
     * This method calls the update() on every object observing this object
     */
    public void notifyObservers() {
        observers.forEach(observer -> observer.update(this));
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
        notifyObservers();
    }

    public List<Observer> getObservers() {
        return observers;
    }
}