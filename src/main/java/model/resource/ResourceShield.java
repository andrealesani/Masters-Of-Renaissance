package model.resource;

import exceptions.ParametersNotValidException;
import model.PlayerBoard;
import model.ResourceType;

/**
 * This class represents the game's shield resource and blue marble
 */
public class ResourceShield extends Resource {
    //CONSTRUCTOR

    /**
     * Constructor
     */
    public ResourceShield() {
        super(ResourceType.SHIELD);
    }

    //PUBLIC METHODS

    /**
     * Adds a shield to the player's warehouse
     *
     * @param playerBoard the player's board
     */
    @Override
    public void addResourceFromMarket(PlayerBoard playerBoard) {
        if(playerBoard == null)
            throw new ParametersNotValidException();

        playerBoard.addResourceToWaitingRoom(ResourceType.SHIELD, 1);
    }

    /**
     * Adds a shield to the player's strongbox
     *
     * @param playerBoard the player's board
     */
    @Override
    public void addResourceFromProduction(PlayerBoard playerBoard) {
        if(playerBoard == null)
            throw new ParametersNotValidException();

        playerBoard.addResourceToStrongbox(ResourceType.SHIELD, 1);
    }
}
