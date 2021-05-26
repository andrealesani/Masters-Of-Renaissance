package client.GUI.controllers;

import client.GUI.GUI;

public interface GUIController {
    /**
     * This method sets the GUI reference to the local controller.
     *
     * @param gui of type GUI - the main GUI class.
     */
    void setGui(GUI gui);

    /**
     * This method notifies the current scene about the message sent by the server
     */
    void updateFromServer(String jsonMessage);
}
