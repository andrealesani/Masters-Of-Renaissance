package network;

import Exceptions.CardNotPresentException;
import com.google.gson.Gson;
import model.CardColor;
import model.ResourceType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class ClientWriter implements Runnable {

    private final BufferedReader stdIn;
    private final PrintWriter out;
    private final ClientView clientView;
    private final CountDownLatch latch;
    private boolean doClose;

    //CONSTRUCTORS

    public ClientWriter(BufferedReader stdIn, PrintWriter out, ClientView clientView, CountDownLatch latch) {
        this.stdIn = stdIn;
        this.out = out;
        this.clientView = clientView;
        this.latch = latch;
        this.doClose = false;
    }

    //MULTITHREADING METHODS

    public void run() {
        Gson gson = new Gson();
        String userInput;
        while (!doClose) {

            try {
                userInput = stdIn.readLine();

                if (userInput.equals("ESC + :q")) {
                    out.println(userInput);
                    System.out.println("Closing connection...");
                    break;
                }

                switch (userInput) {
                    case "show" -> {
                        System.out.println("Specify what you want to see or press ENTER to show all:");
                        System.out.println("(Supported commands: 'market', 'cardtable', 'lorenzo', 'playerboard', 'strongbox', 'waitingroom', 'warehouse')");
                        String request = stdIn.readLine();
                        switch (request) {
                            case "" -> {
                                System.out.println("\n" + clientView);
                            }
                            case "market" -> {
                                System.out.println("\n" + clientView.getMarket());
                            }
                            case "cardtable" -> {
                                System.out.println("\n" + clientView.getCardTable());
                            }
                            case "lorenzo" -> {
                                if(clientView.getLorenzo() == null)
                                    System.out.println("\nLorenzo is not present in this game");
                                else
                                    System.out.println("\n" + clientView.getLorenzo());
                            }
                            case "playerboard" -> {
                                System.out.println("\n" + clientView.getPlayerBoards());
                            }
                            case "strongbox" -> {
                                System.out.println("\n" + clientView.getStrongboxes());
                            }
                            case "waitingroom" -> {
                                System.out.println("\n" + clientView.getWaitingRooms());
                            }
                            case "warehouse" -> {
                                System.out.println("\n" + clientView.getWarehouses());
                            }
                        }
                    }
                    case "card" -> {
                        System.out.println("Specify the card you want to see: ");
                        int cardId = getInt();
                        try {
                            System.out.println(clientView.getCardTable().getDevelopmentCardFromId(cardId));
                        } catch (CardNotPresentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    case "actions" -> {
                        System.out.println("0. chooseBonusResourceType");
                        System.out.println("1. chooseLeaderCard");
                        System.out.println("2. playLeaderCard");
                        System.out.println("3. discardLeaderCard");
                        System.out.println("4. selectMarketRow");
                        System.out.println("5. selectMarketColumn");
                        System.out.println("6. sendResourceToDepot");
                        System.out.println("7. chooseMarbleConversion");
                        System.out.println("8. swapDepotContent");
                        System.out.println("9. moveDepotContent");
                        System.out.println("10. takeDevelopmentCard");
                        System.out.println("11. selectProduction");
                        System.out.println("12. resetProductionChoice");
                        System.out.println("13. chooseJollyInput");
                        System.out.println("14. chooseJollyOutput");
                        System.out.println("15. confirmProductionChoice");
                        System.out.println("16. payFromWarehouse");
                        System.out.println("17. payFromStrongbox");
                        System.out.println("18. endTurn");
                    }
                    case "chooseBonusResourceType", "0." -> {
                        System.out.println("Action chooseBonusResourceType selected");
                        Map<String, Object> map = new HashMap<>();

                        System.out.println("Which Resource?");
                        ResourceType resourceSelected = getResourceType();
                        map.put("resource", resourceSelected);

                        System.out.println("How much?");
                        int num = getInt();
                        map.put("quantity", num);

                        System.out.println("Resource: " + resourceSelected + "\nBonus: " + num);
                        Command command = new Command(UserCommandsType.chooseBonusResourceType, map);
                        out.println(gson.toJson(command));
                    }
                    case "chooseLeaderCard", "1." -> {
                        System.out.println("Action chooseLeaderCard selected");
                        Map<String, Object> map = new HashMap<>();

                        System.out.println("Which LeaderCard?");
                        int leaderCardSelected = getInt();
                        map.put("number", leaderCardSelected);

                        System.out.println("LeaderCard selected: " + leaderCardSelected);
                        Command command = new Command(UserCommandsType.chooseLeaderCard, map);
                        out.println(gson.toJson(command));
                    }
                    case "playLeaderCard", "2." -> {
                        System.out.println("Action playLeaderCard selected");
                        Map<String, Object> map = new HashMap<>();

                        System.out.println("Which LeaderCard?");
                        int leaderCardSelected = getInt();
                        map.put("number", leaderCardSelected);

                        System.out.println("LeaderCard selected: " + leaderCardSelected);
                        Command command = new Command(UserCommandsType.playLeaderCard, map);
                        out.println(gson.toJson(command));
                    }
                    case "discardLeaderCard", "3." -> {
                        System.out.println("Action discardLeaderCard selected");
                        Map<String, Object> map = new HashMap<>();

                        System.out.println("Which LeaderCard?");
                        int leaderCardSelected = getInt();
                        map.put("number", leaderCardSelected);

                        System.out.println("LeaderCard selected: " + leaderCardSelected);
                        Command command = new Command(UserCommandsType.discardLeaderCard, map);
                        out.println(gson.toJson(command));
                    }
                    case "selectMarketRow", "4." -> {
                        System.out.println("Action selectMarketRow selected");
                        Map<String, Object> map = new HashMap<>();

                        System.out.println("Which row?");
                        int rowSelected = getInt();
                        map.put("number", rowSelected);

                        System.out.println("Market's row selected: " + rowSelected);
                        Command command = new Command(UserCommandsType.selectMarketRow, map);
                        out.println(gson.toJson(command));
                    }
                    case "selectMarketColumn", "5." -> {
                        System.out.println("Action selectMarketColumn selected");
                        Map<String, Object> map = new HashMap<>();

                        System.out.println("Which row?");
                        int columnSelected = getInt();
                        map.put("number", columnSelected);

                        System.out.println("Market's column selected: " + columnSelected);
                        Command command = new Command(UserCommandsType.selectMarketColumn, map);
                        out.println(gson.toJson(command));
                    }
                    case "sendResourceToDepot", "6." -> {
                        System.out.println("Action sendResourceToDepot selected");
                        Map<String, Object> map = new HashMap<>();

                        System.out.println("Which Depot?");
                        int depotSelected = getInt();
                        map.put("number", depotSelected);

                        System.out.println("Which Resource?");
                        ResourceType resourceSelected = getResourceType();
                        map.put("resource", resourceSelected);

                        System.out.println("How much?");
                        int num = getInt();
                        map.put("quantity", num);

                        System.out.println("Sending " + num + " " + resourceSelected + " to the " + depotSelected + " depot");
                        Command command = new Command(UserCommandsType.sendResourceToDepot, map);
                        out.println(gson.toJson(command));
                    }
                    case "chooseMarbleConversion", "7." -> {
                        System.out.println("Action chooseMarbleConversion selected");
                        Map<String, Object> map = new HashMap<>();

                        System.out.println("Which Resource?");
                        ResourceType resourceSelected = getResourceType();
                        map.put("resource", resourceSelected);

                        System.out.println("How much?");
                        int num = getInt();
                        map.put("quantity", num);

                        System.out.println("Resource: " + resourceSelected + "\nQuantity: " + num);
                        Command command = new Command(UserCommandsType.chooseMarbleConversion, map);
                        out.println(gson.toJson(command));
                    }
                    case "swapDepotContent", "8." -> {
                        System.out.println("Action swapDepotContent selected");
                        Map<String, Object> map = new HashMap<>();
                        int[] depots = new int[2];

                        System.out.println("First depot?");
                        depots[0] = getInt();
                        System.out.println("Second depot?");
                        depots[1] = getInt();
                        map.put("depots", depots);

                        System.out.println("The depot " + depots[0] + " was swapped with the depot " + depots[1]);
                        Command command = new Command(UserCommandsType.swapDepotContent, map);
                        out.println(gson.toJson(command));
                    }
                    case "moveDepotContent", "9." -> {
                        System.out.println("Action moveDepotContent selected");
                        Map<String, Object> map = new HashMap<>();
                        int[] depots = new int[2];

                        System.out.println("First depot?");
                        depots[0] = getInt();

                        System.out.println("Which Resource?");
                        ResourceType resourceSelected = getResourceType();
                        map.put("resource", resourceSelected);

                        System.out.println("How much?");
                        int num = getInt();
                        map.put("quantity", num);

                        System.out.println("In which depot?");
                        depots[1] = getInt();
                        map.put("depots", depots);

                        System.out.println(num + " " + resourceSelected + " were moved from depot " + depots[0] + " to depot " + depots[1]);
                        Command command = new Command(UserCommandsType.moveDepotContent, map);
                        out.println(gson.toJson(command));
                    }
                    case "takeDevelopmentCard", "10." -> {
                        System.out.println("Action takeDevelopmentCard selected");
                        Map<String, Object> map = new HashMap<>();

                        System.out.println("Which color?");
                        CardColor colorSelected = getColor();
                        map.put("color", colorSelected);

                        System.out.println("Which level?");
                        int levelSelected = getInt();
                        map.put("level", levelSelected);

                        System.out.println("Which number?");
                        int numberSelected = getInt();
                        map.put("number", numberSelected);

                        System.out.println("Color: " + colorSelected);
                        System.out.println("Level: " + levelSelected);
                        System.out.println("Color: " + numberSelected);
                        Command command = new Command(UserCommandsType.takeDevelopmentCard, map);
                        out.println(gson.toJson(command));
                    }
                    case "selectProduction", "11." -> {
                        System.out.println("Action selectProduction selected");
                        Map<String, Object> map = new HashMap<>();

                        System.out.println("Which Production?");
                        int productionSelected = getInt();
                        map.put("number", productionSelected);

                        System.out.println("Production selected: " + productionSelected);
                        Command command = new Command(UserCommandsType.selectProduction, map);
                        out.println(gson.toJson(command));
                    }
                    case "resetProductionChoice", "12." -> {
                        System.out.println("Action resetProductionChoice selected");
                        System.out.println("Resetting production...");

                        Command command = new Command(UserCommandsType.resetProductionChoice, null);
                        out.println(gson.toJson(command));
                    }
                    case "confirmProductionChoice", "13." -> {
                        System.out.println("Action confirmProductionChoice selected");
                        System.out.println("Production Confirmed!");

                        Command command = new Command(UserCommandsType.confirmProductionChoice, null);
                        out.println(gson.toJson(command));

                    }
                    case "chooseJollyInput", "14." -> {
                        System.out.println("Action chooseJollyInput selected");
                        Map<String, Object> map = new HashMap<>();

                        System.out.println("Which Resource?");
                        ResourceType resourceSelected = getResourceType();
                        map.put("resource", resourceSelected);

                        System.out.println("Resource selected: " + resourceSelected);
                        Command command = new Command(UserCommandsType.chooseJollyInput, map);
                        out.println(gson.toJson(command));

                    }
                    case "chooseJollyOutput", "15." -> {
                        System.out.println("Action chooseJollyOutput selected");
                        Map<String, Object> map = new HashMap<>();

                        System.out.println("Which Resource?");
                        ResourceType resourceSelected = getResourceType();
                        map.put("resource", resourceSelected);

                        System.out.println("Resource selected: " + resourceSelected);
                        Command command = new Command(UserCommandsType.chooseJollyOutput, map);
                        out.println(gson.toJson(command));

                    }
                    case "payFromWarehouse", "16." -> {
                        System.out.println("Action payFromWarehouse selected");
                        Map<String, Object> map = new HashMap<>();

                        System.out.println("Which Depot?");
                        int depotSelected = getInt();
                        map.put("number", depotSelected);

                        System.out.println("Which Resource?");
                        ResourceType resourceSelected = getResourceType();
                        map.put("resource", resourceSelected);

                        System.out.println("How much?");
                        int num = getInt();
                        map.put("quantity", num);

                        System.out.println("Received " + num + " " + resourceSelected + " from depot " + depotSelected);
                        Command command = new Command(UserCommandsType.payFromWarehouse, map);
                        out.println(gson.toJson(command));

                    }
                    case "payFromStrongbox", "17." -> {
                        System.out.println("Action payFromStrongbox selected");
                        Map<String, Object> map = new HashMap<>();

                        System.out.println("Which Resource?");
                        ResourceType resourceSelected = getResourceType();
                        map.put("resource", resourceSelected);

                        System.out.println("How much?");
                        int num = getInt();
                        map.put("quantity", num);

                        System.out.println("Received " + num + " " + resourceSelected + " from Strongbox");
                        Command command = new Command(UserCommandsType.payFromStrongbox, map);
                        out.println(gson.toJson(command));

                    }
                    case "endTurn", "18." -> {
                        System.out.println("Action endTurn selected");

                        Command command = new Command(UserCommandsType.endTurn, null);
                        out.println(gson.toJson(command));

                    }
                    default -> out.println(userInput);
                }

            } catch (IOException ex) {
                System.out.println("Uh oh, IO exception when reading from stdIn.");
                break;
            }
        }

        latch.countDown();
    }

    public void doClose() {
        doClose = true;
        System.out.println("Server connection lost, press any key to terminate.");
    }


    private int getInt() throws IOException {
        int result;
        while (true) {
            try {
                String text = stdIn.readLine();
                if (text == null) {
                    throw new IOException();
                }
                result = Integer.parseInt(text);
                break;
            } catch (NumberFormatException ex) {
                System.out.println("This string is not a number, retry.");
            }
        }
        return result;
    }

    private CardColor getColor() throws IOException {
        CardColor result;
        while (true) {
            try {
                String text = stdIn.readLine();
                if (text == null) {
                    throw new IOException();
                }
                result = CardColor.valueOf(text);
                break;
            } catch (IllegalArgumentException ex) {
                System.out.println("This string is not a color, retry.");
            }
        }
        return result;
    }

    private ResourceType getResourceType() throws IOException {
        ResourceType result;
        while (true) {
            try {
                String text = stdIn.readLine();
                if (text == null) {
                    throw new IOException();
                }
                result = ResourceType.valueOf(text);
                break;
            } catch (IllegalArgumentException ex) {
                System.out.println("This string is not a resource, retry.");
            }
        }
        return result;
    }
}
