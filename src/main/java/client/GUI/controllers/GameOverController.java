package client.GUI.controllers;

import client.GUI.GUI;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import network.beans.GameBean;
import network.beans.MessageWrapper;

/**
 * This class is the GUIController which handles the game over screen
 */
public class GameOverController implements GUIController {
    /**
     * The client's GUI object
     */
    private GUI gui;

    /**
     * The graphical elements of this controller's scene
     */
    @FXML
    private Label victoryLabel, lossLabel, winnerNameLabel, victoryPointsLabel, loserVictoryPointsLabel;
    @FXML
    private Button closeGameButton;
    @FXML
    private AnchorPane winnerScorePane;

    //CONSTRUCTORS

    /**
     * Handles initialization for this class
     */
    public void initialize() {

    }

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
            case GAME_END -> setupGameOver();
            default -> System.out.println("Message received after game over.");
        }

    }

    //PRIVATE METHODS

    /**
     * Fills in the scene with the necessary information and activates its interactibles
     */
    private void setupGameOver() {
        GameBean gameBean = gui.getClientView().getGame();

        //Name of the winner
        winnerNameLabel.setText(gameBean.getWinner());

        //Winner victory points
        winnerScorePane.setVisible(true);
        victoryPointsLabel.setText(Integer.toString(gameBean.getWinnerVp()));

        //Title
        if (gameBean.getWinner().equals(gui.getClientView().getUsername())) {
            victoryLabel.setVisible(true);
            lossLabel.setVisible(false);
        } else {
            victoryLabel.setVisible(false);
            lossLabel.setVisible(true);
        }

        //The button for closing the game
        closeGameButton.setOnAction(e -> gui.stop());
    }
}
