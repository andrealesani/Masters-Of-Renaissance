package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.Exceptions.ParametersNotValidException;
import it.polimi.ingsw.model.PlayerBoard;

/**
 * This class represents the game's white marble
 */
public class ResourceWhite extends Resource {

    //CONSTRUCTORS

    /**
     * Constructor
     */
    public ResourceWhite() {
        super(ResourceType.WHITE_MARBLE);
    }

    //PUBLIC METHODS

    /**
     * Adds a white marble to the player's board
     *
     * @param playerBoard the player's board
     */
    @Override
    public void addResourceFromMarket(PlayerBoard playerBoard) {
        if(playerBoard == null)
            throw new ParametersNotValidException();
        
        playerBoard.addWhiteMarble(1);
    }
}