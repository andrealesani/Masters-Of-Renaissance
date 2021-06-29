package client;

import Exceptions.CardNotPresentException;
import model.Color;
import model.TurnPhase;
import model.card.leadercard.LeaderCard;
import network.beans.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the object used by the client to store game information retrieved from the server.
 * It makes use of beans to keep track of the current state of the game's elements.
 */
public class ClientView {
    /**
     * The username assigned to the client's player
     */
    private String username;
    /**
     * The bean storing general game and turn information
     */
    private GameBean game;
    /**
     * The bean storing market information
     */
    private MarketBean market;
    /**
     * The bean storing card table information
     */
    private CardTableBean cardTable;
    /**
     * The bean storing the game's AI's information
     */
    private LorenzoBean lorenzo;
    /**
     * The list of beans storing the players' boards' information
     */
    private List<PlayerBoardBean> playerBoards = new ArrayList<>() {
        @Override
        public String toString() {
            return printPlayerBeanList(playerBoards);
        }
    };
    /**
     * The list of beans storing the player's strongboxes' information
     */
    private List<StrongboxBean> strongboxes = new ArrayList<>() {
        @Override
        public String toString() {
            return printPlayerBeanList(strongboxes);
        }
    };
    /**
     * The list of beans storing the player's waiting rooms' information
     */
    private List<WaitingRoomBean> waitingRooms = new ArrayList<>() {
        @Override
        public String toString() {
            return printPlayerBeanList(waitingRooms);
        }
    };
    /**
     * The list of beans storing the player's warehouses' information
     */
    private List<WarehouseBean> warehouses = new ArrayList<>() {
        @Override
        public String toString() {
            return printPlayerBeanList(warehouses);
        }
    };
    /**
     * The list of beans storing the player's production handlers
     */
    private List<ProductionHandlerBean> productionHandlers = new ArrayList<>() {
        @Override
        public String toString() {
            return printPlayerBeanList(productionHandlers);
        }
    };


    //PUBLIC PRINTING METHODS

    /**
     * Prints a String representation of the class' data
     *
     * @return the String representation
     */
    @Override
    public String toString() {
        //Introduction
        String content = Color.VIEW + "View:" + Color.RESET +
                "\n";

        //Global game elements
        content += drawGlobalGameElements();

        //Player-specific game elements
        if (username != null)
            content += drawPlayerSpecificGameElements(username);

        //Game state
        if (game != null)
            content += game;


        return content;
    }

    //TODO non hardcodare numero di carte leader dopo selezione (anche in draw playerSpecific.. e GUI)

    /**
     * Creates a String that displays in detail the given player's leader cards, according to visibility rules
     *
     * @param username the username of the player whose leader cards are to be displayed
     * @return the formatted String
     */
    public String drawPlayerLeaderCards(String username) {

        PlayerBoardBean playerBoardBean = getPlayerBoard(username);

        //If the player's player board
        if (playerBoardBean == null)
            return "All information for player " + Color.RESOURCE_STD + username + Color.RESET + " has yet to arrive.";

        String content = Color.HEADER + username + "'s leader cards:" + Color.RESET +
                "\n\n";

        List<LeaderCard> shownLeaderCards = new ArrayList<>();
        int[] leaderCards = playerBoardBean.getLeaderCards();

        //If the player has no leader cards
        if (leaderCards.length <= 0) {
            content += " Player has no leader cards." + "\n";
            return content;
        }

        try {
            if (playerBoardBean.getUsername().equals(this.username)) {

                //If the player is the client, visualize all their cards
                for (int id : leaderCards)
                    shownLeaderCards.add(game.getLeaderCardFromId(id));

            } else {

                //If the player is not the client
                if (leaderCards.length <= 2) {

                    //If they have already chosen which cards to keep, visualize only the active ones
                    boolean[] activeLeaderCards = playerBoardBean.getActiveLeaderCards();
                    for (int i = 0; i < leaderCards.length; i++) {
                        if (activeLeaderCards[i]) {
                            shownLeaderCards.add(game.getLeaderCardFromId(leaderCards[i]));
                        }
                    }

                    //Say how many cards are hidden
                    content += " Player has " + (leaderCards.length - shownLeaderCards.size()) + " hidden leader cards." +
                            "\n\n";
                } else {

                    //If they have not yet chosen which cards to keep
                    content += " Player is still choosing their leader cards." + "\n";
                    return content;
                }
            }
        } catch (CardNotPresentException ex) {
            System.err.println("Couldn't find one of the player's leader cards.");
        }

        //Add the cards to visualize
        if (shownLeaderCards.size() > 0) {
            for (int i = 1; i <= 5; i++) {
                for (int j = 0; j < shownLeaderCards.size(); j++) {
                    content += fillBetweenColumns(content, 45 * j);
                    content += " " + shownLeaderCards.get(j).printLine(i);
                }
                content += "\n";
            }
        }

        return content;

    }

