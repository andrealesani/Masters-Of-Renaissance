package client;

import client.GUI.GUI;
import com.google.gson.Gson;
import javafx.application.Platform;
import model.StaticMethods;
import network.MessageWrapper;
import network.beans.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * This class is run on a thread on the client and is executed in parallel with the user interface. It manages all the
 * messages received from the server, updates the ClientView and notifies the interface (CLI or GUI) about the changes
 */
public class ClientReader implements Runnable {
    /**
     * The buffer used to receive messages from the server
     */
    private final BufferedReader in;
    /**
     * The object used to store the client's game data
     */
    private final ClientView clientView;
    /**
     * The CountDownLatch reference used to signal to the CLI class when this thread finishes running
     */
    private CountDownLatch latch;
    /**
     * The reference to the GUI class (used only in GUI mode)
     */
    private final GUI gui;

    //CONSTRUCTORS

    /**
     * CLI constructor
     *
     * @param in         is the inputStream to read
     * @param clientView is the view to update
     * @param latch      countdown latch before connection closes
     */
    public ClientReader(BufferedReader in, ClientView clientView, CountDownLatch latch) {
        this.in = in;
        this.clientView = clientView;
        this.latch = latch;
        this.gui = null;
    }

    /**
     * GUI constructor
     *
     * @param in         is the inputStream to read
     * @param clientView is the view to update
     * @param gui        is the GUI class to notify when the server sends updates
     */
    public ClientReader(BufferedReader in, ClientView clientView, GUI gui) {
        this.in = in;
        this.clientView = clientView;
        this.gui = gui;
    }

    //MULTITHREADING METHODS

    /**
     * The method used to run this class in multithreading.
     * Initiates a loop which reads the messages sent by the server and updates the ClientView accordingly
     */
    public void run() {

        String response;

        //Loop which handles the server's messages
        while (true) {

            //If the server disconnects, interrupt the client
            try {
                response = in.readLine();
            } catch (IOException ex) {
                System.err.println("Server connection lost.");
                if (gui != null)
                    gui.stop();
                break;
            }

            //If the server disconnects, interrupt the client
            if (response == null) {
                System.err.println("Server connection lost.");
                if (gui != null)
                    gui.stop();
                break;
            }

            //Elaborate the message from the server
            elaborateResponse(response);
        }

        //Signals the ending of the reader thread to the CLI class (if in CLI mode)
        if (gui == null)
            latch.countDown();
    }

    //PUBLIC METHODS

    /**
     * This method parses the commands received from the server and updates the client's game data and interfaces accordingly
     *
     * @param jsonMessage is the message received from the server
     */
    private void elaborateResponse(String jsonMessage) {
        Gson gson = new Gson();
        MessageWrapper response = gson.fromJson(jsonMessage, MessageWrapper.class);

        try {

            switch (response.getType()) {

                case INFO -> {
                    notifyGui(response);
                    System.out.println(response.getMessage());
                }

                case ERROR -> {
                    notifyGui(response);
                    System.err.println(response.getMessage());
                }

                case SET_USERNAME -> {
                    clientView.setUsername(response.getMessage());
                    notifyGui(response);
                    System.out.println("Username was correctly set to: " + clientView.getUsername() + ".");
                }

                case WAIT_PLAYERS -> {
                    clientView.setWaitPlayers(true);
                    System.out.println(response.getMessage());
                }

                case GAME_START -> {
                    clientView.setWaitPlayers(false);
                    notifyViewUpdate(response);
                    System.out.println(response.getMessage());
                }

                case GAME_END -> {
                    notifyViewUpdate(response);
                    System.out.println(response.getMessage());
                }

                case PLAYER_CONNECTED -> {
                    notifyViewUpdate(response);
                    System.out.println("Player " + response.getMessage() + " has joined the game.\n");
                }

                case PLAYER_DISCONNECTED -> {
                    notifyViewUpdate(response);
                    System.out.println("Player " + response.getMessage() + " has left the game.\n");
                }

                case GAME -> {
                    clientView.setGame(gson.fromJson(response.getMessage(), GameBean.class));
                    notifyViewUpdate(response);
                }

                case MARKET -> {
                    clientView.setMarket(gson.fromJson(response.getMessage(), MarketBean.class));
                    notifyViewUpdate(response);
                }

                case CARDTABLE -> {
                    clientView.setCardTable(gson.fromJson(response.getMessage(), CardTableBean.class));
                    notifyViewUpdate(response);
                }

                case PLAYERBOARD -> {
                    clientView.setPlayerBoard(gson.fromJson(response.getMessage(), PlayerBoardBean.class));
                    notifyViewUpdate(response);
                }

                case STRONGBOX -> {
                    clientView.setStrongbox(gson.fromJson(response.getMessage(), StrongboxBean.class));
                    notifyViewUpdate(response);
                }

                case WAITINGROOM -> {
                    clientView.setWaitingRoom(gson.fromJson(response.getMessage(), WaitingRoomBean.class));
                    notifyViewUpdate(response);
                }

                case WAREHOUSE -> {
                    clientView.setWarehouse(gson.fromJson(response.getMessage(), WarehouseBean.class));
                    notifyViewUpdate(response);
                }

                case PRODUCTIONHANDLER -> {
                    clientView.setProductionHandler(gson.fromJson(response.getMessage(), ProductionHandlerBean.class));
                    notifyViewUpdate(response);
                }

                case LORENZO -> {
                    clientView.setLorenzo(gson.fromJson((String) response.getMessage(), LorenzoBean.class));
                    notifyViewUpdate(response);
                }

                default -> System.out.println("WARNING: unexpected response." +
                        "\nServer says: " + jsonMessage);

            }

        } catch (Exception ex) {
            System.err.println("Warning: View update failed.");
            ex.printStackTrace();
        }

    }

    //PRIVATE NOTIFICATION METHODS

    /**
     * Notifies either the CLI or the GUI that something in the ClientView has changed and forwards the
     * update message to the one that is currently being used as View
     *
     * @param response the message received from the server
     */
    private void notifyViewUpdate(MessageWrapper response) {
        if (gui == null) {
            //If in CLI mode, clear the console and re-print all of the game's elements
            StaticMethods.clearConsole();
            System.out.println(clientView);
        } else {
            //If in GUI mode, forward the message to the GUI
            Platform.runLater(() -> gui.notifyCurrentScene(response));
            System.out.println("Notified GUI");
        }
    }

    /**
     * Notifies the GUI that a ClientView element has changed
     *
     * @param response the message received from the server
     */
    private void notifyGui(MessageWrapper response) {
        if (gui != null) {
            Platform.runLater(() -> gui.notifyCurrentScene(response));
            System.out.println("Notified GUI");
        }
    }
}
