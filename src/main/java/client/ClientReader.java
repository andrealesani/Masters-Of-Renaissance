package client;

import client.GUI.GUI;
import com.google.gson.Gson;
import javafx.application.Platform;
import network.StaticMethods;
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
    private final CountDownLatch latch;
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
     * @param latch      countdown latch before connection closes
     * @param gui        is the GUI class to notify when the server sends updates
     */
    public ClientReader(BufferedReader in, ClientView clientView, CountDownLatch latch, GUI gui) {
        this.in = in;
        this.clientView = clientView;
        this.latch = latch;
        this.gui = gui;
    }

    //PUBLIC METHODS

    /**
     * The method used to run this class in multithreading.
     * Initiates a loop which reads the messages sent by the server and updates the ClientView accordingly
     */
    public void run() {

        String response;

        //Loop which handles the server's messages
        while (true) {

            try {
                response = in.readLine();
            } catch (IOException ex) {
                System.err.println("Server connection lost.");
                if (gui != null)
                    gui.stop();
                break;
            }

            if (response == null) {
                System.err.println("Server connection lost.");
                if (gui != null)
                    gui.stop();
                break;
            }

            elaborateResponse(response);
        }

        latch.countDown();
    }

    /**
     * This method parses the commands received from the server. If the message contains a view update, it then updates
     * the ClientView and calls notifyViewUpdate() to notify whichever view is being used so that they can update themselves
     *
     * @param jsonMessage is the message received from the server
     */
    private void elaborateResponse(String jsonMessage) {
        Gson gson = new Gson();
        MessageWrapper response = gson.fromJson(jsonMessage, MessageWrapper.class);

        switch (response.getType()) {
            case INFO, WAIT_PLAYERS, GAME_START:
                System.out.println(response.getMessage());
                if (gui != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            gui.notifyCurrentScene(jsonMessage);
                        }
                    });
                }
                break;
            case ERROR:
                System.err.println(response.getMessage());
                if (gui != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            gui.notifyCurrentScene(jsonMessage);
                        }
                    });
                }
                break;
            case SET_USERNAME:
                clientView.setUsername((String) response.getMessage());
                System.out.println("Username was correctly set to: " + clientView.getUsername() + ".");
                break;
            case GAME_END:
                System.out.println(response.getMessage());
                notifyViewUpdate(jsonMessage);
                break;
            case PLAYER_CONNECTED:
                System.out.println("Player " + response.getMessage() + "has joined the game.");
                notifyViewUpdate(jsonMessage);
                break;
            case PLAYER_DISCONNECTED:
                System.out.println("Player " + response.getMessage() + "has left the game.");
                notifyViewUpdate(jsonMessage);
                break;
            case GAME:
                try {
                    clientView.setGame(gson.fromJson((String) response.getMessage(), GameBean.class));
                    notifyViewUpdate(jsonMessage);
                } catch (Exception ex) {
                    System.out.println("Warning: Game update failed");
                    ex.printStackTrace();
                }
                break;
            case MARKET:
                try {
                    clientView.setMarket(gson.fromJson((String) response.getMessage(), MarketBean.class));
                    notifyViewUpdate(jsonMessage);
                } catch (Exception ex) {
                    System.out.println("Warning: Market update failed");
                    ex.printStackTrace();
                }
                break;
            case CARDTABLE:
                try {
                    clientView.setCardTable(gson.fromJson((String) response.getMessage(), CardTableBean.class));
                    notifyViewUpdate(jsonMessage);
                } catch (Exception ex) {
                    System.out.println("Warning: CardTable update failed");
                    ex.printStackTrace();
                }
                break;
            case PLAYERBOARD:
                try {
                    PlayerBoardBean pbbUpdate = gson.fromJson((String) response.getMessage(), PlayerBoardBean.class);
                    boolean found = false;

                    for (int i = 0; i < clientView.getPlayerBoards().size(); i++)
                        if (clientView.getPlayerBoards().get(i).getUsername().equals(pbbUpdate.getUsername())) {
                            clientView.getPlayerBoards().set(i, pbbUpdate);
                            found = true;
                            notifyViewUpdate(jsonMessage);
                            break;
                        }

                    if (!found) {
                        clientView.getPlayerBoards().add(pbbUpdate);
                        notifyViewUpdate(jsonMessage);
                    }
                    break;
                } catch (Exception ex) {
                    System.out.println("Warning: PlayerBoard update failed");
                    ex.printStackTrace();
                }
                break;
            case STRONGBOX:
                try {
                    StrongboxBean strongboxUpdate = gson.fromJson((String) response.getMessage(), StrongboxBean.class);
                    boolean found = false;

                    for (int i = 0; i < clientView.getStrongboxes().size(); i++)
                        if (clientView.getStrongboxes().get(i).getUsername().equals(strongboxUpdate.getUsername())) {
                            clientView.getStrongboxes().set(i, strongboxUpdate);
                            found = true;
                            notifyViewUpdate(jsonMessage);
                            break;
                        }

                    if (!found) {
                        clientView.getStrongboxes().add(strongboxUpdate);
                        notifyViewUpdate(jsonMessage);
                    }
                } catch (Exception ex) {
                    System.out.println("Warning: Strongbox update failed");
                    ex.printStackTrace();
                }
                break;
            case WAITINGROOM:
                try {
                    WaitingRoomBean waitingRoomUpdate = gson.fromJson((String) response.getMessage(), WaitingRoomBean.class);
                    boolean found = false;

                    for (int i = 0; i < clientView.getWaitingRooms().size(); i++)
                        if (clientView.getWaitingRooms().get(i).getUsername().equals(waitingRoomUpdate.getUsername())) {
                            clientView.getWaitingRooms().set(i, waitingRoomUpdate);
                            found = true;
                            notifyViewUpdate(jsonMessage);
                            break;
                        }

                    if (!found) {
                        clientView.getWaitingRooms().add(waitingRoomUpdate);
                        notifyViewUpdate(jsonMessage);
                    }
                } catch (Exception ex) {
                    System.out.println("Warning: WaitingRoom update failed");
                    ex.printStackTrace();
                }
                break;
            case WAREHOUSE:
                try {
                    WarehouseBean warehouseUpdate = gson.fromJson((String) response.getMessage(), WarehouseBean.class);
                    boolean found = false;

                    for (int i = 0; i < clientView.getWarehouses().size(); i++)
                        if (clientView.getWarehouses().get(i).getUsername().equals(warehouseUpdate.getUsername())) {
                            clientView.getWarehouses().set(i, warehouseUpdate);
                            found = true;
                            notifyViewUpdate(jsonMessage);
                            break;
                        }

                    if (!found) {
                        clientView.getWarehouses().add(warehouseUpdate);
                        notifyViewUpdate(jsonMessage);
                    }
                } catch (Exception ex) {
                    System.out.println("Warning: Warehouse update failed");
                    ex.printStackTrace();
                }
                break;
            case PRODUCTIONHANDLER:
                try {
                    ProductionHandlerBean productionHandlerUpdate = gson.fromJson((String) response.getMessage(), ProductionHandlerBean.class);
                    boolean found = false;

                    for (int i = 0; i < clientView.getProductionHandlers().size(); i++)
                        if (clientView.getProductionHandlers().get(i).getUsername().equals(productionHandlerUpdate.getUsername())) {
                            clientView.getProductionHandlers().set(i, productionHandlerUpdate);
                            found = true;
                            notifyViewUpdate(jsonMessage);
                            break;
                        }

                    if (!found) {
                        clientView.getProductionHandlers().add(productionHandlerUpdate);
                        notifyViewUpdate(jsonMessage);
                    }
                } catch (Exception ex) {
                    System.out.println("Warning: ProductionHandler update failed");
                    ex.printStackTrace();
                }
                break;
            case LORENZO:
                try {
                    clientView.setLorenzo(gson.fromJson((String) response.getMessage(), LorenzoBean.class));
                    notifyViewUpdate(jsonMessage);
                } catch (Exception ex) {
                    System.out.println("Warning: Lorenzo update failed");
                    ex.printStackTrace();
                }
                break;
            default:
                System.out.println("WARNING: unexpected response.\nServer says: " + jsonMessage);
        }
    }

    /**
     * This method notifies either the CLI or the GUI that something in the ClientView has changed and forwards the
     * update message to the one that is currently being used as View
     *
     * @param jsonMessage the message received from the server
     */
    private void notifyViewUpdate(String jsonMessage) {
        if (gui == null) {
            StaticMethods.clearConsole();
            System.out.println(clientView);
        } else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    gui.notifyCurrentScene(jsonMessage);
                }
            });

            System.out.println("Notified GUI");
        }
    }
}
