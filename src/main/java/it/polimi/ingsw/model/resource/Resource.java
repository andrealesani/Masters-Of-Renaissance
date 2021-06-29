package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.model.PlayerBoard;

/**
 * This class represents the game's generic resource and marbles, used for the market and productions.
 */
public abstract class Resource {
    /**
     * This attribute stores the type of the resource using the ResourceType ENUM
     */
    private final ResourceType type;

    //CONSTRUCTORS

    /**
     * Constructor called by the subclasses
     *
     * @param type the type of the resource, passed by the subclass depending on its type
     */
    public Resource(ResourceType type) { this.type = type;}

    //PUBLIC METHODS

    /**
     * Method called when the resource is in a row or column of the market that has been selected by the player
     *
     * @param playerBoard the player's board
     */
    public void addResourceFromMarket(PlayerBoard playerBoard) {
        //does nothing
    }

    /**
     * Method called when the resource is in the output of a production activated by the player
     *
     * @param playerBoard the player's board
     */
    public void addResourceFromProduction(PlayerBoard playerBoard) {
        //does nothing
    }

    // GETTERS

    /**
     * Getter
     *
     * @return the resource's type
     */
    public ResourceType getType(){
        return type;
    }

    //EQUALS

    /**
     * Equals
     *
     * @param obj the object to which to compare the resource
     * @return true if the two resources are of the same type
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Resource){
            return ((Resource) obj).getType() == type;
        }
        return false;
    }
}
