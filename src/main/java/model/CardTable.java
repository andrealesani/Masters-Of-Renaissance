package model;

import Exceptions.EmptyDeckException;
import Exceptions.NotEnoughResourceException;
import Exceptions.ParametersNotValidException;
import Exceptions.SlotNotValidException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import model.card.DevelopmentCard;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents what in the physical game is the grid that holds all the development cards still available for players to buy
 */
public class CardTable {
    private final List<List<DevelopmentCard>> greenCards;
    private final List<List<DevelopmentCard>> blueCards;
    private final List<List<DevelopmentCard>> yellowCards;
    private final List<List<DevelopmentCard>> purpleCards;

    private final Map<CardColor, List<List<DevelopmentCard>>> cardTable;

    //CONSTRUCTORS

    /**
     * Constructor
     */
    public CardTable() {
        greenCards = new ArrayList<>();
        blueCards = new ArrayList<>();
        yellowCards = new ArrayList<>();
        purpleCards = new ArrayList<>();

        cardTable = new HashMap<>();

        // GREEN CARDS
        createDecksFromJSON("./src/main/java/persistence/cards/developmentcards/GreenCards.json", greenCards);
        cardTable.put(CardColor.GREEN, greenCards);

        // BLUE CARDS
        createDecksFromJSON("./src/main/java/persistence/cards/developmentcards/BlueCards.json", blueCards);
        cardTable.put(CardColor.BLUE, blueCards);

        // YELLOW CARDS
        createDecksFromJSON("./src/main/java/persistence/cards/developmentcards/YellowCards.json", yellowCards);
        cardTable.put(CardColor.YELLOW, yellowCards);

        // PURPLE CARDS
        createDecksFromJSON("./src/main/java/persistence/cards/developmentcards/PurpleCards.json", purpleCards);
        cardTable.put(CardColor.PURPLE, purpleCards);
    }

    //PUBLIC METHODS

    /**
     * Adds the selected card to the specified PlayerBoard in the specified slot
     *
     * @param cardColor   specifies the column of the card in the grid
     * @param level       specifies the level of the card to be bought (STARTS FROM 1)
     * @param playerBoard specifies which player is buying the card
     * @param cardSlot    specifies in which production slot the player wants to put the card
     */
    public void buyTopCard(CardColor cardColor, int level, PlayerBoard playerBoard, int cardSlot) throws SlotNotValidException, NotEnoughResourceException, EmptyDeckException {
        if (cardSlot <= 0 || cardSlot > playerBoard.getCardSlots().size())
            throw new ParametersNotValidException();

        // Hardcoded connection between the card's level and its row in the CardTable
        int row = -1;
        if (level == 1)
            row = 2;
        else if (level == 2)
            row = 1;
        else if (level == 3)
            row = 0;

        // Checks that the deck is not empty before trying to access it
        if (cardTable.get(cardColor).get(row).isEmpty())
            throw new EmptyDeckException();

        playerBoard.buyDevelopmentCard(cardTable.get(cardColor).get(row).get(0), cardSlot);
        cardTable.get(cardColor).get(row).remove(0);
    }

    /**
     * Removes from the grid the card of the specified color with the lowest level (this method should only be called only in solo mode)
     *
     * @param cardColor specifies the color of the card that has to be removed
     */
    public void discardTop(CardColor cardColor) throws EmptyDeckException {
        List<List<DevelopmentCard>> deckColumn = cardTable.get(cardColor);

        if (deckColumn.get(2).size() > 0)
            deckColumn.get(2).remove(0);
        else if (deckColumn.get(1).size() > 0)
            deckColumn.get(1).remove(0);
        else if (deckColumn.get(0).size() > 0)
            deckColumn.get(0).remove(0);
        else throw new EmptyDeckException();
    }

    /**
     * Checks if there is at least one card of each color still available (this method should only be called in solo mode)
     *
     * @return true if there's at least one card of each color still available
     */
    public boolean checkAllColorsAvailable() {
        for (List<DevelopmentCard> deck : greenCards) {
            if (deck.size() >= 1)
                break;
            else
                return false;
        }
        for (List<DevelopmentCard> deck : blueCards) {
            if (deck.size() >= 1)
                break;
            else
                return false;
        }
        for (List<DevelopmentCard> deck : yellowCards) {
            if (deck.size() >= 1)
                break;
            else
                return false;
        }
        for (List<DevelopmentCard> deck : purpleCards) {
            if (deck.size() >= 1)
                break;
            else
                return false;
        }

        return true;
    }

    //PRIVATE METHODS

    /**
     * Takes in input the path of the JSON file to read and the List of decks of a specific color,
     * then it reads the cards from the file and splits them into decks based on the cards level.
     * To keep the analogy with the physical game, level 3 cards will be on the upper part of the table (first lists of
     * every column), level 2 cards will be in the middle (second lists of every column) and level 1 cards will be on
     * the bottom of the table (third lists of every column)
     *
     * @param JsonPath   specifies the path where the JSON file is stored
     * @param colorCards specifies which column of the deck is going to be instantiated
     */
    private void createDecksFromJSON(String JsonPath, List<List<DevelopmentCard>> colorCards) {
        Gson gson = new Gson();
        JsonReader reader = null;
        Type DevCardArray = new TypeToken<ArrayList<DevelopmentCard>>() {
        }.getType();

        try {
            reader = new JsonReader(new FileReader(JsonPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<DevelopmentCard> allColorCards = gson.fromJson(reader, DevCardArray);
        for (int i = 0; i < 3; i++) {
            colorCards.add(new ArrayList<DevelopmentCard>());
        }
        for (DevelopmentCard developmentCard : allColorCards) {
            if (developmentCard.getLevel() == 1)
                colorCards.get(2).add(developmentCard);
            else if (developmentCard.getLevel() == 2)
                colorCards.get(1).add(developmentCard);
            else if (developmentCard.getLevel() == 3)
                colorCards.get(0).add(developmentCard);
        }
    }

    //GETTERS

    /**
     * Getter
     *
     * @return the CardTable column with only green cards decks
     */
    public List<List<DevelopmentCard>> getGreenCards() {
        return greenCards;
    }

    /**
     * Getter
     *
     * @return the CardTable column with only blue cards decks
     */
    public List<List<DevelopmentCard>> getBlueCards() {
        return blueCards;
    }

    /**
     * Getter
     *
     * @return the CardTable column with only yellow cards decks
     */
    public List<List<DevelopmentCard>> getYellowCards() {
        return yellowCards;
    }

    /**
     * Getter
     *
     * @return the CardTable column with only purple cards decks
     */
    public List<List<DevelopmentCard>> getPurpleCards() {
        return purpleCards;
    }
}
