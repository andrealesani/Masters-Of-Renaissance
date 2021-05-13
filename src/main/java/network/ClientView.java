package network;

import Exceptions.CardNotPresentException;
import Exceptions.ParametersNotValidException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import model.CardColor;
import model.card.DevelopmentCard;
import network.beans.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
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

    Map<CardColor, List<List<DevelopmentCard>>> developmentCards = new HashMap<>();

    // CONSTRUCTOR

    public ClientView() {
        int id = 17;
        List<List<DevelopmentCard>> greenCards = new ArrayList<>();
        List<List<DevelopmentCard>> blueCards = new ArrayList<>();
        List<List<DevelopmentCard>> yellowCards = new ArrayList<>();
        List<List<DevelopmentCard>> purpleCards = new ArrayList<>();

        // BLUE CARDS
        createDecksFromJSON("./src/main/java/persistence/cards/developmentcards/BlueCards.json", blueCards);
        developmentCards.put(CardColor.BLUE, blueCards);
        for (List<DevelopmentCard> deck : developmentCards.get(CardColor.BLUE))
            for (DevelopmentCard card : deck)
                card.setId(id++);

        // GREEN CARDS
        createDecksFromJSON("./src/main/java/persistence/cards/developmentcards/GreenCards.json", greenCards);
        developmentCards.put(CardColor.GREEN, greenCards);
        for(List<DevelopmentCard> deck : developmentCards.get(CardColor.GREEN))
            for(DevelopmentCard card : deck)
                card.setId(id++);

        // PURPLE CARDS
        createDecksFromJSON("./src/main/java/persistence/cards/developmentcards/PurpleCards.json", purpleCards);
        developmentCards.put(CardColor.PURPLE, purpleCards);
        for(List<DevelopmentCard> deck : developmentCards.get(CardColor.PURPLE))
            for(DevelopmentCard card : deck)
                card.setId(id++);

        // YELLOW CARDS
        createDecksFromJSON("./src/main/java/persistence/cards/developmentcards/YellowCards.json", yellowCards);
        developmentCards.put(CardColor.YELLOW, yellowCards);
        for(List<DevelopmentCard> deck : developmentCards.get(CardColor.YELLOW))
            for(DevelopmentCard card : deck)
                card.setId(id++);
    }

    // PRIVATE METHODS

    private void createDecksFromJSON(String jsonPath, List<List<DevelopmentCard>> colorCards) {
        Gson gson = new Gson();
        JsonReader reader = null;
        Type DevCardArray = new TypeToken<ArrayList<DevelopmentCard>>() {
        }.getType();

        try {
            reader = new JsonReader(new FileReader(jsonPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<DevelopmentCard> allColorCards = gson.fromJson(reader, DevCardArray);
        for (int i = 0; i < 3; i++) {
            colorCards.add(new ArrayList<DevelopmentCard>());
        }
        for (DevelopmentCard developmentCard : allColorCards) {
            if (developmentCard.getLevel() == 1)
                colorCards.get(0).add(developmentCard);
            else if (developmentCard.getLevel() == 2)
                colorCards.get(1).add(developmentCard);
            else if (developmentCard.getLevel() == 3)
                colorCards.get(2).add(developmentCard);
        }
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

    public DevelopmentCard getDevelopmentCardFromId(int id) throws CardNotPresentException {
        int j;
        for (Map.Entry<CardColor, List<List<DevelopmentCard>>> color : developmentCards.entrySet()) {
            for (List<DevelopmentCard> deck : color.getValue()) {
                for(DevelopmentCard developmentCard : deck) {
                    if(developmentCard.getId() == id) {
                        return developmentCard;
                    }
                }
            }
        }
        throw new CardNotPresentException();
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
