package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GUI.SceneName;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.network.MessageWrapper;

/**
 * This class is the GUIController which handles the choice of the player's username and, if necessary, of the game's number of players
 */
public class SettingsController implements GUIController {
    /**
     * The client's GUI object
     */
    private GUI gui;

    /**
     * The graphical elements of this controller's scene
     */
    @FXML
    private TextField usernameField;
    @FXML
    private Label serverMessageLabel, playersList, numPlayersLabel;
    @FXML
    private Button confirmUsername, singleplayerButton, multiplayerButton, readyButton, twoPlayersButton, threePlayersButton, fourPlayersButton, changeSettingsButton;

    /**
     * The number of players for the game being created
     */
    private int numPlayers = 0;

    //CONSTRUCTORS

    /**
     * Handles initialization for this class
     */
    public void initialize() {
        serverMessageLabel.setVisible(false);
        singleplayerButton.setVisible(false);
        multiplayerButton.setVisible(false);
        numPlayersLabel.setVisible(false);
        twoPlayersButton.setVisible(false);
        threePlayersButton.setVisible(false);
        fourPlayersButton.setVisible(false);
        readyButton.setVisible(false);
        changeSettingsButton.setVisible(false);
    }

    // PUBLIC METHODS

    /**
     * Checks if the chosen username is valid, then send it to the server for confirmation
     */
    public void checkValidUsername() {
        System.out.println("Checking username...");

        if (usernameField.getText().isBlank()) {
            serverMessageLabel.setVisible(true);
            return;
        }

        serverMessageLabel.setVisible(true);
        serverMessageLabel.setText("Checking username availability...");

        gui.sendMessageToServer(MessageType.USERNAME, usernameField.getText());
    }

    /**
     * Resets the choice for the number of players of the game
     */
    public void changeSettings() {
        singleplayerButton.setVisible(true);
        singleplayerButton.setDisable(false);
        multiplayerButton.setVisible(true);
        multiplayerButton.setDisable(false);
        numPlayersLabel.setVisible(false);
        twoPlayersButton.setVisible(false);
        twoPlayersButton.setDisable(false);
        threePlayersButton.setVisible(false);
        threePlayersButton.setDisable(false);
        fourPlayersButton.setVisible(false);
        fourPlayersButton.setDisable(false);
        changeSettingsButton.setVisible(false);
        readyButton.setDisable(true);
        playersList.setText("");
    }

    // SETTERS

    /**
     * Sets the GUI object for the controller
     *
     * @param gui of type GUI - the main GUI class.
     */
    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    /**
     * Selects the single player options for the scene
     */
    public void setSingleplayerGame() {
        singleplayerButton.setDisable(true);
        multiplayerButton.setVisible(false);
        numPlayersLabel.setVisible(false);
        twoPlayersButton.setVisible(false);
        threePlayersButton.setVisible(false);
        fourPlayersButton.setVisible(false);
        readyButton.setDisable(false);
        changeSettingsButton.setVisible(true);
        numPlayers = 1;
        playersList.setText("You choose to challenge\nLorenzo Il Magnifico\nprepare yourself and\ndo your best!");
    }

    /**
     * Selects the multiplayer options for the scene
     */
    public void setMultiplayerGame() {
        multiplayerButton.setDisable(true);
        singleplayerButton.setVisible(false);
        numPlayersLabel.setVisible(true);
        twoPlayersButton.setVisible(true);
        threePlayersButton.setVisible(true);
        fourPlayersButton.setVisible(true);
        changeSettingsButton.setVisible(true);
    }

    /**
     * Selects the two players options for the scene
     */
    public void setTwoPlayers() {
        twoPlayersButton.setDisable(true);
        threePlayersButton.setVisible(false);
        fourPlayersButton.setVisible(false);
        readyButton.setDisable(false);
        changeSettingsButton.setVisible(true);
        numPlayers = 2;
    }

    /**
     * Selects the three players options for the scene
     */
    public void setThreePlayers() {
        threePlayersButton.setDisable(true);
        twoPlayersButton.setVisible(false);
        fourPlayersButton.setVisible(false);
        readyButton.setDisable(false);
        changeSettingsButton.setVisible(true);
        numPlayers = 3;
    }

    /**
     * Selects the four players options for the scene
     */
    public void setFourPlayers() {
        fourPlayersButton.setDisable(true);
        twoPlayersButton.setVisible(false);
        threePlayersButton.setVisible(false);
        readyButton.setDisable(false);
        changeSettingsButton.setVisible(true);
        numPlayers = 4;
    }

    /**
     * Attempts to send the number of players to the server, then switches to the next scene
     */
    public void setGame() {
        if (numPlayers > 0) {
            gui.sendMessageToServer(MessageType.NUM_OF_PLAYERS, Integer.toString(numPlayers));
        } else {
            System.err.println("Warning: Player pressed 'ready' button before they were supposed to.");
            gui.stop();
        }
    }

    /**
     * Updates the necessary parts of the scene based on what message was received from the server
     *
     * @param response the message received from the server
     */
    @Override
    public void updateFromServer(MessageWrapper response) {
        switch (response.getType()) {

            case CONFIRM_USERNAME -> {

                serverMessageLabel.setVisible(true);
                serverMessageLabel.setText("Username was correctly set.");
                usernameField.setDisable(true);
                confirmUsername.setDisable(true);
                singleplayerButton.setVisible(true);
                multiplayerButton.setVisible(true);
                readyButton.setVisible(true);
                readyButton.setDisable(true);

            }

            case INFO, ERROR -> {
                serverMessageLabel.setVisible(true);
                serverMessageLabel.setText(response.getMessage());
            }

            case WAIT_PLAYERS -> gui.changeScene(SceneName.WAITING);

            case GAME_START -> gui.changeScene(SceneName.GAME_BOARD);

            default -> System.out.println ("Received from server: " + response.getMessage());
        }
    }
}



