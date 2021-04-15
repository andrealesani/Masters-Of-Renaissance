package model.resource;

import model.PlayerBoard;
import model.ResourceType;

/**
 * This class represents the game's stone resource and grey marble
 */
public class ResourceStone extends Resource {
    //CONSTRUCTORS

    /**
     * Constructor
     */
    public ResourceStone() {
        super(ResourceType.STONE);
    }

    //PUBLIC METHODS

    /**
     * Adds a stone to the player's warehouse
     *
     * @param playerBoard the player's board
     */
    @Override
    public void addResourceFromMarket(PlayerBoard playerBoard) {
        playerBoard.addResourceToWarehouse(ResourceType.STONE, 1);
    }

    /**
     * Adds a stone to the player's strongbox
     *
     * @param playerBoard the player's board
     */
    @Override
    public void addResourceFromProduction(PlayerBoard playerBoard) {
        playerBoard.addResourceToStrongbox(ResourceType.STONE, 1);
    }
}
