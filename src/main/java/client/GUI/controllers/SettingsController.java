package client.GUI.controllers;

import client.GUI.GUI;
import client.GUI.controllers.GameBoardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable, GUIController {
    private GUI gui;
    @FXML
    public TextField serverField;
    @FXML
    public Label playersList;
    @FXML
    public Button singleplayerButton;
    @FXML
    public Button multiplayerButton;
    @FXML
    public Label numPlayersLabel;
    @FXML
    public Button okButton;
    @FXML
    public Button readyButton;
    @FXML
    public Button twoPlayersButton;
    @FXML
    public Button threePlayersButton;
    @FXML
    public Button fourPlayersButton;
    @FXML
    public ImageView settingsBackground;
    @FXML
    public AnchorPane pane2;

    int numPlayers = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        settingsBackground.setPreserveRatio(false);
        settingsBackground.fitWidthProperty().bind(pane2.widthProperty());
        settingsBackground.fitHeightProperty().bind(pane2.heightProperty());
    }


    public void setIpServerLabel(URL location, ResourceBundle resources) {
        numPlayersLabel.setOpacity(0);
        twoPlayersButton.setOpacity(0);
        threePlayersButton.setOpacity(0);
        fourPlayersButton.setOpacity(0);
    }

    public void setSingleplayerGame(ActionEvent event) {
        Button button = (Button) event.getSource();
        button.setDisable(true);
        multiplayerButton.setOpacity(0);
        numPlayers = 1;
        playersList.setText("You choose to challenge\nLORENZO IL MAGNIFICO\nprepare yourself and\ndo your best!");

    }

    public void setMultiplayerGame(ActionEvent event) {
        Button button = (Button) event.getSource();
        button.setDisable(true);
        singleplayerButton.setOpacity(0);
        numPlayersLabel.setOpacity(100);
        twoPlayersButton.setOpacity(100);
        threePlayersButton.setOpacity(100);
        fourPlayersButton.setOpacity(100);


    }

    public void setTwoPlayers(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);
        threePlayersButton.setOpacity(0);
        fourPlayersButton.setOpacity(0);
        numPlayers = 2;
    }

    public void setThreePlayers(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);
        twoPlayersButton.setOpacity(0);
        fourPlayersButton.setOpacity(0);
        numPlayers = 3;
    }

    public void setFourPlayers(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);
        twoPlayersButton.setOpacity(0);
        threePlayersButton.setOpacity(0);
        numPlayers = 4;
    }

    public void setGame(ActionEvent actionEvent) throws Exception {
        Button button = (Button) actionEvent.getSource();

        button.setDisable(false);

        // close a window after you press a button
        // ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();

        gui.changeScene("gameBoard.fxml");
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}



