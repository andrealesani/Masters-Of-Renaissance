package client.CLI;

import Exceptions.CardNotPresentException;
import client.ClientView;
import com.google.gson.Gson;
import model.CardColor;
import model.Color;
import model.resource.ResourceType;
import model.TurnPhase;
import network.Command;
import network.UserCommandsType;
import network.beans.GameBean;
import network.beans.PlayerBoardBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * This class is run on a separate thread by the CLI class, and is used to handle player inputs
 */
public class CLIWriter implements Runnable {
    /**
     * The buffer used to process player inputs from the keyboard
     */
    private final BufferedReader stdIn;
    /**
     * The printer used to send messages to the server
     */
    private final PrintWriter out;
    /**
     * The object used to store the client's game data
     */
    private final ClientView clientView;
    /**
     * The CountDownLatch reference used to signal to the CLI class when this thread finishes running
     */
    private final CountDownLatch latch;
    /**
     * Attribute used by the CLI class to signal when this thread should stop running
     */
    private boolean doClose;
    /**
     * Object used to serialize messages for the server
     */
    private final Gson gson;

    //CONSTRUCTORS

    /**
     * The class constructor
     *
     * @param stdIn      the buffer used to process player input
     * @param out        the writer used to send messages to the server
     * @param ClientView the object used to store the client's game data
     * @param latch      the latch used to keep count of the CLI's running threads
     */
    public CLIWriter(BufferedReader stdIn, PrintWriter out, ClientView ClientView, CountDownLatch latch) {
        this.stdIn = stdIn;
        this.out = out;
        this.clientView = ClientView;
        this.latch = latch;
        this.doClose = false;
        this.gson = new Gson();
    }

    //MULTITHREADING METHODS

    //TODO for multi-stage commands insert a strng like 'cancel' that can be used to interrupt

    /**
     * The method used to run this class in multithreading.
     * Initiates a loop which reads and processes the player's input
     */
    public void run() {

        String userInput;

        //Loop which handles the player's input
        while (!doClose) {
            try {
                userInput = stdIn.readLine();

                //String used to terminate the connection
                if (userInput.equals("ESC + :q")) {
                    out.println(userInput);
                    System.out.println("Closing connection...");
                }

                if (clientView.getGame() == null) {
                    //If the game has not begun yet, directly forward the user's input to the server
                    out.println(userInput);
                } else
                    //Otherwise, elaborate the user's input
                    elaborateInput(userInput);

            } catch (IOException ex) {
                System.out.println("Uh oh, IO exception when reading from stdIn.");
            }
        }

        //Signals the ending of the writer thread to the CLI class
        latch.countDown();
    }

    /**
     * Signals to the class that the 'run' method should be interrupted
     */
    public void doClose() {
        doClose = true;
        System.out.println("Server connection lost, press any key to terminate.");
    }

    //PRIVATE ELABORATION

    /**
     * Elaborates the user's input after the start of the game
     *
     * @param userInput the user's input
     * @throws IOException if an I/O error occurs
     */
    private void elaborateInput(String userInput) throws IOException {
        switch (userInput) {

            //Display the player's commands options
            case "help" -> printHelp();

            //Display the game's status
            case "status" -> printStatus();

            //Display a specific game element
            case "show" -> printShow();

            //Display a specific card or production
            case "card", "production" -> printCard();

            //Display a specific player's LeaderCards
            case "leadercards" -> printLeaderCards();

            //Display the list of available actions for the player in the current turn phase
            case "actions" -> printActions();

            //The following methods are used to run game actions

            case "chooseBonusResourceType", "0" -> runChooseBonusResourceType();

            case "chooseLeaderCard", "1" -> runChooseLeaderCard();

            case "playLeaderCard", "2" -> runPlayLeaderCard();

            case "discardLeaderCard", "3" -> runDiscardLeaderCard();

            case "selectMarketRow", "4" -> runSelectMarketRow();

            case "selectMarketColumn", "5" -> runSelectMarketColumn();

            case "sendResourceToDepot", "6" -> runSendResourceToDepot();

            case "chooseMarbleConversion", "7" -> runChooseMarbleConversion();

            case "swapDepotContent", "8" -> runSwapDepotContent();

            case "moveDepotContent", "9" -> runMoveDepotContent();

            case "takeDevelopmentCard", "10" -> runTakeDevelopmentCard();

            case "selectProduction", "11" -> runSelectProduction();

            case "resetProductionChoice", "12" -> runResetProductionChoice();

            case "confirmProductionChoice", "13" -> runConfirmProductionChoice();

            case "chooseJollyInput", "14" -> runChooseJollyInput();

            case "chooseJollyOutput", "15" -> runChooseJollyOutput();

            case "payFromWarehouse", "16" -> runPayFromWarehouse();

            case "payFromStrongbox", "17" -> runPayFromStrongbox();

            case "endTurn", "18" -> runEndTurn();

            default -> {
                if (clientView.getGame() == null)
                    out.println(userInput);
                else
                    System.out.println("This command is not supported. Press 'help' for a list of all available commands.");
            }

        }
    }

