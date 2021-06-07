package network.beans;

import Exceptions.CardNotPresentException;
import Exceptions.ParametersNotValidException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.*;
import model.Observer;
import model.card.DevelopmentCard;
import network.MessageType;
import server.GameController;
import server.ServerMain;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Class used to serialize a CardTable object, send it over the network and store its information in the client
 */
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
    /**
     * Boolean parameters that is used by methods that need cards data to check if the cards have already been initialized
     */
    private transient boolean cardsInitialized = false;

    // CONSTRUCTOR

    public CardTableBean(GameController controller) {
        this.controller = controller;
    }

    // PRIVATE METHODS

    /**
     * Takes in input the path of the JSON file to read and the List of decks of a specific color,
     * then it reads the cards from the file and splits them into decks based on the cards level.
     * Level 1 cards will be in the first lists of every column.
     * Level 2 cards will be in the middle (second lists of every column).
     * Level 3 cards will be in the third lists of every column.
     * The method is hardcoded to receive cards with levels from 1 to 3.
     *
     * @param jsonPath   specifies the path where the JSON file is stored
     * @param colorCards specifies which column of the deck is going to be instantiated
     */
    private void createDecksFromJSON(String jsonPath, List<List<DevelopmentCard>> colorCards, CardColor color) {
        Gson gson = new Gson();
        Type DevCardArray = new TypeToken<ArrayList<DevelopmentCard>>() {
        }.getType();

        Reader reader = new InputStreamReader(ServerMain.class.getResourceAsStream(jsonPath), StandardCharsets.UTF_8);
        List<DevelopmentCard> cards = gson.fromJson(reader, DevCardArray);
        for (int i = 0; i < 3; i++) {
            colorCards.add(new ArrayList<DevelopmentCard>());
        }
        for (DevelopmentCard developmentCard : cards) {
            if (developmentCard.getColor() == color) {
                if (developmentCard.getLevel() == 1)
                    colorCards.get(0).add(developmentCard);
                else if (developmentCard.getLevel() == 2)
                    colorCards.get(1).add(developmentCard);
                else if (developmentCard.getLevel() == 3)
                    colorCards.get(2).add(developmentCard);
            }
        }
    }

    // GETTERS

    /**
     * Getter
     *
     * @return a 2D matrix with the IDs of the cards on top of the CardTable
     */
    public int[][] getCards() {
        return cards;
    }

    /**
     * Getter
     *
     * @param id of the card to be returned
     * @return the DevelopmentCard associated to the specified ID
     * @throws CardNotPresentException when the given ID is not associated with any of the cards in the CardTable
     */
    public DevelopmentCard getDevelopmentCardFromId(int id) throws CardNotPresentException {
        if (!cardsInitialized) {
            setDevelopmentCardsFromJson();
            cardsInitialized = true;
        }
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

    /**
     * Sets the cards 2D matrix reading data from the given CardTable
     *
     * @param cardTable object to take the information from
     */
    private void setCardTableFromGame(CardTable cardTable) {
        cards = new int[3][cardTable.getCards().entrySet().size()];
        int i = 0, j;
        for (i = 0; i < cards.length; i++) {
            j = 0;
            for (Map.Entry<CardColor, List<List<DevelopmentCard>>> color : cardTable.getCards().entrySet()) {
                if (color.getValue().get(i).size() == 0)
                    cards[i][j] = -1;
                else
                    cards[i][j] = color.getValue().get(i).get(0).getId();
                j++;
            }
        }
    }

    /**
     * Sets the developmentCards list reading them from DevelopmentCards.json file
     */
    private void setDevelopmentCardsFromJson() {
        developmentCards = new HashMap<>();
        int id = 17;
        List<List<DevelopmentCard>> greenCards = new ArrayList<>();
        List<List<DevelopmentCard>> blueCards = new ArrayList<>();
        List<List<DevelopmentCard>> yellowCards = new ArrayList<>();
        List<List<DevelopmentCard>> purpleCards = new ArrayList<>();

        // BLUE CARDS
        createDecksFromJSON("/json/cards/developmentcards/DevelopmentCards.json", blueCards, CardColor.BLUE);
        developmentCards.put(CardColor.BLUE, blueCards);
        for(List<DevelopmentCard> deck : developmentCards.get(CardColor.BLUE))
            for(DevelopmentCard card : deck)
                card.setId(id++);

        // GREEN CARDS
        createDecksFromJSON("/json/cards/developmentcards/DevelopmentCards.json", greenCards, CardColor.GREEN);
        developmentCards.put(CardColor.GREEN, greenCards);
        for(List<DevelopmentCard> deck : developmentCards.get(CardColor.GREEN))
            for(DevelopmentCard card : deck)
                card.setId(id++);

        // PURPLE CARDS
        createDecksFromJSON("/json/cards/developmentcards/DevelopmentCards.json", purpleCards, CardColor.PURPLE);
        developmentCards.put(CardColor.PURPLE, purpleCards);
        for(List<DevelopmentCard> deck : developmentCards.get(CardColor.PURPLE))
            for(DevelopmentCard card : deck)
                card.setId(id++);

        // YELLOW CARDS
        createDecksFromJSON("/json/cards/developmentcards/DevelopmentCards.json", yellowCards, CardColor.YELLOW);
        developmentCards.put(CardColor.YELLOW, yellowCards);
        for(List<DevelopmentCard> deck : developmentCards.get(CardColor.YELLOW))
            for(DevelopmentCard card : deck)
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

    /**
     * This method is used to print only one line of the CardTable so that multiple objects can be printed
     * in parallel in the CLI
     *
     * @param line the line to print (starts from 1)
     * @return the String with the line to print
     */
    public String printLine(int line) {
        line--;
        if (line < 0 || line >= cards.length)
            throw new ParametersNotValidException();
        String content = "";

        try {
            content += "Level " + getDevelopmentCardFromId(cards[line][0]).getLevel() + "   ";
        } catch (CardNotPresentException ignored) {
            try {
                content += "Level " + getDevelopmentCardFromId(cards[line][1]).getLevel() + "   ";
            } catch (CardNotPresentException ignored1) {
                try {
                    content += "Level " + getDevelopmentCardFromId(cards[line][2]).getLevel() + "   ";
                } catch (CardNotPresentException ignored2) {
                }
            }
        }
        for (int cell : cards[line]) {
            if (cell == -1)
                content += " " + " " + "xx" + " " + " ";
            try {
                if (getDevelopmentCardFromId(cell).getColor() == CardColor.BLUE)
                    content += " " + Color.BLUE_BG + " " + cell + " " + Color.RESET + " ";
                else if (getDevelopmentCardFromId(cell).getColor() == CardColor.GREEN)
                    content += " " + Color.GREEN_BG + " " + cell + " " + Color.RESET + " ";
                else if (getDevelopmentCardFromId(cell).getColor() == CardColor.PURPLE)
                    content += " " + Color.PURPLE_BG + " " + cell + " " + Color.RESET + " ";
                else if (getDevelopmentCardFromId(cell).getColor() == CardColor.YELLOW)
                    content += " " + Color.YELLOW_DARK_BG + " " + cell + " " + Color.RESET + " ";
            } catch (CardNotPresentException e) {
                System.out.println("Warning: tried to read an ID that doesn't correspond to any DevelopmentCard");
            }
        }

        return content;
    }

    @Override
    public String toString() {
        if (!cardsInitialized) {
            setDevelopmentCardsFromJson();
            cardsInitialized = true;
        }
        String board = "";
        for (int[] row : cards) {
            board += "\n ";
            try {
                board += "Level " + getDevelopmentCardFromId(row[0]).getLevel() + "   ";
            } catch (CardNotPresentException ignored) {
            }
            for (int cell : row) {
                try {
                    if (getDevelopmentCardFromId(cell).getColor() == CardColor.BLUE)
                        board += " " + Color.BLUE_BG + " " + cell + " " + Color.RESET + " ";
                    else if (getDevelopmentCardFromId(cell).getColor() == CardColor.GREEN)
                        board += " " + Color.GREEN_BG + " " + cell + " " + Color.RESET + " ";
                    else if (getDevelopmentCardFromId(cell).getColor() == CardColor.PURPLE)
                        board += " " + Color.PURPLE_BG + " " + cell + " " + Color.RESET + " ";
                    else if (getDevelopmentCardFromId(cell).getColor() == CardColor.YELLOW)
                        board += " " + Color.YELLOW_DARK_BG + " " + cell + " " + Color.RESET + " ";
                } catch (CardNotPresentException e) {
                    System.out.println("Warning: tried to read an ID that doesn't correspond to any DevelopmentCard");
                }
            }
            board += "\n";
        }
        return Color.HEADER + "CardTable:\n" + Color.RESET +
                board;
    }
}
