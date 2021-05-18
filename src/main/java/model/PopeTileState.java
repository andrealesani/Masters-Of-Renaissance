package model;

/**
 * This enum contains all possible states for players' pope's favor tiles
 */
public enum PopeTileState {
    INACTIVE { @Override public String toString() { return Color.ORANGE_LIGHT_FG + "INACTIVE" + Color.RESET; }},
    ACTIVE { @Override public String toString() { return Color.ORANGE_LIGHT_FG + "ACTIVE" + Color.RESET; }},
    DISCARDED { @Override public String toString() { return Color.ORANGE_LIGHT_FG + "DISCARDED" + Color.RESET; }},
}
