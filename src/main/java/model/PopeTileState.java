package model;

/**
 * This enum contains all possible states for players' pope's favor tiles
 */
public enum PopeTileState {
    INACTIVE { @Override public String toString() { return Color.RESOURCE_STD + "INACTIVE" + Color.RESET; }},
    ACTIVE { @Override public String toString() { return Color.RESOURCE_STD + "ACTIVE" + Color.RESET; }},
    DISCARDED { @Override public String toString() { return Color.RESOURCE_STD + "DISCARDED" + Color.RESET; }},
}
