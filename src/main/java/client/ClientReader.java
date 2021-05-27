package client;

import client.GUI.GUI;
import com.google.gson.Gson;
import javafx.application.Platform;
import network.StaticMethods;
import network.beans.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

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

    //TODO wrappare in Command anche INFO, ERROR e GAME_START
    private void elaborateResponse(String response) {
        Gson gson = new Gson();
        Map responseMap = gson.fromJson(response, Map.class);
        switch ((String) responseMap.get("type")) {
            case "INFO":
                System.out.println(responseMap.get("jsonMessage"));
                if (gui != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            gui.notifyCurrentScene(response);
                        }
                    });
                }
                break;
            case "ERROR":
                System.err.println(responseMap.get("jsonMessage"));
                if (gui != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            gui.notifyCurrentScene(response);
                        }
                    });
                }
                break;
            case "WAIT_PLAYERS":
                System.out.println("Please wait for other players to join...");
                if (gui != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            gui.notifyCurrentScene(response);
                        }
                    });
                }
            case "GAME_START":
                // Atm there's no need to notify CLI
                if (gui != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            gui.notifyCurrentScene(response);
                        }
                    });
                }
            case "GAME":
                try {
                    clientView.setGame(gson.fromJson((String) responseMap.get("jsonMessage"), GameBean.class));
                    notifyViewUpdate(response);
                } catch (Exception ex) {
                    System.out.println("Warning: Game update failed");
                    ex.printStackTrace();
                }
                break;
            case "MARKET":
                try {
                    clientView.setMarket(gson.fromJson((String) responseMap.get("jsonMessage"), MarketBean.class));
                    notifyViewUpdate(response);
                } catch (Exception ex) {
                    System.out.println("Warning: Market update failed");
                    ex.printStackTrace();
                }
                break;
            case "CARDTABLE":
                try {
                    clientView.setCardTable(gson.fromJson((String) responseMap.get("jsonMessage"), CardTableBean.class));
                    notifyViewUpdate(response);
                } catch (Exception ex) {
                    System.out.println("Warning: CardTable update failed");
                    ex.printStackTrace();
                }
                break;
            case "PLAYERBOARD":
                try {
                    PlayerBoardBean pbbUpdate = gson.fromJson((String) responseMap.get("jsonMessage"), PlayerBoardBean.class);
                    boolean found = false;

                    for (int i = 0; i < clientView.getPlayerBoards().size(); i++)
                        if (clientView.getPlayerBoards().get(i).getUsername().equals(pbbUpdate.getUsername())) {
                            clientView.getPlayerBoards().set(i, pbbUpdate);
                            found = true;
                            notifyViewUpdate(response);
                            break;
                        }

                    if (!found) {
                        clientView.getPlayerBoards().add(pbbUpdate);
                        notifyViewUpdate(response);
                    }
                    break;
                } catch (Exception ex) {
                    System.out.println("Warning: PlayerBoard update failed");
                    ex.printStackTrace();
                }
                break;
            case "STRONGBOX":
                try {
                    StrongboxBean strongboxUpdate = gson.fromJson((String) responseMap.get("jsonMessage"), StrongboxBean.class);
                    boolean found = false;

                    for (int i = 0; i < clientView.getStrongboxes().size(); i++)
                        if (clientView.getStrongboxes().get(i).getUsername().equals(strongboxUpdate.getUsername())) {
                            clientView.getStrongboxes().set(i, strongboxUpdate);
                            found = true;
                            notifyViewUpdate(response);
                            break;
                        }

                    if (!found) {
                        clientView.getStrongboxes().add(strongboxUpdate);
                        notifyViewUpdate(response);
                    }
                } catch (Exception ex) {
                    System.out.println("Warning: Strongbox update failed");
                    ex.printStackTrace();
                }
                break;
            case "WAITINGROOM":
                try {
                    WaitingRoomBean waitingRoomUpdate = gson.fromJson((String) responseMap.get("jsonMessage"), WaitingRoomBean.class);
                    boolean found = false;

                    for (int i = 0; i < clientView.getWaitingRooms().size(); i++)
                        if (clientView.getWaitingRooms().get(i).getUsername().equals(waitingRoomUpdate.getUsername())) {
                            clientView.getWaitingRooms().set(i, waitingRoomUpdate);
                            found = true;
                            notifyViewUpdate(response);
                            break;
                        }

                    if (!found) {
                        clientView.getWaitingRooms().add(waitingRoomUpdate);
                        notifyViewUpdate(response);
                    }
                } catch (Exception ex) {
                    System.out.println("Warning: WaitingRoom update failed");
                    ex.printStackTrace();
                }
                break;
            case "WAREHOUSE":
                try {
                    WarehouseBean warehouseUpdate = gson.fromJson((String) responseMap.get("jsonMessage"), WarehouseBean.class);
                    boolean found = false;

                    for (int i = 0; i < clientView.getWarehouses().size(); i++)
                        if (clientView.getWarehouses().get(i).getUsername().equals(warehouseUpdate.getUsername())) {
                            clientView.getWarehouses().set(i, warehouseUpdate);
                            found = true;
                            notifyViewUpdate(response);
                            break;
                        }

                    if (!found) {
                        clientView.getWarehouses().add(warehouseUpdate);
                        notifyViewUpdate(response);
                    }
                } catch (Exception ex) {
                    System.out.println("Warning: Warehouse update failed");
                    ex.printStackTrace();
                }
                break;
            case "LORENZO":
                try {
                    clientView.setLorenzo(gson.fromJson((String) responseMap.get("jsonMessage"), LorenzoBean.class));
                    notifyViewUpdate(response);
                } catch (Exception ex) {
                    System.out.println("Warning: Lorenzo update failed");
                    ex.printStackTrace();
                }
                break;
            default:
                System.out.println("WARNING: unexpected message.\nServer says: " + response);
        }
    }

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
