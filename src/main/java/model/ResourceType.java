package model;

import model.resource.*;

/**
 * This enum contains all possible types for the game's resources and marbles
 */
public enum ResourceType {
    SHIELD, STONE, SERVANT, COIN, FAITH, WHITEORB, UNKNOWN;

    public Resource toResource () {
        if(this == ResourceType.COIN) {
            return new ResourceCoin();
        }
        if(this == ResourceType.SERVANT) {
            return new ResourceServant();
        }
        if (this == ResourceType.SHIELD) {
            return new ResourceShield();
        }
        if(this == ResourceType.STONE) {
            return new ResourceStone();
        }
        if(this == ResourceType.UNKNOWN) {
            return new ResourceUnknown();
        }
        if (this == ResourceType.FAITH) {
            return new ResourceFaith();
        }
        // This should never happen
        return null;
    }
}