    /**
     * Creates a String that displays in detail the given player's card slots
     *
     * @param username the username of the player whose card slots are to be displayed
     * @return the formatted String
     */
    public String drawPlayerCardSlots(String username) {

        PlayerBoardBean playerBoardBean = getPlayerBoard(username);

        //If the player's player board
        if (playerBoardBean == null)
            return "All information for player " + Color.RESOURCE_STD + username + Color.RESET + " has yet to arrive.";

        String content = Color.HEADER + username + "'s card slots:" + Color.RESET +
                "\n\n";

        SlotBean[] slotBeans = playerBoardBean.getCardSlots();

        try {
            for (int i = 0; i < slotBeans.length; i++) {

                content += " " + Color.RESOURCE_STD + "Slot " + (i + 1) + ":" + Color.RESET +
                        "\n\n";

                int[] slotCardsId = slotBeans[i].getDevelopmentCards();

                if (slotCardsId.length > 0) {
                    for (int k = 1; k <= 7; k++) {
                        for (int j = 0; j < slotCardsId.length; j++) {
                            content += fillBetweenColumns(content, 45 * j);
                            content += " " + cardTable.getDevelopmentCardFromId(slotCardsId[j]).printLine(k);
                        }
                        content += "\n";
                    }
                } else
                    content += " Empty." + "\n";

                content += "\n";
            }
        } catch (CardNotPresentException ex) {
            System.err.println("Couldn't find one of the development cards.");
        }

        return content;

    }

    /**
     * Creates a String that displays in detail the game's card table
     *
     * @return the formatted String
     */
    public String drawDetailedCardTable() {

        String content = Color.HEADER + "CardTable:\n" + Color.RESET;

        int[][] cardsId = cardTable.getCards();

        try {
            for (int i = 0; i < cardsId.length; i++) {
                for (int k = 1; k <= 7; k++) {
                    for (int j = 0; j < cardsId[i].length; j++) {
                        content += fillBetweenColumns(content, 45 * j);
                        content += " " + cardTable.getDevelopmentCardFromId(cardsId[i][j]).printLine(k);
                    }
                    content += "\n";
                }
                content += "\n\n";
            }
        } catch (CardNotPresentException ex) {
            System.err.println("Couldn't find one of the development cards.");
        }

        return content;
    }

    /**
     * Creates a String that displays Market, CardTable and Lorenzo (if he exists) in parallel
     *
     * @return the formatted String
     */
    public String drawGlobalGameElements() {
        int offsetFirstColumn = 31;
        int offsetSecondColumn = 90;

        if (market == null || cardTable == null)
            return "All global game information has yet to arrive." + "\n";

        String content = "";

        //Row 1
        content += Color.HEADER + "Market: " + Color.RESET;
        content += fillBetweenColumns(content, offsetFirstColumn - 1) +
                Color.HEADER + "CardTable: " + Color.RESET;
        content += fillBetweenColumns(content, offsetSecondColumn);

        if (lorenzo != null)
            content += Color.HEADER + "Lorenzo: " + Color.RESET;

        content += "\n\n";

        //Row 2
        content += market.printLine(1);
        content += fillBetweenColumns(content, offsetFirstColumn) +
                    cardTable.printLine(1);
        content += fillBetweenColumns(content, offsetSecondColumn);

        if (lorenzo != null)
            content += lorenzo.printLine(1);

        content += "\n\n";

        //Row 3
        content += market.printLine(2);
        content += fillBetweenColumns(content, offsetFirstColumn) +
                cardTable.printLine(2);
        content += fillBetweenColumns(content, offsetSecondColumn);

        if (lorenzo != null)
            content += lorenzo.printLine(2);

        content += "\n\n";

        //Row 4
        content += market.printLine(3);
        content += fillBetweenColumns(content, offsetFirstColumn) +
                cardTable.printLine(3);
        content += fillBetweenColumns(content, offsetSecondColumn);

        if (lorenzo != null)
            content += lorenzo.printLine(3);

        content += "\n\n";

        //Row 5
        content += market.printLine(4) +
                "\n\n";

        return content;
    }

