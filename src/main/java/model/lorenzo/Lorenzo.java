package model.lorenzo;

import Exceptions.ParametersNotValidException;
import model.*;
import model.lorenzo.tokens.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the game's base artificial intelligence, whose actions are based on action tokens
 */
public class Lorenzo implements ArtificialIntelligence, Observable {
    /**
     * This attribute stores Lorenzo's faith score (the black cross)
     */
    private int faith;
    /**
     * This attribute stores the player's pope's favor tiles
     */
    private final List<PopeFavorTile> popeFavorTiles;
    /**
     * This list stores Lorenzo's tokens that have not been used since the last shuffleDeck
     */
    private final List<ActionToken> activeDeck = new ArrayList<>();
    /**
     * This list stores Lorenzo's tokens that have been used since the last shuffleDeck
     */
    private final List<ActionToken> usedDeck = new ArrayList<>();

    //CONSTRUCTOR

    /**
     * Constructor
     *
     * @param cardTable      the game's card table
     * @param popeFavorTiles the player's pope's favor tiles
     */
    public Lorenzo(CardTable cardTable, List<PopeFavorTile> popeFavorTiles) {
        if (cardTable == null || popeFavorTiles == null) {
            throw new ParametersNotValidException();
        }
        faith = 0;
        activeDeck.add(new RemoveBlueToken(cardTable));
        activeDeck.add(new RemoveGreenToken(cardTable));
        activeDeck.add(new RemovePurpleToken(cardTable));
        activeDeck.add(new RemoveYellowToken(cardTable));
        activeDeck.add(new DoubleFaithToken(this));
        activeDeck.add(new SingleFaithShuffleToken(this));
        Collections.shuffle(activeDeck);
        this.popeFavorTiles = popeFavorTiles;
    }

    //PUBLIC METHODS

    /**
     * Makes Lorenzo take his turn by activating one of his action tokens
     */
    public void takeTurn() {
        ActionToken currentAction = activeDeck.get(0);
        activeDeck.remove(0);
        usedDeck.add(currentAction);
        currentAction.doAction();
    }

    /**
     * Increases Lorenzo's faith score by the given amount
     *
     * @param quantity the amount by which to increase the faith score
     */
    public void addFaith(int quantity) {
        if (quantity < 0) {
            throw new ParametersNotValidException();
        }
        faith += quantity;
    }

    /**
     * Checks the necessity for a vatican report by checking the player's pope's favor tiles, starting from the one with the highest index to not have yet been triggered up until last turn.
     * A tile is considered 'triggered' once the player's or Lorenzo's faith score has reached or surpassed the tile's faith score.
     * Returns the tile with the highest index to have been triggered by Lorenzo during this turn (might be the same as the last)
     *
     * @param lastTriggeredTile the index of the tile the with the highest index that has been triggered (before this turn)
     * @return the index of the tile the with the highest index that has been triggered (during this turn)
     */
    public int getNewTriggeredTile(int lastTriggeredTile) {
        if (lastTriggeredTile < 0 || lastTriggeredTile > popeFavorTiles.size()) {
            throw new ParametersNotValidException();
        }
        int newTriggeredTile = lastTriggeredTile;
        for (int tileNumber = lastTriggeredTile; tileNumber < popeFavorTiles.size(); tileNumber++) {
            if (popeFavorTiles.get(tileNumber).isTriggered(faith)) {
                newTriggeredTile = tileNumber + 1;
            } else {
                break;
            }
        }
        return newTriggeredTile;
    }

    /**
     * Restores the active deck and shuffles it
     */
    public void shuffleDeck() {
        activeDeck.addAll(usedDeck);
        Collections.shuffle(activeDeck);
        usedDeck.clear();
    }

    //GETTERS

    /**
     * Getter for lorenzo's faith score
     *
     * @return Lorenzo's current faith
     */
    public int getFaith() {
        return faith;
    }

    /**
     * Getter for the active cards deck
     *
     * @return the deck containing all the drawable tokens
     */
    public List<ActionToken> getActiveDeck() {
        return activeDeck;
    }

    /**
     * Getter for the inactive cards deck
     *
     * @return the deck containing all the drawn tokens
     */
    public List<ActionToken> getUsedDeck() {
        return usedDeck;
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
}
