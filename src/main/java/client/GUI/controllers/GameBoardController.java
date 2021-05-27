package client.GUI.controllers;

import client.GUI.GUI;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class GameBoardController implements GUIController{
    private GUI gui;

    @FXML
    private AnchorPane paneBoard;
    @FXML
    private ImageView market;

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