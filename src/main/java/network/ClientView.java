package network;

import network.beans.*;

import java.util.ArrayList;
import java.util.List;

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

    // SETTERS

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

    public void setPlayerBoards(List<PlayerBoardBean> playerBoards) {
        this.playerBoards = playerBoards;
    }

    public void setStrongboxes(List<StrongboxBean> strongboxes) {
        this.strongboxes = strongboxes;
    }

    public void setWaitingRooms(List<WaitingRoomBean> waitingRooms) {
        this.waitingRooms = waitingRooms;
    }

    public void setWarehouses(List<WarehouseBean> warehouses) {
        this.warehouses = warehouses;
    }

    @Override
    public String toString() {
        return "\u001B[34;1mView:\n" +
                "\u001B[0m\n" + game + "\n" +
                "\u001B[0m\n" + market + "\n" +
                "\u001B[0m\n" + cardTable + "\n" +
                "\u001B[0m\n" + lorenzo +
                "\u001B[0m\n" + playerBoards +
                "\u001B[0m\n" + strongboxes +
                "\u001B[0m\n" + waitingRooms +
                "\u001B[0m\n" + warehouses +
                "\u001B[0m\n";
    }
}
