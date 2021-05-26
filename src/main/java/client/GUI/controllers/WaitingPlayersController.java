package client.GUI.controllers;

import client.GUI.GUI;

public class WaitingPlayersController implements GUIController{
    private GUI gui;


    public void initialize() {
        System.out.println("Initializing");
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
