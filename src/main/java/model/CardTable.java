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

    //TODO forse sostituire i 4 deck con l'usare l'unico json per tutte le carte
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

        int row = level -1;

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
        int i;

        for (i=2; i>=0; i--) {
            if (deckColumn.get(i).size() > 0) {
                deckColumn.get(i).remove(0);
                break;
            }
        }

        if (i < 0) {
            throw new EmptyDeckException();
        }
    }

    /**
     * Checks if there is at least one card of each color still available (this method should only be called in solo mode)
     *
     * @return true if there's at least one card of each color still available
     */
    public boolean checkAllColorsAvailable() {

        for (CardColor color : cardTable.keySet()) {
            for (List<DevelopmentCard> deck : cardTable.get(color)) {
                if (deck.size() >= 1)
                    break;
                else
                    return false;
            }
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
                colorCards.get(0).add(developmentCard);
            else if (developmentCard.getLevel() == 2)
                colorCards.get(1).add(developmentCard);
            else if (developmentCard.getLevel() == 3)
                colorCards.get(2).add(developmentCard);
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
