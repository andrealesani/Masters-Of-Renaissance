package it.polimi.ingsw.model.lorenzo;

/**
 * This interface indicates that the implementing class can be used as an artificial intelligence for solo mode.
 * It is not meant to be considered as a second player in the game, but only as a series of actions done between the player's turns
 */
public interface ArtificialIntelligence {

    //PUBLIC METHODS

    /**
     * Makes the A.I. takes its turn
     */
    void takeTurn();

    /**
     * Increases the A.I.'s faith score by the given amount
     *
     * @param quantity the amount by which to increase the faith score
     */
    void addFaith(int quantity);

    /**
     * Checks the necessity for a vatican report by checking the player's pope's favor tiles, starting from the one with the highest index to not have yet been triggered up until last turn.
     * A tile is considered 'triggered' once the player's or the A.I.'s faith score has reached or surpassed the tile's faith score.
     * Returns the tile with the highest index to have been triggered by the A.I. during this turn (might be the same as the last)
     *
     * @param lastTriggeredTile the index of the tile the with the highest index that has been triggered (before this turn)
     * @return the index of the tile the with the highest index that has been triggered (during this turn)
     */
    int getNewTriggeredTile(int lastTriggeredTile);

    //GETTERS

    /**
     * Getter for the A.I's faith score
     *
     * @return A.I's current faith
     */
    int getFaith();
}
