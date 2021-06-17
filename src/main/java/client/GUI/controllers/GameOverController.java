package client.GUI.controllers;

import client.GUI.GUI;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.PopupWindow;
import network.beans.GameBean;
import network.beans.MessageWrapper;

public class GameOverController implements GUIController {
    private GUI gui;
    private Gson gson;

    @FXML
    private Label victoryLabel, lossLabel, winnerNameLabel, victoryPointsLabel, loserVictoryPointsLabel;
    @FXML
    private Button closeGameButton;
    @FXML
    private AnchorPane winnerScorePane;

    //SETTERS

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
        this.gson = new Gson();
    }

    //PUBLIC METHODS

    @Override
    public void updateFromServer(String jsonMessage) {
        MessageWrapper response = gson.fromJson(jsonMessage, MessageWrapper.class);
        switch (response.getType()) {
            case GAME_END -> setupGameOver();
            default -> System.out.println("Message received after game over.");
        }
    }

    //PRIVATE METHODS

    private void setupGameOver() {
        GameBean gameBean = gui.getClientView().getGame();

        //Name of the winner
        winnerNameLabel.setText(gameBean.getWinner());

        //Winner victory points
        if (gameBean.getTurnOrder().length == 1) {
            winnerScorePane.setVisible(false);
        } else {
            winnerScorePane.setVisible(true);
            victoryPointsLabel.setText(Integer.toString(gameBean.getWinnerVp()));
        }

        //Title
        if (gameBean.getWinner().equals(gui.getClientView().getUsername())) {
            victoryLabel.setVisible(true);
            lossLabel.setVisible(false);
        } else {
            victoryLabel.setVisible(false);
            lossLabel.setVisible(true);
        }

        closeGameButton.setOnAction(e -> gui.stop());
    }
}
