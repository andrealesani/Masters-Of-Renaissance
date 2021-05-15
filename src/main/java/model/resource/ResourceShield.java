package model.resource;

import Exceptions.ParametersNotValidException;
import model.PlayerBoard;
import model.ResourceType;

import java.util.HashMap;
import java.util.Map;

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

        playerBoard.addResourcesToWaitingRoom(Map.of(ResourceType.SHIELD, 1));
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

        playerBoard.addResourcesToWaitingRoom(Map.of(ResourceType.SHIELD, 1));
    }
}
