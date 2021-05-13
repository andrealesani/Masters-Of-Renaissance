package network;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class ClientWriter implements Runnable {

    private final BufferedReader stdIn;
    private final PrintWriter out;
    private final ClientView clientView;
    private final CountDownLatch latch;

    //CONSTRUCTORS

    public ClientWriter(BufferedReader stdIn, PrintWriter out, ClientView clientView, CountDownLatch latch) {
        this.stdIn = stdIn;
        this.out = out;
        this.clientView = clientView;
        this.latch = latch;
    }

    //MULTITHREADING METHODS

    public void run() {
        Gson gson = new Gson();
        String userInput;
        while (true) {

            try {
                userInput = stdIn.readLine();
            } catch (IOException ex) {
                System.out.println("Uh oh, IO exception when reading from stdIn.");
                break;
            }

            if (userInput.equals("ESC + :q")) {
                out.println(userInput);
                System.out.println("Closing connection...");
                break;
            }

            if (userInput.contains("show")) {
                System.out.println("\n\n" + clientView);
            }

            else if (userInput.equals("chooseBonusResourceType")) {
                System.out.println("Action chooseBonusResourceType selected");
                Map<String, Object> map = new HashMap<>();

                System.out.println("Which Resource?");
                Scanner type = new Scanner(System.in);
                String resourceSelected = type.nextLine();

                map.put("resource", resourceSelected);

                System.out.println("How much?");
                Scanner quantity = new Scanner(System.in);
                int num = quantity.nextInt();

                map.put("quantity", num);

                System.out.println("Resource: " + resourceSelected + "\nBonus: " + num);
                Command command = new Command(UserCommandsType.chooseBonusResourceType, map);
                out.println(gson.toJson(command));

            } else if (userInput.equals("chooseLeaderCard")) {
                System.out.println("Action chooseLeaderCard selected");
                Map<String, Object> map = new HashMap<>();

                System.out.println("Which LeaderCard?");

                Scanner value = new Scanner(System.in);
                int leaderCardSelected = value.nextInt();

                map.put("number", leaderCardSelected);
                System.out.println("LeaderCard selected: " + leaderCardSelected);
                Command command = new Command(UserCommandsType.chooseLeaderCard, map);
                out.println(gson.toJson(command));

            } else if (userInput.equals("playLeaderCard")) {
                System.out.println("Action playLeaderCard selected");
                Map<String, Object> map = new HashMap<>();

                System.out.println("Which LeaderCard?");

                Scanner value = new Scanner(System.in);
                int leaderCardSelected = value.nextInt();

                map.put("number", leaderCardSelected);
                System.out.println("LeaderCard selected: " + leaderCardSelected);
                Command command = new Command(UserCommandsType.playLeaderCard, map);
                out.println(gson.toJson(command));

            } else if (userInput.equals("discardLeaderCard")) {
                System.out.println("Action discardLeaderCard selected");
                Map<String, Object> map = new HashMap<>();

                System.out.println("Which LeaderCard?");

                Scanner value = new Scanner(System.in);
                int leaderCardSelected = value.nextInt();

                map.put("number", leaderCardSelected);
                System.out.println("LeaderCard selected: " + leaderCardSelected);
                Command command = new Command(UserCommandsType.discardLeaderCard, map);
                out.println(gson.toJson(command));

            } else if (userInput.equals("selectMarketRow")) {
                System.out.println("Action selectMarketRow selected");
                Map<String, Object> map = new HashMap<>();

                System.out.println("Which row?");

                Scanner value = new Scanner(System.in);
                int rowSelected = value.nextInt();

                map.put("number", rowSelected);
                System.out.println("Market's row selected: " + rowSelected);
                Command command = new Command(UserCommandsType.selectMarketRow, map);
                out.println(gson.toJson(command));

            } else if (userInput.equals("selectMarketColumn")) {
                System.out.println("Action selectMarketColumn selected");
                Map<String, Object> map = new HashMap<>();

                System.out.println("Which row?");

                Scanner value = new Scanner(System.in);
                int columnSelected = value.nextInt();

                map.put("number", columnSelected);
                System.out.println("Market's column selected: " + columnSelected);
                Command command = new Command(UserCommandsType.selectMarketColumn, map);
                out.println(gson.toJson(command));

            } else if (userInput.equals("sendResourceToDepot")) {
                System.out.println("Action sendResourceToDepot selected");
                Map<String, Object> map = new HashMap<>();

                System.out.println("Which Depot?");
                Scanner value = new Scanner(System.in);
                int depotSelected = value.nextInt();

                map.put("number", depotSelected);

                System.out.println("Which Resource?");
                Scanner type = new Scanner(System.in);
                String resourceSelected = type.nextLine();

                map.put("resource", resourceSelected);

                System.out.println("How much?");
                Scanner quantity = new Scanner(System.in);
                int num = quantity.nextInt();

                map.put("quantity", num);

                System.out.println("Sending " + num + " " + resourceSelected + " to the " + depotSelected + " depot");
                Command command = new Command(UserCommandsType.sendResourceToDepot, map);
                out.println(gson.toJson(command));

            } else if (userInput.equals("chooseMarbleConversion")) {
                System.out.println("Action chooseMarbleConversion selected");
                Map<String, Object> map = new HashMap<>();

                System.out.println("Which Resource?");
                Scanner type = new Scanner(System.in);
                String resourceSelected = type.nextLine();

                map.put("resource", resourceSelected);

                System.out.println("How much?");
                Scanner quantity = new Scanner(System.in);
                int num = quantity.nextInt();

                map.put("quantity", num);

                System.out.println("Resource: " + resourceSelected + "\nQuantity: " + num);
                Command command = new Command(UserCommandsType.chooseMarbleConversion, map);
                out.println(gson.toJson(command));

            } else if (userInput.equals("swapDepotContent")) {
                System.out.println("Action swapDepotContent selected");
                Map<String, Object> map = new HashMap<>();
                int[] depots = new int[2];

                System.out.println("First depot?");
                Scanner firstDepot = new Scanner(System.in);
                depots[0] = firstDepot.nextInt();

                System.out.println("Second depot?");
                Scanner secondDepot = new Scanner(System.in);
                depots[1] = secondDepot.nextInt();

                map.put("depots", depots);

                System.out.println("The depot " + depots[0] + " was swapped with the depot " + depots[1]);
                Command command = new Command(UserCommandsType.swapDepotContent, map);
                out.println(gson.toJson(command));

            }

            else if (userInput.equals("moveDepotContent")) {
                System.out.println("Action moveDepotContent selected");
                Map<String, Object> map = new HashMap<>();
                int[] depots = new int[2];

                System.out.println("First depot?");
                Scanner firstDepot = new Scanner(System.in);
                depots[0] = firstDepot.nextInt();

                System.out.println("Which Resource?");
                Scanner type = new Scanner(System.in);
                String resourceSelected = type.nextLine();

                map.put("resource", resourceSelected);

                System.out.println("How much?");
                Scanner quantity = new Scanner(System.in);
                int num = quantity.nextInt();

                map.put("quantity", num);

                System.out.println("In which depot?");
                Scanner secondDepot = new Scanner(System.in);
                depots[1] = secondDepot.nextInt();

                map.put("depots", depots);

                System.out.println(num + " " + resourceSelected + " were moved from depot " + depots[0] + " to depot " + depots[1]);
                Command command = new Command(UserCommandsType.moveDepotContent, map);
                out.println(gson.toJson(command));

            }

            else if (userInput.equals("takeDevelopmentCard")) {
                System.out.println("Action takeDevelopmentCard selected");
                Map<String, Object> map = new HashMap<>();

                System.out.println("Which color?");
                Scanner type = new Scanner(System.in);
                String colorSelected = type.nextLine(); // forse meglio CardColor colorSelected = CardColor.valueOf(type.nextLine()); ???

                map.put("color", colorSelected);

                System.out.println("Which level?");
                Scanner levelSelected = new Scanner(System.in);
                int num1 = levelSelected.nextInt();

                map.put("level", num1);

                System.out.println("Which number?");
                Scanner numberSelected = new Scanner(System.in);
                int num2 = numberSelected.nextInt();

                map.put("level", num2);

                System.out.println("Color: " + colorSelected);
                System.out.println("Level: " + levelSelected);
                System.out.println("Color: " + numberSelected);
                Command command = new Command(UserCommandsType.takeDevelopmentCard, map);
                out.println(gson.toJson(command));
            }

            else if (userInput.equals("selectProduction")) {
                System.out.println("Action selectProduction selected");
                Map<String, Object> map = new HashMap<>();

                System.out.println("Which Production?");

                Scanner value = new Scanner(System.in);
                int productionSelected = value.nextInt();

                map.put("number", productionSelected);
                System.out.println("Production selected: " + productionSelected);
                Command command = new Command(UserCommandsType.selectProduction, map);
                out.println(gson.toJson(command));
            }

            else if (userInput.equals("resetProductionChoice")) {
                System.out.println("Action resetProductionChoice selected");
                System.out.println("Resetting production...");

                Command command = new Command(UserCommandsType.resetProductionChoice, null);
                out.println(gson.toJson(command));
            }

            else if (userInput.equals("confirmProductionChoice")) {
                System.out.println("Action confirmProductionChoice selected");
                System.out.println("Production Confirmed!");

                Command command = new Command(UserCommandsType.confirmProductionChoice, null);
                out.println(gson.toJson(command));
            }

            else if (userInput.equals("chooseJollyInput")) {
                System.out.println("Action chooseJollyInput selected");
                Map<String, Object> map = new HashMap<>();

                System.out.println("Which Resource?");
                Scanner type = new Scanner(System.in);
                String resourceSelected = type.nextLine();

                map.put("resource", resourceSelected);

                System.out.println("Resource selected: " + resourceSelected);
                Command command = new Command(UserCommandsType.chooseJollyInput, map);
                out.println(gson.toJson(command));
            }

            else if (userInput.equals("chooseJollyOutput")) {
                System.out.println("Action chooseJollyOutput selected");
                Map<String, Object> map = new HashMap<>();

                System.out.println("Which Resource?");
                Scanner type = new Scanner(System.in);
                String resourceSelected = type.nextLine();

                map.put("resource", resourceSelected);

                System.out.println("Resource selected: " + resourceSelected);
                Command command = new Command(UserCommandsType.chooseJollyOutput, map);
                out.println(gson.toJson(command));
            }

            else if (userInput.equals("payFromWarehouse")) {
                System.out.println("Action payFromWarehouse selected");
                Map<String, Object> map = new HashMap<>();

                System.out.println("Which Depot?");
                Scanner value = new Scanner(System.in);
                int depotSelected = value.nextInt();

                map.put("number", depotSelected);

                System.out.println("Which Resource?");
                Scanner type = new Scanner(System.in);
                String resourceSelected = type.nextLine();

                map.put("resource", resourceSelected);

                System.out.println("How much?");
                Scanner quantity = new Scanner(System.in);
                int num = quantity.nextInt();

                map.put("quantity", num);

                System.out.println("Received " + num + " " + resourceSelected + " from depot " + depotSelected);
                Command command = new Command(UserCommandsType.payFromWarehouse, map);
                out.println(gson.toJson(command));

            }

            else if (userInput.equals("payFromStrongbox")) {
                System.out.println("Action payFromStrongbox selected");
                Map<String, Object> map = new HashMap<>();

                System.out.println("Which Resource?");
                Scanner type = new Scanner(System.in);
                String resourceSelected = type.nextLine();

                map.put("resource", resourceSelected);

                System.out.println("How much?");
                Scanner quantity = new Scanner(System.in);
                int num = quantity.nextInt();

                map.put("quantity", num);

                System.out.println("Received " + num + " " + resourceSelected + " from Strongbox");
                Command command = new Command(UserCommandsType.payFromStrongbox, map);
                out.println(gson.toJson(command));
            }

            else
                out.println(userInput);

        }

        latch.countDown();
    }
}
