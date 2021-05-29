package client;

import model.Color;
import model.TurnPhase;
import network.beans.*;

import java.util.ArrayList;
import java.util.List;

public class ClientView {
    private String username;
    private GameBean game;
    private MarketBean market;
    private CardTableBean cardTable;
    private LorenzoBean lorenzo;
    private List<PlayerBoardBean> playerBoards = new ArrayList<>() {
        @Override
        public String toString() {
            String string = "";
            for (PlayerBoardBean playerBoardBean : playerBoards) {
                string += playerBoardBean.toString() + "\n";
            }
            return string;
        }
    };
    private List<StrongboxBean> strongboxes = new ArrayList<>() {
        @Override
        public String toString() {
            String string = "";
            for (StrongboxBean strongboxBean : strongboxes) {
                string += strongboxBean.toString() + "\n";
            }
            return string;
        }
    };
    private List<WaitingRoomBean> waitingRooms = new ArrayList<>() {
        @Override
        public String toString() {
            String string = "";
            for (WaitingRoomBean waitingRoomBean : waitingRooms) {
                string += waitingRoomBean.toString() + "\n";
            }
            return string;
        }
    };
    private List<WarehouseBean> warehouses = new ArrayList<>() {
        @Override
        public String toString() {
            String string = "";
            for (WarehouseBean warehouseBean : warehouses) {
                string += warehouseBean.toString() + "\n";
            }
            return string;
        }
    };

    // PRIVATE METHODS

    /**
     * Creates a String that displays Market and CardTable in parallel
     *
     * @return the formatted String
     */
    private String marketAndCardTable() {
        String content = "";
        content += Color.HEADER + "Market: " + Color.RESET;
        content += fillBetweenColumns(content) + Color.HEADER + "CardTable: " + Color.RESET;
        content += "\n\n";
        content += market.printLine(1) + "                     " + cardTable.printLine(1);
        content += "\n\n";
        content += market.printLine(2) + "                     " + cardTable.printLine(2);
        content += "\n\n";
        content += market.printLine(3) + "                     " + cardTable.printLine(3);
        content += "\n\n";
        content += market.printLine(4);

        return content;
    }

    /**
     * Creates a String that displays Market, CardTable and Lorenzo in parallel
     *
     * @return the formatted String
     */
    private String marketAndCardTableAndLorenzo() {
        String content = "";
        content += Color.HEADER + "Market: " + Color.RESET + "                          ";
        content += Color.HEADER + "CardTable: " + Color.RESET + "                                             ";
        content += Color.HEADER + "Lorenzo: " + Color.RESET;
        content += "\n\n";
        content += market.printLine(1) + "                     " + cardTable.printLine(1) + "                     " + lorenzo.printLine(1);
        content += "\n\n";
        content += market.printLine(2) + "                     " + cardTable.printLine(2) + "                     " + lorenzo.printLine(2);
        content += "\n\n";
        content += market.printLine(3) + "                     " + cardTable.printLine(3) + "                     " + lorenzo.printLine(3);;
        content += "\n\n";
        content += market.printLine(4);

        return content;
    }

    /**
     * Creates a String that displays PlayerBoard, Strongbox, WaitingRoom and Warehouse in parallel
     *
     * @param i indicates the number of the player that the String needs to be created for
     * @return the formatted String
     */
    private String playerAndStrongAndWaitingAndWarehouse(int i) {
        String content = "";
        // first row
        content += Color.HEADER + playerBoards.get(i).getUsername() + "'s playerBoard: " + Color.RESET;
        content += fillBetweenColumns(content) + Color.HEADER;
        if (game.getTurnPhase() == TurnPhase.CARDPAYMENT || game.getTurnPhase() == TurnPhase.PRODUCTIONPAYMENT) {
            content += "Resources left to pay: ";
        } else if (game.getTurnPhase() == TurnPhase.MARKETDISTRIBUTION) {
            content += "Resources left to distribute: ";
        }
        content += Color.RESET + "\n";

        // second row
        content += playerBoards.get(i).printLine(1);
        content += fillBetweenColumns(content);
        if (game.getTurnPhase() == TurnPhase.CARDPAYMENT || game.getTurnPhase() == TurnPhase.PRODUCTIONPAYMENT || game.getTurnPhase() == TurnPhase.MARKETDISTRIBUTION) {
            content += waitingRooms.get(i).printLine(1);
        }
        content += "\n";
        // third row
        content += playerBoards.get(i).printLine(2);
        content += "\n";
        // fourth row
        content += playerBoards.get(i).printLine(3);

        content += fillBetweenColumns(content) + Color.HEADER + playerBoards.get(i).getUsername() +  "'s strongbox: " + Color.RESET + "\n";

        // fifth row
        content += playerBoards.get(i).printLine(4);
        content += fillBetweenColumns(content) + strongboxes.get(i).printLine(1) + "\n";
        // sixth row
        content += playerBoards.get(i).printLine(5);
        content += "\n";
        // seventh row
        content += playerBoards.get(i).printLine(6);
        content += fillBetweenColumns(content) + Color.HEADER + playerBoards.get(i).getUsername() +  "'s warehouse: " + Color.RESET + "\n";
        // eighth rows
        content += playerBoards.get(i).printLine(7);
        content += fillBetweenColumns(content) + warehouses.get(i).printLine(1) + "\n";
        // ninth row
        if (playerBoards.get(i).getUsername().equals(username))
            content += playerBoards.get(i).printLine(8);
        content += fillBetweenColumns(content) + warehouses.get(i).printLine(2) + "\n";
        // tenth row
        content += playerBoards.get(i).printLine(9) + "\n";

        return content;
    }

