package client.GUI.controllers;

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
import network.beans.MarketBean;
import network.beans.MessageWrapper;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class FourPlayersController implements GUIController {
    private GUI gui;

    @FXML
    private GridPane marketGrid;
    @FXML
    private BorderPane paneBoard;
    @FXML
    private GridPane leftGrid;
    @FXML
    private ImageView market;
    @FXML
    private GridPane playerBoardsGrid;
    @FXML
    public ImageView firstPB;
    @FXML
    public ImageView secondPB;
    @FXML
    public ImageView thirdPB;
    @FXML
    public ImageView fourthPB;
    @FXML
    private ImageView card00;
    @FXML
    private ImageView card01;
    @FXML
    private ImageView card02;
    @FXML
    private ImageView card03;
    @FXML
    private ImageView card10;
    @FXML
    private ImageView card11;
    @FXML
    private ImageView card12;
    @FXML
    private ImageView card13;
    @FXML
    private ImageView card20;
    @FXML
    private ImageView card21;
    @FXML
    private ImageView card22;
    @FXML
    private ImageView card23;
    @FXML
    private ImageView marble00;
    @FXML
    private ImageView marble01;
    @FXML
    private ImageView marble02;
    @FXML
    private ImageView marble03;
    @FXML
    private ImageView marble10;
    @FXML
    private ImageView marble11;
    @FXML
    private ImageView marble12;
    @FXML
    private ImageView marble13;
    @FXML
    private ImageView marble20;
    @FXML
    private ImageView marble21;
    @FXML
    private ImageView marble22;
    @FXML
    private ImageView marble23;

    public void initialize() {

        market.fitWidthProperty().bind(leftGrid.widthProperty());
        market.fitHeightProperty().bind(leftGrid.heightProperty().divide(2));

        firstPB.fitWidthProperty().bind(playerBoardsGrid.widthProperty().divide(2).subtract(15));
        firstPB.fitHeightProperty().bind(playerBoardsGrid.heightProperty().divide(2).subtract(15));

        secondPB.fitWidthProperty().bind(playerBoardsGrid.widthProperty().divide(2).subtract(15));
        secondPB.fitHeightProperty().bind(playerBoardsGrid.heightProperty().divide(2).subtract(15));

        thirdPB.fitWidthProperty().bind(playerBoardsGrid.widthProperty().divide(2).subtract(15));
        thirdPB.fitHeightProperty().bind(playerBoardsGrid.heightProperty().divide(2).subtract(15));

        fourthPB.fitWidthProperty().bind(playerBoardsGrid.widthProperty().divide(2).subtract(15));
        fourthPB.fitHeightProperty().bind(playerBoardsGrid.heightProperty().divide(2).subtract(15));


    }

    public void setGameBoard(URL location, ResourceBundle resources) {
        GridPane gridPane = new GridPane();
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void updateFromServer(String jsonMessage) {
        Gson gson = new Gson();
        MessageWrapper response = gson.fromJson(jsonMessage, MessageWrapper.class);
        switch (response.getType().toString()) {
            case "GAME":
                break;
            case "MARKET":
                MarketBean bean = gson.fromJson(response.getJsonMessage(), MarketBean.class);
                ResourceType[][] marketBoard = bean.getMarketBoard();
                ObservableList<Node> children = marketGrid.getChildren();
                int k = 0;
                for (int i = 0; i < marketBoard.length; i++) {
                    for (int j = 0; j < marketBoard[0].length; j++ ,k++) {
                        ResourceType resource = marketBoard[i][j];
                        Image marble = new Image("/graphics/punchboard/" + resource.getMarbleImage());
                        ((ImageView) children.get(k)).setImage(marble);
                    }
                }
                break;
            case "CARDTABLE":
                break;
            case "PLAYERBOARD":
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
