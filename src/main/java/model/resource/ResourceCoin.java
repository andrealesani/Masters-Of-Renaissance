package model.resource;

import Exceptions.ParametersNotValidException;
import model.PlayerBoard;

import java.util.Map;

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
        playerBoard.addResourcesToWaitingRoom(Map.of(getType(), 1));
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

        playerBoard.addResourcesToStrongbox(Map.of(getType(), 1));
    }


}
