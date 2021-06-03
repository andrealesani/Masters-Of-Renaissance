package client.GUI.controllers;

import client.ClientView;
import client.GUI.SimplePopup;
import client.GUI.GUI;
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
import javafx.scene.text.Text;
import model.PopeTileState;
import model.card.Card;
import model.resource.ResourceType;
import network.Command;
import network.UserCommandsType;
import network.beans.*;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class FourPlayersController implements GUIController {
    private GUI gui;
    private ClientView clientView;
    private Gson gson;

    @FXML
    private GridPane marketGrid;
    @FXML
    private GridPane cardsGrid;
    @FXML
    private GridPane leaderGrid;
    @FXML
    public GridPane faithGrid;
    @FXML
    public GridPane leaderButtonGrid;

    @FXML
    private GridPane depot1Grid;
    @FXML
    private GridPane depot2Grid;
    @FXML
    private GridPane depot3Grid;

    @FXML
    public AnchorPane cardSlotPane1;
    @FXML
    public AnchorPane cardSlotPane2;
    @FXML
    public AnchorPane cardSlotPane3;

    @FXML
    public Label strongboxCoinLabel;
    @FXML
    public Label strongboxServantLabel;
    @FXML
    public Label strongboxShieldLabel;
    @FXML
    public Label strongboxStoneLabel;

    @FXML
    public Label waitingRoomTitleLabel;
    @FXML
    public Label waitingRoomWhiteLabel;
    @FXML
    public Label waitingRoomCoinLabel;
    @FXML
    public Label waitingRoomServantLabel;
    @FXML
    public Label waitingRoomShieldLabel;
    @FXML
    public Label waitingRoomStoneLabel;

    @FXML
    public ImageView waitingRoomCoin;
    @FXML
    public ImageView waitingRoomServant;
    @FXML
    public ImageView waitingRoomShield;
    @FXML
    public ImageView waitingRoomStone;
    @FXML
    public Button endTurnButton;

    @FXML
    public ImageView tile1Image;
    @FXML
    public ImageView tile2Image;
    @FXML
    public ImageView tile3Image;

    @FXML
    public Label currentPlayerLabel;
    @FXML
    public Label turnPhaseLabel;
    @FXML
    public Text descriptionText;

    @FXML
    public GridPane marketRowButtonsGrid;
    @FXML
    public GridPane marketColumnButtonsGrid;


    //CONSTRUCTORS

    public void initialize() {
        this.gson = new Gson();
    }

    //SETTERS

    public void setGameBoard(URL location, ResourceBundle resources) {
        GridPane gridPane = new GridPane();
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
        this.clientView = gui.getClientView();
    }

    //PUBLIC METHODS

    @Override
    public void updateFromServer(String jsonMessage) {
        MessageWrapper response = gson.fromJson(jsonMessage, MessageWrapper.class);
        switch (response.getType().toString()) {

            case "GAME":
                drawGameState(clientView.getGame());
                break;

            case "MARKET":
                MarketBean marketBean = clientView.getMarket();
                drawMarket(marketBean);
                break;

            case "CARDTABLE":
                CardTableBean cardTableBean = clientView.getCardTable();
                drawCardTable(cardTableBean);
                break;

            case "PLAYERBOARD":
                List<PlayerBoardBean> playerBoards = clientView.getPlayerBoards();
                PlayerBoardBean playerBoardBean = null;
                for (PlayerBoardBean playerBoard : playerBoards) {
                    if (playerBoard.getUsername().equals(clientView.getUsername())) {
                        playerBoardBean = playerBoard;
                        break;
                    }
                }
                if (playerBoardBean == null) {
                    System.out.println("Error, player has no player board in client view.");
                    return;
                }

                //Faith track
                drawFaithTrack(playerBoardBean.getFaith());

                //Pope tiles
                PopeTileState[] popeTileStates = playerBoardBean.getPopeTileStates();
                drawPopeTile(popeTileStates[0], 1, tile1Image);
                drawPopeTile(popeTileStates[1], 2, tile2Image);
                drawPopeTile(popeTileStates[2], 3, tile3Image);

                //Leader cards
                drawLeaderCards(playerBoardBean.getLeaderCards());

                //Card slots
                SlotBean[] cardSlotBeans = playerBoardBean.getCardSlots();
                drawCardSlot(cardSlotBeans[0], cardSlotPane1);
                drawCardSlot(cardSlotBeans[1], cardSlotPane2);
                drawCardSlot(cardSlotBeans[2], cardSlotPane3);

                //White marbles
                waitingRoomWhiteLabel.setText(Integer.toString(playerBoardBean.getWhiteMarbles()));

                break;
            case "STRONGBOX":
                List<StrongboxBean> strongboxes = clientView.getStrongboxes();
                StrongboxBean strongboxBean = null;
                for (StrongboxBean strongbox : strongboxes) {
                    if (strongbox.getUsername().equals(clientView.getUsername())) {
                        strongboxBean = strongbox;
                        break;
                    }
                }
                if (strongboxBean == null) {
                    System.out.println("Error, player has no strongbox in client view.");
                    return;
                }

                //TODO usare mappe per inviare le risorse pls
                int[] stronboxContents = strongboxBean.getQuantity();
                strongboxCoinLabel.setText(Integer.toString(stronboxContents[0]));
                strongboxServantLabel.setText(Integer.toString(stronboxContents[1]));
                strongboxShieldLabel.setText(Integer.toString(stronboxContents[2]));
                strongboxStoneLabel.setText(Integer.toString(stronboxContents[3]));

                break;
            case "WAITINGROOM":
                List<WaitingRoomBean> waitingRooms = clientView.getWaitingRooms();
                WaitingRoomBean waitingRoomBean = null;
                for (WaitingRoomBean waitingRoom : waitingRooms) {
                    if (waitingRoom.getUsername().equals(clientView.getUsername())) {
                        waitingRoomBean = waitingRoom;
                        break;
                    }
                }
                if (waitingRoomBean == null) {
                    System.out.println("Error, player has no waiting room in client view.");
                    return;
                }

                int[] waitingRoomContents = waitingRoomBean.getQuantity();
                waitingRoomCoinLabel.setText(Integer.toString(waitingRoomContents[0]));
                waitingRoomServantLabel.setText(Integer.toString(waitingRoomContents[1]));
                waitingRoomShieldLabel.setText(Integer.toString(waitingRoomContents[2]));
                waitingRoomStoneLabel.setText(Integer.toString(waitingRoomContents[3]));

                break;
            case "WAREHOUSE":
                //TODO leader depots
                List<WarehouseBean> warehouses = clientView.getWarehouses();
                WarehouseBean warehouseBean = null;
                for (WarehouseBean warehouse : warehouses) {
                    if (warehouse.getUsername().equals(clientView.getUsername())) {
                        warehouseBean = warehouse;
                        break;
                    }
                }
                if (warehouseBean == null) {
                    System.out.println("Error, player has no warehouse in client view.");
                    return;
                }

                int[] depotQuantities = warehouseBean.getDepotQuantity();
                ResourceType[] depotTypes = warehouseBean.getDepotType();
                drawDepot(depotQuantities[0], depotTypes[0], depot1Grid);
                drawDepot(depotQuantities[1], depotTypes[1], depot2Grid);
                drawDepot(depotQuantities[2], depotTypes[2], depot3Grid);

                break;
            case "LORENZO":
                //TODO?
                break;
            case "ERROR":
                SimplePopup.display(response.getType().toString(), response.getJsonMessage());
            default:
                System.out.println("Warning: received unexpected message " + jsonMessage);
        }
    }

    public void chooseBonusResourceType(ResourceType resource) {
        System.out.println("ChooseBonusResourceType: resource - " + resource + ", quantity - 1");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("resource", resource);
        parameters.put("quantity", 1);
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

    void playLeaderCard(int number) {
        System.out.println("PlayLeaderCard: number - " + number);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("number", number);
        Command command = new Command(UserCommandsType.playLeaderCard, parameters);
        gui.sendCommand(gson.toJson(command));
    }

    void discardLeaderCard(int number) {
        System.out.println("DiscardLeaderCard: number - " + number);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("number", number);
        Command command = new Command(UserCommandsType.discardLeaderCard, parameters);
        gui.sendCommand(gson.toJson(command));
    }

    public void endTurn() {
        System.out.println("EndTurn");
        Command command = new Command(UserCommandsType.endTurn, null);
        gui.sendCommand(gson.toJson(command));
    }

    //PRIVATE BUTTON HANDLING METHODS

    private void disableButtons() {
        waitingRoomCoin.setOnMouseClicked(null);
        waitingRoomServant.setOnMouseClicked(null);
        waitingRoomShield.setOnMouseClicked(null);
        waitingRoomStone.setOnMouseClicked(null);
        for (Node image : leaderGrid.getChildren()) {
            image.setOnMouseClicked(null);
        }
        for (Node button : leaderButtonGrid.getChildren()) {
            button.setVisible(false);
        }
        for (Node button : marketRowButtonsGrid.getChildren()) {
            button.setVisible(false);
        }
        for (Node button : marketColumnButtonsGrid.getChildren()) {
            button.setVisible(false);
        }
        endTurnButton.setDisable(true);
    }

    private void enableCommonButtons() {
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
        //For endTurn
        endTurnButton.setDisable(false);
        endTurnButton.setOnAction(e -> endTurn());
    }

    private void setupLeaderChoice() {
        waitingRoomTitleLabel.setText("Resources left to distribute:");
        descriptionText.setText("Choose two leadercards to keep and, if necessary, which bonus resources to obtain (by clicking on the corresponding resource) and where to store them. Then end your turn.");

        disableButtons();
        waitingRoomCoin.setOnMouseClicked(e -> chooseBonusResourceType(ResourceType.COIN));
        waitingRoomServant.setOnMouseClicked(e -> chooseBonusResourceType(ResourceType.SERVANT));
        waitingRoomShield.setOnMouseClicked(e -> chooseBonusResourceType(ResourceType.SHIELD));
        waitingRoomStone.setOnMouseClicked(e -> chooseBonusResourceType(ResourceType.STONE));
        ObservableList<Node> leaderChildren = leaderGrid.getChildren();
        for (int i = 0; i < leaderChildren.size(); i++) {
            int finalI = i;
            leaderChildren.get(i).setOnMouseClicked(e -> chooseLeaderCard(finalI+1));
        }
        endTurnButton.setDisable(false);
        endTurnButton.setOnAction(e -> endTurn());
    }

    private void setupActionSelection() {
        waitingRoomTitleLabel.setText("Waiting room:");
        descriptionText.setText("Choose either a row or column of the market, a development card to buy or a set of productions to activate. Jolly resources in input and output have to be chosen before confirming.");

        disableButtons();


        //For playLeaderCard, discardLeaderCard, endTurn
        enableCommonButtons();
    }

    private void setupMarketDistribution() {
        waitingRoomTitleLabel.setText("Resources left to distribute:");
        descriptionText.setText("Choose where to store the obtained resources, or discard those left by ending your turn.");

        disableButtons();

        //For playLeaderCard, discardLeaderCard, endTurn
        enableCommonButtons();
    }

    private void setupPayment() {
        waitingRoomTitleLabel.setText("Resources left to pay:");
        descriptionText.setText("Choose from where to pay your resources, or end your turn to have it chosen automatically.");

        disableButtons();
        //For playLeaderCard, discardLeaderCard, endTurn
        enableCommonButtons();
    }


    //PRIVATE DRAWING METHODS

    private void drawGameState(GameBean gameBean) {
        String currPlayer = gameBean.getCurrentPlayer();
        String currPhase = gameBean.getTurnPhase().vanillaToString();

        currentPlayerLabel.setText(currPlayer);
        turnPhaseLabel.setText(currPhase);

        if (!currPlayer.equals(clientView.getUsername())) {
            descriptionText.setText("You are not the current player.");
            disableButtons();
        } else {
            switch (currPhase) {
                case "LEADERCHOICE":
                    setupLeaderChoice();
                    break;
                case "ACTIONSELECTION":
                    setupActionSelection();
                    break;
                case "MARKETDISTRIBUTION":
                    setupMarketDistribution();
                    break;
                case "CARDPAYMENT", "PRODUCTIONPAYMENT":
                    setupPayment();
                    break;
            }
        }
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

    private void drawFaithTrack(int faith) {
        ObservableList<Node> faithTrackChildren = faithGrid.getChildren();
        for (int i = 0; i < faithTrackChildren.size(); i++) {
            if (faith == i) {
                Image token = new Image("/graphics/punchboard/faithMarker.png");
                ((ImageView) faithTrackChildren.get(i)).setImage(token);
                break;
            } else {
                ((ImageView) faithTrackChildren.get(i)).setImage(null);
            }
        }
    }

    private void drawLeaderCards(int[] leaderCards) {
        ObservableList<Node> leaderChildren = leaderGrid.getChildren();
        for (int i = 0; i < leaderChildren.size(); i++) {
            Image card;
            if (i < leaderCards.length) {
                card = new Image("/graphics/front/" + leaderCards[i] + ".png");
            } else {
                card = new Image("/graphics/back/leadercardBack.png");
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
            imageView.setImage(null);
            return;
        }
        imageView.setImage(tile);
    }

    private void drawDepot(int quantity, ResourceType type, GridPane depotGrid) {
        List<Node> depotChildren = depotGrid.getChildren();
        for (int j = 0; j < depotChildren.size() ; j++) {
            if (j < quantity) {
                Image resource = new Image("/graphics/punchboard/" + type.getResourceImage());
                ((ImageView) depotChildren.get(j)).setImage(resource);
            } else {
                ((ImageView) depotChildren.get(j)).setImage(null);
            }
        }
    }
}
