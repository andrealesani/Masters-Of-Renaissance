package client.GUI.controllers;

import client.GUI.GUI;

public class ProductionsController implements GUIController{
    private GUI gui;

    @Override
    public void updateFromServer(String jsonMessage) {

    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