    /**
     * Dear Tom, hello and welcome to this lil precious jewel I crafted. What it does is adding 'space'
     * characters to the last row of a given text so that the every row of the second column of the view you're trying
     * to build will have the right indentation. Enjoy :)
     * P.S. I hope you'll appreciate the 69 constant that marks where the second column starts but you have the
     * permission to change it for the sake of our baby's prettiness
     *
     * @param content is the view you built until now. It needs to have at least one new-line character for the method to work
     * @return the right number of spaces :)
     */
    private String fillBetweenColumns(String content) {
        String space = "";
        //the regex is used to eliminate the special characters that would be counted in the string length
        int lineLen = content.replaceAll("(\\x9B|\\x1B\\[)[0-?]*[ -\\/]*[@-~]", "").length() - content.replaceAll("(\\x9B|\\x1B\\[)[0-?]*[ -\\/]*[@-~]", "").lastIndexOf('\n');
        for (int i = 0; i < 80 - lineLen; i++)
            space += " ";
        return space;
    }

    // GETTERS

    public GameBean getGame() {
        return game;
    }

    public MarketBean getMarket() {
        return market;
    }

    public CardTableBean getCardTable() {
        return cardTable;
    }

    public LorenzoBean getLorenzo() {
        return lorenzo;
    }

    public List<PlayerBoardBean> getPlayerBoards() {
        return playerBoards;
    }

    public List<StrongboxBean> getStrongboxes() {
        return strongboxes;
    }

    public List<WaitingRoomBean> getWaitingRooms() {
        return waitingRooms;
    }

    public List<WarehouseBean> getWarehouses() {
        return warehouses;
    }

    public String getUsername() {
        return username;
    }

    // SETTERS (other setters are not needed because the caller can use List<> setters)

    public void setGame(GameBean game) {
        this.game = game;
    }

    public void setMarket(MarketBean market) {
        this.market = market;
    }

    public void setCardTable(CardTableBean cardTable) {
        this.cardTable = cardTable;
    }

    public void setLorenzo(LorenzoBean lorenzo) {
        this.lorenzo = lorenzo;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //PRINTING METHODS

    @Override
    public String toString() {
        String content = Color.VIEW + "View:" + Color.RESET;
        if (market != null && cardTable != null)
            if (lorenzo != null)
                content += Color.RESET + "\n" + marketAndCardTableAndLorenzo() + "\n";
            else
                content += Color.RESET + "\n" + marketAndCardTable() + "\n";
        if (game != null && playerBoards.size() > 0 && strongboxes.size() > 0 && waitingRooms.size() > 0 && warehouses.size() > 0)
            for (int i = 0; i < playerBoards.size() && i < strongboxes.size() && i < waitingRooms.size() && i < warehouses.size(); i++) {
                if (playerBoards.get(i) != null && strongboxes.get(i) != null && waitingRooms.get(i) != null && warehouses.get(i) != null)
                    content += Color.RESET + "\n" + playerAndStrongAndWaitingAndWarehouse(i) + "\n";
            }
        if (game != null)
            content += Color.RESET + "\n" + game + "\n";

        return content + "\n";
    }
}
