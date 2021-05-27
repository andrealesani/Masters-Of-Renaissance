package client.GUI.controllers;

import client.GUI.GUI;
import client.GUI.controllers.GameBoardController;
import com.google.gson.Gson;
import javafx.application.Application;
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
import network.MessageType;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class SettingsController implements GUIController {
    private GUI gui;
    @FXML
    private AnchorPane pane2;
    @FXML
    private ImageView settingsBackground;
    @FXML
    private TextField usernameField;
    @FXML
    private Button confirmUsername;
    @FXML
    private Label invalidUsername;
    @FXML
    private Label playersList;
    @FXML
    private Button singleplayerButton;
    @FXML
    private Button multiplayerButton;
    @FXML
    private Label numPlayersLabel;
    @FXML
    private Button readyButton;
    @FXML
    private Button twoPlayersButton;
    @FXML
    private Button threePlayersButton;
    @FXML
    private Button fourPlayersButton;

    private int numPlayers = 0;
    private String message;
    private Map responseMap;

    /**
     * @see Application#init()
     */
    public void initialize() {
        settingsBackground.setPreserveRatio(false);
        settingsBackground.fitWidthProperty().bind(pane2.widthProperty());
        settingsBackground.fitHeightProperty().bind(pane2.heightProperty());
        invalidUsername.setVisible(false);
        singleplayerButton.setVisible(false);
        multiplayerButton.setVisible(false);
        numPlayersLabel.setVisible(false);
        twoPlayersButton.setVisible(false);
        threePlayersButton.setVisible(false);
        fourPlayersButton.setVisible(false);
        readyButton.setVisible(false);
    }

    // PUBLIC METHODS

    public void checkValidUsername() {
        System.out.println("Checking username...");

        if (usernameField.getText().isBlank()) {
            invalidUsername.setVisible(true);
            return;
        }

        invalidUsername.setVisible(true);
        invalidUsername.setText("Checking username availability...");

        gui.sendCommand(usernameField.getText());
    }

    // SETTERS

    public void setSingleplayerGame(ActionEvent event) {
        Button button = (Button) event.getSource();
        button.setDisable(true);
        multiplayerButton.setVisible(false);
        numPlayersLabel.setVisible(false);
        twoPlayersButton.setVisible(false);
        threePlayersButton.setVisible(false);
        fourPlayersButton.setVisible(false);
        readyButton.setDisable(false);
        numPlayers = 1;
        playersList.setText("You choose to challenge\nLORENZO IL MAGNIFICO\nprepare yourself and\ndo your best!");
    }

    public void setMultiplayerGame(ActionEvent event) {
        Button button = (Button) event.getSource();
        button.setDisable(true);
        singleplayerButton.setVisible(false);
        numPlayersLabel.setVisible(true);
        twoPlayersButton.setVisible(true);
        threePlayersButton.setVisible(true);
        fourPlayersButton.setVisible(true);
    }

    public void setTwoPlayers(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);
        threePlayersButton.setVisible(false);
        fourPlayersButton.setVisible(false);
        readyButton.setDisable(false);
        numPlayers = 2;
    }

    public void setThreePlayers(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);
        twoPlayersButton.setVisible(false);
        fourPlayersButton.setVisible(false);
        readyButton.setDisable(false);
        numPlayers = 3;
    }

    public void setFourPlayers(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);
        twoPlayersButton.setVisible(false);
        threePlayersButton.setVisible(false);
        readyButton.setDisable(false);
        numPlayers = 4;
    }

    public void setGame() {
        if (numPlayers > 0) {
            gui.sendCommand(Integer.toString(numPlayers));
            gui.changeScene("waitingPlayers.fxml");
        } else throw new RuntimeException("Player clicked 'Ready' button when he wasn't supposed to");
        // close a window after you press a button
        // ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void updateFromServer(String jsonMessage) {
        Gson gson = new Gson();
        Map responseMap = gson.fromJson(jsonMessage, Map.class);
        if (responseMap.get("type").equals("INFO")) {
            if (responseMap.get("jsonMessage").equals("Please, choose the game's number of players.")) {
                invalidUsername.setVisible(true);
                invalidUsername.setText("Username was correctly set");
                usernameField.setDisable(true);
                confirmUsername.setDisable(true);
                singleplayerButton.setVisible(true);
                multiplayerButton.setVisible(true);
                readyButton.setVisible(true);
                readyButton.setDisable(true);
            } else if (responseMap.get("jsonMessage").equals("The number of players for the game that is currently being deployed has not yet been decided.")) {
                invalidUsername.setVisible(true);
                invalidUsername.setText("Please try again in a moment");
            } else if (responseMap.get("jsonMessage").equals("Please, set your username.")) {
                // do nothing
            } else if (jsonMessage.contains("Username was correctly set to:")) {
                // do nothing
            } else
                System.out.println("Unexpected message to Settings scene: " + jsonMessage);
        }
        else if (responseMap.get("type").equals("WAIT_PLAYERS"))
            gui.changeScene("waitingPlayers.fxml");
    }
}


