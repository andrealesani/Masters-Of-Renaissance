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

/**
 * This class is the GUIController which handles choosing which host IP and port to connect to
 */
public class HostAndPortController implements GUIController {
    /**
     * The client's GUI object
     */
    private GUI gui;

    /**
     * The graphical elements of this controller's scene
     */
    @FXML
    private Button loginButton;
    @FXML
    private Label statusLabel;
    @FXML
    private TextField serverAddressField;
    @FXML
    private TextField serverPortField;

    // GETTERS

    /**
     * Getter for the server IP written in the corresponding text box
     *
     * @return the selected server IP
     */
    public String getServer() {
        return serverAddressField.getText();
    }

    /**
     * Getter for the server port written in the corresponding text box
     *
     * @return the selected server port
     */
    public String getServerPort() {
        return serverPortField.getText();
    }

    // PUBLIC METHODS

    /**
     * Attempts to connect the client to the selected host IP and port
     */
    public void connect() {
        loginButton.setDisable(true);
        statusLabel.setText("Wait...");

        if (getServer().isBlank()) {
            statusLabel.setText("Please specify a server address");
            loginButton.setDisable(false);
        } else if (getServerPort().isBlank()) {
            statusLabel.setText("Please specify a server port");
            loginButton.setDisable(false);
        } else {
            statusLabel.setText("SUCCESSFULLY CONNECTED");

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

    /**
     * Sets the GUI object for the controller
     *
     * @param gui of type GUI - the main GUI class.
     */
    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    /**
     * Updates the necessary parts of the scene based on what message was received from the server
     *
     * @param jsonMessage the message received from the server
     */
    @Override
    public void updateFromServer(String jsonMessage) {
        Gson gson = new Gson();
        MessageWrapper response = gson.fromJson(jsonMessage, MessageWrapper.class);
        if (response.getMessage().equals("Please, set your username.")) {
            gui.changeScene(SceneName.SETTINGS);
        } else
            System.out.println("Unexpected message to Login scene: " + jsonMessage);
    }
}





