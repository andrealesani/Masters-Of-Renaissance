package model.resource;

import model.PlayerBoard;
import model.ResourceType;

/**
 * This class represents the game's servant resource and purple marble
 */
public class ResourceServant extends Resource {
    //CONSTRUCTORS

    /**
     * Constructor
     */
    public ResourceServant() {
        super(ResourceType.SERVANT);
    }

    //PUBLIC METHODS

    /**
     * Adds a servant to the player's warehouse
     *
     * @param playerBoard the player's board
     */
    @Override
    public void addResourceFromMarket(PlayerBoard playerBoard) {
        playerBoard.addResourceToWaitingRoom(ResourceType.SERVANT, 1);
    }

    /**
     * Adds a servant to the player's strongbox
     *
     * @param playerBoard the player's board
     */
    @Override
    public void addResourceFromProduction(PlayerBoard playerBoard) {
        playerBoard.addResourceToStrongbox(ResourceType.SERVANT, 1);
    }
}
