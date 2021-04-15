package model;

/**
 * This class represents a single Pope's Favor Tile and all of its distinctive parameters.
 * It has methods for changing its state depending on the player's faith score, and to determine how many victory points they earn from it.
 */
public class PopeFavorTile {
    /**
     * Attribute used to determine the tile's current state
     */
    private PopeTileState state;
    /**
     * Attribute used to store the tile's associated victory points
     */
    private final int victoryPoints;
    /**
     * Attribute used to store the faith value that, once reached, will trigger the tile for all players
     */
    private final int popeSpaceValue;
    /**
     * Attribute used to store the size of the area, before and including the trigger spot, where players can still activate the tile once it's triggered
     */
    private final int activeSectionSize;

    //CONSTRUCTORS

    /**
     * The class constructor
     *
     * @param victoryPoints     the amount of victory points the tile gives the player after activation
     * @param popeSpaceValue    the spot on the faith track that when reached will activate the tile for all players
     * @param activeSectionSize the size of the area before and including the popeSpaceValue that constitutes the tile activation area
     */
    public PopeFavorTile(int victoryPoints, int popeSpaceValue, int activeSectionSize) {
        this.victoryPoints = victoryPoints;
        this.activeSectionSize = activeSectionSize;
        this.popeSpaceValue = popeSpaceValue;
        state = PopeTileState.INACTIVE;
    }

    //PUBLIC METHODS

    /**
     * Returns whether or not the tile has been triggered, thus signaling the necessity for all players to check if they are within the activation area
     *
     * @param playerFaith the faith score of the player to which the tile belongs
     * @return true if the tile was still inactive and the player has reached or moved past its trigger spot
     */
    public boolean isTriggered(int playerFaith) {
        if (state == PopeTileState.INACTIVE && playerFaith >= popeSpaceValue) {
            return true;
        }
        return false;
    }

    /**
     * If the tile was still inactive, checks the player's faith. If they are in or beyond the activation area, sets the tile to active, otherwise discards it.
     *
     * @param playerFaith the faith score of the player to which the tile belongs
     */
    public void checkActivation(int playerFaith) {
        if (state == PopeTileState.INACTIVE) {
            if (playerFaith >= popeSpaceValue - activeSectionSize + 1) {
                state = PopeTileState.ACTIVE;
            } else {
                state = PopeTileState.DISCARDED;
            }
        }
    }

    //GETTERS

    /**
     * Returns the number of victory points the tile awards the player
     *
     * @return the tile's victory points if it is active, otherwise returns 0
     */
    public int getVictoryPoints() {
        if (state == PopeTileState.ACTIVE) {
            return victoryPoints;
        }
        return 0;
    }

    /**
     * Returns the current state of the tile
     *
     * @return the state of the tile
     */
    public PopeTileState getState() {
        return state;
    }
}
