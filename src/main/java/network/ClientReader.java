package network;

import com.google.gson.Gson;
import network.beans.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class ClientReader implements Runnable {

    private final BufferedReader in;
    private final ClientView clientView;
    private final CountDownLatch latch;

    //CONSTRUCTORS

    public ClientReader(BufferedReader in, ClientView clientView, CountDownLatch latch) {
        this.in = in;
        this.clientView = clientView;
        this.latch = latch;
    }

    //MULTITHREADING METHODS

    public void run() {

        String response;
        while (true) {

            try {
                response = in.readLine();
            } catch (IOException ex) {
                System.err.println("Server has disconnected");
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
            case "ERROR":
                System.out.println(responseMap.get("jsonMessage"));
                break;
            case "GAME":
                try {
                    clientView.setGame(gson.fromJson((String) responseMap.get("jsonMessage"), GameBean.class));
                    System.out.println("Updated Game");
                } catch (Exception ignored) {
                    System.out.println("Warning: Game update failed");
                }
                break;
            case "MARKET":
                try {
                    clientView.setMarket(gson.fromJson((String) responseMap.get("jsonMessage"), MarketBean.class));
                    System.out.println("Updated Market");
                } catch (Exception ignored) {
                    System.out.println("Warning: Market update failed");
                }
                ;
                break;
            case "CARDTABLE":
                try {
                    clientView.setCardTable(gson.fromJson((String) responseMap.get("jsonMessage"), CardTableBean.class));
                    System.out.println("Updated CardTable");
                } catch (Exception ignored) {
                    System.out.println("Warning: CardTable update failed");
                }
                break;
            case "PLAYERBOARD":
                try {
                    PlayerBoardBean pbbUpdate = gson.fromJson((String) responseMap.get("jsonMessage"), PlayerBoardBean.class);
                    if (clientView.getPlayerBoards().size() > 0) {
                        for (PlayerBoardBean pbb : clientView.getPlayerBoards())
                            if (pbb.getUsername().equals(pbbUpdate.getUsername())) {
                                pbb = pbbUpdate;
                                System.out.println("Updated PlayerBoard");
                                break;
                            } else {
                                clientView.getPlayerBoards().add(pbbUpdate);
                                System.out.println("Added new PlayerBoard");
                            }
                    } else {
                        clientView.getPlayerBoards().add(pbbUpdate);
                        System.out.println("Added new PlayerBoard");
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Warning: PlayerBoard update failed");
                }
                break;
            case "STRONGBOX":
                try {
                    StrongboxBean strongboxUpdate = gson.fromJson((String) responseMap.get("jsonMessage"), StrongboxBean.class);
                    if (clientView.getStrongboxes().size() > 0) {
                        for (StrongboxBean strongbox : clientView.getStrongboxes())
                            if (strongbox.getUsername().equals(strongboxUpdate.getUsername())) {
                                strongbox = strongboxUpdate;
                                break;
                            } else {
                                clientView.getStrongboxes().add(strongboxUpdate);
                                System.out.println("Updated Strongbox");
                            }
                    } else {
                        clientView.getStrongboxes().add(strongboxUpdate);
                        System.out.println("Updated Strongbox");
                    }
                    break;
                } catch (Exception ignored) {
                    System.out.println("Warning: Strongbox update failed");
                }
                break;
            case "WAITINGROOM":
                try {
                    WaitingRoomBean waitingRoomUpdate = gson.fromJson((String) responseMap.get("jsonMessage"), WaitingRoomBean.class);
                    if(clientView.getWaitingRooms().size() > 0) {
                        for (WaitingRoomBean waitingRoom : clientView.getWaitingRooms())
                            if (waitingRoom.getUsername().equals(waitingRoomUpdate.getUsername())) {
                                waitingRoom = waitingRoomUpdate;
                                break;
                            } else {
                                clientView.getWaitingRooms().add(waitingRoomUpdate);
                                System.out.println("Updated WaitingRoom");
                            }
                    } else {
                        clientView.getWaitingRooms().add(waitingRoomUpdate);
                        System.out.println("Updated WaitingRoom");
                    }
                    break;
                } catch (Exception ignored) {
                    System.out.println("Warning: WaitingRoom update failed");
                }
                break;
            case "WAREHOUSE":
                try {
                    WarehouseBean warehouseUpdate = gson.fromJson((String) responseMap.get("jsonMessage"), WarehouseBean.class);
                    if(clientView.getWarehouses().size() > 0) {
                        for (WarehouseBean warehouse : clientView.getWarehouses())
                            if (warehouse.getUsername().equals(warehouseUpdate.getUsername())) {
                                warehouse = warehouseUpdate;
                                break;
                            } else {
                                clientView.getWarehouses().add(warehouseUpdate);
                                System.out.println("Updated Warehouse");
                            }
                    } else {
                        clientView.getWarehouses().add(warehouseUpdate);
                        System.out.println("Updated Warehouse");
                    }
                    break;
                } catch (Exception ignored) {
                    System.out.println("Warning: Warehouse update failed");
                }
                break;
            case "LORENZO":
                try {
                    clientView.setLorenzo(gson.fromJson((String) responseMap.get("jsonMessage"), LorenzoBean.class));
                    System.out.println("Updated Lorenzo");
                } catch (Exception ignored) {
                    System.out.println("Warning: Lorenzo update failed");
                }
                break;
            default:
                System.out.println("WARNING: unexpected message.\nServer says: " + response);
        }
    }
}
