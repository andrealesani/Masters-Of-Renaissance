package client.GUI.controllers;

import client.ClientView;
import client.GUI.GUI;
import com.google.gson.Gson;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.resource.ResourceType;
import network.MessageType;
import network.beans.CardTableBean;
import network.beans.MarketBean;
import network.beans.MessageWrapper;
import network.beans.PlayerBoardBean;

import java.net.URL;
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
    private ImageView market;
    @FXML
    public ImageView firstPB;

    public void initialize() {
        this.gson = new Gson();
    }

    public void setGameBoard(URL location, ResourceBundle resources) {
        GridPane gridPane = new GridPane();
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
        this.clientView = gui.getClientView();
    }

    @Override
    public void updateFromServer(String jsonMessage) {
        MessageWrapper response = gson.fromJson(jsonMessage, MessageWrapper.class);
        switch (response.getType().toString()) {
            case "GAME":
                break;
            case "MARKET":
                MarketBean marketBean = clientView.getMarket();
                ResourceType[][] marketBoard = marketBean.getMarketBoard();
                ObservableList<Node> marketChildren = marketGrid.getChildren();
                for (int i = 0, k = 0; i < marketBoard.length; i++) {
                    for (int j = 0; j < marketBoard[0].length; j++ ,k++) {
                        ResourceType resource = marketBoard[i][j];
                        Image marble = new Image("/graphics/punchboard/" + resource.getMarbleImage());
                        ((ImageView) marketChildren.get(k)).setImage(marble);
                    }
                }
                ResourceType resource = marketBean.getSlide();
                Image marble = new Image("/graphics/punchboard/" + resource.getMarbleImage());
                ((ImageView) marketChildren.get(marketChildren.size()-1)).setImage(marble);
                break;
            case "CARDTABLE":
                CardTableBean cardTableBean = clientView.getCardTable();
                int[][] cardTable = cardTableBean.getCards();
                ObservableList<Node> cardTableChildren = cardsGrid.getChildren();

                for (int i = 0, k = 0; i < cardTable.length; i++) {
                    for (int j = 0; j < cardTable[0].length; j++ ,k++) {
                        int cardId = cardTable[i][j];
                        Image card = new Image("/graphics/front/" + cardId + ".png");
                        ((ImageView) cardTableChildren.get(k)).setImage(card);
                    }
                }
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
                int faith = playerBoardBean.getFaith();
                ObservableList<Node> faithTrackChildren = faithGrid.getChildren();
                for (int i = 0; i < faithTrackChildren.size(); i++) {
                    if (faith == i) {
                        Image token = new Image("/graphics/punchboard/faith_marble.png");
                        ((ImageView) faithTrackChildren.get(i)).setImage(token);
                        break;
                    } else {
                        ((ImageView) faithTrackChildren.get(i)).setImage(null);
                    }
                }

                //Leader cards
                int[] leaderCards = playerBoardBean.getLeaderCards();
                ObservableList<Node> leaderChildren = leaderGrid.getChildren();

                break;
            case "STRONGBOX":
                break;
            case "WAITINGROOM":
                break;
            case "WAREHOUSE":
                break;
            case "LORENZO":
                break;
        }
    }
}
