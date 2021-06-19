package client.GUI.controllers;

import client.ClientView;
import client.GUI.SceneName;
import client.GUI.SimplePopup;
import client.GUI.GUI;
import client.GUI.ThiccPopup;
import com.google.gson.Gson;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.CardColor;
import model.PopeTileState;
import model.TurnPhase;
import model.lorenzo.tokens.LorenzoTokenType;
import model.resource.ResourceType;
import network.Command;
import network.MessageType;
import network.UserCommandsType;
import network.beans.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is the GUIController which handles updating and interaction for the GAME_BOARD scene, the main scene of the game
 */
public class GameBoardController implements GUIController {
    /**
     * The client's GUI object
     */
    private GUI gui;
    /**
     * The object used to store all of the client's information
     */
    private ClientView clientView;
    /**
     * The object used to deserialize json messages
     */
    private Gson gson;

    /**
     * The color used for connected players in the turn order display
     */
    private Color turnOrderConnectedColor;
    /**
     * The color used for not connected players in the turn order display
     */
    private Color turnOrderDisconnectedColor;

    /**
     * The username of the player whose board is currently being visualized
     */
    private String visualizedPlayer;
    /**
     * The player who was executing their turn before the last update
     */
    private String previousPlayer;

    /**
     * The graphical elements of this controller's scene
     */
    @FXML
    private GridPane marketGrid, cardsGrid, leaderGrid, faithGrid, leaderButtonGrid, depot1Grid, depot2Grid, depot3Grid, marketRowButtonsGrid, marketColumnButtonsGrid, warehouseButtonsGrid, cardSlotsButtonGrid, leaderDepotButtonGrid, leaderDepotGrid, turnOrderGrid, viewBoardButtonsGrid, lorenzoFaithGrid, lorenzoUsedTokenGrid;
    @FXML
    public AnchorPane cardSlotPane1, cardSlotPane2, cardSlotPane3, waitingRoomPane, lorenzoTokenPane;
    @FXML
    public Label strongboxCoinLabel, strongboxServantLabel, strongboxShieldLabel, strongboxStoneLabel, waitingRoomTitleLabel, waitingRoomWhiteLabel, waitingRoomCoinLabel, waitingRoomServantLabel, waitingRoomShieldLabel, waitingRoomStoneLabel, currentPlayerLabel, turnPhaseLabel, viewedPlayerLabel, lorenzoTokenLeftLabel;
    @FXML
    public ImageView waitingRoomCoin, waitingRoomServant, waitingRoomShield, waitingRoomStone, tile1Image, tile2Image, tile3Image;
    @FXML
    public Button endTurnButton, waitingRoomCoinButton, waitingRoomServantButton, waitingRoomShieldButton, waitingRoomStoneButton, strongboxButton, cancelOperationButton, productionsButton, viewBoard1Button, viewBoard2Button, viewBoard3Button;
    @FXML
    public Text descriptionText;

    //CONSTRUCTORS

    /**
     * Handles initialization for this class, by creating the deserializer and setting the used colors
     */
    public void initialize() {
        this.gson = new Gson();
        this.turnOrderConnectedColor = Color.web("0xE1D892");
        this.turnOrderDisconnectedColor = Color.web("0xDFDCDC");
    }

    //SETTERS

