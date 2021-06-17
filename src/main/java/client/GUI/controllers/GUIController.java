package client.GUI.controllers;

import client.GUI.GUI;

/**
 * This interface represents the controllers used for handling the interactive components of javaFX scenes and updating their contents
 */
public interface GUIController {
    /**
     * Sets the GUI object for the controller
     *
     * @param gui of type GUI - the main GUI class.
     */
    void setGui(GUI gui);

    /**
     * Updates the necessary parts of the scene based on what message was received from the server
     *
     * @param jsonMessage the message received from the server
     */
    void updateFromServer(String jsonMessage);
}