    /**
     * Creates a String that displays PlayerBoard, Strongbox, WaitingRoom and Warehouse in parallel
     *
     * @param username the username of the player to be displayed
     * @return the formatted String
     */
    public String drawPlayerSpecificGameElements(String username) {
        int offsetColumn = 90;

        PlayerBoardBean playerBoardBean = getPlayerBoard(username);
        WarehouseBean warehouseBean = getWarehouse(username);
        StrongboxBean strongboxBean = getStrongbox(username);
        ProductionHandlerBean productionHandlerBean = getProductionHandler(username);
        WaitingRoomBean waitingRoomBean = getWaitingRoom(username);

        if (playerBoardBean == null || warehouseBean == null || strongboxBean == null || productionHandlerBean == null || waitingRoomBean == null)
            return "All information for player " + Color.RESOURCE_STD + username + Color.RESET + " has yet to arrive." + "\n";

        String content = "";

        //Row 1
        content += Color.HEADER + playerBoardBean.getUsername() + "'s playerBoard: " + Color.RESET;
        content += fillBetweenColumns(content, offsetColumn) +
                Color.HEADER + "Warehouse: " + Color.RESET +
                "\n";

        //Row 2
        content += fillBetweenColumns(content, offsetColumn) +
                warehouseBean.printLine(1) +
                "\n";

        //Row 3
        content += playerBoardBean.printLine(1) +
                "\n";

        //Row 4
        content += playerBoardBean.printLine(2);
        content += fillBetweenColumns(content, offsetColumn) +
                Color.HEADER + "Strongbox: " + Color.RESET +
                "\n";

        //Row 5
        content += fillBetweenColumns(content, offsetColumn) +
                strongboxBean.printLine(1) +
                "\n";

        //Row 6
        content += playerBoardBean.printLine(3) +
                "\n";

        //Row 7
        content += playerBoardBean.printLine(4);
        content += fillBetweenColumns(content, offsetColumn) +
                Color.HEADER + "Available productions: " + Color.RESET +
                "\n";

        //Row 8
        content += playerBoardBean.printLine(5);
        content += fillBetweenColumns(content, offsetColumn) +
                productionHandlerBean.printLine(1) +
                "\n";

        //Row 9
        content += playerBoardBean.printLine(6);
        content += fillBetweenColumns(content, offsetColumn) +
                productionHandlerBean.printLine(2) +
                "\n";

        //Row 10
        content += playerBoardBean.printLine(7);
        content += fillBetweenColumns(content, offsetColumn) +
                productionHandlerBean.printLine(3) +
                "\n";

        //Row 11
        content += playerBoardBean.printLine(8) +
                "\n";

        //Row 12
        content += playerBoardBean.printLine(9);
        content += fillBetweenColumns(content, offsetColumn) +
                Color.HEADER;

        //determines the header for the waiting room
        if (game.getCurrentPlayer().equals(playerBoardBean.getUsername()))
            if (game.getTurnPhase() == TurnPhase.CARDPAYMENT || game.getTurnPhase() == TurnPhase.PRODUCTIONPAYMENT)
                content += "Resources left to pay: ";
            else if (game.getTurnPhase() == TurnPhase.MARKETDISTRIBUTION)
                content += "Resources left to distribute: ";
            else if (game.getTurnPhase() == TurnPhase.LEADERCHOICE)
                content += "Bonus resources left to allocate: ";

        content += Color.RESET +
                "\n";

        //Row 13
        if (playerBoardBean.getUsername().equals(this.username) || playerBoardBean.getLeaderCards().length <= 2)
            content += playerBoardBean.printLine(10, this.username);

        content += fillBetweenColumns(content, offsetColumn);

        if (game.getCurrentPlayer().equals(playerBoardBean.getUsername()) && game.getTurnPhase() != TurnPhase.ACTIONSELECTION)
            content += waitingRoomBean.printLine(1);

        content += "\n";

        return content;
    }

    //PRIVATE PRINTING METHODS

    /**
     * Adds 'space' characters to the last row of a given text so that the every row of the second column of the view you're trying
     * to build will have the right indentation.
     *
     * @param content the text to print. It needs to have at least one new-line character for the method to work
     * @param offset  the amount of characters between the left side of the screen and the beginning of the next line of text
     * @return the text with the correct spacing added
     */
    private String fillBetweenColumns(String content, int offset) {
        String space = "";
        //The regex is used to eliminate the special characters that would be counted in the string length
        int lineLen = content.replaceAll("(\\x9B|\\x1B\\[)[0-?]*[ -\\/]*[@-~]", "").length() - content.replaceAll("(\\x9B|\\x1B\\[)[0-?]*[ -\\/]*[@-~]", "").lastIndexOf('\n');
        for (int i = 0; i < offset - lineLen; i++)
            space += " ";
        return space;
    }

