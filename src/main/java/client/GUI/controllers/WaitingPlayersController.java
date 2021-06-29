package client.GUI.controllers;

import client.GUI.GUI;
import client.GUI.SceneName;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import network.MessageType;
import network.MessageWrapper;

/**
 * This class is the GUIController which handles the waiting screen for when the game's players are still in the process of joining
 */
public class WaitingPlayersController implements GUIController {
    /**
     * The client's GUI object
     */
    private GUI gui;

    /**
     * The graphical elements of this controller's scene
     */
    @FXML
    private Label newPlayerLabel;

    //SETTERS

    /**
     * Sets the GUI object for the controller
     *
     * @param gui of type GUI - the main GUI class.
     */
    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    //PUBLIC METHODS

    /**
     * Updates the necessary parts of the scene based on what message was received from the server
     *
     * @param response the message received from the server
     */
    @Override
    public void updateFromServer(MessageWrapper response) {

        switch (response.getType()) {
            case GAME_START -> gui.changeScene(SceneName.GAME_BOARD);
            case PLAYER_CONNECTED -> newPlayerLabel.setText("Player " + response.getMessage() + " has joined the game.");
            case PLAYER_DISCONNECTED -> newPlayerLabel.setText("Player " + response.getMessage() + " has left the game.");
            case WAIT_PLAYERS -> {}
            default -> System.out.println("Unexpected message to WaitingPlayers scene: " + response);
        }

    }

}
