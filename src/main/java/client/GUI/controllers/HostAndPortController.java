package client.GUI.controllers;

import client.GUI.GUI;
import client.GUI.SceneName;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import network.beans.MessageWrapper;

import java.io.IOException;
import java.net.Socket;

public class HostAndPortController implements GUIController {
    private GUI gui;

    @FXML
    private Button loginButton;
    @FXML
    private Label statusLabel;
    @FXML
    private TextField serverAddressField;
    @FXML
    private TextField serverPortField;

    // GETTERS

    public String getServer() {
        return serverAddressField.getText();
    }

    public String getServerPort() {
        return serverPortField.getText();
    }

    // PUBLIC METHODS

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
        } else {
            statusLabel.setText("SUCCESSFULLY LOGGED IN");
            try {
                //Attempts connection to server and creates the Reader
                int port = Integer.parseInt(getServerPort());
                Socket clientSocket = new Socket(getServer(), port);
                gui.setupConnection(clientSocket);
            } catch (IOException e) {
                statusLabel.setText("Couldn't connect to the specified server");
                loginButton.setDisable(false);
            } catch (NumberFormatException e1) {
                statusLabel.setText("The specified port is not a number");
                loginButton.setDisable(false);
            }
        }
    }

    public void quitGame() {
        gui.stop();
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void updateFromServer(String jsonMessage) {
        Gson gson = new Gson();
        MessageWrapper response = gson.fromJson(jsonMessage, MessageWrapper.class);
        if (response.getMessage().equals("Please, set your username.")) {
            gui.changeScene(SceneName.SETTINGS);
        }
        else
            System.out.println("Unexpected message to Login scene: " + jsonMessage);
    }
}