    //  PRIVATE METHODS

    /**
     * Adds or substitutes a PlayerBean for one of the game's players
     *
     * @param beanList the list of PlayerBeans to which to add or substitute the new bean
     * @param newBean  the bean to be added or substituted
     * @param <T>      the subclass of PlayerBean used
     */
    private <T extends PlayerBean> void setPlayerSpecificBean(List<T> beanList, T newBean) {
        String beanUsername = newBean.getUsername();
        for (int i = 0; i < beanList.size(); i++) {
            if (beanList.get(i).getUsername().equals(beanUsername)) {
                beanList.set(i, newBean);
                return;
            }
        }
        beanList.add(newBean);
    }

    /**
     * Returns the PlayerBean corresponding to a given username from a list of PlayerBeans
     *
     * @param beanList the list of PlayerBeans from which to return the PlayerBean
     * @param username the username of the player whose PlayerBean should be returned
     * @param <T>      the subclass of PlayerBean used
     * @return the PlayerBean corresponding to the given username, 'null' if there are none
     */
    private <T extends PlayerBean> T getPlayerSpecificBean(List<T> beanList, String username) {
        for (T bean : beanList) {
            if (bean.getUsername().equals(username))
                return bean;
        }
        return null;
    }

    /**
     * Calls the toString function for each PlayerBean in the list
     *
     * @param playerBeanList the list of PlayerBeans to print
     * @param <T>            the subclass of PlayerBean used
     * @return the string version of the PlayerBean list
     */
    public <T extends PlayerBean> String printPlayerBeanList(List<T> playerBeanList) {
        String string = "";
        for (T playerBean : playerBeanList) {
            string += playerBean.toString() + "\n";
        }
        return string;
    }

    // GETTERS

    /**
     * Getter for the object storing the general game state data
     *
     * @return the GameBean
     */
    public GameBean getGame() {
        return game;
    }

    /**
     * Getter for the object storing the market data
     *
     * @return the MarketBean
     */
    public MarketBean getMarket() {
        return market;
    }

    /**
     * Getter for the object storing the card table data
     *
     * @return the CardTableBean
     */
    public CardTableBean getCardTable() {
        return cardTable;
    }

    /**
     * Getter for the object storing the game's AI data
     *
     * @return the LorenzoBean
     */
    public LorenzoBean getLorenzo() {
        return lorenzo;
    }

    /**
     * Getter for the list of objects storing the player boards data
     *
     * @return the List of PlayerBoardBean
     */
    public List<PlayerBoardBean> getPlayerBoards() {
        return playerBoards;
    }

    /**
     * Getter for the object storing the player board data for a specific player
     *
     * @param username the username of the player whose player board should be returned
     * @return the given player's PlayerBoardBean, 'null' if there is not one
     */
    public PlayerBoardBean getPlayerBoard(String username) {
        return getPlayerSpecificBean(playerBoards, username);
    }

    /**
     * Getter for the list of objects storing the strongboxes data
     *
     * @return the List of StrongboxBean
     */
    public List<StrongboxBean> getStrongboxes() {
        return strongboxes;
    }

    /**
     * Getter for the object storing the strongbox data for a specific player
     *
     * @param username the username of the player whose strongbox should be returned
     * @return the given player's StrongboxBean, 'null' if there is not one
     */
    public StrongboxBean getStrongbox(String username) {
        return getPlayerSpecificBean(strongboxes, username);
    }

    /**
     * Getter for the list of objects storing the waiting rooms data
     *
     * @return the List of WaitingRoomBean
     */
    public List<WaitingRoomBean> getWaitingRooms() {
        return waitingRooms;
    }

    /**
     * Getter for the object storing the waiting room data for a specific player
     *
     * @param username the username of the player whose waiting room should be returned
     * @return the given player's WaitingRoomBean, 'null' if there is not one
     */
    public WaitingRoomBean getWaitingRoom(String username) {
        return getPlayerSpecificBean(waitingRooms, username);
    }

    /**
     * Getter for the list of objects storing the warehouses data
     *
     * @return the List of WarehouseBean
     */
    public List<WarehouseBean> getWarehouses() {
        return warehouses;
    }

    /**
     * Getter for the object storing the warehouse data for a specific player
     *
     * @param username the username of the player whose warehouse should be returned
     * @return the given player's WarehouseBean, 'null' if there is not one
     */
    public WarehouseBean getWarehouse(String username) {
        return getPlayerSpecificBean(warehouses, username);
    }

