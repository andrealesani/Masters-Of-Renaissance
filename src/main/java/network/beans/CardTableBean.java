package network.beans;

import Exceptions.CardNotPresentException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import model.*;
import model.Observer;
import model.card.DevelopmentCard;
import network.GameController;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;

public class CardTableBean implements Observer {
    /**
     * The Controller that will have to send the bean when it changes
     */
    private transient final GameController controller;
    /**
     * Holds the IDs of the cards in CardTable's cards attribute
     */
    private int[][] cards;
    /**
     * Map that holds the information about all the DevelopmentCards
     */
    private transient Map<CardColor, List<List<DevelopmentCard>>> developmentCards;

    // CONSTRUCTOR

    public CardTableBean(GameController controller) {
        this.controller = controller;
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

    public int[][] getCards() {
        return cards;
    }

    public DevelopmentCard getDevelopmentCardFromId(int id) throws CardNotPresentException {
        int j;
        for (Map.Entry<CardColor, List<List<DevelopmentCard>>> color : developmentCards.entrySet()) {
            for (List<DevelopmentCard> deck : color.getValue()) {
                for (DevelopmentCard developmentCard : deck) {
                    if (developmentCard.getId() == id) {
                        return developmentCard;
                    }
                }
            }
        }
        throw new CardNotPresentException();
    }

    // SETTERS

    private void setCardTableFromGame(CardTable cardTable) {
        cards = new int[3][cardTable.getCards().entrySet().size()];
        int i = 0, j;
        for (i = 0; i < cards.length; i++) {
            j = 0;
            for (Map.Entry<CardColor, List<List<DevelopmentCard>>> color : cardTable.getCards().entrySet()) {
                cards[i][j] = color.getValue().get(i).get(0).getId();
                j++;
            }
        }
    }

    private void setDevelopmentCardsFromJson() {
        developmentCards = new HashMap<>();
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
        for (List<DevelopmentCard> deck : developmentCards.get(CardColor.GREEN))
            for (DevelopmentCard card : deck)
                card.setId(id++);

        // PURPLE CARDS
        createDecksFromJSON("./src/main/java/persistence/cards/developmentcards/PurpleCards.json", purpleCards);
        developmentCards.put(CardColor.PURPLE, purpleCards);
        for (List<DevelopmentCard> deck : developmentCards.get(CardColor.PURPLE))
            for (DevelopmentCard card : deck)
                card.setId(id++);

        // YELLOW CARDS
        createDecksFromJSON("./src/main/java/persistence/cards/developmentcards/YellowCards.json", yellowCards);
        developmentCards.put(CardColor.YELLOW, yellowCards);
        for (List<DevelopmentCard> deck : developmentCards.get(CardColor.YELLOW))
            for (DevelopmentCard card : deck)
                card.setId(id++);
    }

    // OBSERVER METHODS

    public void update(Object observable) {
        Gson gson = new Gson();
        CardTable cardTable = (CardTable) observable;
        setCardTableFromGame(cardTable);

        controller.broadcastMessage(MessageType.CARDTABLE, gson.toJson(this));
    }

    public void updateSinglePlayer(String username) {
        Gson gson = new Gson();
        controller.playerMessage(username, MessageType.CARDTABLE, gson.toJson(this));
    }

    private transient boolean cardsInitialized = false;

    @Override
    public String toString() {
        if (!cardsInitialized) {
            setDevelopmentCardsFromJson();
            cardsInitialized = true;
        }
        String board = "";
        for (int[] row : cards) {
            board += "\n   ";
            for (int cell : row) {
                try {
                    if (getDevelopmentCardFromId(cell).getColor() == CardColor.BLUE)
                        board += "\033[48;2;0;25;120m  " + cell + "  \u001B[0m ";
                    else if (getDevelopmentCardFromId(cell).getColor() == CardColor.GREEN)
                        board += "\033[48;2;0;102;40m  " + cell + "  \u001B[0m ";
                    else if (getDevelopmentCardFromId(cell).getColor() == CardColor.PURPLE)
                        board += "\033[48;2;80;15;200m  " + cell + "  \u001B[0m ";
                    else if (getDevelopmentCardFromId(cell).getColor() == CardColor.YELLOW)
                        board += "\033[48;2;128;85;0m  " + cell + "  \u001B[0m ";
                } catch (CardNotPresentException e) {
                    e.printStackTrace();
                }
            }
            board += "\n";
        }
        return "\u001B[32;1mCardTable:\u001B[0m\n" +
                board;
    }
}
