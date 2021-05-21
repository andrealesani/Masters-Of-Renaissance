package model;

/**
 * This enum contains all possible states for players' pope's favor tiles
 */
public enum PopeTileState {
    INACTIVE { @Override public String toString() { return Color.RESOURCE_STD + "INACTIVE" + Color.RESET; }},
    ACTIVE { @Override public String toString() { return Color.GREEN_FG + "ACTIVE" + Color.RESET; }},
    DISCARDED { @Override public String toString() { return Color.RED_LIGHT_FG + "DISCARDED" + Color.RESET; }},
}