    /**
     * Getter for the list of objects storing the production handler data
     *
     * @return the List of ProductionHandlerBean
     */
    public List<ProductionHandlerBean> getProductionHandlers() {
        return productionHandlers;
    }

    /**
     * Getter for the object storing the production handler data for a specific player
     *
     * @param username the username of the player whose production handler should be returned
     * @return the given player's ProductionHandlerBean, 'null' if there is not one
     */
    public ProductionHandlerBean getProductionHandler(String username) {
        return getPlayerSpecificBean(productionHandlers, username);
    }

    /**
     * Getter for the username assigned to the client's player
     *
     * @return the player's username
     */
    public String getUsername() {
        return username;
    }

    // SETTERS

    /**
     * Setter for the object storing the general game state data
     *
     * @param game the GameBean containing the data
     */
    public void setGame(GameBean game) {
        this.game = game;
    }

    /**
     * Setter for the object storing the market data
     *
     * @param market the MarketBean containing the data
     */
    public void setMarket(MarketBean market) {
        this.market = market;
    }

    /**
     * Setter for the object storing the card table data
     *
     * @param cardTable the CardTableBean containing the data
     */
    public void setCardTable(CardTableBean cardTable) {
        this.cardTable = cardTable;
    }

    /**
     * Setter for the object storing the general game's AI data
     *
     * @param lorenzo the LorenzoBean containing the data
     */
    public void setLorenzo(LorenzoBean lorenzo) {
        this.lorenzo = lorenzo;
    }

    /**
     * Setter for the list of objects storing the player boards data
     *
     * @param playerBoards the List of PlayerBoardBean containing the data
     */
    public void setPlayerBoards(List<PlayerBoardBean> playerBoards) {
        this.playerBoards = playerBoards;
    }

    /**
     * Setter for the object storing a player's specific player board data
     *
     * @param newPlayerBoard the PlayerBoardBean containing the data
     */
    public void setPlayerBoard(PlayerBoardBean newPlayerBoard) {
        setPlayerSpecificBean(playerBoards, newPlayerBoard);
    }

    /**
     * Setter for the list of objects storing the strongboxes data
     *
     * @param strongboxes the StrongboxBean containing the data
     */
    public void setStrongboxes(List<StrongboxBean> strongboxes) {
        this.strongboxes = strongboxes;
    }

    /**
     * Setter for the object storing a player's specific strongbox data
     *
     * @param newStrongbox the StrongboxBean containing the data
     */
    public void setStrongbox(StrongboxBean newStrongbox) {
        setPlayerSpecificBean(strongboxes, newStrongbox);
    }

    /**
     * Setter for the list of objects storing the waiting rooms data
     *
     * @param waitingRooms the WaitingRoomBean containing the data
     */
    public void setWaitingRooms(List<WaitingRoomBean> waitingRooms) {
        this.waitingRooms = waitingRooms;
    }

    /**
     * Setter for the object storing a player's specific waiting room data
     *
     * @param newWaitingRoom the WaitingRoomBean containing the data
     */
    public void setWaitingRoom(WaitingRoomBean newWaitingRoom) {
        setPlayerSpecificBean(waitingRooms, newWaitingRoom);
    }

    /**
     * Setter for the list of objects storing the warehouses data
     *
     * @param warehouses the WarehouseBean containing the data
     */
    public void setWarehouses(List<WarehouseBean> warehouses) {
        this.warehouses = warehouses;
    }

    /**
     * Setter for the object storing a player's specific warehouse data
     *
     * @param newWarehouse the WarehouseBean containing the data
     */
    public void setWarehouse(WarehouseBean newWarehouse) {
        setPlayerSpecificBean(warehouses, newWarehouse);
    }

    /**
     * Setter for the list of objects storing the production handlers data
     *
     * @param productionHandlers the ProductionHandlerBean containing the data
     */
    public void setProductionHandlers(List<ProductionHandlerBean> productionHandlers) {
        this.productionHandlers = productionHandlers;
    }

    /**
     * Setter for the object storing a player's specific production handler data
     *
     * @param newProductionHandler the ProductionHandlerBean containing the data
     */
    public void setProductionHandler(ProductionHandlerBean newProductionHandler) {
        setPlayerSpecificBean(productionHandlers, newProductionHandler);
    }

    /**
     * Setter for the username chosen by the client's player.
     * To be used only once the username has been accepted by the server
     *
     * @param username the player's chosen username
     */
    public void setUsername(String username) {
        this.username = username;
    }

}
