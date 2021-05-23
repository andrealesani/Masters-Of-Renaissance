package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.print.PrinterAbortException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller {
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

    int numPlayers = 0;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        serverField.setDisable(true);
        Button button = (Button) event.getSource();
        button.setDisable(true);
        String address = serverField.getText();

        try {
            if (address == null || !address.equals("localhost")) {
                playersList.setText("Server Unavailable at: " + address + "\n(Try: localhost)");
                throw new IllegalStateException("Server Unavailable at: " + address + "\n");
            }

        } catch (Exception exception) {
            System.out.println("handleButtonAction failed");
            serverField.setDisable(false);
            button.setDisable(false);
        }

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
        String address = serverField.getText();

        if (numPlayers == 0 || address == null || !address.equals("localhost")) {
            playersList.setText("Parameters are missing \nto start a new game!");
            throw new IllegalStateException("Finish to set your game first");
        }

        serverField.setDisable(false);
        button.setDisable(false);

        // close a window after you press a button
        //((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
        try {
            FXMLLoader loader = new FXMLLoader();
            Stage stage = new Stage(StageStyle.DECORATED);
            loader.setLocation(getClass().getClassLoader().getResource("graphics/boardGame.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.getIcons().add(new Image("/graphics/punchboard/calamaio.png"));
            stage.setTitle("PlayerBoard");

            BoardController controller = loader.getController();
            controller.setGameTable(loader.getLocation(), loader.getResources());

            stage.show();
        } catch (IllegalStateException e) {
            System.out.println("setGame failed");
        }
    }
}



