package model.resource;

import Exceptions.ParametersNotValidException;
import model.PlayerBoard;

/**
 * This class represents the game's faith resource and red marble
 */
public class ResourceFaith extends Resource {

    //CONSTRUCTOR

    /**
     * Constructor
     */
    public ResourceFaith() { super(ResourceType.FAITH); }

    //PUBLIC METHODS

    /**
     * Increases the player's faith score by one
     *
     * @param playerBoard the player's board
     */
    @Override
    public void addResourceFromMarket(PlayerBoard playerBoard) {
        if(playerBoard == null)
            throw new ParametersNotValidException();

        playerBoard.addFaith(1);
    }

    /**
     * Increases the player's faith score by one
     *
     * @param playerBoard the player's board
     */
    @Override
    public void addResourceFromProduction(PlayerBoard playerBoard) {
        if(playerBoard == null)
            throw new ParametersNotValidException();

        playerBoard.addFaith(1);
    }
}
