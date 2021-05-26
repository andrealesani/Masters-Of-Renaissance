package client.GUI.controllers;

import client.GUI.GUI;
import client.GUI.controllers.GameBoardController;
import com.google.gson.Gson;
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
import java.util.Map;
import java.util.ResourceBundle;

public class SettingsController implements Initializable, GUIController {
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

    int numPlayers = 0;

    //used to resize the background image dimension when the window gets resized
    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    // TODO wait if more players need to join the game
    // TODO handle the 'username already taken' message
    public void checkValidUsername() {
        System.out.println("Checking username...");
        Gson gson = new Gson();
        String message;
        Map responseMap;

        if (usernameField.getText().isBlank()) {
            invalidUsername.setVisible(true);
            return;
        }

        message = gui.readMessage();
        responseMap = gson.fromJson(message, Map.class);

        if (responseMap.get("type").equals("INFO") && responseMap.get("jsonMessage").equals("Please, set your username.")) {
            System.out.println("Sending username to the server...");
            gui.sendCommand(usernameField.getText());
            message = gui.readMessage();
            responseMap = gson.fromJson(message, Map.class);
            if (responseMap.get("type").equals("INFO") && ((String) responseMap.get("jsonMessage")).contains("Username was correctly set to:")) {
                System.out.println("Username was correctly set");
                invalidUsername.setVisible(true);
                invalidUsername.setText("Username was correctly set");
                message = gui.readMessage();
                responseMap = gson.fromJson(message, Map.class);
                if (responseMap.get("type").equals("INFO")) {
                    System.out.println("First player, requested additional information");
                    singleplayerButton.setVisible(true);
                    multiplayerButton.setVisible(true);
                    numPlayersLabel.setVisible(true);
                    twoPlayersButton.setVisible(true);
                    threePlayersButton.setVisible(true);
                    fourPlayersButton.setVisible(true);
                    readyButton.setVisible(true);
                }
            } else {
                System.out.println("Changing scene...");
                gui.changeScene("gameBoard.fxml");
            }
        } else {
            System.out.println("Warning: received unexpected message from server");
            System.out.println("Server: " + message);
        }
        System.out.println("END checkValidUsername");
    }

    // GETTERS

    public String getUsernameField() {
        return usernameField.getText();
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
        numPlayers = 1;
        playersList.setText("You choose to challenge\nLORENZO IL MAGNIFICO\nprepare yourself and\ndo your best!");

    }

    public void setMultiplayerGame(ActionEvent event) {
        Button button = (Button) event.getSource();
        button.setDisable(true);
        singleplayerButton.setVisible(false);
    }

    public void setTwoPlayers(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);
        threePlayersButton.setVisible(false);
        fourPlayersButton.setVisible(false);
        numPlayers = 2;
    }

    public void setThreePlayers(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);
        twoPlayersButton.setVisible(false);
        fourPlayersButton.setVisible(false);
        numPlayers = 3;
    }

    public void setFourPlayers(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);
        twoPlayersButton.setVisible(false);
        threePlayersButton.setVisible(false);
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



