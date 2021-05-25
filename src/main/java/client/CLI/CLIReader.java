package client.CLI;

import com.google.gson.Gson;
import network.StaticMethods;
import network.beans.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class CLIReader implements Runnable {

    private final BufferedReader in;
    private final CLIView CLIView;
    private final CountDownLatch latch;

    //CONSTRUCTORS

    public CLIReader(BufferedReader in, CLIView CLIView, CountDownLatch latch) {
        this.in = in;
        this.CLIView = CLIView;
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
                    CLIView.setGame(gson.fromJson((String) responseMap.get("jsonMessage"), GameBean.class));
                    StaticMethods.clearConsole();
                    System.out.println(CLIView);
                } catch (Exception ex) {
                    System.out.println("Warning: Game update failed");
                    ex.printStackTrace();
                }
                break;
            case "MARKET":
                try {
                    CLIView.setMarket(gson.fromJson((String) responseMap.get("jsonMessage"), MarketBean.class));
                    StaticMethods.clearConsole();
                    System.out.println(CLIView);
                } catch (Exception ex) {
                    System.out.println("Warning: Market update failed");
                    ex.printStackTrace();
                }
                break;
            case "CARDTABLE":
                try {
                    CLIView.setCardTable(gson.fromJson((String) responseMap.get("jsonMessage"), CardTableBean.class));
                    StaticMethods.clearConsole();
                    System.out.println(CLIView);
                } catch (Exception ex) {
                    System.out.println("Warning: CardTable update failed");
                    ex.printStackTrace();
                }
                break;
            case "PLAYERBOARD":
                try {
                    PlayerBoardBean pbbUpdate = gson.fromJson((String) responseMap.get("jsonMessage"), PlayerBoardBean.class);
                    boolean found = false;

                    for (int i = 0; i < CLIView.getPlayerBoards().size(); i++)
                        if (CLIView.getPlayerBoards().get(i).getUsername().equals(pbbUpdate.getUsername())) {
                            CLIView.getPlayerBoards().set(i, pbbUpdate);
                            found = true;
                            StaticMethods.clearConsole();
                            System.out.println(CLIView);
                            break;
                        }

                    if (!found) {
                        CLIView.getPlayerBoards().add(pbbUpdate);
                        StaticMethods.clearConsole();
                        System.out.println(CLIView);
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

                    for (int i = 0; i < CLIView.getStrongboxes().size(); i++)
                        if (CLIView.getStrongboxes().get(i).getUsername().equals(strongboxUpdate.getUsername())) {
                            CLIView.getStrongboxes().set(i, strongboxUpdate);
                            found = true;
                            StaticMethods.clearConsole();
                            System.out.println(CLIView);
                            break;
                        }

                    if (!found) {
                        CLIView.getStrongboxes().add(strongboxUpdate);
                        StaticMethods.clearConsole();
                        System.out.println(CLIView);
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

                    for (int i = 0; i < CLIView.getWaitingRooms().size(); i++)
                        if (CLIView.getWaitingRooms().get(i).getUsername().equals(waitingRoomUpdate.getUsername())) {
                            CLIView.getWaitingRooms().set(i, waitingRoomUpdate);
                            found = true;
                            StaticMethods.clearConsole();
                            System.out.println(CLIView);
                            break;
                        }

                    if (!found) {
                        CLIView.getWaitingRooms().add(waitingRoomUpdate);
                        StaticMethods.clearConsole();
                        System.out.println(CLIView);
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

                    for (int i = 0; i < CLIView.getWarehouses().size(); i++)
                        if (CLIView.getWarehouses().get(i).getUsername().equals(warehouseUpdate.getUsername())) {
                            CLIView.getWarehouses().set(i, warehouseUpdate);
                            found = true;
                            StaticMethods.clearConsole();
                            System.out.println(CLIView);
                            break;
                        }

                    if (!found) {
                        CLIView.getWarehouses().add(warehouseUpdate);
                        StaticMethods.clearConsole();
                        System.out.println(CLIView);
                    }
                } catch (Exception ex) {
                    System.out.println("Warning: Warehouse update failed");
                    ex.printStackTrace();
                }
                break;
            case "LORENZO":
                try {
                    CLIView.setLorenzo(gson.fromJson((String) responseMap.get("jsonMessage"), LorenzoBean.class));
                    StaticMethods.clearConsole();
                    System.out.println(CLIView);
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
