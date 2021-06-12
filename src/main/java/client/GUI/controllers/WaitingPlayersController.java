package client.GUI.controllers;

import client.GUI.GUI;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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
        Map responseMap = gson.fromJson(jsonMessage, Map.class);
        if (responseMap.get("type").equals("GAME_START")) {
            gui.changeScene("gameBoard.fxml");
        } else if (responseMap.get("type").equals("WAIT_PLAYERS")) {
            newPlayerLabel.setText(newPlayerLabel.getText() + "A new player joined the match!\n ");
        } else {
            System.out.println("Unexpected message to WaitingPlayers scene: " + jsonMessage);
        }
    }
}
