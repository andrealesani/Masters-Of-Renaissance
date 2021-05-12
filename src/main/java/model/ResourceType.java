package model;

import model.resource.*;

/**
 * This enum contains all possible types for the game's resources and marbles
 */
public enum ResourceType {
    SHIELD { @Override public String toString() { return "\u001B[36mSHIELD\u001B[0m"; }},
    STONE { @Override public String toString() { return "\u001B[36mSTONE\u001B[0m"; }},
    SERVANT { @Override public String toString() { return "\u001B[36mSERVANT\u001B[0m"; }},
    COIN { @Override public String toString() { return "\u001B[36mCOIN\u001B[0m"; }},
    FAITH { @Override public String toString() { return "\u001B[36mFAITH\u001B[0m"; }},
    WHITEORB { @Override public String toString() { return "\u001B[36mWHITEORB\u001B[0m"; }},
    UNKNOWN { @Override public String toString() { return "\u001B[36mUNKNOWN\u001B[0m"; }};

    public Resource toResource() {
        if (this == ResourceType.COIN) {
            return new ResourceCoin();
        }
        if (this == ResourceType.SERVANT) {
            return new ResourceServant();
        }
        if (this == ResourceType.SHIELD) {
            return new ResourceShield();
        }
        if (this == ResourceType.STONE) {
            return new ResourceStone();
        }
        if (this == ResourceType.UNKNOWN) {
            return new ResourceUnknown();
        }
        if (this == ResourceType.FAITH) {
            return new ResourceFaith();
        }
        // This should never happen
        return null;
    }
}
