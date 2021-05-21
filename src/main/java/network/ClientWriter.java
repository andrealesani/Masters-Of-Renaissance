package network;

import Exceptions.CardNotPresentException;
import com.google.gson.Gson;
import model.CardColor;
import model.Color;
import model.resource.ResourceType;
import model.TurnPhase;

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

                }

                if (clientView.getGame() == null) {
                    out.println(userInput);

                } else switch (userInput) {
                    case "help" -> {
                        System.out.println("Supported commands are: 'status', 'show', 'card'.\nIf you're looking for action-specific commands, type 'actions'");

                    }
                    case "status" -> {
                        System.out.println("\n" + clientView.getGame());

                    }
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
                                if (clientView.getLorenzo() == null)
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
                    case "card", "production" -> {
                        System.out.println("Specify the card you want to see: ");
                        int cardId = getInt();

                        //TODO do not hardcode base production power
                        if (cardId == 0) {
                            String content = "";
                            content += Color.HEADER + "Base production:" + Color.RESET;

                            content += "\n Production Input: ";
                            content += " " + ResourceType.UNKNOWN.iconPrint() + " x " + 2 + "  ";

                            content += "\n Production Output: ";
                            content += " " + ResourceType.UNKNOWN.iconPrint() + " x " + 1 + "  ";

                            System.out.println(content);
                        } else {
                            try {
                                System.out.println(clientView.getCardTable().getDevelopmentCardFromId(cardId));
                            } catch (CardNotPresentException ex) {
                                try {
                                    System.out.println(clientView.getGame().getLeaderCardFromId(cardId));
                                } catch (CardNotPresentException ex1) {
                                    System.out.println("The specified ID does not match any Development or Leader Cards");
                                }
                            }
                        }

                    }
                    case "actions" -> {
                        System.out.println("If you want to see all the possible actions in the game, type 'actions -all'");

                        TurnPhase phase = clientView.getGame().getTurnPhase();
                        switch (phase) {
                            case LEADERCHOICE -> {
                                System.out.println(Color.AQUA_GREEN_FG + "We're in " + phase + Color.AQUA_GREEN_FG + " and you can perform one of the following actions" + Color.RESET);
                                System.out.println("'0'  OR 'chooseBonusResourceType' - Choose which bonus resource you want to obtain (only at the beginning of the game)");
                                System.out.println("'1'  OR 'chooseLeaderCard' - Choose which LeaderCard you want to keep. You can choose 2 out of 4 cards (only at the beginning of the game)");
                                System.out.println("'6'  OR 'sendResourceToDepot' - Send a Resource you obtained to a depot");
                                System.out.println("'18' OR 'endTurn' - Confirm your choices and start your first turn");
                            }
                            case ACTIONSELECTION -> {
                                System.out.println(Color.AQUA_GREEN_FG + "We're in " + phase + Color.AQUA_GREEN_FG + " and you can perform one of the following actions" + Color.RESET);
                                System.out.println("'2'  OR 'playLeaderCard' - Activate a LeaderCard");
                                System.out.println("'3'  OR 'discardLeaderCard' - Discard a LeaderCard to get 1 bonus faith point");
                                System.out.println("'4'  OR 'selectMarketRow' - Choose the Market row that you want to get the Resources from");
                                System.out.println("'5'  OR 'selectMarketColumn' - Choose the Market column that you want to get the Resources from");
                                System.out.println("'10' OR 'takeDevelopmentCard' - Take a DevelopmentCard from the CardTable (you can later specify where to pay from or you can end the turn and activate auto-payment)");
                                System.out.println("'11' OR 'selectProduction' - Select a Production you want to activate");
                                System.out.println("'12' OR 'resetProductionChoice' - Unselects all the Productions so that you can choose again to make a different move");
                                System.out.println("'13' OR 'confirmProductionChoice' - When you're finished choosing your Productions and you've defined all of your jolly input/output you can confirm your choice");
                                System.out.println("'14' OR 'chooseJollyInput' - Choose the Resource you want to use to activate the Productions (only if you have JOLLY resources in your input list)");
                                System.out.println("'15' OR 'chooseJollyOutput' - Choose the Resource you want to obtain by activating the Productions (only if you have JOLLY resources in your output list)");
                            }
                            case MARKETDISTRIBUTION -> {
                                System.out.println(Color.AQUA_GREEN_FG + "We're in " + phase + Color.AQUA_GREEN_FG + " and you can perform one of the following actions" + Color.RESET);
                                System.out.println("'2'  OR 'playLeaderCard' - Activate a LeaderCard");
                                System.out.println("'3'  OR 'discardLeaderCard' - Discard a LeaderCard to get 1 bonus faith point");
                                System.out.println("'6'  OR 'sendResourceToDepot' - Send a Resource you obtained to a depot");
                                System.out.println("'7'  OR 'chooseMarbleConversion' - Choose which marble conversion to apply to the White Orb you picked from the Market");
                                System.out.println("'8'  OR 'swapDepotContent' - Swap the content of 2 depots");
                                System.out.println("'9'  OR 'moveDepotContent' - Move the content of a depot to another depot");
                            }
                            case CARDPAYMENT, PRODUCTIONPAYMENT -> {
                                System.out.println(Color.AQUA_GREEN_FG + "We're in " + phase + Color.AQUA_GREEN_FG + " and you can perform one of the following actions" + Color.RESET);
                                System.out.println("'2'  OR 'playLeaderCard' - Activate a LeaderCard");
                                System.out.println("'3'  OR 'discardLeaderCard' - Discard a LeaderCard to get 1 bonus faith point");
                                System.out.println("'16' OR 'payFromWarehouse' - Choose which Resource to pay from the Warehouse (if you don't want to activate auto-payment)");
                                System.out.println("'17' OR 'payFromStrongbox' - Choose which Resource to pay from the Strongbox (if you don't want to activate auto-payment)");
                                System.out.println("'18' OR 'endTurn' - Enable auto-payment and end your turn");
                            }
                        }

                    }
                    case "actions -all" -> {
                        System.out.println("'0'  OR 'chooseBonusResourceType' - Choose which bonus resource you want to obtain (only at the beginning of the game)");
                        System.out.println("'1'  OR 'chooseLeaderCard' - Choose which LeaderCard you want to keep. You can choose 2 out of 4 cards (only at the beginning of the game)");
                        System.out.println("'2'  OR 'playLeaderCard' - Activate a LeaderCard");
                        System.out.println("'3'  OR 'discardLeaderCard' - Discard a LeaderCard to get 1 bonus faith point");
                        System.out.println("'4'  OR 'selectMarketRow' - Choose the Market row that you want to get the Resources from");
                        System.out.println("'5'  OR 'selectMarketColumn' - Choose the Market column that you want to get the Resources from");
                        System.out.println("'6'  OR 'sendResourceToDepot' - Send a Resource you obtained to a depot");
                        System.out.println("'7'  OR 'chooseMarbleConversion' - Choose which marble conversion to apply to the White Orb you picked from the Market");
                        System.out.println("'8'  OR 'swapDepotContent' - Swap the content of 2 depots");
                        System.out.println("'9'  OR 'moveDepotContent' - Move the content of a depot to another depot");
                        System.out.println("'10' OR 'takeDevelopmentCard' - Take a DevelopmentCard from the CardTable (you can later specify where to pay from or you can end the turn and activate auto-payment)");
                        System.out.println("'11' OR 'selectProduction' - Select a Production you want to activate");
                        System.out.println("'12' OR 'resetProductionChoice' - Unselects all the Productions so that you can choose again to make a different move");
                        System.out.println("'13' OR 'confirmProductionChoice' - When you're finished choosing your Productions and you've defined all of your jolly input/output you can confirm your choice");
                        System.out.println("'14' OR 'chooseJollyInput' - Choose the Resource you want to use to activate the Productions (only if you have JOLLY resources in your input list)");
                        System.out.println("'15' OR 'chooseJollyOutput' - Choose the Resource you want to obtain by activating the Productions (only if you have JOLLY resources in your output list)");
                        System.out.println("'16' OR 'payFromWarehouse' - Choose which Resource to pay from the Warehouse (if you don't want to activate auto-payment)");
                        System.out.println("'17' OR 'payFromStrongbox' - Choose which Resource to pay from the Strongbox (if you don't want to activate auto-payment)");
                        System.out.println("'18' OR 'endTurn' - end your turn");


                    }
                    case "chooseBonusResourceType", "0" -> {
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
                    case "chooseLeaderCard", "1" -> {
                        System.out.println("Action chooseLeaderCard selected");
                        Map<String, Object> map = new HashMap<>();

                        System.out.println("Which LeaderCard?");
                        int leaderCardSelected = getInt();
                        map.put("number", leaderCardSelected);

                        System.out.println("LeaderCard selected: " + leaderCardSelected);
                        Command command = new Command(UserCommandsType.chooseLeaderCard, map);
                        out.println(gson.toJson(command));


                    }
                    case "playLeaderCard", "2" -> {
                        System.out.println("Action playLeaderCard selected");
                        Map<String, Object> map = new HashMap<>();

                        System.out.println("Which LeaderCard?");
                        int leaderCardSelected = getInt();
                        map.put("number", leaderCardSelected);

                        System.out.println("LeaderCard selected: " + leaderCardSelected);
                        Command command = new Command(UserCommandsType.playLeaderCard, map);
                        out.println(gson.toJson(command));


                    }
                    case "discardLeaderCard", "3" -> {
                        System.out.println("Action discardLeaderCard selected");
                        Map<String, Object> map = new HashMap<>();

                        System.out.println("Which LeaderCard?");
                        int leaderCardSelected = getInt();
                        map.put("number", leaderCardSelected);

                        System.out.println("LeaderCard selected: " + leaderCardSelected);
                        Command command = new Command(UserCommandsType.discardLeaderCard, map);
                        out.println(gson.toJson(command));


                    }
                    case "selectMarketRow", "4" -> {
                        System.out.println("Action selectMarketRow selected");
                        Map<String, Object> map = new HashMap<>();

                        System.out.println("Which row?");
                        int rowSelected = getInt();
                        map.put("number", rowSelected);

                        System.out.println("Market's row selected: " + rowSelected);
                        Command command = new Command(UserCommandsType.selectMarketRow, map);
                        out.println(gson.toJson(command));


                    }
                    case "selectMarketColumn", "5" -> {
                        System.out.println("Action selectMarketColumn selected");
                        Map<String, Object> map = new HashMap<>();

                        System.out.println("Which column?");
                        int columnSelected = getInt();
                        map.put("number", columnSelected);

                        System.out.println("Market's column selected: " + columnSelected);
                        Command command = new Command(UserCommandsType.selectMarketColumn, map);
                        out.println(gson.toJson(command));


                    }
                    case "sendResourceToDepot", "6" -> {
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
                    case "chooseMarbleConversion", "7" -> {
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
                    case "swapDepotContent", "8" -> {
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
                    case "moveDepotContent", "9" -> {
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
                    case "takeDevelopmentCard", "10" -> {
                        System.out.println("Action takeDevelopmentCard selected");
                        Map<String, Object> map = new HashMap<>();

                        System.out.println("Which color?");
                        CardColor colorSelected = getColor();
                        map.put("color", colorSelected);

                        System.out.println("Which level?");
                        int levelSelected = getInt();
                        map.put("level", levelSelected);

                        System.out.println("To put in which slot?");
                        int numberSelected = getInt();
                        map.put("number", numberSelected);

                        System.out.println("Color: " + colorSelected);
                        System.out.println("Level: " + levelSelected);
                        System.out.println("Color: " + numberSelected);
                        Command command = new Command(UserCommandsType.takeDevelopmentCard, map);
                        out.println(gson.toJson(command));


                    }
                    case "selectProduction", "11" -> {
                        System.out.println("Action selectProduction selected");
                        Map<String, Object> map = new HashMap<>();

                        System.out.println("Which Production?");
                        int productionSelected = getInt();
                        map.put("number", productionSelected);

                        System.out.println("Production selected: " + productionSelected);
                        Command command = new Command(UserCommandsType.selectProduction, map);
                        out.println(gson.toJson(command));


                    }
                    case "resetProductionChoice", "12" -> {
                        System.out.println("Action resetProductionChoice selected");
                        System.out.println("Resetting production...");

                        Command command = new Command(UserCommandsType.resetProductionChoice, null);
                        out.println(gson.toJson(command));


                    }
                    case "confirmProductionChoice", "13" -> {
                        System.out.println("Action confirmProductionChoice selected");
                        System.out.println("Confirming production...!");

                        Command command = new Command(UserCommandsType.confirmProductionChoice, null);
                        out.println(gson.toJson(command));


                    }
                    case "chooseJollyInput", "14" -> {
                        System.out.println("Action chooseJollyInput selected");
                        Map<String, Object> map = new HashMap<>();

                        System.out.println("Which Resource?");
                        ResourceType resourceSelected = getResourceType();
                        map.put("resource", resourceSelected);

                        System.out.println("Resource selected: " + resourceSelected);
                        Command command = new Command(UserCommandsType.chooseJollyInput, map);
                        out.println(gson.toJson(command));

                    }
                    case "chooseJollyOutput", "15" -> {
                        System.out.println("Action chooseJollyOutput selected");
                        Map<String, Object> map = new HashMap<>();

                        System.out.println("Which Resource?");
                        ResourceType resourceSelected = getResourceType();
                        map.put("resource", resourceSelected);

                        System.out.println("Resource selected: " + resourceSelected);
                        Command command = new Command(UserCommandsType.chooseJollyOutput, map);
                        out.println(gson.toJson(command));

                    }
                    case "payFromWarehouse", "16" -> {
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
                    case "payFromStrongbox", "17" -> {
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
                    case "endTurn", "18" -> {
                        System.out.println("Action endTurn selected");

                        Command command = new Command(UserCommandsType.endTurn, null);
                        out.println(gson.toJson(command));

                    }
                    default -> {
                        if (clientView.getGame() == null)
                            out.println(userInput);
                        else
                            System.out.println("Uh oh, I couldn't catch your command. Type 'actions' for a list of possible commands");
                    }
                }

            } catch (IOException ex) {
                System.out.println("Uh oh, IO exception when reading from stdIn.");

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