    //PRIVATE PRINTING METHODS

    /**
     * Displays the player's command options
     */
    private void printHelp() {
        System.out.println(
                "Supported commands are: " +
                        "\n" +
                        "\n- 'status': show the player who is currently taking their turn and the turn phase" +
                        "\n- 'show': display a specific game element" +
                        "\n- 'card': display a card's details" +
                        "\n- 'production: display a production's card" +
                        "\n- 'leadercards': display a player's leadercards" +
                        "\n" +
                        "\nIf you're looking for in-game action commands, type 'actions'.");
    }

    /**
     * Displays the game's status
     */
    private void printStatus() {
        System.out.println("\n" + clientView.getGame());
    }

    /**
     * Displays a specific game element
     *
     * @throws IOException if an I/O error occurs
     */
    private void printShow() throws IOException {
        System.out.println("Specify what you want to see, or press ENTER to show all elements:");

        System.out.println(
                "Supported show commands:" +
                        "\n- 'market': the game's market board" +
                        "\n- 'cardtable': the game's card table" +
                        "\n- 'playerboard', 'strongbox', 'waitingroom', 'warehouse')");

        if (clientView.getLorenzo() != null)
            System.out.println("- 'lorenzo': the state of the game's AI");

        String request = stdIn.readLine();

        switch (request) {
            case "" -> System.out.println("\n" + clientView);

            case "market" -> System.out.println("\n" + clientView.getMarket());

            case "cardtable" -> System.out.println("\n" + clientView.getCardTable());

            case "lorenzo" -> {
                if (clientView.getLorenzo() == null)
                    System.out.println("\n" + "This command is not supported.");
                else
                    System.out.println("\n" + clientView.getLorenzo());
            }

            case "playerboard" -> System.out.println("\n" + clientView.getPlayerBoards());

            case "strongbox" -> System.out.println("\n" + clientView.getStrongboxes());

            case "waitingroom" -> System.out.println("\n" + clientView.getWaitingRooms());

            case "warehouse" -> System.out.println("\n" + clientView.getWarehouses());

            default -> System.out.println("\n" + "This command is not supported.");
        }
    }

    //TODO do not hardcode default production

