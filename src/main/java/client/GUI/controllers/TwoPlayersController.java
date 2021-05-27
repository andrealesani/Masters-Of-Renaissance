package client.GUI.controllers;

import client.GUI.GUI;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class TwoPlayersController implements GUIController{
    private GUI gui;

    @FXML
    private AnchorPane paneBoard;
    @FXML
    private ImageView market;
    @FXML
    private ImageView firstPB;
    @FXML
    private ImageView secondPB;
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
        market.fitWidthProperty().bind(paneBoard.widthProperty().divide(2));
        market.fitHeightProperty().bind(paneBoard.heightProperty().divide(2));
    }

    public void setGameBoard(URL location, ResourceBundle resources) {
        GridPane gridPane = new GridPane();
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void updateFromServer(String jsonMessage){
        //TODO
    }
}