    /**
     * Sets the GUI object for the controller
     *
     * @param gui of type GUI - the main GUI class.
     */
    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
        this.clientView = gui.getClientView();
    }

    //PUBLIC METHODS

    //TODO don't send the entire wrapper but only the type (must save info/error in clientView)

    /**
     * Updates the necessary parts of the scene based on what message was received from the server
     *
     * @param jsonMessage the message received from the server
     */
    @Override
    public void updateFromServer(String jsonMessage) {
        //If there is no visualized player, set it to this client's
        if (visualizedPlayer == null)
            visualizedPlayer = clientView.getUsername();

        MessageWrapper response = gson.fromJson(jsonMessage, MessageWrapper.class);

        //Updates only the part of the scene corresponding to the message received
        switch (response.getType()) {
            case GAME -> drawGameState(clientView.getGame());
            case MARKET -> drawMarket(clientView.getMarket());
            case CARDTABLE -> drawCardTable(clientView.getCardTable());
            case PLAYERBOARD -> drawPlayerBoard(clientView.getPlayerBoard(visualizedPlayer));
            //TODO usare mappe per inviare le risorse pls
            case STRONGBOX -> drawStrongbox(clientView.getStrongbox(visualizedPlayer));
            case WAITINGROOM -> drawWaitingRoom(clientView.getWaitingRoom(visualizedPlayer));
            case WAREHOUSE -> drawWarehouse(clientView.getWarehouse(visualizedPlayer));
            case LORENZO -> drawLorenzo(clientView.getLorenzo());
            case PRODUCTIONHANDLER -> gui.getControllerBySceneName(SceneName.PRODUCTIONS).updateFromServer(jsonMessage);
            case ERROR -> SimplePopup.display(response.getType(), response.getMessage());
            case GAME_END -> switchToGameOverScreen(jsonMessage);
            case PLAYER_CONNECTED -> SimplePopup.display(MessageType.INFO, "Player " + response.getMessage() + " has joined the game.");
            case PLAYER_DISCONNECTED -> SimplePopup.display(MessageType.INFO, "Player " + response.getMessage() + " has left the game (say goodbye like you mean it).");
            default -> System.out.println("Warning: received unexpected message " + jsonMessage);
        }
    }

    //PUBLIC VISUALIZATION METHODS

    /**
     * Displays the production selection screen as a popup
     */
    public void viewProductions() {
        ThiccPopup.display(gui.getSceneBySceneName(SceneName.PRODUCTIONS));
    }

    /**
     * Displays the player board corresponding to the given player and enables only the necessary buttons
     *
     * @param username the username of the player whose board is to be displayed
     */
    public void viewPlayerBoard(String username) {
        visualizedPlayer = username;
        //Drawing the player's board contents
        drawGameState(clientView.getGame());
        drawPlayerBoard(clientView.getPlayerBoard(username));
        drawStrongbox(clientView.getStrongbox(username));
        drawWaitingRoom(clientView.getWaitingRoom(username));
        drawWarehouse(clientView.getWarehouse(username));
        //Buttons for switching view
        enableVisualizedPlayerButtons();
    }

    //PUBLIC COMMAND METHODS

    /**
     * Sends the chooseBonusResourceType command to the server with the given parameters
     *
     * @param resource the type of resource to obtain
     * @param quantity the quantity of resource of the given type to obtain
     */
    public void chooseBonusResourceType(ResourceType resource, int quantity) {
        System.out.println("ChooseBonusResourceType: resource - " + resource + ", quantity - " + quantity);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("resource", resource);
        parameters.put("quantity", quantity);
        Command command = new Command(UserCommandsType.chooseBonusResourceType, parameters);
        gui.sendCommand(gson.toJson(command));
    }

    /**
     * Sends the chooseLeaderCard command to the server with the given parameters
     *
     * @param number the number of the leader card to choose
     */
    public void chooseLeaderCard(int number) {
        System.out.println("ChooseLeaderCard: number - " + number);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("number", number);
        Command command = new Command(UserCommandsType.chooseLeaderCard, parameters);
        gui.sendCommand(gson.toJson(command));
    }

    /**
     * Sends the playLeaderCard command to the server with the given parameters
     *
     * @param number the number of the leader card to play
     */
    public void playLeaderCard(int number) {
        System.out.println("PlayLeaderCard: number - " + number);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("number", number);
        Command command = new Command(UserCommandsType.playLeaderCard, parameters);
        gui.sendCommand(gson.toJson(command));
    }

    /**
     * Sends the discardLeaderCard command to the server with the given parameters
     *
     * @param number the number of the player card to discard
     */
    public void discardLeaderCard(int number) {
        System.out.println("DiscardLeaderCard: number - " + number);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("number", number);
        Command command = new Command(UserCommandsType.discardLeaderCard, parameters);
        gui.sendCommand(gson.toJson(command));
    }

    /**
     * Sends the selectMarketRow command to the server with the given parameters
     *
     * @param number the number of the selected row
     */
    public void selectMarketRow(int number) {
        System.out.println("SelectMarketRow: number - " + number);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("number", number);
        Command command = new Command(UserCommandsType.selectMarketRow, parameters);
        gui.sendCommand(gson.toJson(command));
    }

    /**
     * Sends the selectMarketColumn command to the server with the given parameters
     *
     * @param number the number of the selected column
     */
    public void selectMarketColumn(int number) {
        System.out.println("SelectMarketColumn: number - " + number);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("number", number);
        Command command = new Command(UserCommandsType.selectMarketColumn, parameters);
        gui.sendCommand(gson.toJson(command));
    }

    /**
     * Sends the chooseMarbleConversion command to the server with the given parameters
     *
     * @param resource the resource to convert the white marble into
     * @param quantity the number of white marbles to convert
     */
    public void chooseMarbleConversion(ResourceType resource, int quantity) {
        System.out.println("ChooseMarbleConversion: resource - " + resource + ", quantity - " + quantity);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("resource", resource);
        parameters.put("quantity", quantity);
        Command command = new Command(UserCommandsType.chooseMarbleConversion, parameters);
        gui.sendCommand(gson.toJson(command));
    }

    /**
     * Sends the sendResourceToDepot command to the server with the given parameters
     *
     * @param depotNumber the number of the depot to which to send the resources
     * @param resource    the resource to send
     * @param quantity    the amount of resource to send
     */
    public void sendResourceToDepot(int depotNumber, ResourceType resource, int quantity) {
        System.out.println("SendResourceToDepot: depot number - " + depotNumber + ", resource - " + resource + ", quantity - " + quantity);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("number", depotNumber);
        parameters.put("resource", resource);
        parameters.put("quantity", quantity);
        Command command = new Command(UserCommandsType.sendResourceToDepot, parameters);
        gui.sendCommand(gson.toJson(command));
        //Restore normal buttons
        drawGameState(clientView.getGame());
    }

    /**
     * Sends the swapDepotContent command to the server with the given parameters
     *
     * @param depotNumber1 the first of the depots to be swapped
     * @param depotNumber2 the second of the depots to be swapped
     */
    public void swapDepotContent(int depotNumber1, int depotNumber2) {
        System.out.println("SwapDepotContent: first depot - " + depotNumber1 + ", second depot - " + depotNumber2);
        Map<String, Object> parameters = new HashMap<>();
        int[] depots = new int[2];
        depots[0] = depotNumber1;
        depots[1] = depotNumber2;
        parameters.put("depots", depots);
        Command command = new Command(UserCommandsType.swapDepotContent, parameters);
        gui.sendCommand(gson.toJson(command));
        //Restore normal buttons
        drawGameState(clientView.getGame());
    }

    /**
     * Sends the sendResourceToDepot command to the server with the given parameters
     *
     * @param providingDepotNumber the number of the depot from which to take the resource
     * @param receivingDepotNumber the number of the depot to which to send the resource
     * @param resource             the resource to be transferred
     * @param quantity             the amount of resource to transfer
     */
    public void moveDepotContent(int providingDepotNumber, int receivingDepotNumber, ResourceType resource, int quantity) {

    }

    /**
     * Sends the takeDevelopmentCard command to the server with the given parameters
     *
     * @param cardColor the color of the card to take
     * @param level     the level of the card to take
     * @param slot      the card slot in which to put the card
     */
    public void takeDevelopmentCard(CardColor cardColor, int level, int slot) {
        System.out.println("TakeDevelopmentCard: color - " + cardColor + ", level - " + level + ", slot - " + slot);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("number", slot);
        parameters.put("color", cardColor);
        parameters.put("level", level);
        Command command = new Command(UserCommandsType.takeDevelopmentCard, parameters);
        gui.sendCommand(gson.toJson(command));
        //Restore normal buttons
        drawGameState(clientView.getGame());
    }

    /**
     * Sends the payFromWarehouse command to the server with the given parameters
     *
     * @param depotNumber the number of the depot from which to take the resource
     * @param resource    the resource to take
     * @param quantity    the amount of resource to take
     */
    public void payFromWarehouse(int depotNumber, ResourceType resource, int quantity) {
        System.out.println("PayFromWarehouse: depot number - " + depotNumber + ", resource - " + resource + ", quantity - " + quantity);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("number", depotNumber);
        parameters.put("resource", resource);
        parameters.put("quantity", quantity);
        Command command = new Command(UserCommandsType.payFromWarehouse, parameters);
        gui.sendCommand(gson.toJson(command));
        //Restore normal buttons
        drawGameState(clientView.getGame());
    }

    /**
     * Sends the payFromStrongbox command to the server with the given parameters
     *
     * @param resource the resource to take
     * @param quantity the amount of resource to take
     */
    public void payFromStrongbox(ResourceType resource, int quantity) {
        System.out.println("PayFromWarehouse: resource - " + resource + ", quantity - " + quantity);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("resource", resource);
        parameters.put("quantity", quantity);
        Command command = new Command(UserCommandsType.payFromStrongbox, parameters);
        gui.sendCommand(gson.toJson(command));
        //Restore normal buttons
        drawGameState(clientView.getGame());
    }

    /**
     * Sends the endTurn command to the server
     */
    public void endTurn() {
        System.out.println("EndTurn");
        Command command = new Command(UserCommandsType.endTurn, null);
        gui.sendCommand(gson.toJson(command));
    }

    //PRIVATE BUTTON HANDLING METHODS

    /**
     * Disables all of the board's buttons and interactive elements
     */
    private void disableButtons() {
        waitingRoomPane.setVisible(false);
        waitingRoomCoin.setOnMouseClicked(null);
        waitingRoomServant.setOnMouseClicked(null);
        waitingRoomShield.setOnMouseClicked(null);
        waitingRoomStone.setOnMouseClicked(null);
        waitingRoomCoinButton.setVisible(false);
        waitingRoomServantButton.setVisible(false);
        waitingRoomShieldButton.setVisible(false);
        waitingRoomStoneButton.setVisible(false);
        strongboxButton.setVisible(false);
        cancelOperationButton.setVisible(false);
        for (Node card : leaderGrid.getChildren())
            card.setOnMouseClicked(null);
        for (Node button : leaderButtonGrid.getChildren())
            button.setVisible(false);
        for (Node button : marketRowButtonsGrid.getChildren())
            button.setVisible(false);
        for (Node button : marketColumnButtonsGrid.getChildren())
            button.setVisible(false);
        for (Node button : warehouseButtonsGrid.getChildren())
            button.setVisible(false);
        for (Node card : cardsGrid.getChildren()) {
            card.setOnMouseClicked(null);
            card.getStyleClass().clear();
        }
        for (Node button : cardSlotsButtonGrid.getChildren())
            button.setVisible(false);
        for (Node button : leaderDepotButtonGrid.getChildren())
            button.setVisible(false);
        for (Node button : viewBoardButtonsGrid.getChildren())
            button.setVisible(false);
        productionsButton.setVisible(false);
        endTurnButton.setDisable(true);
    }

    /**
     * Handles enabling the interactive elements necessary for switching between different players' boards
     */
    private void enableVisualizedPlayerButtons() {
        //For viewing other player's boards
        String[] turnOrder = clientView.getGame().getTurnOrder();
        List<Node> viewBoardChildren = viewBoardButtonsGrid.getChildren();
        for (int i = turnOrder.length - 1, j = viewBoardChildren.size() - 1; i >= 0; i--, j--) {
            Button viewBoardButton = (Button) viewBoardChildren.get(j);
            viewBoardButton.setText("View " + turnOrder[i] + "'s board");
            int finalI = i;
            viewBoardButton.setOnAction(e -> viewPlayerBoard(turnOrder[finalI]));
            //Disable button for currently visualized player
            if (turnOrder[i].equals(visualizedPlayer))
                viewBoardButton.setDisable(true);
            else
                viewBoardButton.setDisable(false);
            //Change text for your personal button
            if (turnOrder[i].equals(clientView.getUsername()))
                viewBoardButton.setText("View your board");
            else
                viewBoardButton.setText("View " + turnOrder[i] + "'s board");
            //Make buttons visible for all players
            viewBoardButton.setVisible(true);
        }
    }

    /**
     * Handles enabling for the interactive elements that have to always be active when the player is acting after the preliminary game phase
     */
    private void enableAfterBeginningButtons() {
        //For playLeaderCard
        ObservableList<Node> leaderChildren = leaderGrid.getChildren();
        for (int i = 0; i < leaderChildren.size(); i++) {
            int finalI = i + 1;
            leaderChildren.get(i).setOnMouseClicked(e -> playLeaderCard(finalI));
        }
        //For discardLeaderCard
        ObservableList<Node> leaderButtons = leaderButtonGrid.getChildren();
        for (int i = 0; i < leaderButtons.size(); i++) {
            leaderButtons.get(i).setVisible(true);
            int finalI = i + 1;
            leaderButtons.get(i).setOnMouseClicked(e -> discardLeaderCard(finalI));
        }
    }

    /**
     * Handles enabling for the interactive elements necessary for the first phase of sending resources to a depot
     */
    private void enableSendResourceToDepotButtons() {
        waitingRoomCoinButton.setVisible(true);
        waitingRoomServantButton.setVisible(true);
        waitingRoomShieldButton.setVisible(true);
        waitingRoomStoneButton.setVisible(true);
        waitingRoomCoinButton.setText("Send to depot");
        waitingRoomServantButton.setText("Send to depot");
        waitingRoomShieldButton.setText("Send to depot");
        waitingRoomStoneButton.setText("Send to depot");
        waitingRoomCoinButton.setOnAction(e -> setupSendToDepotChoice(ResourceType.COIN));
        waitingRoomServantButton.setOnAction(e -> setupSendToDepotChoice(ResourceType.SERVANT));
        waitingRoomShieldButton.setOnAction(e -> setupSendToDepotChoice(ResourceType.SHIELD));
        waitingRoomStoneButton.setOnAction(e -> setupSendToDepotChoice(ResourceType.STONE));
    }

    /**
     * Handles enabling for the interactive elements necessary for the first phase of paying resources after taking a card or activating productions
     */
    private void enablePaymentButtons() {
        waitingRoomCoinButton.setVisible(true);
        waitingRoomServantButton.setVisible(true);
        waitingRoomShieldButton.setVisible(true);
        waitingRoomStoneButton.setVisible(true);
        waitingRoomCoinButton.setText("Pay");
        waitingRoomServantButton.setText("Pay");
        waitingRoomShieldButton.setText("Pay");
        waitingRoomStoneButton.setText("Pay");
        waitingRoomCoinButton.setOnAction(e -> setupPaymentChoice(ResourceType.COIN));
        waitingRoomServantButton.setOnAction(e -> setupPaymentChoice(ResourceType.SERVANT));
        waitingRoomShieldButton.setOnAction(e -> setupPaymentChoice(ResourceType.SHIELD));
        waitingRoomStoneButton.setOnAction(e -> setupPaymentChoice(ResourceType.STONE));
    }

    //TODO un modo migliore di capire il colore

    /**
     * Handles enabling for the interactive elements necessary for the first phase of taking a development card
     */
    private void enableTakeDevelopmentCardButtons() {
        ObservableList<Node> cardTableChildren = cardsGrid.getChildren();
        int[][] cardTable = clientView.getCardTable().getCards();
        for (int i = 0, k = 0; i < cardTable.length; i++) {
            for (int j = 0; j < cardTable[0].length; j++, k++) {
                int cardId = cardTable[i][j];
                int finalI = i + 1;
                //switch by card color (first 12 cards starting from id 17 are blue, the following 12 are green and so on)
                switch ((cardId - 17) / 12) {
                    case 0 -> cardTableChildren.get(k).setOnMouseClicked(e -> setupCardSlotChoice(CardColor.BLUE, finalI));
                    case 1 -> cardTableChildren.get(k).setOnMouseClicked(e -> setupCardSlotChoice(CardColor.GREEN, finalI));
                    case 2 -> cardTableChildren.get(k).setOnMouseClicked(e -> setupCardSlotChoice(CardColor.PURPLE, finalI));
                    case 3 -> cardTableChildren.get(k).setOnMouseClicked(e -> setupCardSlotChoice(CardColor.YELLOW, finalI));
                }
                //Makes the card graphically responsive to hovering
                cardTableChildren.get(k).getStyleClass().add("card");
            }
        }
    }

    /**
     * Handles enabling for the interactive elements necessary for the first phase of swapping two depots
     */
    private void enableSwapDepotButtons() {
        //Warehouse depots
        List<Node> depotButtons = warehouseButtonsGrid.getChildren();
        for (int i = 0; i < depotButtons.size(); i++) {
            Button button = (Button) depotButtons.get(i);
            button.setVisible(true);
            button.setText("Swap");
            int finalI = i + 1;
            button.setOnAction(e -> setupDepotSwap(finalI));
        }
        //Leader Depots
        PlayerBoardBean playerBoard = clientView.getPlayerBoard(clientView.getUsername());
        if (playerBoard != null) {
            Map<Integer, Integer> leaderDepotCards = playerBoard.getLeaderDepotCards();
            List<Node> leaderDepotButtons = leaderDepotButtonGrid.getChildren();
            for (int depot : leaderDepotCards.keySet()) {
                int card = leaderDepotCards.get(depot);
                ((Button) leaderDepotButtons.get(card - 1)).setOnAction(e -> setupDepotSwap(depot));
                ((Button) leaderDepotButtons.get(card - 1)).setText("Swap");
                leaderDepotButtons.get(card - 1).setVisible(true);
            }
        }
    }

    //PRIVATE SETUP METHODS

    /**
     * Sets up the interface in accordance to what the player can do during the preliminary phase of the game
     */
    private void setupLeaderChoice() {
        waitingRoomTitleLabel.setText("Resources left to distribute:");
        descriptionText.setText("Choose two leadercards to keep and, if necessary, which bonus resources to obtain (by clicking on the corresponding resource) and where to store them. Then confirm.");

        disableButtons();
        waitingRoomPane.setVisible(true);
        //For chooseBonusResourceType
        waitingRoomCoin.setOnMouseClicked(e -> chooseBonusResourceType(ResourceType.COIN, 1));
        waitingRoomServant.setOnMouseClicked(e -> chooseBonusResourceType(ResourceType.SERVANT, 1));
        waitingRoomShield.setOnMouseClicked(e -> chooseBonusResourceType(ResourceType.SHIELD, 1));
        waitingRoomStone.setOnMouseClicked(e -> chooseBonusResourceType(ResourceType.STONE, 1));
        //For chooseLeaderCard
        ObservableList<Node> leaderChildren = leaderGrid.getChildren();
        for (int i = 0; i < leaderChildren.size(); i++) {
            int finalI = i;
            leaderChildren.get(i).setOnMouseClicked(e -> chooseLeaderCard(finalI + 1));
        }
        //For sendResourceToDepot
        enableSendResourceToDepotButtons();
        //For swapDepotContent
        enableSwapDepotButtons();
        //For endTurn
        endTurnButton.setDisable(false);
        endTurnButton.setText("Confirm");
        endTurnButton.setOnAction(e -> endTurn());
    }

    /**
     * Sets up the interface in accordance to what the player can do during the choice of an action
     */
    private void setupActionSelection() {
        waitingRoomTitleLabel.setText("Waiting room:");
        descriptionText.setText("Choose either a row or column of the market, a development card to buy or a set of productions to activate. Jolly resources in input and output have to be chosen before confirming.");

        disableButtons();
        waitingRoomPane.setVisible(false);
        //For selectMarketRow
        List<Node> rowButtons = marketRowButtonsGrid.getChildren();
        for (int i = 0; i < rowButtons.size(); i++) {
            Button button = (Button) rowButtons.get(i);
            button.setVisible(true);
            int finalI = i + 1;
            button.setOnAction(e -> selectMarketRow(finalI));
        }
        //For selectMarketColumn
        List<Node> columnButtons = marketColumnButtonsGrid.getChildren();
        for (int i = 0; i < columnButtons.size(); i++) {
            Button button = (Button) columnButtons.get(i);
            button.setVisible(true);
            int finalI = i + 1;
            button.setOnAction(e -> selectMarketColumn(finalI));
        }
        //For takeDevelopmentCard
        enableTakeDevelopmentCardButtons();
        //For swapDepotContent
        enableSwapDepotButtons();
        //For playLeaderCard, discardLeaderCard
        enableAfterBeginningButtons();
        //For switching view
        enableVisualizedPlayerButtons();
        //For productions stuff
        productionsButton.setVisible(true);
        //For the end turn button
        endTurnButton.setText("End Turn");
    }

    /**
     * Sets up the interface in accordance to what the player can do during the choice of where to send resources taken from the market
     */
    private void setupMarketDistribution() {
        waitingRoomTitleLabel.setText("Resources left to distribute:");
        descriptionText.setText("Choose where to store the obtained resources, or discard those left by ending your turn.");

        disableButtons();
        waitingRoomPane.setVisible(true);
        //For chooseMarbleConversion
        waitingRoomCoin.setOnMouseClicked(e -> chooseMarbleConversion(ResourceType.COIN, 1));
        waitingRoomServant.setOnMouseClicked(e -> chooseMarbleConversion(ResourceType.SERVANT, 1));
        waitingRoomShield.setOnMouseClicked(e -> chooseMarbleConversion(ResourceType.SHIELD, 1));
        waitingRoomStone.setOnMouseClicked(e -> chooseMarbleConversion(ResourceType.STONE, 1));
        //For sendResourceToDepot
        enableSendResourceToDepotButtons();
        //For swapDepotContent
        enableSwapDepotButtons();
        //For playLeaderCard, discardLeaderCard
        enableAfterBeginningButtons();
        //For switching view
        enableVisualizedPlayerButtons();
        //For endTurn
        endTurnButton.setDisable(false);
        endTurnButton.setText("End Turn");
        endTurnButton.setOnAction(e -> endTurn());
    }

    /**
     * Sets up the interface in accordance to what the player can do during the choice of where to take the resources used in payments
     */
    private void setupPayment() {
        waitingRoomTitleLabel.setText("Resources left to pay:");
        descriptionText.setText("Choose from where to pay your resources, or end your turn to have it chosen automatically.");

        disableButtons();
        waitingRoomPane.setVisible(true);
        //For payFromWarehouse, payFromStrongbox
        enablePaymentButtons();
        //For swapDepotContent
        enableSwapDepotButtons();
        //For playLeaderCard, discardLeaderCard
        enableAfterBeginningButtons();
        //For switching view
        enableVisualizedPlayerButtons();
        //For endTurn
        endTurnButton.setDisable(false);
        endTurnButton.setText("End Turn");
        endTurnButton.setOnAction(e -> endTurn());
    }

    /**
     * Sets up the interface in accordance to what the player can do during the second phase of sending a resource to a depot.
     * Specifically, allows the player to select the destination depot
     *
     * @param resource the resource chosen to be sent
     */
    private void setupSendToDepotChoice(ResourceType resource) {
        descriptionText.setText("Choose which depot to send the resource to.");

        disableButtons();
        //Warehouse depots
        List<Node> depotButtons = warehouseButtonsGrid.getChildren();
        for (int i = 0; i < depotButtons.size(); i++) {
            Button button = (Button) depotButtons.get(i);
            button.setVisible(true);
            int finalI = i + 1;
            button.setText("Depot " + finalI);
            button.setOnAction(e -> sendResourceToDepot(finalI, resource, 1));
        }
        //Leader Depots
        PlayerBoardBean playerBoard = clientView.getPlayerBoard(clientView.getUsername());
        if (playerBoard != null) {
            Map<Integer, Integer> leaderDepotCards = playerBoard.getLeaderDepotCards();
            List<Node> leaderDepotButtons = leaderDepotButtonGrid.getChildren();
            for (int depot : leaderDepotCards.keySet()) {
                int card = leaderDepotCards.get(depot);
                ((Button) leaderDepotButtons.get(card - 1)).setOnAction(e -> sendResourceToDepot(depot, resource, 1));
                ((Button) leaderDepotButtons.get(card - 1)).setText("Leader depot " + card);
                leaderDepotButtons.get(card - 1).setVisible(true);
            }
        }
        //For resetting halfway through
        cancelOperationButton.setVisible(true);
        cancelOperationButton.setOnAction(e -> drawGameState(clientView.getGame()));
    }

    /**
     * Sets up the interface in accordance to what the player can do during the second phase of taking a development card.
     * Specifically, allows the player to choose in which slot to send the selected card
     *
     * @param color the color of the chosen card
     * @param level the level of the chosen card
     */
    private void setupCardSlotChoice(CardColor color, int level) {
        descriptionText.setText("Choose which slot to put the card in.");

        disableButtons();
        List<Node> cardSlotsButtons = cardSlotsButtonGrid.getChildren();
        for (int i = 0; i < cardSlotsButtons.size(); i++) {
            Button button = (Button) cardSlotsButtons.get(i);
            button.setVisible(true);
            int finalI = i + 1;
            button.setText("Slot " + finalI);
            button.setOnAction(e -> takeDevelopmentCard(color, level, finalI));
        }
        //For resetting halfway through
        cancelOperationButton.setVisible(true);
        cancelOperationButton.setOnAction(e -> drawGameState(clientView.getGame()));
    }

    /**
     * Sets up the interface in accordance to what the player can do during the second phase of paying for a development card or production.
     * Specifically, allows the player to choose from which storage to take the resource
     *
     * @param resource the resource chosen to be payed
     */
    private void setupPaymentChoice(ResourceType resource) {
        descriptionText.setText("Choose whether to take the resource from a depot or the strongbox.");

        disableButtons();
        //Basic depots
        List<Node> depotButtons = warehouseButtonsGrid.getChildren();
        for (int i = 0; i < depotButtons.size(); i++) {
            Button button = (Button) depotButtons.get(i);
            button.setVisible(true);
            int finalI = i + 1;
            button.setText("Depot " + finalI);
            button.setOnAction(e -> payFromWarehouse(finalI, resource, 1));
        }
        //Leader Depots
        PlayerBoardBean playerBoard = clientView.getPlayerBoard(clientView.getUsername());
        if (playerBoard != null) {
            Map<Integer, Integer> leaderDepotCards = playerBoard.getLeaderDepotCards();
            List<Node> leaderDepotButtons = leaderDepotButtonGrid.getChildren();
            for (int depot : leaderDepotCards.keySet()) {
                int card = leaderDepotCards.get(depot);
                ((Button) leaderDepotButtons.get(card - 1)).setOnAction(e -> payFromWarehouse(depot, resource, 1));
                ((Button) leaderDepotButtons.get(card - 1)).setText("Leader depot " + card);
                leaderDepotButtons.get(card - 1).setVisible(true);
            }
        }
        //Strongbox
        strongboxButton.setOnAction(e -> payFromStrongbox(resource, 1));
        strongboxButton.setVisible(true);
        //For resetting halfway through
        cancelOperationButton.setVisible(true);
        cancelOperationButton.setOnAction(e -> drawGameState(clientView.getGame()));
    }

    /**
     * Sets up the interface in accordance to what the player can do during the second phase of swapping two depots.
     * Specifically, allows the player to choose which depot to swap the one they have already selected with
     *
     * @param depot1 the chosen depot to be swapped
     */
    private void setupDepotSwap(int depot1) {
        descriptionText.setText("Choose which depot to swap the selected one with.");

        disableButtons();
        List<Node> depotButtons = warehouseButtonsGrid.getChildren();
        //Warehouse depots
        for (int i = 0; i < depotButtons.size(); i++) {
            Button button = (Button) depotButtons.get(i);
            button.setVisible(true);
            int finalI = i + 1;
            button.setText("Depot " + finalI);
            button.setOnAction(e -> swapDepotContent(depot1, finalI));
        }
        //Leader Depots
        PlayerBoardBean playerBoard = clientView.getPlayerBoard(clientView.getUsername());
        if (playerBoard != null) {
            Map<Integer, Integer> leaderDepotCards = playerBoard.getLeaderDepotCards();
            List<Node> leaderDepotButtons = leaderDepotButtonGrid.getChildren();
            for (int depot : leaderDepotCards.keySet()) {
                int card = leaderDepotCards.get(depot);
                ((Button) leaderDepotButtons.get(card - 1)).setOnAction(e -> swapDepotContent(depot1, depot));
                ((Button) leaderDepotButtons.get(card - 1)).setText("Leader depot " + card);
                leaderDepotButtons.get(card - 1).setVisible(true);
            }
        }
        //For resetting halfway through
        cancelOperationButton.setVisible(true);
        cancelOperationButton.setOnAction(e -> drawGameState(clientView.getGame()));
    }

    //PRIVATE DRAWING METHODS

    /**
     * Updates the sections of the scene pertaining to the game state, and activates the buttons corresponding to the current game phase
     *
     * @param gameBean the object containing the game state information
     */
    private void drawGameState(GameBean gameBean) {
        String currPlayer = gameBean.getCurrentPlayer();
        TurnPhase currPhase = gameBean.getTurnPhase();

        //Turn phase information
        drawTurnPhaseInfo(currPlayer, currPhase);

        //Turn order
        drawTurnOrder(gameBean.getTurnOrder(), gameBean.getConnectedPlayers());

        //Activates buttons and interactibles
        if (!visualizedPlayer.equals(clientView.getUsername())) {
            //If viewing another player's board
            descriptionText.setText("You are viewing another player's board.");
            disableButtons();
            if (currPhase != TurnPhase.LEADERCHOICE)
                enableVisualizedPlayerButtons();
        } else if (!currPlayer.equals(clientView.getUsername())) {
            //If not currently the client's turn
            descriptionText.setText("You are not the current player.");
            disableButtons();
            if (currPhase != TurnPhase.LEADERCHOICE)
                enableVisualizedPlayerButtons();
        } else {
            //Activates specific interactibles depending on the current turn phase
            switch (currPhase) {
                case LEADERCHOICE -> setupLeaderChoice();
                case ACTIONSELECTION -> setupActionSelection();
                case MARKETDISTRIBUTION -> setupMarketDistribution();
                case CARDPAYMENT, PRODUCTIONPAYMENT -> setupPayment();
            }
        }
    }

    /**
     * Updates the sections of the scene pertaining to the current game phase
     *
     * @param currPlayer the username of the current player
     * @param currPhase  the current turn phase
     */
    private void drawTurnPhaseInfo(String currPlayer, TurnPhase currPhase) {
        currentPlayerLabel.setText(currPlayer);
        //Alerts the player if it is now their turn and previously was not
        if (currPlayer.equals(clientView.getUsername()) && !currPlayer.equals(previousPlayer))
            SimplePopup.display(MessageType.INFO, "It's your turn to act!");
        previousPlayer = (currPlayer);
        turnPhaseLabel.setText(currPhase.vanillaToString());
        viewedPlayerLabel.setText(visualizedPlayer);
    }

    /**
     * Updates the sections of the scene pertaining to the game's turn order
     *
     * @param turnOrder        the game's players' usernames, arranged following the turn order
     * @param connectedPlayers an array detailing which players are still connected
     */
    private void drawTurnOrder(String[] turnOrder, boolean[] connectedPlayers) {
        List<Node> turnOrderChildren = turnOrderGrid.getChildren();
        int i = 0;
        //Draw the players' usernames on the list
        for (; i < turnOrder.length; i++) {
            String userName = turnOrder[i];
            //If drawing your username add "(you)"
            if (turnOrder[i].equals(clientView.getUsername())) {
                userName += " (you)";
            }
            //If player is disconnected change color
            if (connectedPlayers[i]) {
                ((Label) turnOrderChildren.get(i)).setTextFill(turnOrderConnectedColor);
            } else {
                ((Label) turnOrderChildren.get(i)).setTextFill(turnOrderDisconnectedColor);
                userName += " (x)";
            }
            ((Label) turnOrderChildren.get(i)).setText(userName);
        }
        //Empty the rest of the list
        for (; i < turnOrderChildren.size(); i++) {
            ((Label) turnOrderChildren.get(i)).setText(null);
        }
        //Add lorenzo on the list if in single player game
        if (turnOrder.length == 1)
            ((Label) turnOrderChildren.get(1)).setText("LORENZO");
    }

    /**
     * Updates the sections of the scene pertaining to the market
     *
     * @param marketBean the object containing the market information
     */
    private void drawMarket(MarketBean marketBean) {
        ResourceType[][] marketBoard = marketBean.getMarketBoard();
        ObservableList<Node> marketChildren = marketGrid.getChildren();
        //Draws the market board
        for (int i = 0, k = 0; i < marketBoard.length; i++) {
            for (int j = 0; j < marketBoard[0].length; j++, k++) {
                ResourceType resource = marketBoard[i][j];
                Image marble = new Image("/graphics/punchboard/" + resource.getMarbleImage());
                ((ImageView) marketChildren.get(k)).setImage(marble);
            }
        }
        //Draws the marble on the slider
        ResourceType resource = marketBean.getSlide();
        Image marble = new Image("/graphics/punchboard/" + resource.getMarbleImage());
        ((ImageView) marketChildren.get(marketChildren.size() - 1)).setImage(marble);
    }

    /**
     * Updates the sections of the scene pertaining to the card table
     *
     * @param cardTableBean the object containing the card table information
     */
    private void drawCardTable(CardTableBean cardTableBean) {
        int[][] cardTable = cardTableBean.getCards();
        ObservableList<Node> cardTableChildren = cardsGrid.getChildren();

        //Draws the card table
        for (int i = 0, k = 0; i < cardTable.length; i++) {
            for (int j = 0; j < cardTable[0].length; j++, k++) {
                int cardId = cardTable[i][j];
                Image card;
                //If the deck is empty, display a placeholder
                if (cardId != -1) {
                    card = new Image("/graphics/front/" + cardId + ".png");
                } else {
                    card = new Image("/graphics/back/leadercardBack.png");
                }
                ((ImageView) cardTableChildren.get(k)).setImage(card);
            }
        }
    }

    /**
     * Updates the sections of the scene pertaining to the player board
     *
     * @param playerBoardBean the object containing the visualized player's player board information
     */
    private void drawPlayerBoard(PlayerBoardBean playerBoardBean) {
        //Faith track
        drawFaithTrack(faithGrid, "/graphics/punchboard/faithMarker.png", playerBoardBean.getFaith());

        //Pope tiles
        PopeTileState[] popeTileStates = playerBoardBean.getPopeTileStates();
        drawPopeTile(popeTileStates[0], 1, tile1Image);
        drawPopeTile(popeTileStates[1], 2, tile2Image);
        drawPopeTile(popeTileStates[2], 3, tile3Image);

        //Leader cards
        drawLeaderCards(playerBoardBean);

        //Card slots
        SlotBean[] cardSlotBeans = playerBoardBean.getCardSlots();
        drawCardSlot(cardSlotBeans[0], cardSlotPane1);
        drawCardSlot(cardSlotBeans[1], cardSlotPane2);
        drawCardSlot(cardSlotBeans[2], cardSlotPane3);

        //White marbles
        waitingRoomWhiteLabel.setText(Integer.toString(playerBoardBean.getWhiteMarbles()));
    }

    /**
     * Updates the sections of the scene pertaining to the strongbox
     *
     * @param strongboxBean the object containing the visualized player's strongbox information
     */
    private void drawStrongbox(StrongboxBean strongboxBean) {
        int[] strongboxContents = strongboxBean.getQuantity();
        //The amount of each resource in strongbox
        strongboxCoinLabel.setText(Integer.toString(strongboxContents[0]));
        strongboxServantLabel.setText(Integer.toString(strongboxContents[1]));
        strongboxShieldLabel.setText(Integer.toString(strongboxContents[2]));
        strongboxStoneLabel.setText(Integer.toString(strongboxContents[3]));
    }

    /**
     * Updates the sections of the scene pertaining to the waiting room for resources
     *
     * @param waitingRoomBean the object containing the visualized player's waiting room information
     */
    private void drawWaitingRoom(WaitingRoomBean waitingRoomBean) {
        int[] waitingRoomContents = waitingRoomBean.getQuantity();
        //The amount of each resource in waiting room
        waitingRoomCoinLabel.setText(Integer.toString(waitingRoomContents[0]));
        waitingRoomServantLabel.setText(Integer.toString(waitingRoomContents[1]));
        waitingRoomShieldLabel.setText(Integer.toString(waitingRoomContents[2]));
        waitingRoomStoneLabel.setText(Integer.toString(waitingRoomContents[3]));
    }

    /**
     * Updates the sections of the scene pertaining to the warehouse and leader depots
     *
     * @param warehouseBean the object containing the visualized player's warehouse and leader depot information
     */
    private void drawWarehouse(WarehouseBean warehouseBean) {
        int[] depotQuantities = warehouseBean.getDepotQuantity();
        ResourceType[] depotTypes = warehouseBean.getDepotType();

        //The resources in each depot
        drawDepot(depotQuantities[0], depotTypes[0], depot1Grid);
        drawDepot(depotQuantities[1], depotTypes[1], depot2Grid);
        drawDepot(depotQuantities[2], depotTypes[2], depot3Grid);

        //The resources in each leader depot
        if (depotQuantities.length > 3 && clientView.getPlayerBoard(clientView.getUsername()) != null) {
            int i = 3;
            List<Node> leaderDepots = leaderDepotGrid.getChildren();
            Map<Integer, Integer> leaderDepotCards = clientView.getPlayerBoard(clientView.getUsername()).getLeaderDepotCards();
            for (int depot : leaderDepotCards.keySet()) {
                drawDepot(depotQuantities[i], depotTypes[i], (GridPane) leaderDepots.get(leaderDepotCards.get(depot) - 1));
                i++;
            }
        }
    }

    /**
     * Updates the sections of the scene pertaining to the faith track
     *
     * @param grid     the scene element used to represent the track, either the visualized player's or lorenzo's
     * @param imageUrl the image used to represent the faith marker
     * @param faith    the faith score being represented
     */
    private void drawFaithTrack(GridPane grid, String imageUrl, int faith) {
        Image token = new Image(imageUrl);
        ObservableList<Node> faithTrackChildren = grid.getChildren();
        for (Node faithTrackChild : faithTrackChildren) {
            ((ImageView) faithTrackChild).setImage(null);
        }
        if (faith > 24)
            faith = 24;
        ((ImageView) faithTrackChildren.get(faith)).setImage(token);

    }

    /**
     * Updates the sections of the scene pertaining to the leader cards (excluding depots)
     *
     * @param playerBoardBean the object containing the visualized player's player board information
     */
    private void drawLeaderCards(PlayerBoardBean playerBoardBean) {
        int[] leaderCards = playerBoardBean.getLeaderCards();
        boolean[] activeLeaderCards = playerBoardBean.getActiveLeaderCards();
        ObservableList<Node> leaderChildren = leaderGrid.getChildren();

        for (int i = 0; i < leaderChildren.size(); i++) {
            Image card;
            if (i < leaderCards.length) {
                //Handles effects and viewing
                if (activeLeaderCards[i]) {
                    leaderChildren.get(i).getStyleClass().add("selectedCard");
                    card = new Image("/graphics/front/" + leaderCards[i] + ".png");
                } else {
                    //If viewing other players, only shows active cards
                    if (visualizedPlayer.equals(clientView.getUsername()))
                        card = new Image("/graphics/front/" + leaderCards[i] + ".png");
                    else
                        card = new Image("/graphics/back/leadercardBack.png");
                    leaderChildren.get(i).getStyleClass().clear();
                    leaderChildren.get(i).getStyleClass().add("card");
                }
            } else {
                card = null;
                leaderChildren.get(i).getStyleClass().clear();
            }
            ((ImageView) leaderChildren.get(i)).setImage(card);
        }
    }

    /**
     * Updates the sections of the scene pertaining to a card slot
     *
     * @param cardSlotBean the object containing the visualized player's card slots information
     * @param slotPane     the scene element representing the visualized player's specific card slot
     */
    private void drawCardSlot(SlotBean cardSlotBean, AnchorPane slotPane) {
        ObservableList<Node> slotChildren = slotPane.getChildren();
        int[] slot = cardSlotBean.getDevelopmentCards();
        for (int i = 0; i < slotChildren.size(); i++) {
            Image card;
            if (i < slot.length) {
                card = new Image("/graphics/front/" + slot[i] + ".png");
                ((ImageView) slotChildren.get(i)).setImage(card);
            }
        }
    }

    /**
     * Updates the sections of the scene pertaining to a pope's favor tile
     *
     * @param state      the state of the tile
     * @param tileNumber the number of the tile
     * @param imageView  the scene element representing the visualized player's specific pope's favor tile
     */
    private void drawPopeTile(PopeTileState state, int tileNumber, ImageView imageView) {
        Image tile;
        if (state == PopeTileState.INACTIVE) {
            tile = new Image("/graphics/punchboard/tile" + tileNumber + "Back.png");
        } else if (state == PopeTileState.ACTIVE) {
            tile = new Image("/graphics/punchboard/tile" + tileNumber + "Front.png");
        } else {
            tile = null;
        }
        imageView.setImage(tile);
    }

    /**
     * Updates the sections of the scene pertaining to a warehouse depot
     *
     * @param quantity  the amount of resource do be represented
     * @param type      the type of resource to be represented
     * @param depotGrid the scene element representing the visualized player's specific warehouse depot
     */
    private void drawDepot(int quantity, ResourceType type, GridPane depotGrid) {
        List<Node> depotChildren = depotGrid.getChildren();
        int i = 0;
        for (; i < quantity; i++) {
            Image resource = new Image("/graphics/punchboard/" + type.getResourceImage());
            ((ImageView) depotChildren.get(i)).setImage(resource);
        }
        for (; i < depotChildren.size(); i++) {
            ((ImageView) depotChildren.get(i)).setImage(null);
        }
    }

    /**
     * Updates the sections of the scene pertaining to lorenzo, for single player mode
     *
     * @param lorenzoBean the object containing the artificial intelligence's information
     */
    private void drawLorenzo(LorenzoBean lorenzoBean) {
        //Faith marker
        drawFaithTrack(lorenzoFaithGrid, "/graphics/punchboard/lorenzoFaithMarker.png", lorenzoBean.getFaith());
        //Number of active tokens
        lorenzoTokenLeftLabel.setText(Integer.toString(lorenzoBean.getActiveTokens().length));
        //Discarded tokens
        LorenzoTokenType[] discardedTokens = lorenzoBean.getDiscardedTokens();
        List<Node> lorenzoTokenChildren = lorenzoUsedTokenGrid.getChildren();
        int i = 0;
        for (; i < discardedTokens.length; i++) {
            Image token = new Image("/graphics/punchboard/" + discardedTokens[i].getTokenImage());
            ((ImageView) lorenzoTokenChildren.get(i)).setImage(token);
        }
        for (; i < lorenzoTokenChildren.size(); i++) {
            ((ImageView) lorenzoTokenChildren.get(i)).setImage(null);
        }
        //Making lorenzo visible
        lorenzoTokenPane.setVisible(true);
    }

    //PRIVATE SWITCHING METHODS

    /**
     * Switches the current scene to the game over screen
     *
     * @param jsonMessage the contents of the message received from the server
     */
    private void switchToGameOverScreen(String jsonMessage) {
        gui.getControllerBySceneName(SceneName.GAME_OVER).updateFromServer(jsonMessage);
        gui.changeScene(SceneName.GAME_OVER);
    }
}