    /**
     * Displays one of the game's cards
     *
     * @throws IOException if an I/O error occurs
     */
    private void printCard() throws IOException {
        System.out.println("Specify the card ID you wish to see: ");
        int cardId = getInt();

        if (cardId == 0) {
            String content = "";
            content += Color.HEADER + "Base production:" + Color.RESET;

            content += "\n Production Input: ";
            content += " " + ResourceType.JOLLY.iconPrint() + " x " + 2 + "  ";

            content += "\n Production Output: ";
            content += " " + ResourceType.JOLLY.iconPrint() + " x " + 1 + "  ";

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

    /**
     * Displays one of the game's players' LeaderCards
     *
     * @throws IOException if an I/O error occurs
     */
    private void printLeaderCards() throws IOException {
        System.out.println("Specify the username of the player whose cards you wish to see:");

        String username = stdIn.readLine();

        PlayerBoardBean playerBoard = clientView.getPlayerBoard(username);
        if (playerBoard == null) {
            System.out.println("The given player does not exist.");
            return;
        }

        for (int id : playerBoard.getLeaderCards()) {
            try {
                System.out.println(clientView.getGame().getLeaderCardFromId(id));
            } catch (Exception ignored) {
                System.out.println("Couldn't find one of the player's leader cards.");
            }
        }
    }

    /**
     * Displays the actions available to the player depending on the current turn phase
     */
    private void printActions() {
        GameBean gameBean = clientView.getGame();

        if (!clientView.getUsername().equals(gameBean.getCurrentPlayer())) {
            System.out.println("It is not your turn to act.");
            return;
        }

        TurnPhase phase = gameBean.getTurnPhase();

        switch (phase) {
            case LEADERCHOICE -> System.out.println(Color.AQUA_GREEN_FG + "We're in " + phase + Color.AQUA_GREEN_FG + " and you can perform one of the following actions" + Color.RESET +
                    "\n- '0'  OR 'chooseBonusResourceType' - Choose which bonus resource you want to obtain (only at the beginning of the game)" +
                    "\n- '1'  OR 'chooseLeaderCard' - Choose which LeaderCard you want to keep. You can choose 2 out of 4 cards (only at the beginning of the game" +
                    "\n- '6'  OR 'sendResourceToDepot' - Send a Resource you obtained to a depot" +
                    "\n- '18' OR 'endTurn' - Confirm your choices and start your first turn");

            case ACTIONSELECTION -> System.out.println(Color.AQUA_GREEN_FG + "We're in " + phase + Color.AQUA_GREEN_FG + " and you can perform one of the following actions" +
                    "\n- '2'  OR 'playLeaderCard' - Activate a LeaderCard" +
                    "\n- '3'  OR 'discardLeaderCard' - Discard a LeaderCard to get 1 bonus faith point" +
                    "\n- '4'  OR 'selectMarketRow' - Choose the Market row that you want to get the Resources from" +
                    "\n- '5'  OR 'selectMarketColumn' - Choose the Market column that you want to get the Resources from" +
                    "\n- '10' OR 'takeDevelopmentCard' - Take a DevelopmentCard from the CardTable (you can later specify where to pay from or you can end the turn and activate auto-payment)" +
                    "\n- '11' OR 'selectProduction' - Select a Production you want to activate" +
                    "\n- '12' OR 'resetProductionChoice' - Unselects all the Productions so that you can choose again to make a different move" +
                    "\n- '13' OR 'confirmProductionChoice' - When you're finished choosing your Productions and you've defined all of your jolly input/output you can confirm your choice" +
                    "\n- '14' OR 'chooseJollyInput' - Choose the Resource you want to use to activate the Productions (only if you have JOLLY resources in your input list)" +
                    "\n- '15' OR 'chooseJollyOutput' - Choose the Resource you want to obtain by activating the Productions (only if you have JOLLY resources in your output list)");

            case MARKETDISTRIBUTION -> System.out.println(Color.AQUA_GREEN_FG + "We're in " + phase + Color.AQUA_GREEN_FG + " and you can perform one of the following actions" + Color.RESET +
                    "\n- '2'  OR 'playLeaderCard' - Activate a LeaderCard" +
                    "\n- '3'  OR 'discardLeaderCard' - Discard a LeaderCard to get 1 bonus faith point" +
                    "\n- '6'  OR 'sendResourceToDepot' - Send a Resource you obtained to a depot" +
                    "\n- '7'  OR 'chooseMarbleConversion' - Choose which marble conversion to apply to the White Orb you picked from the Market" +
                    "\n- '8'  OR 'swapDepotContent' - Swap the content of 2 depots" +
                    "\n- '9'  OR 'moveDepotContent' - Move the content of a depot to another depot");

            case CARDPAYMENT, PRODUCTIONPAYMENT -> System.out.println(Color.AQUA_GREEN_FG + "We're in " + phase + Color.AQUA_GREEN_FG + " and you can perform one of the following actions" + Color.RESET +
                    "\n- '2'  OR 'playLeaderCard' - Activate a LeaderCard" +
                    "\n- '3'  OR 'discardLeaderCard' - Discard a LeaderCard to get 1 bonus faith point" +
                    "\n- '16' OR 'payFromWarehouse' - Choose which Resource to pay from the Warehouse (if you don't want to activate auto-payment)" +
                    "\n- '17' OR 'payFromStrongbox' - Choose which Resource to pay from the Strongbox (if you don't want to activate auto-payment)" +
                    "\n- '18' OR 'endTurn' - Enable auto-payment and end your turn");

            default -> System.out.println("Couldn't determine the current turn phase.");
        }
    }

    //PRIVATE ACTION METHODS

    /**
     * Executes the chooseBonusResourceType command
     *
     * @throws IOException if an I/O error occurs
     */
    private void runChooseBonusResourceType() throws IOException {
        System.out.println("Action chooseBonusResourceType selected.");
        Map<String, Object> parameters = new HashMap<>();

        System.out.println("Which type of bonus resource do you want to obtain?");
        ResourceType resource = getResourceType();
        parameters.put("resource", resource);

        System.out.println("What amount of this resource do you wish to obtain?");
        int quantity = getInt();
        parameters.put("quantity", quantity);

        System.out.println("Choosing " + quantity + " bonus resources of type " + resource + ".");
        Command command = new Command(UserCommandsType.chooseBonusResourceType, parameters);
        out.println(gson.toJson(command));
    }

    /**
     * Executes the chooseLeaderCard command
     *
     * @throws IOException if an I/O error occurs
     */
    private void runChooseLeaderCard() throws IOException {
        System.out.println("Action chooseLeaderCard selected.");
        Map<String, Object> parameters = new HashMap<>();

        System.out.println("Which is the position of the leader card do you wish to select?");
        int number = getInt();
        parameters.put("number", number);

        System.out.println("Selecting leader card in position " + number + ".");
        Command command = new Command(UserCommandsType.chooseLeaderCard, parameters);
        out.println(gson.toJson(command));
    }

    /**
     * Executes the playLeaderCard command
     *
     * @throws IOException if an I/O error occurs
     */
    private void runPlayLeaderCard() throws IOException {
        System.out.println("Action playLeaderCard selected.");
        Map<String, Object> parameters = new HashMap<>();

        System.out.println("Which is the position of the leader card do you wish to play?");
        int number = getInt();
        parameters.put("number", number);

        System.out.println("Playing leader card in position " + number + ".");
        Command command = new Command(UserCommandsType.playLeaderCard, parameters);
        out.println(gson.toJson(command));
    }

    /**
     * Executes the discardLeaderCard command
     *
     * @throws IOException if an I/O error occurs
     */
    private void runDiscardLeaderCard() throws IOException {
        System.out.println("Action discardLeaderCard selected.");
        Map<String, Object> parameters = new HashMap<>();

        System.out.println("Which is the position of the leader card do you wish to discard?");
        int number = getInt();
        parameters.put("number", number);

        System.out.println("Discarding leader card in position " + number + ".");
        Command command = new Command(UserCommandsType.discardLeaderCard, parameters);
        out.println(gson.toJson(command));
    }

    /**
     * Executes the selectMarketRow command
     *
     * @throws IOException if an I/O error occurs
     */
    private void runSelectMarketRow() throws IOException {
        System.out.println("Action selectMarketRow selected.");
        Map<String, Object> parameters = new HashMap<>();

        System.out.println("Which market row do you wish to select?");
        int number = getInt();
        parameters.put("number", number);

        System.out.println("Selecting market row number " + number + ".");
        Command command = new Command(UserCommandsType.selectMarketRow, parameters);
        out.println(gson.toJson(command));
    }

    /**
     * Executes the selectMarketColumn command
     *
     * @throws IOException if an I/O error occurs
     */
    private void runSelectMarketColumn() throws IOException {
        System.out.println("Action selectMarketColumn selected.");
        Map<String, Object> parameters = new HashMap<>();

        System.out.println("Which market column do you wish to select?");
        int number = getInt();
        parameters.put("number", number);

        System.out.println("Selecting market column number " + number + ".");
        Command command = new Command(UserCommandsType.selectMarketColumn, parameters);
        out.println(gson.toJson(command));
    }

    /**
     * Executes the sendResourceToDepot command
     *
     * @throws IOException if an I/O error occurs
     */
    private void runSendResourceToDepot() throws IOException {
        System.out.println("Action sendResourceToDepot selected.");
        Map<String, Object> parameters = new HashMap<>();

        System.out.println("Which depot do you wish to send the resources to?");
        int number = getInt();
        parameters.put("number", number);

        System.out.println("Which type of resource do you wish to send to this depot?");
        ResourceType resource = getResourceType();
        parameters.put("resource", resource);

        System.out.println("What amount of this resource do you to send to the depot?");
        int quantity = getInt();
        parameters.put("quantity", quantity);

        System.out.println("Sending " + quantity + " resources of type " + resource + " to depot number " + number + ".");
        Command command = new Command(UserCommandsType.sendResourceToDepot, parameters);
        out.println(gson.toJson(command));
    }

    /**
     * Executes the chooseMarbleConversion command
     *
     * @throws IOException if an I/O error occurs
     */
    private void runChooseMarbleConversion() throws IOException {
        System.out.println("Action chooseMarbleConversion selected.");
        Map<String, Object> parameters = new HashMap<>();

        System.out.println("Which type of resource do you wish to convert the white marble to?");
        ResourceType resource = getResourceType();
        parameters.put("resource", resource);

        System.out.println("What amount of white marbles do you wish to convert to this resource?");
        int quantity = getInt();
        parameters.put("quantity", quantity);

        System.out.println("Converting " + quantity + " white marbles into resources of type " + resource + ".");
        Command command = new Command(UserCommandsType.chooseMarbleConversion, parameters);
        out.println(gson.toJson(command));
    }

    /**
     * Executes the swapDepotContent command
     *
     * @throws IOException if an I/O error occurs
     */
    private void runSwapDepotContent() throws IOException {
        System.out.println("Action swapDepotContent selected.");
        Map<String, Object> parameters = new HashMap<>();
        int[] depots = new int[2];

        System.out.println("What is the number of the first depot you wish to swap?");
        depots[0] = getInt();

        System.out.println("What is the number of the second depot you wish to swap?");
        depots[1] = getInt();
        parameters.put("depots", depots);

        System.out.println("Swapping depots number " + depots[0] + " and " + depots[1] + ".");
        Command command = new Command(UserCommandsType.swapDepotContent, parameters);
        out.println(gson.toJson(command));
    }

    /**
     * Executes the moveDepotContent command
     *
     * @throws IOException if an I/O error occurs
     */
    private void runMoveDepotContent() throws IOException {
        System.out.println("Action moveDepotContent selected.");
        Map<String, Object> parameters = new HashMap<>();
        int[] depots = new int[2];

        System.out.println("What is the number of the depot you wish to take the resources from?");
        depots[0] = getInt();

        System.out.println("What is the type of resource you wish to take from this depot?");
        ResourceType resource = getResourceType();
        parameters.put("resource", resource);

        System.out.println("What amount of this resource do you wish to take from the depot?");
        int quantity = getInt();
        parameters.put("quantity", quantity);

        System.out.println("What is the number of the depot you wish to send these resources to?");
        depots[1] = getInt();
        parameters.put("depots", depots);

        System.out.println("Sending " + quantity + " resources of type " + resource + " from depot number " + depots[0] + " to depot number " + depots[1] + ".");
        Command command = new Command(UserCommandsType.moveDepotContent, parameters);
        out.println(gson.toJson(command));
    }

    /**
     * Executes the takeDevelopmentCard command
     *
     * @throws IOException if an I/O error occurs
     */
    private void runTakeDevelopmentCard() throws IOException {
        System.out.println("Action takeDevelopmentCard selected.");
        Map<String, Object> parameters = new HashMap<>();

        System.out.println("What is the color of the card you wish to select?");
        CardColor color = getColor();
        parameters.put("color", color);

        System.out.println("What is the level of the card you wish to select?");
        int level = getInt();
        parameters.put("level", level);

        System.out.println("Which card slot do you wish to place this card into?");
        int number = getInt();
        parameters.put("number", number);

        System.out.println("Taking development card of color " + color + " and level " + level + " for slot number " + number + ".");
        Command command = new Command(UserCommandsType.takeDevelopmentCard, parameters);
        out.println(gson.toJson(command));
    }

    /**
     * Executes the selectProduction command
     *
     * @throws IOException if an I/O error occurs
     */
    private void runSelectProduction() throws IOException {
        System.out.println("Action selectProduction selected.");
        Map<String, Object> parameters = new HashMap<>();

        System.out.println("What is the position of the production you wish to activate?");
        int number = getInt();
        parameters.put("number", number);

        System.out.println("Selecting production number " + number + ".");
        Command command = new Command(UserCommandsType.selectProduction, parameters);
        out.println(gson.toJson(command));
    }

    /**
     * Executes the resetProductionChoice command
     */
    private void runResetProductionChoice() {
        System.out.println("Action resetProductionChoice selected.");

        System.out.println("Resetting production choice.");
        Command command = new Command(UserCommandsType.resetProductionChoice, null);
        out.println(gson.toJson(command));
    }

    /**
     * Executes the confirmProductionChoice command
     */
    private void runConfirmProductionChoice() {
        System.out.println("Action confirmProductionChoice selected.");

        System.out.println("Confirming production choice.");
        Command command = new Command(UserCommandsType.confirmProductionChoice, null);
        out.println(gson.toJson(command));
    }

    /**
     * Executes the chooseJollyInput command
     *
     * @throws IOException if an I/O error occurs
     */
    private void runChooseJollyInput() throws IOException {
        System.out.println("Action chooseJollyInput selected.");
        Map<String, Object> parameters = new HashMap<>();

        System.out.println("Which type of resource do you wish to convert the production input jolly into?");
        ResourceType resource = getResourceType();
        parameters.put("resource", resource);

        System.out.println("Choosing conversion to resource of type " + resource + " for one input jolly in currently selected productions.");
        Command command = new Command(UserCommandsType.chooseJollyInput, parameters);
        out.println(gson.toJson(command));
    }

    /**
     * Executes the chooseJollyOutput command
     *
     * @throws IOException if an I/O error occurs
     */
    private void runChooseJollyOutput() throws IOException {
        System.out.println("Action chooseJollyOutput selected.");
        Map<String, Object> parameters = new HashMap<>();

        System.out.println("Which type of resource do you wish to convert the production output jolly into?");
        ResourceType resource = getResourceType();
        parameters.put("resource", resource);

        System.out.println("Choosing conversion to resource of type " + resource + " for one output jolly in currently selected productions.");
        Command command = new Command(UserCommandsType.chooseJollyOutput, parameters);
        out.println(gson.toJson(command));
    }

    /**
     * Executes the payFromWarehouse command
     *
     * @throws IOException if an I/O error occurs
     */
    private void runPayFromWarehouse() throws IOException {
        System.out.println("Action payFromWarehouse selected.");
        Map<String, Object> parameters = new HashMap<>();

        System.out.println("Which depot do you wish to pay the resources from?");
        int number = getInt();
        parameters.put("number", number);

        System.out.println("Which type of resource do you wish to pay from this depot?");
        ResourceType resource = getResourceType();
        parameters.put("resource", resource);

        System.out.println("What amount of this resource do you wish to pay for?");
        int quantity = getInt();
        parameters.put("quantity", quantity);

        System.out.println("Paying " + quantity + " resources of type " + resource + " from depot number " + number + ".");
        Command command = new Command(UserCommandsType.payFromWarehouse, parameters);
        out.println(gson.toJson(command));
    }

    /**
     * Executes the payFromStrongbox command
     *
     * @throws IOException if an I/O error occurs
     */
    private void runPayFromStrongbox() throws IOException {
        System.out.println("Action payFromStrongbox selected.");
        Map<String, Object> parameters = new HashMap<>();

        System.out.println("Which type of resource do you wish to pay from the strongbox?");
        ResourceType resource = getResourceType();
        parameters.put("resource", resource);

        System.out.println("What amount of this resource do you wish to pay for?");
        int quantity = getInt();
        parameters.put("quantity", quantity);

        System.out.println("Paying " + quantity + " resources of type " + resource + " from the strongbox.");
        Command command = new Command(UserCommandsType.payFromStrongbox, parameters);
        out.println(gson.toJson(command));
    }

    /**
     * Executes the endTurn command
     */
    private void runEndTurn() {
        System.out.println("Action endTurn selected.");

        System.out.println("Ending the turn.");
        Command command = new Command(UserCommandsType.endTurn, null);
        out.println(gson.toJson(command));
    }

    //PRIVATE INPUT METHODS

    /**
     * Attempts to read an integer from command line
     *
     * @return the integer read from command line
     * @throws IOException if an I/O error occurs
     */
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

    /**
     * Attempts to read a CardColor from command line
     *
     * @return the CardColor read from command line
     * @throws IOException if an I/O error occurs
     */
    private CardColor getColor() throws IOException {
        CardColor result;
        while (true) {
            try {
                String text = stdIn.readLine();
                if (text == null) {
                    throw new IOException();
                }
                text = text.toUpperCase();
                result = CardColor.valueOf(text);
                break;
            } catch (IllegalArgumentException ex) {
                System.out.println("This string is not a color, retry.");
            }
        }
        return result;
    }

    /**
     * Attempts to read a ResourceType from command line
     *
     * @return the ResourceType read from command line
     * @throws IOException if an I/O error occurs
     */
    private ResourceType getResourceType() throws IOException {
        ResourceType result;
        while (true) {
            try {
                String text = stdIn.readLine();
                if (text == null) {
                    throw new IOException();
                }
                text = text.toUpperCase();
                result = ResourceType.valueOf(text);
                break;
            } catch (IllegalArgumentException ex) {
                System.out.println("This string is not a resource, retry.");
            }
        }
        return result;
    }
}
