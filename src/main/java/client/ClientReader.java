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
 * This class is run on a thread on the client and is executed in parallel with the View. It manages all the
 * messages received from the server, updates the ClientView and notifies the view (CLI or GUI) about the changes
 */
public class ClientReader implements Runnable {

    private final BufferedReader in;
    private final ClientView clientView;
    private final CountDownLatch latch;
    private final GUI gui;
    private final int clientMode;

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
        this.clientMode = 0;
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
        this.clientMode = 1;
        this.gui = gui;
    }

    //PUBLIC METHODS

    /**
     * @see Thread#run()
     */
    public void run() {

        String response;
        while (true) {

            try {
                response = in.readLine();
            } catch (IOException ex) {
                System.err.println("Server connection lost.");
                break;
            }

            if (response == null) {
                System.err.println("Server connection lost.");
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
    //TODO wrappare in Command anche INFO, ERROR e GAME_START
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
        if (clientMode == 0) {
            StaticMethods.clearConsole();
            System.out.println(clientView);
        } else if (clientMode == 1) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    assert gui != null; // Intellij garbage
                    gui.notifyCurrentScene(jsonMessage);
                }
            });

            System.out.println("Notified GUI");
        } else
            throw new RuntimeException("Unknown clientMode specified");
    }
}
