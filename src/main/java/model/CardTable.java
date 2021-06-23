package model;

import Exceptions.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.card.DevelopmentCard;
import model.resource.*;
import network.StaticMethods;
import server.ServerMain;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Represents what in the physical game is the grid that holds all the development cards still available for players to buy
 */
public class CardTable implements Observable {
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
    private boolean cardsInitialized;

    //CONSTRUCTORS

    /**
     * Constructor
     */
    //TODO ti prego alesani fixa sta roba
    public CardTable(int id) {

        greenCards = new ArrayList<>();
        blueCards = new ArrayList<>();
        yellowCards = new ArrayList<>();
        purpleCards = new ArrayList<>();

        cards = new HashMap<>();

        id++;

        // BLUE CARDS
        StaticMethods.createDecksFromJSON(blueCards, CardColor.BLUE);
        cards.put(CardColor.BLUE, blueCards);
        for (List<DevelopmentCard> deck : cards.get(CardColor.BLUE)) {
            for (DevelopmentCard card : deck)
                card.setId(id++);
            Collections.shuffle(deck);
        }

        // GREEN CARDS
        StaticMethods.createDecksFromJSON(greenCards, CardColor.GREEN);
        cards.put(CardColor.GREEN, greenCards);
        for (List<DevelopmentCard> deck : cards.get(CardColor.GREEN)) {
            for (DevelopmentCard card : deck)
                card.setId(id++);
            Collections.shuffle(deck);
        }

        // PURPLE CARDS
        StaticMethods.createDecksFromJSON(purpleCards, CardColor.PURPLE);
        cards.put(CardColor.PURPLE, purpleCards);
        for (List<DevelopmentCard> deck : cards.get(CardColor.PURPLE)) {
            for (DevelopmentCard card : deck)
                card.setId(id++);
            Collections.shuffle(deck);
        }

        // YELLOW CARDS
        StaticMethods.createDecksFromJSON(yellowCards, CardColor.YELLOW);
        cards.put(CardColor.YELLOW, yellowCards);
        for (List<DevelopmentCard> deck : cards.get(CardColor.YELLOW)) {
            for (DevelopmentCard card : deck)
                card.setId(id++);
            Collections.shuffle(deck);
        }

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

        int row = level - 1;

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

        for (i = 0; i <= 2; i++) {
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
            for (i = 0; i <= 2; i++) {
                List<DevelopmentCard> levelDeck = colorDeck.get(i);
                if (levelDeck.size() >= 1)
                    break;
            }
            if (i > 2)
                return false;
        }

        return true;
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

    /**
     * Getter
     *
     * @param id of the card to be returned
     * @return the DevelopmentCard associated to the specified ID
     * @throws CardNotPresentException when the given ID is not associated with any of the cards in the CardTable
     */
    public DevelopmentCard getDevelopmentCardFromId(int id) throws CardNotPresentException {
        for (Map.Entry<CardColor, List<List<DevelopmentCard>>> color : cards.entrySet()) {
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

    // PERSISTENCE METHODS

    public void restoreCards(int[][][] cardTable) {
        for (int i = 0; i < cardTable.length; i++) {
            for (int j = 0; j < cardTable[0].length; j++) {
                // TODO
            }
        }
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
