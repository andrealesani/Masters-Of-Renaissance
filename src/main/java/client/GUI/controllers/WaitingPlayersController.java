package client.GUI.controllers;

import client.GUI.GUI;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import network.MessageType;
import network.beans.MessageWrapper;

import java.util.Map;

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
            gui.changeScene("gameBoard.fxml");
        } else if (response.getType() == MessageType.PLAYER_CONNECTED) {
            newPlayerLabel.setText("Player " + response.getJsonMessage() + " has joined the game.");
        } else {
            System.out.println("Unexpected message to WaitingPlayers scene: " + jsonMessage);
        }
    }
}
