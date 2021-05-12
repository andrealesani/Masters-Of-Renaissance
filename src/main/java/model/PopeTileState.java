package model;

/**
 * This enum contains all possible states for players' pope's favor tiles
 */
public enum PopeTileState {
    INACTIVE { @Override public String toString() { return "\u001B[36mINACTIVE\u001B[0m"; }},
    ACTIVE { @Override public String toString() { return "\u001B[36mACTIVE\u001B[0m"; }},
    DISCARDED { @Override public String toString() { return "\u001B[36mDISCARDED\u001B[0m"; }},
}
