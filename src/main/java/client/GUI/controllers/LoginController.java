package client.GUI.controllers;

import client.GUI.GUI;
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

public class LoginController implements GUIController {
    private GUI gui;

    @FXML
    private Button loginButton;
    @FXML
    private TextField usernameField;
    @FXML
    private Label statusLabel;
    @FXML
    private TextField serverAddressField;
    @FXML
    private TextField serverPortField;

    // GETTERS

    public String getUsername() {
        return usernameField.getText();
    }

    public String getServer() {
        return serverAddressField.getText();
    }

    public String getServerPort() { return serverPortField.getText(); }

    // PUBLIC METHODS

    //TODO mettere condizione username già usato (controllo in risposta dal server)
    //TODO Mettere i metodi della playerboard in CLIWriter

    public void loggingIn(ActionEvent event) throws Exception {
        loginButton.setDisable(true);
        statusLabel.setText("Wait...");

        if (getServer().isBlank()) {
            statusLabel.setText("Please specify a server address");
            loginButton.setDisable(false);
        } else if (getServerPort().isBlank()) {
            statusLabel.setText("Please specify a server port");
            loginButton.setDisable(false);
        } else if (getUsername().isBlank()) {
            statusLabel.setText("Please specify a username");
            loginButton.setDisable(false);
        }
        else {
            statusLabel.setText("SUCCESSFULLY LOGGED IN");
            try {
                int port = Integer.parseInt(getServerPort());
                //Attempts connection to server
                gui.setClientSocket(new Socket(getServer(), port));
                //TODO maybe make a Constants static class to store all paths in one single place
                gui.changeScene("gameSettings.fxml");
            } catch (IOException e) {
                statusLabel.setText("Couldn't connect to the specified server address");
                loginButton.setDisable(false);
            } catch (NumberFormatException e1) {
                statusLabel.setText("The specified port is not a number");
                loginButton.setDisable(false);
            }
        }
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}





