package model.lorenzo;

import model.CardColor;
import model.CardTable;
import model.PopeFavorTile;
import model.lorenzo.tokens.ActionToken;
import model.lorenzo.tokens.DoubleFaithToken;
import model.lorenzo.tokens.RemoveCardsToken;
import model.lorenzo.tokens.SingleFaithShuffleToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the game's base artificial intelligence, whose actions are based on action tokens
 */
public class Lorenzo implements ArtificialIntelligence {
    /**
     * This attribute stores Lorenzo's faith score (the black cross)
     */
    private int faith;
    /**
     * This attribute stores the player's pope's favor tiles
     */
    private List<PopeFavorTile> popeFavorTiles;
    /**
     * This list stores Lorenzo's tokens that have not been used since the last shuffleDeck
     */
    private List<ActionToken> activeDeck = new ArrayList<>();
    /**
     * This list stores Lorenzo's tokens that have been used since the last shuffleDeck
     */
    private List<ActionToken> usedDeck = new ArrayList<>();

    //CONSTRUCTOR

    /**
     * Constructor
     *
     * @param cardTable the game's card table
     * @param popeFavorTiles the player's pope's favor tiles
     */
    public Lorenzo(CardTable cardTable, List<PopeFavorTile> popeFavorTiles) {
        faith = 0;
        //TODO Read ActionTokens from file
        activeDeck.add(new RemoveCardsToken(CardColor.BLUE, cardTable));
        activeDeck.add(new RemoveCardsToken(CardColor.YELLOW, cardTable));
        activeDeck.add(new RemoveCardsToken(CardColor.PURPLE, cardTable));
        activeDeck.add(new RemoveCardsToken(CardColor.GREEN, cardTable));
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
        currentAction.doAction();
        activeDeck.remove(0);
        usedDeck.add(currentAction);
    }

    /**
     * Increases Lorenzo's faith score by the given amount
     *
     * @param quantity the amount by which to increase the faith score
     */
    public void increaseFaith(int quantity) {
        faith+=quantity;
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
        int newTriggeredTile = lastTriggeredTile;
        for (int tileNumber = lastTriggeredTile; tileNumber < popeFavorTiles.size(); tileNumber++) {
            if (popeFavorTiles.get(tileNumber).isTriggered(faith)) {
                newTriggeredTile = tileNumber + 1;
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
     * @return the deck containing all the drawable cards
     */
    public List<ActionToken> getActiveDeck() {
        return new ArrayList<>(activeDeck);
    }
}
