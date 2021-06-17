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

public class GameBoardController implements GUIController {
    private GUI gui;
    private ClientView clientView;
    private Gson gson;

    private Color turnOrderConnectedColor;
    private Color turnOrderDisconnectedColor;

    private String visualizedPlayer;
    private String previousPlayer;

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

    public void initialize() {
        this.gson = new Gson();
        this.turnOrderConnectedColor = Color.web("0xE1D892");
        this.turnOrderDisconnectedColor = Color.web("0xDFDCDC");
    }

    //SETTERS

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
        this.clientView = gui.getClientView();
    }

    //PUBLIC METHODS

    //TODO don't send the entire wrapper but only the type (must save info/error in clientView)
    @Override
    public void updateFromServer(String jsonMessage) {
        if (visualizedPlayer == null)
            visualizedPlayer = clientView.getUsername();

        MessageWrapper response = gson.fromJson(jsonMessage, MessageWrapper.class);
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
            case PLAYER_CONNECTED -> {
                SimplePopup.display(MessageType.INFO, "Player " + response.getMessage() + " has joined the game.");
                System.out.println ("Player connected");
            }
            case PLAYER_DISCONNECTED -> {
                SimplePopup.display(MessageType.INFO, "Player " + response.getMessage() + " has left the game (say goodbye like you mean it).");
                System.out.println("Player disconnected");
            }
            default -> System.out.println("Warning: received unexpected message " + jsonMessage);
        }
    }

    //PUBLIC VISUALIZATION METHODS

    public void viewProductions() {
        ThiccPopup.display(gui, SceneName.PRODUCTIONS);
    }

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

    public void chooseBonusResourceType(ResourceType resource, int quantity) {
        System.out.println("ChooseBonusResourceType: resource - " + resource + ", quantity - " + quantity);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("resource", resource);
        parameters.put("quantity", quantity);
        Command command = new Command(UserCommandsType.chooseBonusResourceType, parameters);
        gui.sendCommand(gson.toJson(command));
    }

    public void chooseLeaderCard(int number) {
        System.out.println("ChooseLeaderCard: number - " + number);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("number", number);
        Command command = new Command(UserCommandsType.chooseLeaderCard, parameters);
        gui.sendCommand(gson.toJson(command));
    }

    public void playLeaderCard(int number) {
        System.out.println("PlayLeaderCard: number - " + number);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("number", number);
        Command command = new Command(UserCommandsType.playLeaderCard, parameters);
        gui.sendCommand(gson.toJson(command));
    }

    public void discardLeaderCard(int number) {
        System.out.println("DiscardLeaderCard: number - " + number);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("number", number);
        Command command = new Command(UserCommandsType.discardLeaderCard, parameters);
        gui.sendCommand(gson.toJson(command));
    }

    public void selectMarketRow(int number) {
        System.out.println("SelectMarketRow: number - " + number);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("number", number);
        Command command = new Command(UserCommandsType.selectMarketRow, parameters);
        gui.sendCommand(gson.toJson(command));
    }

    public void selectMarketColumn(int number) {
        System.out.println("SelectMarketColumn: number - " + number);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("number", number);
        Command command = new Command(UserCommandsType.selectMarketColumn, parameters);
        gui.sendCommand(gson.toJson(command));
    }

    public void chooseMarbleConversion(ResourceType resource, int quantity) {
        System.out.println("ChooseMarbleConversion: resource - " + resource + ", quantity - " + quantity);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("resource", resource);
        parameters.put("quantity", quantity);
        Command command = new Command(UserCommandsType.chooseMarbleConversion, parameters);
        gui.sendCommand(gson.toJson(command));
    }

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

    public void moveDepotContent(int providingDepotNumber, int receivingDepotNumber, ResourceType resource, int quantity) {

    }

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

    public void endTurn() {
        System.out.println("EndTurn");
        Command command = new Command(UserCommandsType.endTurn, null);
        gui.sendCommand(gson.toJson(command));
    }

    //PRIVATE BUTTON HANDLING METHODS

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
        for (Node card : cardsGrid.getChildren())
            card.setOnMouseClicked(null);
        for (Node button : cardSlotsButtonGrid.getChildren())
            button.setVisible(false);
        for (Node button : leaderDepotButtonGrid.getChildren())
            button.setVisible(false);
        for (Node button : viewBoardButtonsGrid.getChildren())
            button.setVisible(false);
        productionsButton.setVisible(false);
        endTurnButton.setDisable(true);
    }

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

    private void enableAfterLeaderChoiceButtons() {
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
            }
        }
    }

    public void enableSwapDepotButtons() {
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

    private void setupLeaderChoice() {
        waitingRoomTitleLabel.setText("Resources left to distribute:");
        descriptionText.setText("Choose two leadercards to keep and, if necessary, which bonus resources to obtain (by clicking on the corresponding resource) and where to store them. Then end your turn.");

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
        endTurnButton.setOnAction(e -> endTurn());
    }

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
        enableAfterLeaderChoiceButtons();
        //For switching view
        enableVisualizedPlayerButtons();
        //For productions stuff
        productionsButton.setVisible(true);
    }

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
        enableAfterLeaderChoiceButtons();
        //For switching view
        enableVisualizedPlayerButtons();
        //For endTurn
        endTurnButton.setDisable(false);
        endTurnButton.setOnAction(e -> endTurn());
    }

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
        enableAfterLeaderChoiceButtons();
        //For switching view
        enableVisualizedPlayerButtons();
        //For endTurn
        endTurnButton.setDisable(false);
        endTurnButton.setOnAction(e -> endTurn());
    }

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

    //TODO turnOrder è sufficiente aggiornarlo a inizio partita
    private void drawGameState(GameBean gameBean) {
        String currPlayer = gameBean.getCurrentPlayer();
        TurnPhase currPhase = gameBean.getTurnPhase();

        //Turn phase information
        drawTurnPhaseInfo(currPlayer, currPhase);

        //Turn order
        drawTurnOrder(gameBean.getTurnOrder(), gameBean.getConnectedPlayers());

        //Activate buttons
        if (!visualizedPlayer.equals(clientView.getUsername())) {
            descriptionText.setText("You are viewing another player's board.");
            disableButtons();
            if (currPhase != TurnPhase.LEADERCHOICE)
                enableVisualizedPlayerButtons();
        } else if (!currPlayer.equals(clientView.getUsername())) {
            descriptionText.setText("You are not the current player.");
            disableButtons();
            if (currPhase != TurnPhase.LEADERCHOICE)
                enableVisualizedPlayerButtons();
        } else {
            switch (currPhase) {
                case LEADERCHOICE -> setupLeaderChoice();
                case ACTIONSELECTION -> setupActionSelection();
                case MARKETDISTRIBUTION -> setupMarketDistribution();
                case CARDPAYMENT, PRODUCTIONPAYMENT -> setupPayment();
            }
        }
    }

    private void drawTurnPhaseInfo(String currPlayer, TurnPhase currPhase) {
        currentPlayerLabel.setText(currPlayer);
        if (currPlayer.equals(clientView.getUsername()) && !currPlayer.equals(previousPlayer))
            SimplePopup.display(MessageType.INFO, "It's your turn to act!");
        previousPlayer = (currPlayer);
        turnPhaseLabel.setText(currPhase.vanillaToString());
        viewedPlayerLabel.setText(visualizedPlayer);
    }

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

    private void drawMarket(MarketBean marketBean) {
        ResourceType[][] marketBoard = marketBean.getMarketBoard();
        ObservableList<Node> marketChildren = marketGrid.getChildren();
        for (int i = 0, k = 0; i < marketBoard.length; i++) {
            for (int j = 0; j < marketBoard[0].length; j++, k++) {
                ResourceType resource = marketBoard[i][j];
                Image marble = new Image("/graphics/punchboard/" + resource.getMarbleImage());
                ((ImageView) marketChildren.get(k)).setImage(marble);
            }
        }
        ResourceType resource = marketBean.getSlide();
        Image marble = new Image("/graphics/punchboard/" + resource.getMarbleImage());
        ((ImageView) marketChildren.get(marketChildren.size() - 1)).setImage(marble);
    }

    private void drawCardTable(CardTableBean cardTableBean) {
        int[][] cardTable = cardTableBean.getCards();
        ObservableList<Node> cardTableChildren = cardsGrid.getChildren();

        for (int i = 0, k = 0; i < cardTable.length; i++) {
            for (int j = 0; j < cardTable[0].length; j++, k++) {
                int cardId = cardTable[i][j];
                Image card;
                if (cardId != -1) {
                    card = new Image("/graphics/front/" + cardId + ".png");
                } else {
                    card = new Image("/graphics/back/leadercardBack.png");
                }
                ((ImageView) cardTableChildren.get(k)).setImage(card);
            }
        }
    }

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

    private void drawStrongbox(StrongboxBean strongboxBean) {
        int[] strongboxContents = strongboxBean.getQuantity();
        strongboxCoinLabel.setText(Integer.toString(strongboxContents[0]));
        strongboxServantLabel.setText(Integer.toString(strongboxContents[1]));
        strongboxShieldLabel.setText(Integer.toString(strongboxContents[2]));
        strongboxStoneLabel.setText(Integer.toString(strongboxContents[3]));
    }

    private void drawWaitingRoom(WaitingRoomBean waitingRoomBean) {
        int[] waitingRoomContents = waitingRoomBean.getQuantity();
        waitingRoomCoinLabel.setText(Integer.toString(waitingRoomContents[0]));
        waitingRoomServantLabel.setText(Integer.toString(waitingRoomContents[1]));
        waitingRoomShieldLabel.setText(Integer.toString(waitingRoomContents[2]));
        waitingRoomStoneLabel.setText(Integer.toString(waitingRoomContents[3]));
    }

    private void drawWarehouse(WarehouseBean warehouseBean) {
        int[] depotQuantities = warehouseBean.getDepotQuantity();
        ResourceType[] depotTypes = warehouseBean.getDepotType();
        drawDepot(depotQuantities[0], depotTypes[0], depot1Grid);
        drawDepot(depotQuantities[1], depotTypes[1], depot2Grid);
        drawDepot(depotQuantities[2], depotTypes[2], depot3Grid);

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

    private void switchToGameOverScreen(String jsonMessage) {
        gui.getControllerBySceneName(SceneName.GAME_OVER).updateFromServer(jsonMessage);
        gui.changeScene(SceneName.GAME_OVER);
    }
}
