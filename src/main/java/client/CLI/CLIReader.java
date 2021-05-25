package client.CLI;

import client.ClientView;
import com.google.gson.Gson;
import network.StaticMethods;
import network.beans.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class CLIReader implements Runnable {

    private final BufferedReader in;
    private final client.ClientView ClientView;
    private final CountDownLatch latch;

    //CONSTRUCTORS

    public CLIReader(BufferedReader in, ClientView ClientView, CountDownLatch latch) {
        this.in = in;
        this.ClientView = ClientView;
        this.latch = latch;
    }

    //MULTITHREADING METHODS

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

    private void elaborateResponse(String response) {
        Gson gson = new Gson();
        Map responseMap = gson.fromJson(response, Map.class);
        switch ((String) responseMap.get("type")) {
            case "INFO":
                System.out.println(responseMap.get("jsonMessage"));
                break;
            case "ERROR":
                System.err.println(responseMap.get("jsonMessage"));
                break;
            case "GAME":
                try {
                    ClientView.setGame(gson.fromJson((String) responseMap.get("jsonMessage"), GameBean.class));
                    StaticMethods.clearConsole();
                    System.out.println(ClientView);
                } catch (Exception ex) {
                    System.out.println("Warning: Game update failed");
                    ex.printStackTrace();
                }
                break;
            case "MARKET":
                try {
                    ClientView.setMarket(gson.fromJson((String) responseMap.get("jsonMessage"), MarketBean.class));
                    StaticMethods.clearConsole();
                    System.out.println(ClientView);
                } catch (Exception ex) {
                    System.out.println("Warning: Market update failed");
                    ex.printStackTrace();
                }
                break;
            case "CARDTABLE":
                try {
                    ClientView.setCardTable(gson.fromJson((String) responseMap.get("jsonMessage"), CardTableBean.class));
                    StaticMethods.clearConsole();
                    System.out.println(ClientView);
                } catch (Exception ex) {
                    System.out.println("Warning: CardTable update failed");
                    ex.printStackTrace();
                }
                break;
            case "PLAYERBOARD":
                try {
                    PlayerBoardBean pbbUpdate = gson.fromJson((String) responseMap.get("jsonMessage"), PlayerBoardBean.class);
                    boolean found = false;

                    for (int i = 0; i < ClientView.getPlayerBoards().size(); i++)
                        if (ClientView.getPlayerBoards().get(i).getUsername().equals(pbbUpdate.getUsername())) {
                            ClientView.getPlayerBoards().set(i, pbbUpdate);
                            found = true;
                            StaticMethods.clearConsole();
                            System.out.println(ClientView);
                            break;
                        }

                    if (!found) {
                        ClientView.getPlayerBoards().add(pbbUpdate);
                        StaticMethods.clearConsole();
                        System.out.println(ClientView);
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

                    for (int i = 0; i < ClientView.getStrongboxes().size(); i++)
                        if (ClientView.getStrongboxes().get(i).getUsername().equals(strongboxUpdate.getUsername())) {
                            ClientView.getStrongboxes().set(i, strongboxUpdate);
                            found = true;
                            StaticMethods.clearConsole();
                            System.out.println(ClientView);
                            break;
                        }

                    if (!found) {
                        ClientView.getStrongboxes().add(strongboxUpdate);
                        StaticMethods.clearConsole();
                        System.out.println(ClientView);
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

                    for (int i = 0; i < ClientView.getWaitingRooms().size(); i++)
                        if (ClientView.getWaitingRooms().get(i).getUsername().equals(waitingRoomUpdate.getUsername())) {
                            ClientView.getWaitingRooms().set(i, waitingRoomUpdate);
                            found = true;
                            StaticMethods.clearConsole();
                            System.out.println(ClientView);
                            break;
                        }

                    if (!found) {
                        ClientView.getWaitingRooms().add(waitingRoomUpdate);
                        StaticMethods.clearConsole();
                        System.out.println(ClientView);
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

                    for (int i = 0; i < ClientView.getWarehouses().size(); i++)
                        if (ClientView.getWarehouses().get(i).getUsername().equals(warehouseUpdate.getUsername())) {
                            ClientView.getWarehouses().set(i, warehouseUpdate);
                            found = true;
                            StaticMethods.clearConsole();
                            System.out.println(ClientView);
                            break;
                        }

                    if (!found) {
                        ClientView.getWarehouses().add(warehouseUpdate);
                        StaticMethods.clearConsole();
                        System.out.println(ClientView);
                    }
                } catch (Exception ex) {
                    System.out.println("Warning: Warehouse update failed");
                    ex.printStackTrace();
                }
                break;
            case "LORENZO":
                try {
                    ClientView.setLorenzo(gson.fromJson((String) responseMap.get("jsonMessage"), LorenzoBean.class));
                    StaticMethods.clearConsole();
                    System.out.println(ClientView);
                } catch (Exception ex) {
                    System.out.println("Warning: Lorenzo update failed");
                    ex.printStackTrace();
                }
                break;
            default:
                System.out.println("WARNING: unexpected message.\nServer says: " + response);
        }
    }
}
