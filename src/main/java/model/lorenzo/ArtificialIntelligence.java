package model.lorenzo;

/**
 * This interface indicates that the implementing class can be used as an artificial intelligence for solo mode.
 * It has methods to increase its faith score, check the necessity for a vatican report and taking its own turn.
 */
public interface ArtificialIntelligence {
    /**
     * Makes the A.I. take its turn
     */
    void takeTurn();

    /**
     * Increases the A.I.'s faith score by the given amount
     *
     * @param quantity the amount by which to increase the faith score
     */
    void increaseFaith(int quantity);

    /**
     * Checks the necessity for a vatican report by checking the player's pope's favor tiles, starting from the one with the highest index to not have yet been triggered up until last turn.
     * A tile is considered 'triggered' once the player's or the A.I.'s faith score has reached or surpassed the tile's faith score.
     * Returns the tile with the highest index to have been triggered by the A.I. during this turn (might be the same as the last)
     *
     * @param lastTriggeredTile the index of the tile the with the highest index that has been triggered (before this turn)
     * @return the index of the tile the with the highest index that has been triggered (during this turn)
     */
    public int getNewTriggeredTile(int lastTriggeredTile);
}
