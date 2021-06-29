package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.Exceptions.ParametersNotValidException;
import it.polimi.ingsw.model.PlayerBoard;

import java.util.Map;

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
        if(playerBoard == null)
            throw new ParametersNotValidException();

        playerBoard.addResourcesToWaitingRoom(Map.of(ResourceType.STONE, 1));
    }

    /**
     * Adds a stone to the player's strongbox
     *
     * @param playerBoard the player's board
     */
    @Override
    public void addResourceFromProduction(PlayerBoard playerBoard) {
        if(playerBoard == null)
            throw new ParametersNotValidException();

        playerBoard.addResourcesToStrongbox(Map.of(ResourceType.STONE, 1));
    }
}
