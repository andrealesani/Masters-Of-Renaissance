package model.resource;

import model.PlayerBoard;
import model.ResourceType;

/**
 * This class represents the game's white marble
 */
public class ResourceWhite extends Resource {
    //CONSTRUCTORS

    /**
     * Constructor
     */
    public ResourceWhite() {
        super(ResourceType.WHITEORB);
    }

    //PUBLIC METHODS

    /**
     * Adds a white marble to the player's board
     *
     * @param playerBoard the player's board
     */
    @Override
    public void addResourceFromMarket(PlayerBoard playerBoard) {
        playerBoard.addWhiteMarble(1);
    }
}