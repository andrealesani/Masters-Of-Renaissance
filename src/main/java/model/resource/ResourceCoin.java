package model.resource;

import exceptions.ParametersNotValidException;
import model.PlayerBoard;
import model.ResourceType;

/**
 * This class represents the game's coin resource and yellow marble
 */
public class ResourceCoin extends Resource {
    //CONSTRUCTORS

    /**
     * Constructor
     */
    public ResourceCoin() {
        super(ResourceType.COIN);
    }

    //PUBLIC METHODS

    /**
     * Adds a coin to the player's warehouse
     *
     * @param playerBoard the player's board
     */
    @Override
    public void addResourceFromMarket(PlayerBoard playerBoard) {
        if(playerBoard == null)
            throw new ParametersNotValidException();

        playerBoard.addResourceToWaitingRoom(getType(), 1);
    }

    /**
     * Adds a coin to the player's strongbox
     *
     * @param playerBoard the player's board
     */
    @Override
    public void addResourceFromProduction(PlayerBoard playerBoard) {
        if(playerBoard == null)
            throw new ParametersNotValidException();

        playerBoard.addResourceToStrongbox(ResourceType.COIN, 1);
    }


}
