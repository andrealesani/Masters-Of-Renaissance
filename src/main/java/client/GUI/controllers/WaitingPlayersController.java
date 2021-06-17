package client.GUI.controllers;

import client.GUI.GUI;
import client.GUI.SceneName;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import network.MessageType;
import network.beans.MessageWrapper;

public class WaitingPlayersController implements GUIController {
    private GUI gui;

    @FXML
    private Label newPlayerLabel;

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void updateFromServer(String jsonMessage) {
        Gson gson = new Gson();
        MessageWrapper response = gson.fromJson(jsonMessage, MessageWrapper.class);
        if (response.getType() == MessageType.GAME_START) {
            gui.changeScene(SceneName.GAME_BOARD);
        } else if (response.getType() == MessageType.PLAYER_CONNECTED) {
            newPlayerLabel.setText("Player " + response.getMessage() + " has joined the game.");
        } else if (response.getType() == MessageType.PLAYER_DISCONNECTED) {
            newPlayerLabel.setText("Player " + response.getMessage() + " has left the game.");
        } else if (response.getType() == MessageType.WAIT_PLAYERS) {
            //do nothing
        } else {
            System.out.println("Unexpected message to WaitingPlayers scene: " + jsonMessage);
        }
    }
}
