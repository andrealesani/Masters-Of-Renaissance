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
public class CardTable implements Observable{
    /**
     * Represents the table column with all green cards
     */
    private final List<List<DevelopmentCard>> greenCards;
    /**
     * Represents the table column with all blue cards
     */
    private final List<List<DevelopmentCard>> blueCards;
    /**
     * Represents the table column with all yellow cards
     */
    private final List<List<DevelopmentCard>> yellowCards;
    /**
     * Represents the table column with all purple cards
     */
    private final List<List<DevelopmentCard>> purpleCards;

    /**
     * Map of all the DevelopmentCards on the table. It's a map that for each CardColor has list of decks that represent the group of cards of a specific color and level
     */
    private final Map<CardColor, List<List<DevelopmentCard>>> cards;

    //CONSTRUCTORS

    /**
     * Constructor
     */
    //TODO forse sostituire i 4 deck con l'usare l'unico json per tutte le carte
    //TODO shufflare tutti i mazzetti
    public CardTable(int id) {
        greenCards = new ArrayList<>();
        blueCards = new ArrayList<>();
        yellowCards = new ArrayList<>();
        purpleCards = new ArrayList<>();

        cards = new HashMap<>();

        id++;

        // BLUE CARDS
        createDecksFromJSON("./src/main/java/persistence/cards/developmentcards/BlueCards.json", blueCards);
        cards.put(CardColor.BLUE, blueCards);
        for(List<DevelopmentCard> deck : cards.get(CardColor.BLUE))
            for(DevelopmentCard card : deck)
                card.setId(id++);

        // GREEN CARDS
        createDecksFromJSON("./src/main/java/persistence/cards/developmentcards/GreenCards.json", greenCards);
        cards.put(CardColor.GREEN, greenCards);
        for(List<DevelopmentCard> deck : cards.get(CardColor.GREEN))
            for(DevelopmentCard card : deck)
                card.setId(id++);

        // PURPLE CARDS
        createDecksFromJSON("./src/main/java/persistence/cards/developmentcards/PurpleCards.json", purpleCards);
        cards.put(CardColor.PURPLE, purpleCards);
        for(List<DevelopmentCard> deck : cards.get(CardColor.PURPLE))
            for(DevelopmentCard card : deck)
                card.setId(id++);

        // YELLOW CARDS
        createDecksFromJSON("./src/main/java/persistence/cards/developmentcards/YellowCards.json", yellowCards);
        cards.put(CardColor.YELLOW, yellowCards);
        for(List<DevelopmentCard> deck : cards.get(CardColor.YELLOW))
            for(DevelopmentCard card : deck)
                card.setId(id++);

        notifyObservers();
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
        if (cards.get(cardColor).get(row).isEmpty())
            throw new EmptyDeckException();

        playerBoard.buyDevelopmentCard(cards.get(cardColor).get(row).get(0), cardSlot);
        cards.get(cardColor).get(row).remove(0);

        notifyObservers();
    }

    /**
     * Removes from the grid the card of the specified color with the lowest level (this method should only be called only in solo mode)
     *
     * @param cardColor specifies the color of the card that has to be removed
     */
    public void discardTop(CardColor cardColor) throws EmptyDeckException {
        List<List<DevelopmentCard>> deckColumn = cards.get(cardColor);
        int i;

        for (i=0; i<=2; i++) {
            if (deckColumn.get(i).size() > 0) {
                deckColumn.get(i).remove(0);
                break;
            }
        }

        if (i > 2) {
            throw new EmptyDeckException();
        }

        notifyObservers();
    }

    /**
     * Checks if there is at least one card of each color still available (this method should only be called in solo mode)
     *
     * @return true if there's at least one card of each color still available
     */
    public boolean checkAllColorsAvailable() {
        for (CardColor color : cards.keySet()) {
            List<List<DevelopmentCard>> colorDeck = cards.get(color);
            int i;
            for (i=0; i<=2; i++) {
                List<DevelopmentCard> levelDeck = colorDeck.get(i);
                if (levelDeck.size() >= 1)
                    break;
            }
            if (i>2)
                return false;
        }

        return true;
    }

    //PRIVATE METHODS

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

    //GETTERS

    /**
     * Getter
     *
     * @return the Map with all the cards in the CardTable
     */
    public Map<CardColor, List<List<DevelopmentCard>>> getCards() {
        return cards;
    }

    /**
     * Gets the ID of the card on top of a specific deck of the CardTable
     *
     * @param deck of the card that u need
     * @return the ID of the card on top of the specified deck
     */
    public int getTopCardId(List<DevelopmentCard> deck) {
        return deck.get(0).getId();
    }

    /**
     * Getter (DEPRECATED, don't use it)
     *
     * @return the CardTable column with only green cards decks
     */
    public List<List<DevelopmentCard>> getGreenCards() {
        return greenCards;
    }

    /**
     * Getter (DEPRECATED, don't use it)
     *
     * @return the CardTable column with only blue cards decks
     */
    public List<List<DevelopmentCard>> getBlueCards() {
        return blueCards;
    }

    /**
     * Getter (DEPRECATED, don't use it)
     *
     * @return the CardTable column with only yellow cards decks
     */
    public List<List<DevelopmentCard>> getYellowCards() {
        return yellowCards;
    }

    /**
     * Getter (DEPRECATED, don't use it)
     *
     * @return the CardTable column with only purple cards decks
     */
    public List<List<DevelopmentCard>> getPurpleCards() {
        return purpleCards;
    }


    // OBSERVABLE ATTRIBUTES AND METHODS

    /**
     * List of observers that need to get updated when the object state changes
     */
    private final List<Observer> observers = new ArrayList<>();

    /**
     * This method calls the update() on every object observing this object
     */
    public void notifyObservers() {
        observers.forEach(observer -> observer.update(this));
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
        notifyObservers();
    }

    public List<Observer> getObservers() {
        return observers;
    }
}
