package client.GUI.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController {

    @FXML
    public AnchorPane pane;
    @FXML
    public Button loginButton;
    @FXML
    public TextField usernameField;
    @FXML
    public Label statusLabel;
    @FXML
    public TextField serverField;

    // GETTERS

    public String getUsername() {
        return usernameField.getText();
    }

    public String getServer() {
        return serverField.getText();
    }

    // PUBLIC METHODS

    //TODO mettere condizione username già usato (controllo in risposta dal server)
    //TODO Mettere i metodi della playerboard in CLIWriter

    public void loggingIn(ActionEvent event) throws Exception {
        loginButton.setDisable(true);
        statusLabel.setText("Wait...");

        if (getServer().isBlank()) {
            statusLabel.setText("Please specify a server address");
        } else if (getUsername().isBlank())
            statusLabel.setText("Please specify a username");
        else {
            statusLabel.setText("SUCCESSFULLY LOGGED IN");
            //((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
            try {
                //Attempts connection to server
                Socket clientSocket = new Socket(getServer(), 1234);
                statusLabel.setText("Connected to the server");

                FXMLLoader loader = new FXMLLoader();
                Stage stage = new Stage(StageStyle.DECORATED);
                loader.setLocation(getClass().getResource("/graphics/gameSettings.fxml"));
                Parent parent = loader.load();
                Scene scene = new Scene(parent);
                stage.setScene(scene);
                stage.getIcons().add(new Image("/graphics/punchboard/calamaio.png"));
                stage.setTitle("Game Settings");

                SettingsController controller = loader.getController();
                controller.setIpServerLabel(loader.getLocation(), loader.getResources());

                stage.setFullScreen(true);
                stage.showAndWait();
            } catch (IOException e) {
                statusLabel.setText("Couldn't connect to the specified server address");
                loginButton.setDisable(false);
            }
        }
    }


}





