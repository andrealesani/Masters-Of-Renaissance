package network;

import Exceptions.CardNotPresentException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import model.CardColor;
import model.Color;
import model.card.DevelopmentCard;
import network.beans.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientView {
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

    // CONSTRUCTOR

    public ClientView() {
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

    private String marketAndCardTable() {
        String content = "";
        String SPACING = "";
        for (int i = 0; i < 60; i++)
            SPACING += " ";
        content += Color.HEADER + "Market: " + Color.DEFAULT + SPACING + Color.HEADER + "CardTable: " + Color.DEFAULT;
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

    private String marketAndCardTableAndLorenzo() {
        String content = "";
        String SPACING = "";
        for (int i = 0; i < 60; i++)
            SPACING += " ";
        content += Color.HEADER + "Market: " + Color.DEFAULT + SPACING + Color.HEADER + "CardTable: " + Color.DEFAULT + "                                             " + Color.HEADER + "Lorenzo: " + Color.DEFAULT;
        content += "\n\n";
        content += market.printLine(1) + "                     " + cardTable.printLine(1) + "                     " + lorenzo.printLine(1);
        content += "\n\n";
        content += market.printLine(2) + "                     " + cardTable.printLine(2) + "                     " + lorenzo.printLine(2);
        content += "\n\n";
        content += market.printLine(3) + "                     " + cardTable.printLine(3) + "                     " + lorenzo.printLine(3);
        content += "\n\n";
        content += market.printLine(4);

        return content;
    }

    private String playerAndStrongAndWaitingAndWarehouse() {
        String content = "";
        String SPACING = "";
        for (int i = 0; i < 60; i++)
            SPACING += " ";
        content += Color.HEADER + "PlayerBoard: " + Color.DEFAULT + SPACING + Color.HEADER + "Strongbox: " + Color.DEFAULT;
        content += "\n";
        content += playerBoards.get(0).printLine(0) + "                     " + strongboxes.get(0).printLine(0);
        content += playerBoards.get(0).printLine(1) + "                     " + Color.HEADER + "WaitingRoom: " + Color.DEFAULT;
        content += playerBoards.get(0).printLine(2) + "                     " + waitingRooms.get(0).printLine(0);
        content += playerBoards.get(0).printLine(3);
        content += playerBoards.get(0).printLine(4);
        content += playerBoards.get(0).printLine(5);
        content += playerBoards.get(0).printLine(6);
        content += playerBoards.get(0).printLine(7);

        return content;
    }

    @Override
    public String toString() {
        String content = Color.VIEW + "View:" + Color.DEFAULT;
        if (game != null)
            content += Color.DEFAULT + "\n" + game + "\n";
        if (market != null && cardTable != null)
            if (lorenzo != null)
                content += Color.DEFAULT + "\n" + marketAndCardTableAndLorenzo() + "\n";
            else
                content += Color.DEFAULT + "\n" + marketAndCardTable() + "\n";
        if (playerBoards != null)
            content += Color.DEFAULT + "\n" + playerBoards;
        if (strongboxes != null)
            content += Color.DEFAULT + "\n" + strongboxes;
        if (waitingRooms != null)
            content += Color.DEFAULT + "\n" + waitingRooms;
        if (warehouses != null)
            content += Color.DEFAULT + "\n" + warehouses;

        return content + "\n";
    }
}
