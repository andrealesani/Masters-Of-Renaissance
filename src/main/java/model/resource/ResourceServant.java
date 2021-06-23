package model.resource;

import Exceptions.ParametersNotValidException;
import model.PlayerBoard;

import java.util.Map;

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
        if(playerBoard == null)
            throw new ParametersNotValidException();

        playerBoard.addResourcesToWaitingRoom(Map.of(ResourceType.SERVANT, 1));
    }

    /**
     * Adds a servant to the player's strongbox
     *
     * @param playerBoard the player's board
     */
    @Override
    public void addResourceFromProduction(PlayerBoard playerBoard) {
        if(playerBoard == null)
            throw new ParametersNotValidException();

        playerBoard.addResourcesToStrongbox(Map.of(ResourceType.SERVANT, 1));
    }
}
